package com.example.percetakan_witra.LOGIN;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.percetakan_witra.Menu;

import java.util.HashMap;

public class shared_prefences {



    private static final String SHARED_PREF_NAME = "datacostumer";
    private static final String USER_LOGIN = "Login";
    public  static final String ID = "id_pelanggan";



    private SharedPreferences sharedPrefences;
    private static SharedPreferences.Editor editor;
    public static Context context;


    public shared_prefences(Context context) {
        this.context = context;
        sharedPrefences = context.getSharedPreferences(SHARED_PREF_NAME,context.MODE_PRIVATE);
        editor = sharedPrefences.edit();
    }
    public boolean Login_User(){
        return sharedPrefences.getBoolean(USER_LOGIN,false);
    }
    public void usersession(String id){
        editor.putBoolean(USER_LOGIN,true);
        editor.putString(ID,id);
        editor.apply();
    }
    public void chekLogin(){
        if (!this.Login_User()){
            Intent intent = new Intent(context, Login.class);
            context.startActivity(intent);
            ((Menu) context).finish();

        }
    }

    public HashMap<String ,String>UserData(){
        HashMap<String,String>data = new HashMap<>();
        data.put(ID,sharedPrefences.getString(ID,null));
        return data;
    }
    public static void Logout(){
        editor.clear();
        editor.commit();

        Intent intent = new Intent(context, Login.class);
        context.startActivity(intent);
        ((Menu) context).finish();
    }


}
