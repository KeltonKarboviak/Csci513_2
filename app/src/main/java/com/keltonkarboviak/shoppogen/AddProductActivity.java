package com.keltonkarboviak.shoppogen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class AddProductActivity extends AppCompatActivity
{
    private EditText mNameEditText;

    private EditText mPriceEditText;

    private Button mSubmitButton;

    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        DbHelper dbHelper = new DbHelper(this);

        mDb = dbHelper.getWriteableDatabase();

        mNameEditText = (EditText) findViewById(R.id.et_product_name);

        mPriceEditText = (EditText) findViewById(R.id.et_product_price);

        mSubmitButton = (Button) findViewById(R.id.btn_product_submit);
        mSubmitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String name = mNameEditText.getText().toString();

                // TODO: Add a try/catch around this
                double discount = Double.parseDouble(mPriceEditText.getText().toString());

                Product product = new Product(name, discount);

                if (!insertProduct(product)) {
                    Toast.makeText(this, "SQL Error: Failed to insert Product: " + product, Toast.LENGTH_LONG).show();
                }

                resetComponents();
            }
        });
    }

    private void resetComponents()
    {
        mNameEditText.setText("");
        mPriceEditText.setText("");
    }

    private boolean insertProduct(Product product)
    {
        // Returns true if Product was successfully inserted, false otherwise
        return mDb.insert(
            ShoppoContract.ProductEntry.TABLE_NAME,
            null,
            product.toContentValues()
        ) >= 0;  // mDb.insert() returns row ID of newly inserted row
    }
}
