package com.khashan.rand.shopmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


public class SignUp_ACTIVITY extends AppCompatActivity implements View.OnClickListener {

    private EditText et_shopName;
    private Spinner sp_shopType;
    private EditText et_OwnerName;
    private EditText et_phoneNumber;
    private EditText et_OwnerPassword;
    private CheckBox cb_paymentAgree;
    private Button b_signUp;
    private ProgressBar progressBar;

    private Firebase firebase ;
    private Shop shop;
    private boolean signSuccess=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Firebase.setAndroidContext(getApplicationContext());
        firebase = new Firebase(Firebase_keys.firebaseURL);

        initialization();
        fillTheSpinner();


    }


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void initialization() {

        et_shopName = (EditText) findViewById(R.id.et_shopName);
        sp_shopType = (Spinner) findViewById(R.id.sp_shopType);
        et_OwnerName = (EditText) findViewById(R.id.et_ownerName);
        et_phoneNumber=(EditText)findViewById(R.id.et_phoneNumber);
        et_OwnerPassword = (EditText) findViewById(R.id.et_ownerPassword);
        cb_paymentAgree = (CheckBox) findViewById(R.id.cb_paymentAgree);
        b_signUp = (Button) findViewById(R.id.b_signUp);
        progressBar=(ProgressBar)findViewById(R.id.progressBar_signUp) ;

        b_signUp.setOnClickListener(this);
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    private void fillTheSpinner() {

        String[] arr = new String[2];
        arr[0] = getResources().getString(R.string.shopType_clothes);
        arr[1] = getResources().getString(R.string.shopType_foodstuffs);
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, arr);
        sp_shopType.setAdapter(adapter);
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    private String getShopTypeFromTheSpinner() {

        switch (sp_shopType.getSelectedItemPosition()){

            case 0 : return Shop.CLOTHES;
            case 1 : return Shop.FOODSTUFF;
        }

        return Shop.CLOTHES;
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @Override
    public void onClick(View v) {

        progressBar.setVisibility(ProgressBar.VISIBLE);

        String shopName = et_shopName.getText().toString().trim();
        String ownerName = et_OwnerName.getText().toString().trim();
        String phoneNumber = et_phoneNumber.getText().toString().trim();
        String ownerPass = et_OwnerPassword.getText().toString().trim();

        Check check = new Check(getApplicationContext());

        // if one of then is false , then exsis from the method
        if (!check.valid_shopName(shopName) || !check.valid_userName(ownerName) || !check.valid_password(ownerPass) || !check.valid_phoneNumber(phoneNumber) ) {
            return;
        }

        // check of the user agree to bay
        if (!cb_paymentAgree.isChecked()) {
            String msg = getResources().getString(R.string.error_paymentAgreement);
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            return;
        }

        Owner owner=new Owner(ownerName,ownerPass,phoneNumber);
         shop = new Shop(shopName, getShopTypeFromTheSpinner(), owner);

        insert_shop_to_firebase(shop);


    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     *
     * thats two methods implements by ValueEventListener >> how its work
     *  >> onDataChange : get the firebase refrance from firebase to the same shopName the user want to creat
     *     is the chiled is eists so the user can't creat this account beacouse duplicate userName is not allowed
     *     otherwise >> invoke method 'insert'   will insert the shopname and information ...ets
     */
    private void insert_shop_to_firebase(final Shop shop) {

        final Firebase fire = firebase.child(shop.getName());

        fire.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                progressBar.setVisibility(ProgressBar.INVISIBLE);

                if (dataSnapshot.exists() && !signSuccess) {
                    String msg = getResources().getString(R.string.error_shopNameAlreadyExists);
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    return ;
                } else {
                    fire.child(Firebase_keys.ShopDetails).setValue(shop);
                    signSuccess=true;
                    saveCurrentUserInformation();
                    fire.removeEventListener(this); // عشان الفيربيس شوي متخلفه ان ما ازلته بصير مشاكل ههه كه كه كه
                    return;
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private  void saveCurrentUserInformation(){

        User currentuser=new User(shop.getOwner().getName(),shop.getOwner().getPassword(), shop.getOwner().getPhoneNumber(),USER_TYPE.OWNER,-1,null,"");
        CurrentUser.user=currentuser;
        CurrentUser.workAt_ShopName=shop.getName();
        CurrentUser.storeCurrentUserInMobileMemorrey(getApplicationContext());

        startActivity(new Intent(getApplicationContext(),WithoutName_LOL_ACTIVITY.class));
        this.finish();
    }





}