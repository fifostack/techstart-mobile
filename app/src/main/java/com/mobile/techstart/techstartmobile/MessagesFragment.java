package com.mobile.techstart.techstartmobile;

import android.app.ExpandableListActivity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Lucas on 2/2/2018.
 */

public class MessagesFragment extends Fragment {

    View myView;
    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHash;

    List<String[]> databaseResult; //full result of the database
    List<List<String>> messageBodyList;//list of the message bodies (for the list_body)

    private SearchView search;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initData();

    }

    private void initData() {
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();
        messageBodyList = new ArrayList<>();
        databaseResult = new ArrayList<>();

        try {
            new loadMessages().execute(true);



            Log.e("yea","" + databaseResult.size());

            for (int i = 0; i < databaseResult.size(); i++) {

                listDataHeader.add(databaseResult.get(i)[1]);
                List<String> temp = new ArrayList<>();
                temp.add(databaseResult.get(i)[2]);
                messageBodyList.add(temp);
                listHash.put(listDataHeader.get(i),messageBodyList.get(i));

                i++;
            }

        } catch (NullPointerException e) {
            Log.e("InitData","Null pointer in initData: " + e.toString());
            e.printStackTrace();
        }




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
        /*listDataHeader.add("Important Dates");
        listDataHeader.add("Notice to Students");
        listDataHeader.add("Message Test");

        List<String> exampleitem1 = new ArrayList<>();
        exampleitem1.add("Monday \nTuesday \nWednesday");

        List<String> exampleitem2 = new ArrayList<>();
        exampleitem2.add("The FitnessGram™ Pacer Test is a multistage aerobic capacity test that progressively gets more difficult as it continues. The 20 meter pacer test will begin in 30 seconds. Line up at the start. The running speed starts slowly, but gets faster each minute after you hear this signal. [beep] A single lap should be completed each time you hear this sound. [ding] Remember to run in a straight line, and run as long as possible. The second time you fail to complete a lap before the sound, your test is over. The test will begin on the word start.");

        List<String> exampleitem3 = new ArrayList<>();
        exampleitem3.add("Testing");

        listHash.put(listDataHeader.get(0),exampleitem1);
        listHash.put(listDataHeader.get(1),exampleitem2);
        listHash.put(listDataHeader.get(2),exampleitem3);*/
        //end placeholder values
    }

    public void setDatabaseData(List<String[]> db)
    {
        databaseResult = db;
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


    class loadMessages extends AsyncTask<Boolean, Integer, List<String[]>>
    {
        String TAG = "loadMessages";
        List<String[]> messageData;

        @Override
        protected List<String[]> doInBackground(Boolean... booleans) {

            try {
                dbManager db = new dbManager();
                Log.d(TAG, "Database connection established in message thread" + db.toString());
                //skips the message grab if the boolean is false (for testing)
                if (booleans[0]) //if the execute is called with a boolean of true
                {
                    Log.d(TAG, "loadMessage called with true flag");

                    messageData = db.getAllMessages();
                }
                db.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            databaseResult = messageData;

            for (int i = 0; i < databaseResult.size(); i++) {

                listDataHeader.add(databaseResult.get(i)[1]);
                List<String> temp = new ArrayList<>();
                temp.add(databaseResult.get(i)[2]);
                messageBodyList.add(temp);
                listHash.put(listDataHeader.get(i),messageBodyList.get(i));

                i++;
            }

            return messageData;
        }

        @Override
        protected void onPostExecute(List<String[]> result) {
            super.onPostExecute(result);


        }
    }

}
