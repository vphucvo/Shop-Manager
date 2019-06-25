package com.khashan.rand.shopmanager;



public class Owner {

    private String name;
    private String password;
    private String phoneNumber;



    public Owner(){

    }

    public Owner(String name, String password, String phoneNumber){
        this.setName(name);
        this.setPassword(password);
        this.setPhoneNumber(phoneNumber);
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
