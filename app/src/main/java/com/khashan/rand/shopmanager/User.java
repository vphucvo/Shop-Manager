package com.khashan.rand.shopmanager;


import android.content.Context;

import java.util.HashMap;

/**
 *  if the user is owner >> so
 *  USER_TYPE =OWNER
 *  workAtBranch =-1
 *  Permissions=null
 *  addBy =""
 *
 *
 */
public class User {

    private String name;
    private String password;
    private String phone;
    private USER_TYPE userType;
    private int workAtBranch;
    private String permission;
    private String addedBy;

    public User() {

    }

    public User(String name, String password, String phone, USER_TYPE userType, int workAtBranch,String permission,String addedBy) {
        this.setName(name);
        this.setWorkAtBranch(workAtBranch);
        this.setUserType(userType);
        this.setPassword(password);
        this.setPhone(phone);
        this.setPermission(permission);
        this.setAddedBy(addedBy);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public int getWorkAtBranch() {
        return workAtBranch;
    }

    public void setWorkAtBranch(int workAtBranch) {
        this.workAtBranch = workAtBranch;
    }

    public USER_TYPE getUserType() {
        return userType;
    }

    public void setUserType(USER_TYPE userType) {
        this.userType = userType;
    }


    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStringUserType(Context context){

        switch (userType) {

            case MANAGER: return context.getResources().getString(R.string.Manager);
            case EMPLOYEE: return context.getResources().getString(R.string.employee);
        }
        return "";
    }

}


