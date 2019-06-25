package com.khashan.rand.shopmanager;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;

public class AccountSetting_ACTIVITY extends AppCompatActivity implements View.OnClickListener{

    private TextView tv_name;
    private EditText et_pass;
    private EditText et_phone;
    private ImageButton ib_editPass;
    private ImageButton ib_editPhone;
    private Button b_save;

    private Dialog dialog;
    private boolean correctOldPassword=false;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);

        initialization();
    }


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    private void initialization(){

        tv_name=(TextView)findViewById(R.id.tv_name);
        et_pass= (EditText) findViewById(R.id.et_password);
        et_phone= (EditText) findViewById(R.id.et_phoneNum);
        ib_editPass=(ImageButton)findViewById(R.id.ib_editPassword);
        ib_editPhone=(ImageButton)findViewById(R.id.ib_editPhone);
        b_save=(Button)findViewById(R.id.b_saveSetting);

        et_pass.setFocusableInTouchMode(false);
        et_phone.setFocusableInTouchMode(false);
        b_save.setFocusableInTouchMode(false);
        b_save.setAlpha(0.5f);

        fillComponnentWithAccountInfo();

        ib_editPhone.setOnClickListener(this);
        ib_editPass.setOnClickListener(this);
        b_save.setOnClickListener(this);

    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private  void fillComponnentWithAccountInfo(){
        tv_name.setText(CurrentUser.user.getName());
        et_pass.setText(CurrentUser.user.getPassword());
        et_phone.setText(CurrentUser.user.getPhone());
    }
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    private void createDialog(){


        final EditText et_oldPass=new EditText(getApplicationContext());
        et_oldPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        et_oldPass.setPadding(15,15,15,15);
        et_oldPass.setTextColor(Color.BLACK);

        AlertDialog.Builder builder= new AlertDialog.Builder(this);

        builder.setTitle(getResources().getString(R.string.oldPassword));

        builder.setView(et_oldPass);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String oldPass=et_oldPass.getText().toString();

                    if(CurrentUser.user.getPassword().equals(oldPass)){
                        System.out.println("********************* corrct pass "+oldPass);
//                        correctOldPassword=true;
                        et_pass.setFocusableInTouchMode(true);
                        et_phone.setFocusableInTouchMode(true);
                        b_save.setFocusableInTouchMode(true);
                        b_save.setAlpha(1f);
                    }else{
//                        correctOldPassword=false;
                        et_pass.setFocusable(false);
                        et_phone.setFocusable(false);
                        b_save.setAlpha(0.5f);
                        b_save.setFocusableInTouchMode(false);

                }

                et_oldPass.setText("");
            }
        });

        dialog=builder.create();

    }


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    @Override
    public void onClick(View v) {

        createDialog();


        switch (v.getId()) {

            case R.id.ib_editPassword :
                dialog.show();
                ;break;

            case R.id.ib_editPhone :
                dialog.show();
                ;break;

            case R.id.b_saveSetting:
                b_save.setFocusableInTouchMode(false);
                b_save.setAlpha(0.5f);
                editAccountInformation();
                ;break;


        }



    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    private void editAccountInformation(){

        String newPass=et_pass.getText().toString();
        String newPhone=et_phone.getText().toString();
        Check check =new Check(getApplicationContext());
        if(!check.valid_password(newPass) || !check.valid_phoneNumber(newPhone)){
            return;
        }

        Firebase firebase=null;
        if(CurrentUser.user.getUserType().equals(USER_TYPE.OWNER)){

            Owner owner=new Owner(CurrentUser.user.getName(),newPass,newPhone);

            firebase = new Firebase(Firebase_keys.firebaseURL)
                    .child(CurrentUser.workAt_ShopName)
                    .child(Firebase_keys.ShopDetails)
                    .child(Firebase_keys.owner_key);

            firebase.getRef().setValue(owner);
            CurrentUser.user.setPassword(newPass);
            CurrentUser.user.setPhone(newPhone);

        }else {
            firebase = new Firebase(Firebase_keys.firebaseURL)
                    .child(CurrentUser.workAt_ShopName)
                    .child(Firebase_keys.ShopUsers)
                    .child(CurrentUser.user.getName());

            User user= CurrentUser.user;
            user.setPassword(newPass);
            user.setPhone(newPhone);
            firebase.getRef().setValue(user);
        }


        et_phone.setFocusable(false);
        et_pass.setFocusable(false);

        Toast.makeText(getApplicationContext(),"",Toast.LENGTH_SHORT).show();


    }


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
}
