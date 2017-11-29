package com.keltonkarboviak.shoppogen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class ListDataActivity extends AppCompatActivity
{
    private RecyclerView mProductsRecyclerView;

    private RecyclerView mCouponsRecyclerView;

    private ProductsAdapter mProductsAdapter;

    private CouponsAdapter mCouponsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mProductsRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_products);
        mProductsRecyclerView.setLayoutManager(layoutManager);
        mProductsRecyclerView.setHasFixedSize(true);

        mProductsAdapter = new ProductsAdapter();
        mProductsRecyclerView.setAdapter(mProductsAdapter);

        mCouponsRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_coupons);
        mCouponsRecyclerView.setLayoutManager(layoutManager);
        mCouponsRecyclerView.setHasFixedSize(true);

        mCouponsAdapter = new CouponsAdapter();
        mCouponsRecyclerView.setAdapter(mCouponsAdapter);

        loadProductData();
        loadCouponData();
    }

    private void loadProductData() {
        showProductDataView();

        String[] names = new String[]{
            "Apple",
            "Banana",
            "Coke",
        };

        double[] prices = new double[]{
            1.50,
            0.75,
            2.99
        };

        mProductsAdapter.setProductData(names, prices);
    }

    private void loadCouponData() {
        showCouponDataView();

        int[] ids = new int[]{
            0, 1, 2
        };

        mCouponsAdapter.setCouponData(ids);
    }

    private void showProductDataView() {
        mProductsRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showCouponDataView() {
        mCouponsRecyclerView.setVisibility(View.VISIBLE);
    }
}
