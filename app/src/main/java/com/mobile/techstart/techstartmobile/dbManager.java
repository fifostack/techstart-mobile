package com.mobile.techstart.techstartmobile;
import android.util.Log;

import java.sql.*;
import java.util.List;

class dbManager{

    Connection con;


    public void dbManager()
    {
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection( "jdbc:mysql://lucas.lan/tsdb","techstart","techstart"); //here sonoo is database name, root is username and password
            Log.d("dbManager", "dbManager: " + con.toString());
            //Statement stmt = con.createStatement();
            //ResultSet rs = stmt.executeQuery("SELECT * from emp");

            /*while(rs.next())
                System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));*/
        }
        catch(Exception e)
        {
            Log.e("dbManager", "dbManager: PLEASE EAT MY ASS " );
        }
    }

    public void submit(String[] studentData)
    {
        //"CALL insertS( '" + last + "', '" + first + "', '" + email + "', " + cID + ")"

        try
        {
            PreparedStatement subInfo;
            subInfo = con.prepareStatement("{CALL insertS(?, ?, ?, ?)}" );
            subInfo.setString(1,studentData[0]);
            subInfo.setString(2,studentData[1]);
            subInfo.setString(3,studentData[2]);
            subInfo.setString(4,studentData[3]);
            subInfo.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close()
    {
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getAllMessages()
    {
        String result = "";
        PreparedStatement comm;
        try {
            comm = con.prepareStatement("CALL getM");
            ResultSet rs = comm.getResultSet();

            int title = rs.findColumn("author");
            int body = rs.findColumn("content");

            while(rs.next())
            {
                result += rs.getString(title);
                result += rs.getString(body);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }


}