package com.khashan.rand.shopmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class DisplayAllProducts_ACTIVITY extends AppCompatActivity implements View.OnClickListener{



    private Button b_scanBarcode;
    private ListView lv_displayAllData;
    private String barcode="";
    private ProgressBar progressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_all_products);

        // step 1
        initialization();

        // step 2
        setDataToListView();

    }



    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void initialization(){
        b_scanBarcode=(Button)findViewById(R.id.b_serchByBarcode);
        lv_displayAllData= (ListView) findViewById(R.id.lv_displayAllData);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);


    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    private void setDataToListView(){
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child(CurrentUser.workAt_ShopName)
                .child(Firebase_keys.ShopData);


        MyFirebaseListAdapter_Products adapter=new MyFirebaseListAdapter_Products(
                this,ProductDetails.class,R.layout.listview_products_row,ref, progressBar
        );

        lv_displayAllData.setAdapter(adapter);



    }
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.b_serchByBarcode){
            scanBarcode();
            if(!barcode.isEmpty()){
                int i=0;

                lv_displayAllData.smoothScrollToPosition(i);
            }

        }

    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    private void scanBarcode(){

        IntentIntegrator intent=new IntentIntegrator(this);
        intent.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intent.setPrompt("Tap to scan");
        intent.setBeepEnabled(true);
        intent.initiateScan();


    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            barcode = result.getContents();
        } else {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.error_scanFailed), Toast.LENGTH_SHORT).show();
        }
    }
}


