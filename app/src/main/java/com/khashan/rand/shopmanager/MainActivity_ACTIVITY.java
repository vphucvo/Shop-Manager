package com.khashan.rand.shopmanager;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class MainActivity_ACTIVITY extends AppCompatActivity implements View.OnClickListener, ValueEventListener {


    private EditText et_shopName;
    private EditText et_userName;
    private EditText et_userPassword;
    private TextView tv_signUp;
    private Button b_login;
    private RadioButton rb_owner;
    private RadioButton rb_employee;
    private RadioButton rb_manager;
    private MyBranchSpinner sp_branch;
    private ProgressBar progressBar;

    private Firebase firebase;
    private boolean successfully_logged_in = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeComponent();

        Firebase.setAndroidContext(getApplicationContext());


    }
/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

    private void initializeComponent() {

        et_shopName = (EditText) findViewById(R.id.et_shopName);
        et_userName = (EditText) findViewById(R.id.et_userName);
        et_userPassword = (EditText) findViewById(R.id.et_password);
        tv_signUp = (TextView) findViewById(R.id.tv_signUp);
        b_login = (Button) findViewById(R.id.b_logIn);
        rb_owner = (RadioButton) findViewById(R.id.rb_owner);
        rb_employee = (RadioButton) findViewById(R.id.rb_employee);
        rb_manager = (RadioButton) findViewById(R.id.rb_manager);
        sp_branch = (MyBranchSpinner) findViewById(R.id.sp_branch);
        progressBar=(ProgressBar)findViewById(R.id.proggressBar_main);


        tv_signUp.setOnClickListener(this);
        b_login.setOnClickListener(this);

    }


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/
    private boolean checkInternetConnection(){

        ConnectivityManager maneger= (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        return  maneger.getActiveNetworkInfo() != null && maneger.getActiveNetworkInfo().isConnected();
    }
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/
    private USER_TYPE getUserType() {

        if (rb_owner.isChecked()) {
            return USER_TYPE.OWNER;
        } else if (rb_manager.isChecked()) {
            return USER_TYPE.MANAGER;
        } else if (rb_employee.isChecked()) {
            return USER_TYPE.EMPLOYEE;
        } else {
            return null;
        }
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/
    @Override
    public void onClick(View v) {

        if(checkInternetConnection() == false){

            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.error_noInternetConnection)
            ,Toast.LENGTH_SHORT).show();
            return;
        }


        switch (v.getId()) {

            case R.id.b_logIn:
                progressBar.setVisibility(ProgressBar.VISIBLE);
                logIn();
                break;

            case R.id.tv_signUp:
                startActivity(new Intent(getApplicationContext(), SignUp_ACTIVITY.class));
                this.finish();
                break;
        }

    }

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/


    private String shopName;
    private String userName;
    private String password;
    private USER_TYPE userType;

    private void getDataInputFromComponent() {

        shopName = et_shopName.getText().toString().trim();
        userName = et_userName.getText().toString().trim();
        password = et_userPassword.getText().toString().trim();
        userType = getUserType();

    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/
    private void logIn() {

        getDataInputFromComponent(); // method up  ↑↑↑↑

        Check check = new Check(getApplicationContext());

        // if the user didn't select any RadioButton
        if (!check.Valid_userType(getUserType())) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_missingData), Toast.LENGTH_SHORT).show();
            return;
        }

        // get refrance to myShop in firebase
        {
            firebase = new Firebase(Firebase_keys.firebaseURL)
                    .child(shopName);
        }

        // get a refrance from firebase to the user accont>> if exists or
        if (userType.equals(USER_TYPE.OWNER)) {
            firebase.child(Firebase_keys.ShopDetails).addListenerForSingleValueEvent(this);
        } else {
            setShopType();
            firebase.child(Firebase_keys.ShopUsers)
                    .child(userName).addListenerForSingleValueEvent(this);

        }


    }
/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void setShopType(){

        final Firebase fire = firebase.child(Firebase_keys.ShopDetails);
        fire.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    Shop shop=dataSnapshot.getValue(Shop.class);
                    CurrentUser.shopType=shop.getType();
                    fire.removeEventListener(this);

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                fire.removeEventListener(this);
            }
        });

    }

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    private void failed_to_login(){
        String msg = getResources().getString(R.string.error_invalidAccountInfo);
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        firebase.removeEventListener(this);
    }
/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/


    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        progressBar.setVisibility(ProgressBar.INVISIBLE);

        if (!dataSnapshot.exists()) {
            failed_to_login();
            return;
        }


        if (userType.equals(USER_TYPE.OWNER)) {

            Shop shop = dataSnapshot.getValue(Shop.class); //  get inserted shop from firebase
            Owner owner=shop.getOwner();

            if (shop != null  && owner!=null && owner.getName().equals(userName) && owner.getPassword().equals(password)) {
                successfully_logged_in = true;
                User u = new User(userName, password, owner.getPhoneNumber() ,USER_TYPE.OWNER, -1, null, "");
                CurrentUser.user = u;  // set the current user;
                CurrentUser.workAt_ShopName=shopName;
                CurrentUser.shopType=shop.getType();

            } else {
                // do nothing but becouse nested else-if
            }

        } else {
            //  maneger or employee
            User user = dataSnapshot.getValue(User.class);
            if (user != null && user.getPassword().equals(password) && user.getUserType().equals(getUserType())) {
                successfully_logged_in = true;
                CurrentUser.user = user;  // set the current user;
                CurrentUser.workAt_ShopName=shopName;
            }
        }


        /// ------------ go to next activity if logged_in success
        if (successfully_logged_in) {
            startActivity(new Intent(getApplicationContext(), WithoutName_LOL_ACTIVITY.class));
            // remove th connection between app and firebase
            firebase.removeEventListener(this);

        } else {
            failed_to_login();
        }
    }

    /*********************************************************************************************/
    @Override
    public void onCancelled(FirebaseError firebaseError) {
        if (firebase != null) {
            firebase.removeEventListener(this);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
