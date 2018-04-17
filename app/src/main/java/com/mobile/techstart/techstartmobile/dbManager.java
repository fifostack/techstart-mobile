package com.mobile.techstart.techstartmobile;
import android.icu.util.Calendar;
import android.util.Log;

import com.mysql.jdbc.CallableStatement;
import com.mysql.jdbc.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class dbManager{

    private com.mysql.jdbc.Connection con;
    String TAG = "dbManager";

    public dbManager()
    {
        Log.d(TAG, "dbManager: OUTSIDE THE TRY");
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = (com.mysql.jdbc.Connection) DriverManager.getConnection("jdbc:mysql://173.80.18.23:3306/tsdb", "techstart","techstart");
            //here tsdb is database name, techstart is username and password
            Log.d("dbManager", "Connection String: " + con.toString());

            //Statement stmt = con.createStatement();
            //ResultSet rs = stmt.executeQuery("SELECT * from emp");

            /*while(rs.next())
                System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));*/
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    public void submit(String[] studentData)
    {
        //"CALL insertS( '" + last + "', '" + first + "', '" + email + "', " + cID + ")"

        try
        {
            PreparedStatement subInfo;
            subInfo = (PreparedStatement) con.prepareStatement("{CALL insertS(?, ?, ?, ?)}" );
            subInfo.setString(1,studentData[0]);
            subInfo.setString(2,studentData[1]);
            subInfo.setString(3,studentData[2]);
            subInfo.setString(4,studentData[3]);
            subInfo.execute();

        } catch (SQLException e) {
            Log.e("dbManager", "dbManager - error in submit method: " + e.toString());
        } catch (NullPointerException e) {
            Log.e("dbManager", "dbManager - error in submit method: " + e.toString());
        }
    }

    public boolean checkSession(String attempt)
    {

        CallableStatement comm;
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        try {
            comm = (CallableStatement) con.prepareCall("{CALL getSess}");
            comm.execute();
            ResultSet rs = comm.getResultSet();

            while(rs.next()) {

                String code = rs.getString(1);
                String post = rs.getString(2);
                String end = rs.getString(3);
                Date exp = f.parse(end);

                Date currentTime = null;
                currentTime = java.util.Calendar.getInstance().getTime();

                //compare the entered session
                if(code.equals(attempt)) //if the code is correct
                {
                    if(currentTime.before(exp)) //if the expiration date has not passed
                    {  return true; }
                }

            }

            comm.close();
        } catch (SQLException e) {
            Log.d("dbManager", "dbManager - error in getAllMessages: " + e.toString());
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return false;
    }

    public void close()
    {
        try {
            con.close();
        }
        catch(NullPointerException e)
        {
            Log.e("dbManager", "dbManager - error closing connection: " + e.toString());
        }
        catch (SQLException e) {
            Log.e("dbManager", "dbManager - error closing connection: " + e.toString());
        }
    }

    public List<String[]> getAllMessages()
    {
        List<String[]> result = new ArrayList<>();
        CallableStatement comm;
        try {
            comm = (CallableStatement) con.prepareCall("{CALL getM}");
            comm.execute();
            ResultSet rs = comm.getResultSet();

            while(rs.next()) {
                String[] entry = new String[4];
                entry[0] = rs.getString(1);
                entry[1] = rs.getString(2);
                entry[2] = rs.getString(3);
                entry[3] = rs.getString(4);
                result.add(entry);
            }

            comm.close();
        } catch (SQLException e) {
            Log.d("dbManager", "dbManager - error in getAllMessages: " + e.toString());
            e.printStackTrace();
        }

        return result;
    }


}