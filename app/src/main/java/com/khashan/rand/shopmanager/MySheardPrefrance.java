package com.khashan.rand.shopmanager;


import android.content.Context;
import android.content.SharedPreferences;

public class MySheardPrefrance{

    private Context context;
    private SharedPreferences sharedPreferences;
    private final String name ="sharedPreferences_shopManeger"; // SharedPreferences userName



    public MySheardPrefrance(Context context){
        this.context=context;
        sharedPreferences=context.getSharedPreferences(name,Context.MODE_PRIVATE);
    }

    /***************************************************************************/

    public void storeCurrentUserInformation(){

        SharedPreferences.Editor editor=sharedPreferences.edit();

        editor.putString(sharedPrefrancec_Keys.sharedPrefrancec_name,CurrentUser.user.getName());
        editor.putString(sharedPrefrancec_Keys.sharedPrefrancec_password,CurrentUser.user.getPassword());
        editor.putString(sharedPrefrancec_Keys.sharedPrefrancec_userType,CurrentUser.user.getUserType().toString());
        editor.putString(sharedPrefrancec_Keys.sharedPrefrancec_atBranch,CurrentUser.user.getWorkAtBranch()+"");

        editor.commit();

    }

}
