package com.khashan.rand.shopmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;

public class AddUser_ACTIVITY extends AppCompatActivity implements View.OnClickListener {

    private EditText et_userName;
    private EditText et_userPassword;
    private EditText et_userPhone;
    private RadioButton rb_manager;
    private RadioButton rb_employee;
    private MyBranchSpinner sp_branch;
    private Button b_add;
    private Button b_choosePermossions;

    private String[]permissionArray;
    private boolean [] permissionCheckedArray;
    private String permissionsString="";
    private AlertDialog permissionsDialog;

    private User user;


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        initialization();

    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    private void initialization() {

        et_userName = (EditText) findViewById(R.id.et_userName);
        et_userPassword = (EditText) findViewById(R.id.et_userPassword);
        et_userPhone = (EditText) findViewById(R.id.et_userPhone);
        rb_manager = (RadioButton) findViewById(R.id.rb_manager);
        rb_employee = (RadioButton) findViewById(R.id.rb_employee);
        sp_branch = (MyBranchSpinner) findViewById(R.id.sp_branch);
        b_choosePermossions = (Button) findViewById(R.id.b_choosePermossions);
        b_add = (Button) findViewById(R.id.b_addUser);

        fillPermissionArray();
        creatTheAlertDialog();
        b_add.setOnClickListener(this);
        b_choosePermossions.setOnClickListener(this);


    }


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    private void fillPermissionArray() {
        int size=7;
        permissionArray = new String[size];

        // in the same order in 'Permission-interface'

        permissionArray=new Permission(getApplicationContext()).getPermissionArray();

        permissionCheckedArray=new boolean[size];
        Arrays.fill(permissionCheckedArray,false);


    }



    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    private void creatTheAlertDialog(){


        String title=getResources().getString(R.string.choosePermission);

        AlertDialog.Builder builder= new AlertDialog.Builder(this)
        .setTitle(title)

        .setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        })
        .setMultiChoiceItems(permissionArray, permissionCheckedArray, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
               if(isChecked && which == permissionArray.length-1){ // no need to mantain other permisstion if he/she select All
                   permissionsString=Permission.PermissionKey_All;
               }
                else if (isChecked && !permissionsString.contains(Permission.PermissionKey_All)) {
                    permissionCheckedArray[which]=isChecked;
                    permissionsString+=" "+which;
                }else{
                    permissionCheckedArray[which]=isChecked;
                    permissionsString.replace(" "+which,"");
                }

            }
        });
        permissionsDialog=builder.create();


    }


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private USER_TYPE getUserType() {
        if (rb_employee.isChecked()) {
            return USER_TYPE.EMPLOYEE;
        } else if (rb_manager.isChecked()) {
            return USER_TYPE.MANAGER;
        } else {
            return null;
        }
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.b_addUser: addButtonClickListener() ; break;

            case  R.id.b_choosePermossions :
                permissionsDialog.show();
                ; break;
        }



    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    private void addButtonClickListener(){
        String userName = et_userName.getText().toString().trim();
        String password = et_userPassword.getText().toString().trim();
        String phone = et_userPhone.getText().toString().trim();
        USER_TYPE userType = getUserType();

        int branch = sp_branch.getBranchNumber();

        Check check = new Check(getApplicationContext());

        if (!check.valid_userName(userName) || !check.valid_password(password) || !check.valid_phoneNumber(phone)
               || !check.Valid_userType(userType) || !check.Valid_InGeneralNotEmpty(permissionsString)) {
            return;
        }


        user = new User(userName, password,phone, userType, branch, permissionsString,CurrentUser.user.getName());
        insertUser(); // insert the user to firebase database
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    private void insertUser() {

        Firebase firebase = new Firebase(Firebase_keys.firebaseURL);

        final Firebase fire = firebase.child(CurrentUser.workAt_ShopName)
                .child(Firebase_keys.ShopUsers)
                .child(user.getName());

        // --- check if user is alredy exists if so display a msg for the user.... otherwise invoke 'insert()' method
        fire.addListenerForSingleValueEvent(new ValueEventListener() {
            boolean b=false;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists() && !b) {
                    String msg = getResources().getString(R.string.error_userNameAlreadyExists);
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    fire.setValue(user);
                    String msg = getResources().getString(R.string.sucessMsg_addUser);
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    clean();
                    b=true;
                    fire.removeEventListener(this); // عشان الفيربيس شوي متخلفه ان ما ازلته بصير مشاكل ههه كه كه كه

                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void clean() {
        et_userName.setText("");
        et_userPassword.setText("");
        et_userPhone.setText("");
        rb_employee.setSelected(true);
    }
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
}
