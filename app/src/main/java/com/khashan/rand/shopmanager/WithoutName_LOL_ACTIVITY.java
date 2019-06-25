package com.khashan.rand.shopmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.firebase.client.Firebase;

public class WithoutName_LOL_ACTIVITY extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout draweLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    private TextView tv_name;
    private TextView tv_workAt;
    private TextView tv_adapter_addedBy;
    private TextView tv_permition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_without_name__lol);

        Firebase.setAndroidContext(getApplicationContext()); // permission for the firebase LOOOOL

        manage_navigation_drawerLayout();
        initialization();
        fill_textViews_with_account_information();


    }



    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    private void initialization() {

        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_workAt = (TextView) findViewById(R.id.tv_workAt);
        tv_adapter_addedBy = (TextView) findViewById(R.id.tv_adapter_addedBy);
        tv_permition = (TextView) findViewById(R.id.tv_permition);
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
    private void fill_textViews_with_account_information() {
        User user = CurrentUser.user;
        if (user == null) {
            return;
        }
        if (user.getUserType().equals(USER_TYPE.OWNER)) {
            tv_name.append(" " + user.getName());
            tv_workAt.append(" " + CurrentUser.workAt_ShopName);
            tv_adapter_addedBy.append(" him/herself hhhhhh :v");
            tv_permition.setText(""); // implement toString الي
        } else {
            tv_name.append(" " + user.getName());
            tv_workAt.append(" " + getResources().getString(R.string.branch) + " " + user.getWorkAtBranch());
            tv_adapter_addedBy.append(" " + user.getAddedBy());
            tv_permition.append(" " + new Permission(getApplicationContext()).getPermissionString(user.getPermission())); // implement toString الي
            manageNavigationMenuAccordingToUserPermission(user.getPermission());


        }
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
    private void manageNavigationMenuAccordingToUserPermission(String permission) {

        Menu menu = navigationView.getMenu();


        if (!permission.contains(Permission.PermissionKey_addEmployee) && !permission.contains(Permission.PermissionKey_addManager)) {
            menu.removeItem(R.id.menu_addUser);
        }

        if (!permission.contains(Permission.PermissionKey_addProducts)) {
            menu.removeItem(R.id.menu_addProduct);
        }

        if (!permission.contains(Permission.PermissionKey_retriveProducts)) {
            menu.removeItem(R.id.menu_viewAllProducts);
        }

        if (!permission.contains(Permission.PermissionKey_retriveUsers)) {
            menu.removeItem(R.id.menu_view_all_users);
        }

        menu.removeItem(R.id.menu_view_sales); // only the owner can see the sales


    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
    private void manage_navigation_drawerLayout() {

        draweLayout = (DrawerLayout) findViewById(R.id.activity_without_name__lol);
        toggle = new ActionBarDrawerToggle(this, draweLayout, R.string.open, R.string.close);
        draweLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

    }
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        // each item in the (( main_menu )) will display one fragment and put it in the activity
        switch (item.getItemId()) {

            case R.id.menu_myAccount:
                startActivity(new Intent(getApplicationContext(), AccountSetting_ACTIVITY.class));
                ;
                break;

            case R.id.menu_logOut:
                CurrentUser.delete();
                startActivity(new Intent(getApplicationContext(), MainActivity_ACTIVITY.class));
                break;

            case R.id.menu_addUser:
                startActivity(new Intent(getApplicationContext(), AddUser_ACTIVITY.class));
                break;

            case R.id.menu_addProduct:
                startActivity(new Intent(getApplicationContext(), AddProduct_ACTIVITY.class));
                break;

            case R.id.menu_viewAllProducts:
                startActivity(new Intent(getApplicationContext(), DisplayAllProducts_ACTIVITY.class));
                break;

            case R.id.menu_view_all_users:
                startActivity(new Intent(getApplicationContext(), DisplayAllUsers_ACTIVITY.class));
                break;


        }
        return true;
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // hamburger button for the navigation drawer HaHaHaHaHa
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return false;
    }


}
