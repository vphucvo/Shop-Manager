package com.khashan.rand.shopmanager;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Scanner;


public class MyCurrencySpinner extends Spinner{

    public static String SHEKEL;
    public static String JOD;
    public static String DOLLAR;

    private  String []arr=new String[3];
    private boolean isFilled=false;

    private Context context;
    public MyCurrencySpinner(Context context) {
        super(context);
        this.context=context;
        fillCurrencySpinner();
    }

    public MyCurrencySpinner(Context context, int mode) {
        super(context, mode);
        this.context=context;
        fillCurrencySpinner();
    }

    public MyCurrencySpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        fillCurrencySpinner();
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void fillCurrencySpinner(){
        if(isFilled){
            return;
        }
        arr[0]=getResources().getString(R.string.currency_shekel);
        arr[1]=getResources().getString(R.string.currency_dinar);
        arr[2]=getResources().getString(R.string.currency_dollar);
        ArrayAdapter<String> adapter=new ArrayAdapter(context,R.layout.spinner_item,arr);
        this.setAdapter(adapter);
        isFilled=true;
    }

  /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public String getCurrencyCode(){
        return getSelectedItemPosition()+"";
    }

  /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public  String getCurencySympole(String code){

        try {
            return arr[Integer.parseInt(code)];
        }catch (Exception e ){
            return "-";
        }

    }
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/


}
