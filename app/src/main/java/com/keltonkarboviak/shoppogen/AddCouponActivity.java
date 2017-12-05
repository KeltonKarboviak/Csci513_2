package com.keltonkarboviak.shoppogen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.TextView;


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

        mDb = dbHelper.getWriteableDatabase();

        mIdLabel = (TextView) findViewById(R.id.tv_coupon_id_label);
        mIdLabel.setVisibility(View.INVISIBLE);

        mIdEditText = (EditText) findViewById(R.id.et_coupon_id);
        mIdEditText.setVisibility(View.INVISIBLE);

        mDiscountEditText = (EditText) findViewById(R.id.et_coupon_discount);

        mSubmitButton = (Button) findViewById(R.id.btn_coupon_submit);
        mSubmitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // TODO: Add a try/catch around this
                double discount = Double.parseDouble(mDiscountEditText.getText().toString());

                Coupon coupon = new Coupon(discount);

                if (!insertCoupon(coupon)) {
                    Toast.makeText(this, "SQL Error: Failed to insert Coupon: " + coupon, Toast.LENGTH_LONG).show();
                }

                resetComponents();
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
        // Returns true if Coupon was successfully inserted, false otherwise
        return mDb.insert(
            ShoppoContract.CouponEntry.TABLE_NAME,
            null,
            coupon.toContentValues()
        ) >= 0;  // mDb.insert() returns row ID of newly inserted row
    }
}
