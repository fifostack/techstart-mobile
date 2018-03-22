package com.mobile.techstart.techstartmobile;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.profile_layout, container, false);

        firstName = myView.findViewById(R.id.firstTF);
        //middleName = myView.findViewById(R.id.middleTF);
        lastName = myView.findViewById(R.id.lastTF);
        email = myView.findViewById(R.id.emailTF);
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
            //middleName.setFocusable(true);
            //middleName.setFocusableInTouchMode(true);
           // middleName.setClickable(true);
            lastName.setFocusable(true);
            lastName.setFocusableInTouchMode(true);
            lastName.setClickable(true);
            email.setFocusable(true);
            email.setFocusableInTouchMode(true);
            email.setClickable(true);
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
            email.setFocusable(false);
            email.setFocusableInTouchMode(false);
            email.setClickable(false);
            school.setFocusable(false);
            school.setFocusableInTouchMode(false);
            school.setClickable(false);

            db = new dbManager(); //establish database connection
            // TODO: after locking, send data to database
            if(formCompleted())
            {
                sInfo = new String[] {lastName.getText().toString(), firstName.getText().toString(),email.getText().toString(),school.getText().toString()};
                db.submit(sInfo);
            }

            db.close();


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


}