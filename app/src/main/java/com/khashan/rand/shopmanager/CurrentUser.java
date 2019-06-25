package com.khashan.rand.shopmanager;


import android.content.Context;

public class CurrentUser {

    public static String workAt_ShopName;
    public static String shopType;
    public static User user;


    public static void delete(){
        workAt_ShopName="";
        shopType="";
        user=null;
    }

    public static void storeCurrentUserInMobileMemorrey(Context context){
        // store it in the moble storge
        MySheardPrefrance sheardPrefrance=new MySheardPrefrance(context);
        sheardPrefrance.storeCurrentUserInformation();
    }



}
