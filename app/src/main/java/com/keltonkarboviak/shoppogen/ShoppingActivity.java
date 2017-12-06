package com.keltonkarboviak.shoppogen;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.keltonkarboviak.shoppogen.DB.DbHelper;
import com.keltonkarboviak.shoppogen.DB.ShoppoContract;
import com.keltonkarboviak.shoppogen.Models.Coupon;
import com.keltonkarboviak.shoppogen.Models.Product;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ShoppingActivity extends AppCompatActivity
{
    private TextView mLogTextView;

    private Button mBudgetSubmitButton;

    private Button mListSubmitButton;

    private RecyclerView mProductsRecyclerView;

    private ProductsAdapter mProductsAdapter;

    private SQLiteDatabase mDb;

    private List<Coupon> mCoupons;

    private static final String LOG_TAG = ShoppingActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        DbHelper dbHelper = new DbHelper(this);

        mDb = dbHelper.getReadableDatabase();

        mLogTextView = (TextView) findViewById(R.id.tv_log);

        mBudgetSubmitButton = (Button) findViewById(R.id.btn_budget_submit);
        mBudgetSubmitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });

        mListSubmitButton = (Button) findViewById(R.id.btn_list_submit);
        mListSubmitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                List<Product> productsSelected = mProductsAdapter.getSelectedProducts();

                List<Coupon> couponsApplicable = filterCouponsByDesiredProducts(
                    getAllCouponsList(),
                    productsSelected
                );

                generateBestPriceForProducts(couponsApplicable, productsSelected);
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
//        getAllCouponsList();
    }

    private Set<Set<Integer>> generatePowerSet(int size)
    {
        Set<Set<Integer>> powerSet = new HashSet<>();
        for (int i = 0; i < size; i++) {
            Set<Set<Integer>> temp = new HashSet<>();
            Set<Integer> s = new HashSet<>();
            s.add(i);
            temp.add(s);

            for (int j = i + 1; j < size; j++) {
                temp.addAll(unionAll(temp, j));
            }

            powerSet.addAll(temp);
        }

        return powerSet;
    }

    private List<Set<Integer>> unionAll(Set<Set<Integer>> setOfSets, int i)
    {
        List<Set<Integer>> sets = new ArrayList<>();

        for (Set<Integer> s : setOfSets) {
            Set<Integer> temp = new HashSet<>(s);
            temp.add(i);
            sets.add(temp);
        }

        return sets;
    }

    private void generateBestPriceForProducts(
        List<Coupon> couponsApplicable,
        List<Product> productsSelected)
    {
        // This will be the Power Set of the indices from the couponsApplicable List. This will be
        // used to get an index then access the coupon from couponsApplicable using that index.
        Set<Set<Integer>> powerSet = generatePowerSet(couponsApplicable.size());

        for (Set<Integer> s : powerSet) {
            StringBuilder sb = new StringBuilder();
            sb.append("{");

            for (int i : s) {
                sb.append(" " + i);
            }

            sb.append(" }");
            Log.d(LOG_TAG, sb.toString());
        }
    }

    private void resetComponents()
    {

    }

    private Set<Long> convertProductsToIdSet(List<Product> products)
    {
        Set<Long> set = new HashSet<>();
        for (Product p : products) {
            set.add(p.getId());
        }

        return set;
    }

    private List<Coupon> filterCouponsByDesiredProducts(
        List<Coupon> coupons,
        List<Product> products)
    {
        Set<Long> shoppingSet = convertProductsToIdSet(products);
        final List<Coupon> selection = new ArrayList<>();

        for (Coupon c : coupons) {
            if (c.canBeRedeemed.test(shoppingSet)) {
                selection.add(c);
            }
        }

        return selection;
    }

    private List<Coupon> getAllCouponsList()
    {
        List<Coupon> list = new ArrayList<>();

        Cursor cursor = getAllCouponsCursor();
        while (cursor.moveToNext()) {
            Coupon c = Coupon.fromCursor(cursor);
            List<Product> products = new ArrayList<>();

            Cursor pCursor = getProductsByCouponId(c.getId());
            while (pCursor.moveToNext()) {
                products.add(Product.fromCursor(pCursor));
            }

            pCursor.close();

            c.setProducts(products);

            list.add(c);
        }

        cursor.close();

        return list;
    }

    private void loadProductData()
    {
        mProductsAdapter.swapCursor(getAllProductsCursor());
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

    private Cursor getAllProductsCursor()
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

    private Cursor getAllCouponsCursor()
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
}
