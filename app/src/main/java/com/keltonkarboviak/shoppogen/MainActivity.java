package com.keltonkarboviak.shoppogen;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.keltonkarboviak.shoppogen.DB.DbHelper;


public class MainActivity extends AppCompatActivity
{

    private Button mAddProductBtn;

    private Button mAddCouponBtn;

    private Button mListDataBtn;

    private Button mGoShoppingBtn;

    private Button mResetSystemBtn;

    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DbHelper dbHelper = new DbHelper(this);
        mDb = dbHelper.getWritableDatabase();

        mAddProductBtn = (Button) findViewById(R.id.btn_add_product);
        mAddProductBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });

        mAddCouponBtn = (Button) findViewById(R.id.btn_add_coupon);
        mAddCouponBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });

        mListDataBtn = (Button) findViewById(R.id.btn_list_data);
        mListDataBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, ListDataActivity.class);
                startActivity(intent);
            }
        });

        mGoShoppingBtn = (Button) findViewById(R.id.btn_shop);
        mGoShoppingBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });

        mResetSystemBtn = (Button) findViewById(R.id.btn_reset_system);
        mResetSystemBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dbHelper.fresh(mDb);

                Toast.makeText(MainActivity.this, "System has been reset.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
