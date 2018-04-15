package com.mobile.techstart.techstartmobile;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.io.IOException;
import java.net.MalformedURLException;

import static com.mobile.techstart.techstartmobile.R.id.action_advanced;
import static com.mobile.techstart.techstartmobile.R.id.action_logout;
import static com.mobile.techstart.techstartmobile.R.id.drawer_layout;
import static com.mobile.techstart.techstartmobile.R.id.nav_about_layout;
import static com.mobile.techstart.techstartmobile.R.id.nav_home;
import static com.mobile.techstart.techstartmobile.R.id.nav_messages_layout;
import static com.mobile.techstart.techstartmobile.R.id.nav_profile;
import static com.mobile.techstart.techstartmobile.R.id.nav_tutorials_layout;
import static com.mobile.techstart.techstartmobile.R.id.nav_view;

public class MainPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    GoogleSignInAccount account;
    Intent logout;

    @Override
    protected void onStart() {


        super.onStart();
        logout.putExtra("NEED_LOGIN", false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        account = GoogleSignIn.getLastSignedInAccount(this);
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        //set up navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);


        //initialize profile info on nav drawer
        TextView user = headerView.findViewById(R.id.usernameText);
        TextView uEmail = headerView.findViewById(R.id.emailText);
        ImageView profileImage = headerView.findViewById(R.id.userImageView);


        user.setText(account.getDisplayName());
        uEmail.setText(account.getEmail());
        /*Bitmap iconBMP = null;
        try {
            Uri imageURI = account.getPhotoUrl();
            if(imageURI != null)
                iconBMP = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageURI);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            profileImage.setImageBitmap(iconBMP);
        }*/


        logout = new Intent(this, LoginActivity.class);

        FragmentManager fragMan = getFragmentManager();
        toolbar.setTitle("TechStart Mobile");
        fragMan.beginTransaction()
                .replace(R.id.content_frame, new MainFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == action_logout) {

            View view = findViewById(drawer_layout);


            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            logout.putExtra("NEED_LOGIN", true); //tell the login activity we need to login
                            startActivity(logout);      //load login activity
                            finish();                   //end this activity
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            //nothing to do here
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this.getBaseContext());
            builder.setMessage("Are you sure you want to logout? You will need a session ID to login again.").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();



            /*Snackbar.make(view,"What's the matter little fella?", Snackbar.LENGTH_LONG )
            .setAction("Action", null).show();
            */
            return true;
        }
        else if (id == action_advanced) {

            View view = findViewById(drawer_layout);
            Snackbar.make(view,"Bad at video games?", Snackbar.LENGTH_LONG )
                    .setAction("Action", null).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragMan = getFragmentManager();

        if (id == nav_home) {
            toolbar.setTitle("TechStart Mobile");
            fragMan.beginTransaction()
                    .replace(R.id.content_frame, new MainFragment())
                    .commit();
        } else if (id == nav_profile) {
            toolbar.setTitle("Profile");
            fragMan.beginTransaction()
                    .replace(R.id.content_frame, new ProfileFragment())
                    .commit();
        } else if (id == nav_tutorials_layout) {
            toolbar.setTitle("Tutorials");
            fragMan.beginTransaction()
                    .replace(R.id.content_frame, new TutorialsFragment())
                    .commit();
        } else if (id == nav_messages_layout) {
            toolbar.setTitle("Messages");
            fragMan.beginTransaction()
                    .replace(R.id.content_frame, new MessagesFragment())
                    .commit();
        } else if (id == nav_about_layout) {
            toolbar.setTitle("About Us");
            fragMan.beginTransaction()
                    .replace(R.id.content_frame, new AboutFragment())
                    .commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
