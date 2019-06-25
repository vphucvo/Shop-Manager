package com.khashan.rand.shopmanager;



public class Shop {

    public static final String CLOTHES="Clothes";
    public static final String FOODSTUFF="Foodstuff";

    private String name;
    private Owner owner;
    private String type;

    public Shop(){

    }

    public Shop(String name, String type,Owner owner){

        this.setName(name);
        this.setType(type);
        this.setOwner(owner);
    }


    @Override
    public String toString() {
        return Firebase_keys.ShopDetails; // Firebase_keys is an interface LOOOL
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }
}
