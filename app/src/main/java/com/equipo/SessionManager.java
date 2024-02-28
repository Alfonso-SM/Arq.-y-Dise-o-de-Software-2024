package com.equipo;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    //variables
    SharedPreferences usersSession;
    SharedPreferences.Editor editor;
    Context context;

    //Session names
    public static final String SESSION_USERSESSION = "userLoginSession";


    // User Session variables
    private static final String IS_LOGIN = "false";
    public static final String KEY_PHONENO = "PhoneNo";




    public SessionManager(Context _context, String sessionName) {
        context = _context;
        usersSession = context.getSharedPreferences(sessionName, Context.MODE_PRIVATE);
        editor = usersSession.edit();
    }


    public void createLoginSession( String phoneNo) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_PHONENO,phoneNo);
        editor.commit();
    }

    public HashMap<String, String> getUsersDetailFromSession() {
        HashMap<String, String> userData = new HashMap<String, String>();
        userData.put(KEY_PHONENO, usersSession.getString(KEY_PHONENO, null));
        return userData;
    }

    public boolean checkLogin() {
        if (usersSession.getBoolean(IS_LOGIN, false)) {
            return true;
        } else {
            return false;
        }
    }

    public void LogoutUserFromSession() {
        editor.putString(KEY_PHONENO,null);
        editor.commit();
        editor.apply();
    }

    /*
    Remember Me
    Session Functions
     */


}
