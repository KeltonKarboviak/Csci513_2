package com.keltonkarboviak.shoppogen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.TextView;


public class AddCouponActivity extends AppCompatActivity
{
    private TextView mIdLabel;

    private TextView mIdTextView;

    private EditText mDiscountEditText;

    private RecyclerView mProductsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coupon);
    }
}
