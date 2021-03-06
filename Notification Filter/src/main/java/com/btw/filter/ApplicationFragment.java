package com.btw.filter;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class ApplicationFragment extends ListFragment {

    private ApplicationFragmentCallbacks mCallbacks;

    private static final String ARG_SECTION_NUMBER = "section_number";

    private String[] applications;

    public interface ApplicationFragmentCallbacks {
        public void onListItemSelected(String application);

        public void onSectionAttached(int section_number);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, applications);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // do something with the data
        mCallbacks.onListItemSelected(applications[position]);

    }

    public static ApplicationFragment newInstance(int sectionNumber, List<String> names) {
        ApplicationFragment fragment = new ApplicationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        fragment.setApplications(names.toArray(new String[names.size()]));
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (ApplicationFragmentCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement ApplicationFragmentCallbacks.");
        }
        mCallbacks.onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }

    public void setApplications(String[] applications) {
        this.applications = applications;
    }

}
