package com.khashan.rand.shopmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

public class Sell_ACTIVITY extends AppCompatActivity {

    ImageButton b_popUp;
    TextView tv_name_madein;
    TextView tv_price;
    TextView tv_addedBy_date ;
    TextView tv_quantity ;
    Spinner sp_type;
    MyBranchSpinner sp_branch ;
    TextView tv_editedBy_date;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_);




    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    private void initialization(){
        ImageButton b_popUp = (ImageButton) findViewById(R.id.b_popup_deleteUser);
        TextView tv_name_madein = (TextView) findViewById(R.id.tv_adapter_productName);
        TextView tv_price = (TextView) findViewById(R.id.tv_adapter_productPrice);
        TextView tv_addedBy_date = (TextView) findViewById(R.id.tv_adapter_addedBy);
        TextView tv_quantity = (TextView) findViewById(R.id.tv_adapter_quantity);
        Spinner sp_type = (Spinner) findViewById(R.id.sp_adapter_productType);
        MyBranchSpinner sp_branch = (MyBranchSpinner) findViewById(R.id.sp_branch);
        TextView tv_editedBy_date = (TextView) findViewById(R.id.tv_adapter_edditedBy);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        b_popUp.setVisibility(View.INVISIBLE);
        ratingBar.setVisibility(View.INVISIBLE);
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
}
