package com.keltonkarboviak.shoppogen;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.keltonkarboviak.shoppogen.DB.DbHelper;
import com.keltonkarboviak.shoppogen.DB.ShoppoContract;
import com.keltonkarboviak.shoppogen.DB.TestUtil;
import com.keltonkarboviak.shoppogen.Models.Coupon;
import com.keltonkarboviak.shoppogen.Models.Product;

import java.util.ArrayList;
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

        // NOTE: DEBUGGING PURPOSES ONLY
        TestUtil.insertFakeData(mDb);

        /**
         * Setup Products
         */
        Cursor productCursor = getAllProducts();

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        );
        mProductsRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_products);
        mProductsRecyclerView.setLayoutManager(layoutManager1);
        mProductsRecyclerView.setHasFixedSize(true);

        mProductsAdapter = new ProductsAdapter(this, productCursor);
        mProductsRecyclerView.setAdapter(mProductsAdapter);

        /**
         * Setup Coupons
         */
        Cursor couponCursor = getAllCoupons();

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        );
        mCouponsRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_coupons);
        mCouponsRecyclerView.setLayoutManager(layoutManager2);
        mCouponsRecyclerView.setHasFixedSize(true);

        mCouponsAdapter = new CouponsAdapter(this, couponCursor);
        mCouponsRecyclerView.setAdapter(mCouponsAdapter);

        loadProductData();
        loadCouponData();

        /**
         * Setup Buttons
         */
        mUpdateProductPricesBtn = (Button) findViewById(R.id.btn_update_products);
        mUpdateProductPricesBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

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

                }
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
            ShoppoContract.ProductEntry.COLUMN_PRODUCT_NAME
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

    private void updateProductPrice(String productName, double productPrice)
    {

    }

    private boolean deleteCoupon(int id)
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

        List<Product> products = new ArrayList<>();
        products.add(new Product("apple", 2.50));
        products.add(new Product("banana", 1.50));
        products.add(new Product("pepsi", 0.75));
        products.add(new Product("pear", 1.25));
        products.add(new Product("grapes", 2.75));

        mProductsAdapter.setProductData(products);
    }

    private void loadCouponData()
    {
        showCouponDataView();

        List<Coupon> coupons = new ArrayList<>();
        coupons.add(new Coupon(0, 1.50, new String[]{"apple", "banana"}));
        coupons.add(new Coupon(1, 0.75, new String[]{"apple", "banana", "pepsi"}));
        coupons.add(new Coupon(2, 1.00, new String[]{"apple", "pear", "grapes"}));

        mCouponsAdapter.setCouponData(coupons);
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
