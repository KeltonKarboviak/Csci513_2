package com.keltonkarboviak.shoppogen;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.keltonkarboviak.shoppogen.DB.DbHelper;
import com.keltonkarboviak.shoppogen.DB.ShoppoContract;
import com.keltonkarboviak.shoppogen.Models.Coupon;
import com.keltonkarboviak.shoppogen.Models.Product;

import java.util.List;


public class AddCouponActivity extends AppCompatActivity
{
    private TextView mIdLabel;

    private EditText mIdEditText;

    private EditText mDiscountEditText;

    private Button mSubmitButton;

    private RecyclerView mProductsRecyclerView;

    private ProductsAdapter mProductsAdapter;

    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_detail);

        DbHelper dbHelper = new DbHelper(this);

        mDb = dbHelper.getWritableDatabase();

        // Remove since not needed in this view
        mIdLabel = (TextView) findViewById(R.id.label_coupon_id);
        ((ViewManager) mIdLabel.getParent()).removeView(mIdLabel);

        // Remove since not needed in this view
        mIdEditText = (EditText) findViewById(R.id.et_coupon_id);
        ((ViewManager) mIdEditText.getParent()).removeView(mIdEditText);

        mDiscountEditText = (EditText) findViewById(R.id.et_coupon_discount);

        mSubmitButton = (Button) findViewById(R.id.btn_coupon_submit);
        mSubmitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                double discount = -1.0;
                try {
                    discount = Double.parseDouble(mDiscountEditText.getText().toString());

                    List<Product> products = mProductsAdapter.getCheckedProducts();

                    Coupon coupon = new Coupon(discount, products);

                    String msg = !insertCoupon(coupon)
                        ? "SQL Error: Failed to insert Coupon: " + coupon
                        : "Coupon successfully added!";

                    Toast.makeText(
                        AddCouponActivity.this,
                        msg,
                        Toast.LENGTH_LONG
                    ).show();

                    // Reload Product RecyclerView to get rid of checked boxes
                    loadProductData();
                } catch (Exception e) {
                    Toast.makeText(
                        AddCouponActivity.this,
                        "Discount is not a valid number",
                        Toast.LENGTH_LONG
                    ).show();
                } finally {
                    resetComponents();
                }
            }
        });

        /*
         * Setup Products
         */
        mProductsRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_products);
        mProductsRecyclerView.setLayoutManager(new LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        ));
        mProductsRecyclerView.setHasFixedSize(true);

        // The product list items should not be editable, but should be
        // selectable
        mProductsAdapter = new ProductsAdapter(this, null, false, true);
        mProductsRecyclerView.setAdapter(mProductsAdapter);

        loadProductData();
    }

    private void resetComponents()
    {
        mDiscountEditText.setText("");
    }

    private void loadProductData()
    {
        mProductsAdapter.swapCursor(getAllProducts());
    }

    private Cursor getAllProducts()
    {
        return mDb.query(
            ShoppoContract.ProductEntry.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            ShoppoContract.ProductEntry._ID
        );
    }

    private boolean insertCoupon(Coupon coupon)
    {
        long couponId = -1;
        int count = 0;
        try {
            mDb.beginTransaction();

            couponId = mDb.insert(
                ShoppoContract.CouponEntry.TABLE_NAME,
                null,
                coupon.toContentValues()
            );  // mDb.insert() returns row ID of newly inserted row

            // Need to set ID of Coupon obj for the next call to productsToContentValues()
            coupon.setId(couponId);

            List<ContentValues> values = coupon.productsToContentValues();

            for (ContentValues cv : values) {
                if (mDb.insert(ShoppoContract.CouponProductEntry.TABLE_NAME, null, cv) >= 0) {
                    count++;
                }
            }

            mDb.setTransactionSuccessful();
        } catch (SQLException e) {
            Toast.makeText(
                AddCouponActivity.this,
                "SQLException: " + e.getMessage(),
                Toast.LENGTH_LONG
            ).show();
        } finally {
            mDb.endTransaction();
        }

        // Returns true if Coupon and CouponProducts were successfully inserted, false otherwise
        return couponId >= 0 && count == coupon.getProducts().size();
    }
}
