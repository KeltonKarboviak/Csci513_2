package com.keltonkarboviak.shoppogen.DB;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kelton on 12/3/17.
 */

public class TestUtil
{
    public static void insertFakeData(SQLiteDatabase db)
    {
        if (db == null) {
            return;
        }

        /**
         * Insert Test Products
         */
        List<ContentValues> list = new ArrayList<>();

        ContentValues cv = new ContentValues();
        cv.put(ShoppoContract.ProductEntry._ID, 0);
        cv.put(ShoppoContract.ProductEntry.COLUMN_PRODUCT_NAME, "apple");
        cv.put(ShoppoContract.ProductEntry.COLUMN_PRODUCT_PRICE, 2.50);
        list.add(cv);

        cv = new ContentValues();
        cv.put(ShoppoContract.ProductEntry._ID, 1);
        cv.put(ShoppoContract.ProductEntry.COLUMN_PRODUCT_NAME, "banana");
        cv.put(ShoppoContract.ProductEntry.COLUMN_PRODUCT_PRICE, 1.50);
        list.add(cv);

        cv = new ContentValues();
        cv.put(ShoppoContract.ProductEntry._ID, 2);
        cv.put(ShoppoContract.ProductEntry.COLUMN_PRODUCT_NAME, "pepsi");
        cv.put(ShoppoContract.ProductEntry.COLUMN_PRODUCT_PRICE, 0.75);
        list.add(cv);

        cv = new ContentValues();
        cv.put(ShoppoContract.ProductEntry._ID, 3);
        cv.put(ShoppoContract.ProductEntry.COLUMN_PRODUCT_NAME, "pear");
        cv.put(ShoppoContract.ProductEntry.COLUMN_PRODUCT_PRICE, 1.25);
        list.add(cv);

        cv = new ContentValues();
        cv.put(ShoppoContract.ProductEntry._ID, 4);
        cv.put(ShoppoContract.ProductEntry.COLUMN_PRODUCT_NAME, "grapes");
        cv.put(ShoppoContract.ProductEntry.COLUMN_PRODUCT_PRICE, 2.75);
        list.add(cv);

        try {
            db.beginTransaction();

            // Wipe table
            db.delete(ShoppoContract.ProductEntry.TABLE_NAME, null, null);

            for (ContentValues c : list) {
                db.insert(ShoppoContract.ProductEntry.TABLE_NAME, null, c);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {

        } finally {
            db.endTransaction();
        }

        /**
         * Insert Test Coupons
         */
        list.clear();

        cv = new ContentValues();
        cv.put(ShoppoContract.CouponEntry._ID, 0);
        cv.put(ShoppoContract.CouponEntry.COLUMN_COUPON_DISCOUNT, 1.50);
        list.add(cv);

        cv = new ContentValues();
        cv.put(ShoppoContract.CouponEntry._ID, 1);
        cv.put(ShoppoContract.CouponEntry.COLUMN_COUPON_DISCOUNT, 0.75);
        list.add(cv);

        cv = new ContentValues();
        cv.put(ShoppoContract.CouponEntry._ID, 2);
        cv.put(ShoppoContract.CouponEntry.COLUMN_COUPON_DISCOUNT, 1.00);
        list.add(cv);

        try {
            db.beginTransaction();

            // Wipe table
            db.delete(ShoppoContract.CouponEntry.TABLE_NAME, null, null);

            for (ContentValues c : list) {
                db.insert(ShoppoContract.CouponEntry.TABLE_NAME, null, c);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {

        } finally {
            db.endTransaction();
        }

        /**
         * Insert Test CouponProducts
         */
        list.clear();

        cv = new ContentValues();
        cv.put(ShoppoContract.CouponProductEntry.COLUMN_COUPON_ID, 0);
        cv.put(ShoppoContract.CouponProductEntry.COLUMN_PRODUCT_ID, 0);
        list.add(cv);

        cv = new ContentValues();
        cv.put(ShoppoContract.CouponProductEntry.COLUMN_COUPON_ID, 0);
        cv.put(ShoppoContract.CouponProductEntry.COLUMN_PRODUCT_ID, 1);
        list.add(cv);

        cv = new ContentValues();
        cv.put(ShoppoContract.CouponProductEntry.COLUMN_COUPON_ID, 1);
        cv.put(ShoppoContract.CouponProductEntry.COLUMN_PRODUCT_ID, 0);
        list.add(cv);

        cv = new ContentValues();
        cv.put(ShoppoContract.CouponProductEntry.COLUMN_COUPON_ID, 1);
        cv.put(ShoppoContract.CouponProductEntry.COLUMN_PRODUCT_ID, 1);
        list.add(cv);

        cv = new ContentValues();
        cv.put(ShoppoContract.CouponProductEntry.COLUMN_COUPON_ID, 1);
        cv.put(ShoppoContract.CouponProductEntry.COLUMN_PRODUCT_ID, 2);
        list.add(cv);

        cv = new ContentValues();
        cv.put(ShoppoContract.CouponProductEntry.COLUMN_COUPON_ID, 2);
        cv.put(ShoppoContract.CouponProductEntry.COLUMN_PRODUCT_ID, 0);
        list.add(cv);

        cv = new ContentValues();
        cv.put(ShoppoContract.CouponProductEntry.COLUMN_COUPON_ID, 2);
        cv.put(ShoppoContract.CouponProductEntry.COLUMN_PRODUCT_ID, 3);
        list.add(cv);

        cv = new ContentValues();
        cv.put(ShoppoContract.CouponProductEntry.COLUMN_COUPON_ID, 2);
        cv.put(ShoppoContract.CouponProductEntry.COLUMN_PRODUCT_ID, 4);
        list.add(cv);

        try {
            db.beginTransaction();

            // Wipe table
            db.delete(ShoppoContract.CouponProductEntry.TABLE_NAME, null, null);

            for (ContentValues c : list) {
                db.insert(ShoppoContract.CouponProductEntry.TABLE_NAME, null, c);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {

        } finally {
            db.endTransaction();
        }
    }
}
