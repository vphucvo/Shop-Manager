package com.khashan.rand.shopmanager;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;

public class MyFirebaseListAdapter_Products extends FirebaseListAdapter<ProductDetails> {


    private Context context;
    private ProgressBar progressBar;

    /**
     * @param activity    The activity containing the ListView
     * @param modelClass  Firebase will marshall the data at a location into an instance of a class that you provide
     * @param modelLayout This is the layout used to represent a single list item. You will be responsible for populating an
     *                    instance of the corresponding view with the data from an instance of modelClass.
     * @param ref         The Firebase location to watch for data changes. Can also be a slice of a location, using some
     *                    combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */
    public MyFirebaseListAdapter_Products(Activity activity, Class<ProductDetails> modelClass, int modelLayout, Query ref, ProgressBar progressBar) {
        super(activity, modelClass, modelLayout, ref);
        this.context=activity.getApplicationContext();
        this.progressBar=progressBar;
    }






    /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/


    @Override
    protected void populateView(View view, final ProductDetails product, final int position) {

        // hide the progressBar
        progressBar.setVisibility(View.INVISIBLE);

        final ImageButton b_popUp=(ImageButton) view.findViewById(R.id.b_popup_deleteUser);
        TextView  tv_name_madein = (TextView) view.findViewById(R.id.tv_adapter_productName);
        TextView tv_price = (TextView) view.findViewById(R.id.tv_adapter_productPrice);
        TextView  tv_addedBy_date = (TextView) view.findViewById(R.id.tv_adapter_addedBy);
        final TextView tv_quantity = (TextView) view.findViewById(R.id.tv_adapter_quantity);
        final Spinner sp_type = (Spinner) view.findViewById(R.id.sp_adapter_productType);
        MyBranchSpinner  sp_branch = (MyBranchSpinner) view.findViewById(R.id.sp_branch);
        TextView tv_editedBy_date = (TextView) view.findViewById(R.id.tv_adapter_edditedBy);
        final RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);

        // set the data to componnent
        tv_name_madein.setText(product.getName()+" - "+product.getMadeIn());

        tv_price.setText(product.getFirstPrice()+" - "+product.getFinalPrice()+" "+
                new MyCurrencySpinner(context).getCurencySympole(product.getCurrency()));

        tv_addedBy_date.setText(context.getResources().getString(R.string.By) + " " +
                product.getAddedBy() + " " +
                product.getAddedDate());


        // if the product was edited ...
        if (product.getEditetBy() != null && !product.getEditetBy().toString().isEmpty()) {
            tv_editedBy_date.setText(context.getResources().getString(R.string.laseEdited) + " " +
                    product.getLaseEditeDate() + " " + context.getResources().getString(R.string.By) + " " + product.getEditetBy());
        }else{
            tv_editedBy_date.setText("");
        }

        /// branch spinner --------------
        sp_branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onItemCliclListener_spBranch(position,product, sp_type,tv_quantity,ratingBar);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                onItemCliclListener_spBranch(0,product, sp_type,tv_quantity,ratingBar);
            }
        });


    //  set the listener for popup imagebutton >> create popup menu  ---------
        b_popUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_createPopupMenu(b_popUp, product ,getRef(position));
            }
        });






    }



    /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void onItemCliclListener_spBranch(int position, ProductDetails pro, Spinner sp_type, final TextView tv_quantity, final RatingBar ratingBar) {

        sp_type.setAdapter(null); // clean all previouse item

        HashMap<String,ArrayList<NodeTypeQuantity>> table=pro.getMap();

        final ArrayList<NodeTypeQuantity> list = table.get(Firebase_keys.branch+(position+1)); // get the list for sprsific branh

        // check if the branch is not  existe
        if(list == null){
            tv_quantity.setText("");
            ratingBar.setRating(0f);
            return;
        }

        // set data to sp_type
        ArrayAdapter adapter=new ArrayAdapter(context,R.layout.spinner_item,list); // toString for this node is the type
        sp_type.setAdapter(adapter);

        // make the action when sp_type items selected ...
        sp_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                NodeTypeQuantity node=list.get(position);
                int remain=node.getOriginalQuantity()-node.getSoldQuantity();
                tv_quantity.setText(remain+"/"+node.getOriginalQuantity());

                float reate=(node.getSoldQuantity()/node.getOriginalQuantity())*(5.0f);
                ratingBar.setRating(reate);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                NodeTypeQuantity node=list.get(0);
                int remain=node.getOriginalQuantity()-node.getSoldQuantity();
                tv_quantity.setText(remain+"/"+node.getOriginalQuantity());
            }
        });

    }

    /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    private void onClick_createPopupMenu(View v, ProductDetails product,  DatabaseReference ref){
        PopupMenu pop = new PopupMenu(context, v);
        MenuInflater inflater = pop.getMenuInflater();
        inflater.inflate(R.menu.popup_menu, pop.getMenu());
        pop.show();
        PopUp_onItemClickListener(pop,product, ref);
    }

    /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    private void PopUp_onItemClickListener(PopupMenu pop, final ProductDetails product, final DatabaseReference ref) {

        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.popup_deleteProduct) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);

                    dialog.setMessage(context.getResources().getString(R.string.msg_dialogDeleteProduct));

                    dialog.setPositiveButton(context.getResources().getString(R.string.Yes),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    ref.setValue(null);

                                }
                            });

                    dialog.setNegativeButton(context.getResources().getString(R.string.No),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // nothing to do hhhhhhhhhhhhh
                                }
                            });

                    dialog.show();

                }else if(item.getItemId() == R.id.popup_editProduct){
                    // go to add pro activity
                    Intent i=new Intent(context, AddProduct_ACTIVITY.class);
                    i.putExtra(IntentKeys.key_editProduct,product);
                    context.startActivity(i);

                }

                return false;
            }
        });
    }


    /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/



}
