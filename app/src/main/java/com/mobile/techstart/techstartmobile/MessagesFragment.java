package com.mobile.techstart.techstartmobile;

import android.app.ExpandableListActivity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lucas on 2/2/2018.
 */

public class MessagesFragment extends Fragment {

    View myView;
    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHash;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();

    }

    private void initData() {
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();

        /*
            // TODO: grab messages from the DB and populate the lists
            PSEUDOCODE incoming:

            for each message returned from the query
            {
                seperate and store its title, author, body

                listDataHeader.add("<titleAndAuthor1>");
                List<String> titleAndAuthor1 = new ArrayList<>();
                titleAndAuthor1.add("<messageBody>");
                listHash.put(listDataHeader.get(0),titleAndAuthor1);
            }
         */

        //begin placeholder list values
        listDataHeader.add("Exampleitem1");
        listDataHeader.add("Exampleitem2");
        listDataHeader.add("Exampleitem3");
        listDataHeader.add("Exampleitem4");

        List<String> exampleitem1 = new ArrayList<>();
        exampleitem1.add("This is example item 1.");

        List<String> exampleitem2 = new ArrayList<>();
        exampleitem2.add("This is example item 2.");
        exampleitem2.add("Hot dang.");
        exampleitem2.add("This is fun stuff.");

        listHash.put(listDataHeader.get(0),exampleitem1);
        listHash.put(listDataHeader.get(1),exampleitem2);
        //end placeholder values
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.messages_layout, container, false);

        //create and attach ExpandableListAdapter now that the view has been created
        listView = myView.findViewById(R.id.expListView);
        listAdapter = new ExpandableListAdapter(this.getActivity(),listDataHeader,listHash);
        listView.setAdapter(listAdapter);

        return myView;
    }
}
