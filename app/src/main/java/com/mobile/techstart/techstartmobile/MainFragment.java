package com.mobile.techstart.techstartmobile;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Lucas on 2/2/2018.
 */

public class MainFragment extends Fragment {

    View myView;
    TextView home, bookstore,directory, calendar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.home_layout, container, false);


        home = myView.findViewById(R.id.homepage);
        bookstore = myView.findViewById(R.id.bookstoreL);
        directory = myView.findViewById(R.id.directoryL);
        calendar = myView.findViewById(R.id.calendarL);
        home.setClickable(true);
        bookstore.setClickable(true);
        directory.setClickable(true);
        calendar.setClickable(true);
        home.setMovementMethod(LinkMovementMethod.getInstance());
        String t1 = "<a href='https://www.wvutech.edu/'>WVU Tech Homepage</a>";
        bookstore.setMovementMethod(LinkMovementMethod.getInstance());
        String t2 = "<a href='http://wvutech.bncollege.com/webapp/wcs/stores/servlet/BNCBHomePage?storeId=15051&catalogId=10001&langId=-1'>Barnes and Noble at Tech</a>";
        directory.setMovementMethod(LinkMovementMethod.getInstance());
        String t3 = "<a href='https://www.wvutech.edu/directory'>Campus Directory</a>";
        calendar.setMovementMethod(LinkMovementMethod.getInstance());
        String t4 = "<a href='https://techregistrar.wvutech.edu/academic-calendar'>Acedemic Calendar</a>";
        home.setText(Html.fromHtml(t1));
        bookstore.setText(Html.fromHtml(t2));
        directory.setText(Html.fromHtml(t3));
        calendar.setText(Html.fromHtml(t4));

        return myView;
    }
}
