package com.khashan.rand.shopmanager;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Date;

public class AddProduct_ACTIVITY extends AppCompatActivity implements IntentKeys, View.OnClickListener,AdapterView.OnItemSelectedListener{

    // xml componnent
    private MyBranchSpinner sp_branchNumber;
    private EditText et_productName;
    private EditText et_madeIn;
    private EditText et_firstPrice;
    private EditText et_finalPrice;
    private MyCurrencySpinner sp_currencyType;
    private ListView lv_productTyps;
    private Button b_addTypeQuantity;
    private Button b_scanBarCode;
    private Button b_add;
    private ProgressBar progressBar;

    //---
    private String barcode;
    private Dialog dialog;

    //---
    private ArrayList<NodeTypeQuantity> list_typeQuantity;
    private Adapter_AddTypeQuantity adapter;

    //---
    private Firebase firebase;

    //--- to edit the product
    private ProductDetails previousProduce=null;


    public AddProduct_ACTIVITY(){

    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        initialization();

        previousProduce= (ProductDetails) getIntent().getSerializableExtra(IntentKeys.key_editProduct);
        if(previousProduce != null){
            putDataToComponent();
            b_add.setText(getResources().getString(R.string.edit)); // beouse here is the edit activity not add !!

        }


    }


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void initialization(){
        sp_branchNumber=(MyBranchSpinner)findViewById(R.id.sp_branchNumber);
        et_productName = (EditText) findViewById(R.id.et_productName);
        et_madeIn = (EditText) findViewById(R.id.et_madeIn);
        et_firstPrice = (EditText) findViewById(R.id.et_firstPrice);
        et_finalPrice = (EditText) findViewById(R.id.et_finalPrice);
        sp_currencyType = (MyCurrencySpinner) findViewById(R.id.sp_currency);
        lv_productTyps=(ListView)findViewById(R.id.lv_productTyps);
        b_addTypeQuantity = (Button) findViewById(R.id.b_addTypeQuantity);
        b_scanBarCode = (Button) findViewById(R.id.b_scanBarcode);
        b_add = (Button) findViewById(R.id.b_addProduct);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);


        b_addTypeQuantity.setOnClickListener(this);
        b_scanBarCode.setOnClickListener(this);
        b_add.setOnClickListener(this);

        setAdapterToListView();

        sp_branchNumber.setOnItemSelectedListener(this);


    }


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    // Edit prioduct - fill the complnent with data ..
    private void putDataToComponent(){
        sp_branchNumber.setSelection(getIntent().getIntExtra(Key_branchWantToBeEdited,0));
        sp_branchNumber.setE
        et_productName.setText(previousProduce.getName());
        et_madeIn.setText(previousProduce.getMadeIn());
        et_firstPrice.setText(previousProduce.getFirstPrice());
        et_finalPrice.setText(previousProduce.getFinalPrice());
        sp_currencyType.setVerticalScrollbarPosition(Integer.parseInt(previousProduce.getCurrency()));

    }


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void setAdapterToListView(){
        list_typeQuantity=new ArrayList();
        adapter=new Adapter_AddTypeQuantity(getApplicationContext());
        lv_productTyps.setAdapter(adapter);
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.b_addTypeQuantity:
                onClick_addQuantityTupe();
                break;
            case R.id.b_scanBarcode:
                scanBarcode();
                break;
            case R.id.b_addProduct:
                onClick_addButton();
                break;
        }


    }
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    // to change the data when branch change
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        ArrayList<NodeTypeQuantity> list=previousProduce.getMap().get(Firebase_keys.branch+sp_branchNumber.getBranchNumber()); /**************/
//        if(list != null){
//            list_typeQuantity.clear();
//            list_typeQuantity=list;
//            adapter.notifyDataSetChanged();
//        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
//        ArrayList<NodeTypeQuantity> list= previousProduce.getMap().get(Firebase_keys.branch+1+"");
//        if(list != null){
//            list_typeQuantity.clear();
//            list_typeQuantity=list;
//            adapter.notifyDataSetChanged();
//        }


    }
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/


    private void onClick_addQuantityTupe(){
        if( dialog == null){
            createTheInputDialog(); // Down ↓ ↓ ↓ ↓
        }
        dialog.show();
    }

    
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void createTheInputDialog(){

        LayoutInflater inflater;
        View view;

        inflater=getLayoutInflater();
        view=inflater.inflate(R.layout.dialog_layout,null);

        final EditText et_Type= (EditText) view.findViewById(R.id.etDialog_type);
        final EditText et_Quantity= (EditText) view.findViewById(R.id.etDialog_quantity);

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("This is the TiTLe LOOL"); // you must change it  :3
        builder.setView(view);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String t=et_Type.getText().toString();
                int q=Integer.parseInt(et_Quantity.getText().toString());

                if(!t.isEmpty() && q>0 ){
                    NodeTypeQuantity node=new NodeTypeQuantity(t,q,0);
                    int i=list_typeQuantity.indexOf(node); // if the same type already exists
                    if(i<0){
                        list_typeQuantity.add(0,node);
                    }else{
                        list_typeQuantity.get(i).setOriginalQuantity(q); // update the previous quantity
                    }
                }
                adapter.notifyDataSetChanged();
                et_Quantity.setText("");
                et_Type.setText("");

            }
        });

        dialog=builder.create();
    }
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    private void scanBarcode(){

        IntentIntegrator intent=new IntentIntegrator(this);
        intent.setPrompt("Tap to Scan");
        intent.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intent.setBeepEnabled(true);
        intent.initiateScan(); // open fragment to sacn >> start scanning
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // this is to receive the barcode string from scan fragment
        IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result != null ){

            if( result.getContents() == null ){
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.error_scanFailed),Toast.LENGTH_SHORT).show();
            }else{
                barcode=result.getContents();
            }

        }
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    private void onClick_addButton(){

        progressBar.setVisibility(View.VISIBLE);

        String name=et_productName.getText().toString().trim();
        String madeIn=et_madeIn.getText().toString().trim();
        String firstPrice=et_firstPrice.getText().toString().trim();
        String finalPrice=et_finalPrice.getText().toString().trim();
        String date= DateFormat.format("yyy-MM-dd",new Date()).toString();
        String addedBy=CurrentUser.user.getName();


        Check check=new Check(getApplicationContext());
        if (!check.Valid_InGeneralNotEmpty(name) || !check.Valid_InGeneralNotEmpty(madeIn) ||
                !check.Valid_price(firstPrice) || !check.Valid_price(finalPrice) ||
                 ! check.Valid_Barcode(barcode) || !check.Valid_InGeneralNotEmpty(date)
                || !check.Valid_InGeneralNotEmpty(addedBy) ) {
            progressBar.setVisibility(View.INVISIBLE);
            return ;
        }

        ProductDetails product=new ProductDetails(name,barcode, firstPrice, finalPrice,sp_currencyType.getCurrencyCode(),
                madeIn, addedBy, date,null,null);
        product.setDataForEachBranchInhashTable(Firebase_keys.branch+sp_branchNumber.getBranchNumber(),list_typeQuantity);

        insertDataToFirebase(product);


    }

     /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    private void insertDataToFirebase(final ProductDetails product) {

        firebase=new Firebase(Firebase_keys.firebaseURL)
                .child(CurrentUser.workAt_ShopName)
                .child(Firebase_keys.ShopData)
                .child(Firebase_keys.open_parenthesis+barcode+Firebase_keys.closed_parenthesis);

        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ProductDetails pro = dataSnapshot.getValue(ProductDetails.class);
                if (!dataSnapshot.exists()) {
                    firebase.setValue(product); // insert it for the first time kh kh kh

                }

//                else if (pro != null && previousProduce !=null || pro.getMap().get(Firebase_keys.branch + sp_branchNumber.getBranchNumber()) == null) {
//                    pro.setDataForEachBranchInhashTable(Firebase_keys.branch + sp_branchNumber.getBranchNumber(),
//                            product.getMap().get(Firebase_keys.branch + sp_branchNumber.getBranchNumber()));
//                    firebase.setValue(pro); // update the value
//
//                }
                else {
                    String msg = getResources().getString(R.string.error) +
                            getResources().getString(R.string.error_duplicateBarcode) + "\n" +
                            getResources().getString(R.string.error_duplicateProductsForThisBranch);
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    firebase.removeEventListener(this);
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                claen();
                firebase.removeEventListener(this);
                progressBar.setVisibility(View.INVISIBLE);

                // go to display all product if the operation was Edit
//                if(previousProduce != null){
//                    startActivity(new Intent(getApplicationContext(),DisplayAllProducts_ACTIVITY.class));
//                    finish();
//                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                firebase.removeEventListener(this);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }



/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
        private void claen(){

            et_productName.setText("");
            et_madeIn.setText("");
            et_firstPrice.setText("");
            et_finalPrice.setText("");

            barcode = "";

            adapter.notifyDataSetChanged();
            list_typeQuantity.clear();

        }


     /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
     /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~       My Adapter            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    public class Adapter_AddTypeQuantity extends ArrayAdapter {

        Context context;
        int layoutId = R.layout.type_quantity_listview_row;

        public Adapter_AddTypeQuantity(Context context) {
            super(context, R.layout.type_quantity_listview_row, R.id.QT_text_view, list_typeQuantity);
            this.context = context;

        }
        /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

        @NonNull
        @Override
        public View getView(final int position, View view, ViewGroup parent) {

            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(layoutId, parent, false);
            }


            TextView tv = (TextView) view.findViewById(R.id.QT_text_view);
            NodeTypeQuantity node=list_typeQuantity.get(position);
            tv.setText(node.getType()+" - "+node.getOriginalQuantity()); //  ' type - quantity'

            Button button = (Button) view.findViewById(R.id.b_deleteRow);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list_typeQuantity.remove(position);
                    adapter.notifyDataSetChanged();

                }
            });


            return view;
        }
    }

}
