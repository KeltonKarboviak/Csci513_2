package com.keltonkarboviak.shoppogen;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    private static final String LOG_TAG = ShoppingActivity.class.getSimpleName();

    private TextView mLogTextView;

    private EditText mBudgetEditText;

    private Button mBudgetSubmitButton;

    private Button mListSubmitButton;

    private RecyclerView mProductsRecyclerView;

    private ProductsAdapter mProductsAdapter;

    private SQLiteDatabase mDb;

    private List<Coupon> mCoupons;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        DbHelper dbHelper = new DbHelper(this);

        mDb = dbHelper.getReadableDatabase();

        mLogTextView = (TextView) findViewById(R.id.tv_log);

        mBudgetEditText = (EditText) findViewById(R.id.et_budget);

        mBudgetSubmitButton = (Button) findViewById(R.id.btn_budget_submit);
        mBudgetSubmitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try {
                    double budget = Double.parseDouble(
                        mBudgetEditText.getText().toString()
                    );

                    List<Product> productsWithCoupons = getAllProductsWithCouponList();
                    List<Coupon> couponsApplicable = getAllCouponsList();

                    mLogTextView.setText("");

                    generateBestPriceForProductsUsingCoupons(
                        productsWithCoupons,
                        couponsApplicable,
                        budget,
                        false
                    );
                } catch (Exception e) {
                    Toast.makeText(
                        ShoppingActivity.this,
                        "Error: Entered Budget is not a valid number.",
                        Toast.LENGTH_LONG
                    ).show();
                }
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

                mLogTextView.setText("");

                generateBestPriceForProductsUsingCoupons(
                    productsSelected,
                    couponsApplicable,
                    Double.POSITIVE_INFINITY,
                    true
                );
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

    private void generateBestPriceForProductsUsingCoupons(
        List<Product> products,
        List<Coupon> coupons,
        double budget,
        boolean isUserGenerated)
    {
        // This will be the Power Set of the indices from the coupons List. This will be
        // used to get an index then access the coupon from coupons using that index.
        Set<Set<Integer>> powerSet = generatePowerSet(coupons.size());

        double totalProductPrice = calculateTotalProductAmount(products);

        double bestDiscount = 0.0;
        double bestGrossTotalPrice = isUserGenerated ? totalProductPrice : 0.0;
        Set<Integer> bestSet = new HashSet<>();
        boolean validSet;
        for (Set<Integer> set : powerSet) {
            validSet = true;

            // Foreach set in the power set, we'll check to see if every coupon
            // in the set is compatible with each other. If so, we'll then check
            // to see if the set gives a better discount than the current best.
            List<Integer> list = new ArrayList<>(set);
            for (int i = 0; i < list.size(); i++) {
                for (int j = i + 1; j < list.size(); j++) {
                    if (coupons.get(list.get(i)).conflictsWith(coupons.get(list.get(j)))) {
                        validSet = false;
                        break;
                    }
                }

                if (!validSet) {
                    break;
                }
            }

            if (validSet) {
                // Since this was a valid set of coupons that are compatible
                // with each other, we will check to see if it's total discount
                // is better than the current best

                // Calculate the total discount that would be applied with this
                // set of coupons
                List<Coupon> couponSubset = getSubsetOfList(set, coupons);

                double totalDiscount = calculateTotalCouponDiscount(couponSubset);

                // Calculate the total cost of the products associated with this
                // set of coupons before the discount is applied
                double grossTotalPrice = isUserGenerated
                    ? totalProductPrice
                    : calculateGrossTotalPriceFromCoupons(couponSubset);

                double netTotalPrice = grossTotalPrice - totalDiscount;

                if (totalDiscount > bestDiscount && netTotalPrice <= budget) {
                    bestDiscount = totalDiscount;
                    bestGrossTotalPrice = grossTotalPrice;
                    bestSet = new HashSet<>(set);
                }
            }
        }

        mLogTextView.append(
            String.format(
                "$%01.2f total - $%01.2f discount = $%01.2f net\n\n",
                bestGrossTotalPrice,
                bestDiscount,
                bestGrossTotalPrice - bestDiscount
            )
        );

        for (int i : bestSet) {
            mLogTextView.append(coupons.get(i).toString() + '\n');
            for (Product p : coupons.get(i).getProducts()) {
                mLogTextView.append('\t' + p.toString() + '\n');
            }
        }
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

    private List<Coupon> getSubsetOfList(Set<Integer> set, List<Coupon> coupons)
    {
        List<Coupon> list = new ArrayList<>();
        for (int i : set) {
            list.add(coupons.get(i));
        }

        return list;
    }

    private double calculateGrossTotalPriceFromCoupons(List<Coupon> coupons)
    {
        double total = 0.0;
        for (Coupon c : coupons) {
            for (Product p : c.getProducts()) {
                total += p.getPrice();
            }
        }

        return total;
    }

    private double calculateTotalProductAmount(List<Product> products)
    {
        double total = 0.0;
        for (Product p : products) {
            total += p.getPrice();
        }

        return total;
    }

    private double calculateTotalCouponDiscount(List<Coupon> coupons)
    {
        double total = 0.0;
        for (Coupon c : coupons) {
            total += c.getDiscount();
        }

        return total;
    }

    private List<Coupon> filterCouponsByDesiredProducts(
        List<Coupon> coupons,
        List<Product> products)
    {
        Set<Long> shoppingSet = convertProductsToIdSet(products);
        final List<Coupon> selection = new ArrayList<>();

        for (Coupon c : coupons) {
            if (c.canBeAppliedToShoppingSet(shoppingSet)) {
                selection.add(c);
            }
        }

        return selection;
    }

    private Set<Long> convertProductsToIdSet(List<Product> products)
    {
        Set<Long> set = new HashSet<>();
        for (Product p : products) {
            set.add(p.getId());
        }

        return set;
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

    private void loadProductData()
    {
        mProductsAdapter.swapCursor(getAllProductsCursor());
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

    private List<Product> getAllProductsWithCouponList()
    {
        List<Product> list = new ArrayList<>();

        Cursor cursor = getAllProductsWithCouponCursor();
        while (cursor.moveToNext()) {
            list.add(Product.fromCursor(cursor));
        }

        cursor.close();

        return list;
    }

    private Cursor getAllProductsWithCouponCursor()
    {
        String sql = String.format(
            "SELECT p.* " +
                "FROM coupons_products cp " +
                "JOIN products p " +
                "  ON cp.%s = p.%s",
            ShoppoContract.CouponProductEntry.COLUMN_PRODUCT_ID,
            ShoppoContract.ProductEntry._ID,
            ShoppoContract.CouponProductEntry.COLUMN_COUPON_ID
        );

        return mDb.rawQuery(
            sql,
            null
        );
    }

    private void resetComponents()
    {
        mBudgetEditText.setText("");
    }
}
