package com.mobile.techstart.techstartmobile;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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

import static com.mobile.techstart.techstartmobile.R.id.drawer_layout;

/**
 * Created by Lucas on 2/2/2018.
 */

public class MessagesFragment extends Fragment {

    View myView;
    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private List<String> listDataBody;

    List<String[]> databaseResult; //full result of the database
    List<String> messageBodyList;//list of the message bodies (for the list_body)

    private SearchView search;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initData();

    }

    private void initData() {
        listDataHeader = new ArrayList<>();
        listDataBody = new ArrayList<>();
        messageBodyList = new ArrayList<>();
        databaseResult = new ArrayList<>();

        try {
            new loadAllMessages().execute(true);



            Log.e("yea","" + databaseResult.size());

            /*for (int i = 0; i < databaseResult.size(); i++) {

                listDataHeader.add(databaseResult.get(i)[1]);
                List<String> temp = new ArrayList<>();
                temp.add(databaseResult.get(i)[2]);
                messageBodyList.add(temp);
                listHash.put(listDataHeader.get(i),messageBodyList.get(i));

                i++;
            }*/

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


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.messages_layout, container, false);

        //create and attach ExpandableListAdapter now that the view has been created
        listView = myView.findViewById(R.id.expListView);

        final Activity myActivity = this.getActivity();
        listAdapter = new ExpandableListAdapter(myActivity,listDataHeader,listDataBody);
        listView.setAdapter(listAdapter);
        search = myView.findViewById(R.id.searchView);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {



                List<String> searchDataHeader = new ArrayList<>();
                List<String> searchMessageBody = new ArrayList<>();


                for (int i = 0; i < listDataHeader.size(); i++)
                {
                    String xHead = listDataHeader.get(i);
                    String xBody = messageBodyList.get(i);


                    if(xHead.toLowerCase().contains(s.toLowerCase()) || xBody.toLowerCase().contains(s.toLowerCase())) //if neither the head or body contain the string
                    {
                        searchDataHeader.add(listDataHeader.get(i));
                        searchMessageBody.add(messageBodyList.get(i));
                    }

                }


                setAdapter(searchDataHeader,searchMessageBody);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {


                if(s.length() < 1)
                {
                    new loadAllMessages().execute(true);
                }

                return false;
            }
        });

        return myView;
    }

    public void setAdapter(List<String> headerList, List<String> bodyList)
    {
        listAdapter = new ExpandableListAdapter(myView.getContext(),headerList,bodyList);
        listView.setAdapter(listAdapter); //reset the adapter to show on screen
    }


    class loadAllMessages extends AsyncTask<Boolean, Integer, List<String[]>>
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
            catch(NullPointerException e)
            {
                View view = myView;
                Snackbar.make(view,"Unable to establish database connection.", Snackbar.LENGTH_LONG )
                        .setAction("Action", null).show();
                e.printStackTrace();
            }

            databaseResult = messageData; //save to database result (for browsing later)


            return databaseResult;
        }



        @Override
        protected void onPostExecute(List<String[]> result) {
            super.onPostExecute(result);

            listDataHeader.clear();

            try {
                for (int i = 0; i < databaseResult.size(); i++) {

                    Log.e(TAG, "" + listDataHeader.size());


                    String header = databaseResult.get(i)[1] + "     " + databaseResult.get(i)[3]; //prepare the title box
                    listDataHeader.add(header); //make author the list_head (for now)


                    String temp = databaseResult.get(i)[2];
                    messageBodyList.add(temp); //add message body to a list

                    Log.e(TAG, "" + messageBodyList.get(i));
                    listDataBody.add(messageBodyList.get(i)); //add message  body to the body list
                }
            }
            catch(NullPointerException e)
            {
                View view = myView;
                Snackbar.make(view,"Unable to establish database connection.", Snackbar.LENGTH_LONG )
                        .setAction("Action", null).show();
            }

            setAdapter(listDataHeader,listDataBody);

        }
    }

}
