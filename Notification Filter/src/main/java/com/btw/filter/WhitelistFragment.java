package com.btw.filter;


import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class WhitelistFragment extends ListFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private WhitelistFragmentCallback mCallbacks;

    private List<String> whitelist;

    public interface WhitelistFragmentCallback {
        public void onSectionAttached(int section_number);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, whitelist);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.i("selected", whitelist.get(position));

    }

    public static WhitelistFragment newInstance(int sectionNumber, List<String> whitelist) {
        WhitelistFragment fragment = new WhitelistFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        fragment.setWhitelist(whitelist);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (WhitelistFragmentCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement ApplicationFragmentCallbacks.");
        }
        mCallbacks.onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }

    public void setWhitelist(List<String> whitelist) {
        this.whitelist = whitelist;
    }

}
