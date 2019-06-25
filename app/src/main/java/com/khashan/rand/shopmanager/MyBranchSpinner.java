package com.khashan.rand.shopmanager;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MyBranchSpinner extends Spinner{


    private Context context;
    private boolean isFilled=false;
    private static int numOfBranches=5;

    public MyBranchSpinner(Context context) {
        super(context);
        this.context=context;
        fillBranchSpinner();
    }

    public MyBranchSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        fillBranchSpinner();
    }

    public MyBranchSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        fillBranchSpinner();
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/
    private void fillBranchSpinner() {
        if (isFilled) {
        return;}

        String[] arr = new String[numOfBranches];
        String b = getResources().getString(R.string.branch);
        for (int i = 0; i < arr.length; i++) {
            arr[i] = b + " _ " + (i + 1);
        }
        ArrayAdapter adapter = new ArrayAdapter(context, R.layout.spinner_item, arr);
        this.setAdapter(adapter);
        isFilled=true;

    }
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

    public int getBranchNumber() {

        return 1 + getSelectedItemPosition();

    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/
    public static int getNumOfBranches(){
        return numOfBranches;
    }
}
