package com.khashan.rand.shopmanager;


import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Check {

    private Context context;


    public Check(Context context) {
        this.context = context;
    }
 /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    private void showMessage(String msg) {

        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public boolean valid_password(String pass) {


        if (pass == null || pass.length() == 0) {
            showMessage(context.getResources().getString(R.string.errotMsg_noPassword));
            return false;
        }
        if (pass.length() < 8) {
            showMessage(context.getResources().getString(R.string.error_msg_shortPassword));
            return false;
        }

        return true;
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    public boolean valid_userName(String name) {
        if (name == null) {
            return false;
        }

        if (!name.contains(" ") || name.length() == 0) {
            showMessage(context.getResources().getString(R.string.error_usernameFirstNameOnly));
            return false;
        }

        return true;
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public boolean valid_shopName(String shopName) {

        if (shopName == null || shopName.length() == 0) {
            return false;
        }

        return true;
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    public boolean valid_phoneNumber(String phoneNumber){

        if (phoneNumber == null || phoneNumber.length()<8) {
            showMessage(context.getResources().getString(R.string.error_invalidPhoneNumber));
        }
        for (int i = 0; i < phoneNumber.length(); i++) {
            if (!Character.isDigit(phoneNumber.charAt(i)) && phoneNumber.charAt(i) != '+') {
                showMessage(context.getResources().getString(R.string.error_invalidPhoneNumber));
                return false;

            }
        }
        return true;
    }
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public boolean Valid_userType(USER_TYPE user_type) {
        if (user_type == null) {
            showMessage(context.getResources().getString(R.string.error_missingData));
            return false;
        }
        return true;
    }


/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    public  boolean Valid_price(String price){
        if(price == null || price.isEmpty()){
            showMessage(context.getResources().getString(R.string.error_missingData));
            return false;
        }
        for (int i = 0; i < price.length(); i++) {
            if (!Character.isDigit(price.charAt(i)) && price.charAt(i)!= '.') {
                showMessage(context.getResources().getString(R.string.error_invalidPrice));
                return false;
            }
        }
        return true;
    }
/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public boolean Valid_InGeneralNotEmpty(String s){
            if(s == null || s.isEmpty()){
                showMessage(context.getResources().getString(R.string.error_missingData));
                return  false;
            }
        return true;
    }
/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public boolean Valid_listOfTypeAndQuantity(ArrayList<NodeTypeQuantity> list){
        if(list == null || list.size() ==0){
            showMessage(context.getResources().getString(R.string.error_listTypeQuantityIsEmpty));
            return false;
        }
        return true;
    }
/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public boolean Valid_Barcode(String barcode){
        if(barcode == null || barcode.length() ==0){
            showMessage(context.getResources().getString(R.string.error_missingScanBarcode));
            return false;
        }
        return true;
    }
/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/



}
