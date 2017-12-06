package com.keltonkarboviak.shoppogen.Models;

import android.content.ContentValues;
import android.database.Cursor;

import com.keltonkarboviak.shoppogen.DB.ShoppoContract;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;


/**
 * Created by kelton on 12/3/17.
 */

public class Coupon
{
    private long id;

    private double discount;

    private List<Product> products;

    private Set<Long> productIdSet;

    public Coupon()
    {
        this(-1, -1.0, new ArrayList<Product>());
    }

    public Coupon(long id, double discount)
    {
        this(id, discount, new ArrayList<Product>());
    }

    public Coupon(double discount)
    {
        this(-1, discount, new ArrayList<Product>());
    }

    public Coupon(double discount, List<Product> products)
    {
        this(-1, discount, products);
    }

    public Coupon(long id, double discount, List<Product> products)
    {
        this.id = id;
        this.discount = discount;
        this.products = products;
        this.productIdSet = new HashSet<>();
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public double getDiscount()
    {
        return discount;
    }

    public void setDiscount(double discount)
    {
        this.discount = discount;
    }

    public List<Product> getProducts()
    {
        return products;
    }

    public void setProducts(List<Product> products)
    {
        this.products = products;
    }

    public Set<Long> getProductIdSet()
    {
        return productIdSet;
    }

    public void setProductIdSet(Set<Long> productIdSet)
    {
        this.productIdSet = productIdSet;
    }

    // TODO: See if this can easily be done using the existing List<Product>
    public boolean conflictsWith(Coupon other)
    {
        Set<Long> set = new HashSet<>(getProductIdSet());
        set.retainAll(other.getProductIdSet());

        return !set.isEmpty();
    }

    public boolean canBeAppliedToShoppingSet(Set<Long> shoppingSet)
    {
        Set<Long> set = new HashSet<>(getProductIdSet());
        set.removeAll(shoppingSet);

        return set.isEmpty();
    }

    public final Predicate<Set<Long>> canBeRedeemed = new Predicate<Set<Long>>() {
        @Override
        public boolean test(Set<Long> longs)
        {
            return canBeAppliedToShoppingSet(longs);
        }
    };

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Coupon coupon = (Coupon) o;

        return getId() == coupon.getId();
    }

    @Override
    public String toString()
    {
        return "Coupon{" + "id=" + id + ", discount=" + discount + '}';
    }

    public ContentValues toContentValues()
    {
        ContentValues cv = new ContentValues();

        if (this.id >= 0) {
            cv.put(ShoppoContract.CouponEntry._ID, this.id);
        }

        cv.put(ShoppoContract.CouponEntry.COLUMN_COUPON_DISCOUNT, this.discount);

        return cv;
    }

    public List<ContentValues> productsToContentValues()
    {
        List<ContentValues> values = new ArrayList<>();

        for (Product p : this.products) {
            ContentValues cv = new ContentValues();
            cv.put(ShoppoContract.CouponProductEntry.COLUMN_COUPON_ID, this.id);
            cv.put(ShoppoContract.CouponProductEntry.COLUMN_PRODUCT_ID, p.getId());
            values.add(cv);
        }

        return values;
    }

    public static Coupon fromCursor(Cursor cursor)
    {
        return new Coupon(
            cursor.getLong(cursor.getColumnIndex(ShoppoContract.CouponEntry._ID)),
            cursor.getDouble(cursor.getColumnIndex(ShoppoContract.CouponEntry.COLUMN_COUPON_DISCOUNT))
        );
    }
}
