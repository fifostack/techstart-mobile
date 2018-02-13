package com.mobile.techstart.techstartmobile;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import static com.mobile.techstart.techstartmobile.R.id.*;

public class MainPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        if (id == action_settings) {

            View view = findViewById(drawer_layout);
            Snackbar.make(view,"What's the matter little fella?", Snackbar.LENGTH_LONG )
            .setAction("Action", null).show();

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
