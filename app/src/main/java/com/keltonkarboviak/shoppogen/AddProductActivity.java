package com.keltonkarboviak.shoppogen;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.keltonkarboviak.shoppogen.DB.DbHelper;
import com.keltonkarboviak.shoppogen.DB.ShoppoContract;
import com.keltonkarboviak.shoppogen.Models.Product;


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

        mDb = dbHelper.getWritableDatabase();

        mNameEditText = (EditText) findViewById(R.id.et_product_name);

        mPriceEditText = (EditText) findViewById(R.id.et_product_price);

        mSubmitButton = (Button) findViewById(R.id.btn_product_submit);
        mSubmitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String name = mNameEditText.getText().toString();

                double price = -1.0;
                try {
                    price = Double.parseDouble(mPriceEditText.getText().toString());

                    Product product = new Product(name, price);

                    String msg = !insertProduct(product)
                        ? "SQL Error: Failed to insert Product: " + product
                        : "Product successfully added!";

                    Toast.makeText(
                        AddProductActivity.this,
                        msg,
                        Toast.LENGTH_LONG
                    ).show();
                } catch (Exception e) {
                    Toast.makeText(
                        AddProductActivity.this,
                        "Price is not a valid number",
                        Toast.LENGTH_LONG
                    ).show();
                } finally {
                    resetComponents();
                }
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
        boolean successful = false;
        try {
            mDb.beginTransaction();

            successful = mDb.insert(
                ShoppoContract.ProductEntry.TABLE_NAME,
                null,
                product.toContentValues()
            ) >= 0;  // mDb.insert() returns row ID of newly inserted row

            mDb.setTransactionSuccessful();
        } catch (SQLException e) {
            Toast.makeText(
                AddProductActivity.this,
                "SQLException: " + e.getMessage(),
                Toast.LENGTH_LONG
            ).show();
        } finally {
            mDb.endTransaction();
        }

        // Returns true if Product was successfully inserted, false otherwise
        return successful;
    }
}
