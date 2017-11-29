package com.keltonkarboviak.shoppogen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button mAddProductBtn;
    private Button mAddCouponBtn;
    private Button mListDataBtn;
    private Button mGoShoppingBtn;
    private Button mResetSystemBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAddProductBtn = (Button) findViewById(R.id.btn_add_product);
        mAddProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mAddCouponBtn = (Button) findViewById(R.id.btn_add_coupon);
        mAddCouponBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mListDataBtn = (Button) findViewById(R.id.btn_list_data);
        mListDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mGoShoppingBtn = (Button) findViewById(R.id.btn_shop);
        mGoShoppingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mResetSystemBtn = (Button) findViewById(R.id.btn_reset_system);
        mResetSystemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(MainActivity.this, "System has been reset.", Toast.LENGTH_LONG);
            }
        });
    }
}
