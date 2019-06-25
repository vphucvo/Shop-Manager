package com.khashan.rand.shopmanager;


import android.content.Context;

import java.util.Arrays;

public class Permission {


    public static String PermissionKey_addManager ="0";
    public static String PermissionKey_addEmployee ="1";
    public static String PermissionKey_addProducts ="2";
    public static String PermissionKey_editDeleteProduts ="3";
    public static String PermissionKey_retriveUsers ="4";
    public static String PermissionKey_sellGoods ="5";
    public static String PermissionKey_All ="6";
    public static String PermissionKey_retriveProducts ="4";


    private Context context;
    private String[]permissionArray;




    public  Permission(Context context){
        this.context=context;
        permissionArray=new String[7];
        fillPermissionArray();
    }


  /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void fillPermissionArray(){

        permissionArray[0] = context.getResources().getString(R.string.permissions_AddManager);
        permissionArray[1] = context.getResources().getString(R.string.permissions_AddEmployee);
        permissionArray[2] = context.getResources().getString(R.string.permissions_AddGoods);
        permissionArray[3] = context.getResources().getString(R.string.permissions_DeleteOrEditGoods);
        permissionArray[4] = context.getResources().getString(R.string.permissions_RetriveUsers);
        permissionArray[5] = context.getResources().getString(R.string.permissions_sellGoods);
        permissionArray[6] = context.getResources().getString(R.string.permissions_ALL);
    }


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public String[]getPermissionArray(){
        return permissionArray;
    }

     /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

     public String getPermissionString(String permission){

         String []arr=permission.split(" ");
         String s="\n";

         for (int i=0 ; i<arr.length ; i++) {

             try {

             s+=permissionArray[Integer.parseInt(arr[i])]+"\n";

             }catch (Exception e){

             }

         }

         return  s;
     }



}
