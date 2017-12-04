package com.keltonkarboviak.shoppogen;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.keltonkarboviak.shoppogen.DB.DbHelper;
import com.keltonkarboviak.shoppogen.DB.ShoppoContract;
import com.keltonkarboviak.shoppogen.Models.Coupon;


public class CouponDetailActivity extends AppCompatActivity
{
    private TextView mIdTextView;

    private EditText mDiscountEditText;

    private RecyclerView mProductsRecyclerView;

    private ProductsAdapter mProductsAdapter;

    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_detail);

        DbHelper dbHelper = new DbHelper(this);

        mDb = dbHelper.getReadableDatabase();

        Intent intent = getIntent();
        long couponId = intent.getLongExtra("COUPON_ID", -1);

        Coupon coupon = getCouponById(couponId);

        if (coupon == null) {
            Toast.makeText(this, "Error, no Coupon found!", Toast.LENGTH_LONG).show();
            return;
        }

        mIdTextView = (TextView) findViewById(R.id.tv_coupon_id);
        mIdTextView.setText(String.valueOf(coupon.getId()));

        mDiscountEditText = (EditText) findViewById(R.id.et_coupon_discount);
        mDiscountEditText.setText(String.format("%01.2f", coupon.getDiscount()));
        mDiscountEditText.setFocusable(false);

        /*
          Setup Products
         */
        mProductsRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_products);
        mProductsRecyclerView.setLayoutManager(new LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        ));
        mProductsRecyclerView.setHasFixedSize(true);

        mProductsAdapter = new ProductsAdapter(this, getProductsByCouponId(couponId), false);
        mProductsRecyclerView.setAdapter(mProductsAdapter);
    }

    private Coupon getCouponById(long id)
    {
        Cursor cursor = mDb.query(
            ShoppoContract.CouponEntry.TABLE_NAME,
            null,
            ShoppoContract.CouponEntry._ID + " = " + id,
            null,
            null,
            null,
            null
        );

        return cursor.moveToNext()
            ? Coupon.fromCursor(cursor)
            : null;
    }

    private Cursor getProductsByCouponId(long id)
    {
        String sql = String.format(
            "SELECT p.* " +
                "FROM coupons_products cp " +
                "JOIN products p " +
                "  ON cp.%s = p.%s " +
                "WHERE cp.%s = ?",
            ShoppoContract.CouponProductEntry.COLUMN_PRODUCT_ID,
            ShoppoContract.ProductEntry._ID,
            ShoppoContract.CouponProductEntry.COLUMN_COUPON_ID
        );

        return mDb.rawQuery(
            sql,
            new String[]{String.valueOf(id)}
        );
    }
}
