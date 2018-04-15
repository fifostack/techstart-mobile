package com.mobile.techstart.techstartmobile;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.List;

/**
 * Created by Lucas on 2/2/2018.
 */

public class ProfileFragment extends Fragment {

    View myView;

    dbManager db;
    String[] sInfo;

    private EditText firstName, lastName, middleName, school, email;
    private Button toggleModeB;
    private boolean mode; //true if in edit mode
    private GoogleSignInAccount account;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.profile_layout, container, false);

        account = GoogleSignIn.getLastSignedInAccount(this.getActivity());

        firstName = myView.findViewById(R.id.firstTF);
        //middleName = myView.findViewById(R.id.middleTF);
        lastName = myView.findViewById(R.id.lastTF);
        email = myView.findViewById(R.id.emailTF);
        email.setText(account.getEmail()); //retrieve email address from sign-in
        school = myView.findViewById(R.id.schoolTF);

        toggleModeB = myView.findViewById(R.id.toggleB);
        mode = false; //start in locked mode


        firstName.setFocusable(false);
        firstName.setFocusableInTouchMode(false);
        firstName.setClickable(false);
        //middleName.setFocusable(false);
        //middleName.setFocusableInTouchMode(false);
        //middleName.setClickable(false);
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
        if(!mode){
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
            //mode = false;

        }
        else{

            firstName.setFocusable(false);
            firstName.setFocusableInTouchMode(false);
            firstName.setClickable(false);
            //middleName.setFocusable(false);
           // middleName.setFocusableInTouchMode(false);
            //middleName.setClickable(false);
            lastName.setFocusable(false);
            lastName.setFocusableInTouchMode(false);
            lastName.setClickable(false);
            //email.setFocusable(false);
            //email.setFocusableInTouchMode(false);
            //email.setClickable(false);
            school.setFocusable(false);
            school.setFocusableInTouchMode(false);
            school.setClickable(false);

            db = new dbManager(); //establish database connection
            // TODO: after locking, send data to database
            if(formCompleted())
            {
                sInfo = new String[] {lastName.getText().toString(), firstName.getText().toString(),email.getText().toString(),school.getText().toString()};
                new submitInfo().execute(sInfo);
            }


            toggleModeB.setText("Edit");
            //mode = true;
        }

        mode = !mode;
    }

    private boolean formCompleted()
    {
        if(firstName.getText().length() < 1 || lastName.getText().length() < 1)
            return false;
        if(email.getText().length() < 1 || school.getText().length() < 1)
            return false;

        return true;
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
                    db.submit(strings);
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


}