package com.btw.filter;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.btw.filter.db.NFDbHelper;
import com.btw.filter.db.WhitelistEntry;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, ApplicationFragment.ApplicationFragmentCallbacks, WhitelistFragment.WhitelistFragmentCallback {

    public static final String ARG_SELECTED_APP = "selected_application";

    private MenuItem startItem;
    private MenuItem stopItem;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private PackageManager pm = null;

    private List<String> names = null;

    private List<WhitelistEntry> whitelist;

    private List<String> whitelistforapp = new ArrayList<String>();

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pm = getPackageManager();

        List<ApplicationInfo> applications = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        names = new ArrayList<String>();

        for (ApplicationInfo info : applications) {

            if ((info.flags & ApplicationInfo.FLAG_SYSTEM) != 1) {

                names.add(String.valueOf(pm.getApplicationLabel(info)));

            }
        }

        NFDbHelper db = new NFDbHelper(this);

        whitelist = db.getEntriesForPackage("ingress");

        for (WhitelistEntry entry : whitelist) {
            whitelistforapp.add(entry.getWhitelistEntry());
        }

        setContentView(R.layout.activity_controll);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();

        switch (position) {
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, ApplicationFragment.newInstance(position + 1, names))
                        .commit();
                break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, WhitelistFragment.newInstance(position + 1, whitelistforapp))
                        .commit();
                break;
        }
    }

    @Override
    public void onFilterStart() {

        Intent serviceIntent = new Intent(this, FilterService.class);
        serviceIntent.putExtra("whitelist", whitelistforapp.toArray(new String[whitelistforapp.size()]));
        this.startService(serviceIntent);

        swapButtonsToStop();

    }

    @Override
    public void onFilterStop() {

        Intent serviceIntent = new Intent(this, FilterService.class);
        this.stopService(serviceIntent);

        swapButtonsToStart();

    }

    @Override
    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    private void swapButtonsToStop() {
        stopItem.setVisible(true);
        startItem.setVisible(false);
    }

    private void swapButtonsToStart() {
        stopItem.setVisible(false);
        startItem.setVisible(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.controll, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        startItem = menu.findItem(R.id.action_start);
        stopItem = menu.findItem(R.id.action_stop);

        if (startItem != null && startItem != null && isMyServiceRunning()) {
            swapButtonsToStop();
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_start) {
            onFilterStart();
            return true;
        }
        if (id == R.id.action_stop) {
            onFilterStop();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemSelected(String application) {

        //NFDbHelper db = new NFDbHelper(this);

        //list = db.getEntriesForPackage(application);

        Log.i("Selected", application);
    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (FilterService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


}
