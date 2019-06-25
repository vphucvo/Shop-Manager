package com.khashan.rand.shopmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class DisplayAllUsers_ACTIVITY extends AppCompatActivity {


    private ListView listView;
    private SearchView searchView;
    private ProgressBar progressBar;
    private DatabaseReference reference;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_all_users);

        initialization();
        setDataToListView();


    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    private void initialization() {

        listView = (ListView) findViewById(R.id.lv_allUsers);
        searchView = (SearchView) findViewById(R.id.searchView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        reference = FirebaseDatabase.getInstance().getReference(CurrentUser.workAt_ShopName).child(Firebase_keys.ShopUsers);

    }


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    private void setDataToListView() {
        progressBar.setVisibility(View.VISIBLE);
        MyFirebaseListView adapter = new MyFirebaseListView(this, User.class, R.layout.listview_users_row, reference);
        listView.setAdapter(adapter);
    }




    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~      FirebaseListView abstract class                 ~~~~~~~~~~~~~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/


    private class MyFirebaseListView extends FirebaseListAdapter<User> {


        private int currentPosition;

        /**
         * @param activity    The activity containing the ListView
         * @param modelClass  Firebase will marshall the data at a location into an instance of a class that you provide
         * @param modelLayout This is the layout used to represent a single list item. You will be responsible for populating an
         *                    instance of the corresponding view with the data from an instance of modelClass.
         * @param ref         The Firebase location to watch for data changes. Can also be a slice of a location, using some
         *                    combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
         */
        public MyFirebaseListView(Activity activity, Class<User> modelClass, int modelLayout, Query ref) {
            super(activity, modelClass, modelLayout, ref);
        }

        @Override
        protected void populateView(View view, final User user, int position) {

            progressBar.setVisibility(View.INVISIBLE);
            currentPosition = position;

            TextView tv_nameType = (TextView) view.findViewById(R.id.tv_userNameType);
            TextView tv_wortAtBranch = (TextView) view.findViewById(R.id.tv_userAtBranch);
            TextView tv_phone = (TextView) view.findViewById(R.id.tv_userPhoneNumber);
            ImageButton b_call = (ImageButton) view.findViewById(R.id.imageButton_call);
            ImageButton b_popupDeleteUser = (ImageButton) view.findViewById(R.id.b_popup_deleteUser);
            TextView tv_permission = (TextView) view.findViewById(R.id.tv_userPermition);


            tv_nameType.setText(getResources().getString(R.string.userName) + ": " + user.getName());
            tv_wortAtBranch.setText(user.getStringUserType(getApplicationContext()) + " at " + getResources().getString(R.string.branch) + " - " + user.getWorkAtBranch());
            tv_phone.setText(user.getPhone());
            tv_permission.setText(getResources().getString(R.string.profile_permition) + ": " +
                    new Permission(getApplicationContext()).getPermissionString(user.getPermission())
                    );


            // ------------------- only the owner has the right to delete a user ...
            if (!CurrentUser.user.getUserType().equals(USER_TYPE.OWNER)) {
                b_popupDeleteUser.setVisibility(View.INVISIBLE);
            } else {

                b_popupDeleteUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClick_popUpMenu(v,user);
                    }
                });
            }

            // -------------------  event lisener for call ...
            b_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClick_callButton(user);
                }
            });





        }
       /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

        private void onClick_callButton(User user){

            Intent i = new Intent(Intent.ACTION_CALL);
            i.setData(Uri.parse("tel:" + getItem(currentPosition).getPhone()));
            startActivity(Intent.createChooser(i, "Call .."));

        }
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

        private void onClick_popUpMenu(View v ,final User user){

            // listener for popup menue
            PopupMenu pop = new PopupMenu(getApplicationContext(), v);
            MenuInflater inflater = pop.getMenuInflater();
            inflater.inflate(R.menu.popup_delete_user, pop.getMenu());
            pop.show();

            pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    deleteUser(user);
                    return false;
                }
            });


        }

        /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
        private void deleteUser(final User user){

            AlertDialog.Builder dialog = new AlertDialog.Builder(this.mActivity);

            dialog.setMessage(getResources().getString(R.string.msg_dilogDeleteUser))

                    .setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                             Firebase firebase = new Firebase(Firebase_keys.firebaseURL)
                                    .child(CurrentUser.workAt_ShopName)
                                    .child(Firebase_keys.ShopUsers)
                                    .child(Firebase_keys.branch + user.getWorkAtBranch() + user.getName());
                                firebase.setValue(null);

                        }
                    })


                    .setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }



    }
}