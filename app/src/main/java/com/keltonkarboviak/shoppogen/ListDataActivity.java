package com.keltonkarboviak.shoppogen;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.keltonkarboviak.shoppogen.DB.DbHelper;
import com.keltonkarboviak.shoppogen.DB.ShoppoContract;
import com.keltonkarboviak.shoppogen.Models.Coupon;
import com.keltonkarboviak.shoppogen.Models.Product;

import java.util.List;


public class ListDataActivity extends AppCompatActivity
{
    private RecyclerView mProductsRecyclerView;

    private RecyclerView mCouponsRecyclerView;

    private ProductsAdapter mProductsAdapter;

    private CouponsAdapter mCouponsAdapter;

    private Button mUpdateProductPricesBtn;

    private Button mDeleteCouponBtn;

    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data);

        DbHelper dbHelper = new DbHelper(this);

        mDb = dbHelper.getWritableDatabase();

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

        // Product list item should be editable & selectable
        mProductsAdapter = new ProductsAdapter(this, null, true);
        mProductsRecyclerView.setAdapter(mProductsAdapter);

        /*
         * Setup Coupons
         */
        mCouponsRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_coupons);
        mCouponsRecyclerView.setLayoutManager(new LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        ));
        mCouponsRecyclerView.setHasFixedSize(true);

        mCouponsAdapter = new CouponsAdapter(this, null);
        mCouponsRecyclerView.setAdapter(mCouponsAdapter);

        loadProductData();
        loadCouponData();

        /*
         * Setup Buttons
         */
        mUpdateProductPricesBtn = (Button) findViewById(R.id.btn_update_products);
        mUpdateProductPricesBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                List<Product> products = mProductsAdapter.getCheckedProducts();

                if (updateProducts(products)) {
                    Toast.makeText(
                        ListDataActivity.this,
                        "Products successfully updated!",
                        Toast.LENGTH_LONG
                    ).show();
                }

                loadProductData();
            }
        });

        mDeleteCouponBtn = (Button) findViewById(R.id.btn_delete_coupon);
        mDeleteCouponBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Coupon coupon = mCouponsAdapter.getSelectedCoupon();

                if (coupon != null) {
                    if (deleteCoupon(coupon.getId())) {
                        Toast.makeText(
                            ListDataActivity.this,
                            "Coupon successfully deleted!",
                            Toast.LENGTH_LONG
                        ).show();
                    }

                    loadCouponData();
                }

                mCouponsAdapter.resetSelectedPosition();
            }
        });
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

    private Cursor getAllCoupons()
    {
        return mDb.query(
            ShoppoContract.CouponEntry.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            ShoppoContract.CouponEntry._ID
        );
    }

    private boolean updateProducts(List<Product> products)
    {
        int count = 0;
        try {
            mDb.beginTransaction();

            for (Product p : products) {
                if (updateProduct(p)) {
                    count++;
                }
            }

            mDb.setTransactionSuccessful();
        } catch (SQLException e) {

        } finally {
            mDb.endTransaction();
        }

        return count == products.size();
    }

    private boolean updateProduct(Product product)
    {
        return mDb.update(
            ShoppoContract.ProductEntry.TABLE_NAME,
            product.toContentValues(),
            ShoppoContract.ProductEntry._ID + " = ?",
            new String[]{String.valueOf(product.getId())}
        ) > 0;
    }

    private boolean deleteCoupon(long id)
    {
        return mDb.delete(
            ShoppoContract.CouponEntry.TABLE_NAME,
            ShoppoContract.CouponEntry._ID + " = " + id,
            null
        ) > 0;
    }

    private void loadProductData()
    {
        showProductDataView();

        mProductsAdapter.swapCursor(getAllProducts());
    }

    private void loadCouponData()
    {
        showCouponDataView();

        mCouponsAdapter.swapCursor(getAllCoupons());
    }

    private void showProductDataView()
    {
        mProductsRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showCouponDataView()
    {
        mCouponsRecyclerView.setVisibility(View.VISIBLE);
    }
}
