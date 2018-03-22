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
import android.widget.SearchView;

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
    private SearchView search;
    private dbManager db;

    private String dbString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initData();

    }

    private void initData() {
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();
        getMessages();

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
        listDataHeader.add("Important Dates");
        listDataHeader.add("Notice to Students");
        listDataHeader.add("Message Test");

        List<String> exampleitem1 = new ArrayList<>();
        exampleitem1.add("Monday \nTuesday \nWednesday");

        List<String> exampleitem2 = new ArrayList<>();
        exampleitem2.add("The FitnessGram™ Pacer Test is a multistage aerobic capacity test that progressively gets more difficult as it continues. The 20 meter pacer test will begin in 30 seconds. Line up at the start. The running speed starts slowly, but gets faster each minute after you hear this signal. [beep] A single lap should be completed each time you hear this sound. [ding] Remember to run in a straight line, and run as long as possible. The second time you fail to complete a lap before the sound, your test is over. The test will begin on the word start.");

        List<String> exampleitem3 = new ArrayList<>();
        exampleitem3.add(dbString);

        listHash.put(listDataHeader.get(0),exampleitem1);
        listHash.put(listDataHeader.get(1),exampleitem2);
        listHash.put(listDataHeader.get(2),exampleitem3);
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
        search = myView.findViewById(R.id.searchView);

        return myView;
    }

    public void getMessages()
    {
        db = new dbManager();
        dbString = db.getAllMessages();
        db.close();

    }
}
