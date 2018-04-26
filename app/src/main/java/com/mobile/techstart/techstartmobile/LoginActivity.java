package com.mobile.techstart.techstartmobile;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity /*implements LoaderCallbacks<Cursor>*/ {


    private static final String TAG = "LoginActivity";

    // UI references.
    private Intent loadMain;
    private View myView;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsiClient;
    private GoogleSignInAccount account;
    private TextView sessionView;
    private boolean signedIn;
    private static int RC_SIGN_IN = 100;
    boolean needLogin;


    @Override
    protected void onStart() { //whenever this page is loaded
        super.onStart();

        //--bypass login activity (debugging)--//
        //startActivity(loadMain);
        //
        signedIn = false;

        needLogin = getIntent().getBooleanExtra("NEED_LOGIN",false);

        if(!needLogin) {
            account = GoogleSignIn.getLastSignedInAccount(this);
        }
        else
        {
            gsiClient.signOut();
        }

        if(account != null) { //if someone is already logged in
            signedIn = true;
            startActivity(loadMain); //load main page and bypass/kill login
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) { //when this activity is first created
        super.onCreate(savedInstanceState);
        myView = this.findViewById(android.R.id.content).getRootView();
        setContentView(R.layout.activity_login);
        loadMain = new Intent(this, MainPage.class);
        sessionView = (TextView) findViewById(R.id.sessionET);
        sessionView.setError(null);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gsiClient = GoogleSignIn.getClient(this, gso);
        SignInButton gb = findViewById(R.id.sign_in_button);
        gb.setSize(SignInButton.SIZE_WIDE);
        gb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new checkSession().execute(); //create a thread to check the session
            }
        });


    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "handleSignInResult: ON ACTIVITY RESULT");
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Log.d(TAG, "handleSignInResult: IN IF REQUEST CODE IS SIGN IN");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        Log.d(TAG, "handleSignInResult: BEFORE THE TRY");
        try {
            account = completedTask.getResult(ApiException.class);

            
            // Signed in successfully, load the main page
            // TODO: send google account over to MainPage to update the account-based GUI elements
            startActivity(loadMain);
            finish();


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());

            //update your gui
        }
    }


    private void signIn() {

        Intent signInIntent = gsiClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    class checkSession extends AsyncTask<String, Integer, Boolean>
    {
        String TAG = "checkSession";
        int status = 0;
        dbManager db;
        boolean validSession;

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                db = new dbManager();
                Log.d(TAG, "Database connection established in login thread" + db.toString());
                Log.d(TAG,"Session code " + sessionView.getText().toString() + "entered.");
                validSession = db.checkSession(sessionView.getText().toString());
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

            return validSession;
        }



        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if(status == 0 && !result) {
                View view = myView;
                sessionView.setError("Invalid or expired session ID");
            }
            else if(status == 0 && result)
            {
                signIn();
            }


        }
    }

}

