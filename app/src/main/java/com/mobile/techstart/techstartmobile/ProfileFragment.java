package com.mobile.techstart.techstartmobile;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 2/2/2018.
 */

public class ProfileFragment extends Fragment {

    View myView;

    dbManager db;
    String[] sInfo;

    private EditText firstName, lastName, middleName;
    private TextView email, school;
    private Button toggleModeB;
    private boolean mode; //true if in edit mode
    private boolean entryMode; //true if we are inserting, false if we are editing
    private GoogleSignInAccount account;
    ProgressDialog mProgress;
    DialogInterface.OnClickListener editClickListener;
    DialogInterface.OnClickListener saveClickListener;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.profile_layout, container, false);
        entryMode = true;
        account = GoogleSignIn.getLastSignedInAccount(this.getActivity());

        firstName = myView.findViewById(R.id.firstTF);
        //middleName = myView.findViewById(R.id.middleTF);
        lastName = myView.findViewById(R.id.lastTF);
        email = (TextView) myView.findViewById(R.id.emailTF);
        email.setText(account.getEmail()); //retrieve email address from sign-in
        school = (TextView) myView.findViewById(R.id.schoolTF);

        toggleModeB = myView.findViewById(R.id.toggleB);
        mode = false; //start in locked mode

        mProgress = new ProgressDialog(myView.getContext());
        mProgress.setMessage("Checking for existing user entry...");
        new checkDBforStudent().execute(email.getText().toString());




        firstName.setFocusable(false);
        firstName.setFocusableInTouchMode(false);
        firstName.setClickable(false);
        lastName.setFocusable(false);
        lastName.setFocusableInTouchMode(false);
        lastName.setClickable(false);
        email.setFocusable(false);
        email.setFocusableInTouchMode(false);
        email.setClickable(false);
        school.setFocusable(false);
        school.setFocusableInTouchMode(false);
        school.setClickable(false);


        toggleModeB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleEditMode();
            }
        });


        return myView;
    }


    private void toggleEditMode() {

        boolean shouldEdit = false;
        editClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        firstName.setFocusable(true);
                        firstName.setFocusableInTouchMode(true);
                        firstName.setClickable(true);

                        lastName.setFocusable(true);
                        lastName.setFocusableInTouchMode(true);
                        lastName.setClickable(true);

                        school.setFocusable(true);
                        school.setFocusableInTouchMode(true);
                        school.setClickable(true);

                        toggleModeB.setText("Save");


                        mode = !mode;
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        //nothing to do here
                        break;
                }
            }
        };

        saveClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        firstName.setFocusable(false);
                        firstName.setFocusableInTouchMode(false);
                        firstName.setClickable(false);

                        lastName.setFocusable(false);
                        lastName.setFocusableInTouchMode(false);
                        lastName.setClickable(false);

                        school.setFocusable(false);
                        school.setFocusableInTouchMode(false);
                        school.setClickable(false);

                        //db = new dbManager(); //establish database connection
                        // TODO: after locking, send data to database
                        if(formCompleted())
                        {
                            sInfo = new String[] {lastName.getText().toString(), firstName.getText().toString(),email.getText().toString(),school.getText().toString()};
                            new submitInfo().execute(sInfo);
                        }
                        else
                        {
                            Snackbar.make(myView, "Invalid entry, please try again.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }

                        //grab the data just entered for possible editing
                        new checkDBforStudent().execute(email.getText().toString());
                        toggleModeB.setText("Edit");
                        mode = !mode;
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        //nothing to do here
                        break;
                }
            }
        };


        if(!mode){

            AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
            builder.setMessage("Faculty will use this information to contact you. Are you sure you want to edit?").setPositiveButton("Yes", editClickListener)
                    .setNegativeButton("No", editClickListener).show();

        }
        else{

            AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
            builder.setMessage("Are you sure you would like to submit this data?").setPositiveButton("Yes", saveClickListener)
                    .setNegativeButton("No", saveClickListener).show();


        }


    }

    private boolean formCompleted() {
        boolean valid = true;

        if (firstName.getText().length() < 1)
        {
            firstName.setError("All fields must be complete.");
            valid = false;
        }
        if(lastName.getText().length() < 1)
        {
            lastName.setError("All fields must be complete.");
            valid = false;
        }
        if(email.getText().length() < 1) {
            email.setError(" ");
            valid = false;
        }
        if(school.getText().length() >= 1)
        {
            try {
                Integer.parseInt(school.getText().toString());
            }
            catch(NumberFormatException e)
            {
                school.setError("Counselor ID must be a number.");
                valid = false;
            }
        }
        else
        {
            school.setError("All fields must be complete.");
            valid = false;
        }


        return valid;
    }



    class submitInfo extends AsyncTask<String, Integer, Integer>
    {
        String TAG = "InfoSubmit";
        int status = 0;

        @Override
        protected Integer doInBackground(String... strings) {

            try {
                dbManager db = new dbManager();
                Log.d(TAG, "Database connection established in profile thread" + db.toString());
                if (strings.length != 4)
                {
                    Log.d(TAG, "String input is incorrect");
                }
                else
                {
                    if(entryMode)
                        db.submit(strings);
                    else
                        db.editEntry(strings);
                }
                db.close();
            }
            catch(NullPointerException e)
            {
                db.close();
                View view = myView;
                Snackbar.make(view,"Unable to establish database connection.", Snackbar.LENGTH_LONG )
                        .setAction("Action", null).show();
                status = 1;
            }
            catch(Exception e)
            {
                db.close();
                Log.e(TAG, "" + e.toString());
                status = 1;
            }

            return status;
        }



        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);


            if(status == 0) {
                View view = myView;
                Snackbar.make(view, "Information sent successfully", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        }
    }

    class checkDBforStudent extends AsyncTask<String, Integer, String[]>
    {
        String TAG = "CheckForStudent";
        int status = 0;
        List<String[]> results;

        @Override
        protected String[] doInBackground(String... strings) {
            results = new ArrayList<>();
            try
            {
                dbManager db = new dbManager();
                Log.d(TAG, "Database connection established in profile thread " + db.toString());


                results = db.getStudents(strings[0]);


                if(results.size() > 1)
                {
                    Log.e(TAG, "Multiple students with that email." + db.toString()); //should not happen
                }
                else if(results.size() == 0)
                {
                    return null;
                }
                else
                {

                    return results.get(0);
                }
                db.close();
            }
            catch(NullPointerException e)
            {
                db.close();
                View view = myView;
                Snackbar.make(view,"Unable to check for student.", Snackbar.LENGTH_LONG )
                        .setAction("Action", null).show();
                status = 1;
            }
            catch(Exception e)
            {
                //db.close();
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgress.show();
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);

            if(result != null) {
                entryMode = false; //this is not a new entry
                lastName.setText(result[0]);
                firstName.setText(result[1]);
                email.setText(result[2]);
                school.setText(result[3]);
            }

            mProgress.hide();
        }
    }
}