package com.keltonkarboviak.shoppogen.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.keltonkarboviak.shoppogen.DB.ShoppoContract.*;


/**
 * Created by kelton on 12/3/17.
 */

public class DbHelper extends SQLiteOpenHelper
{
    private static final String TAG = DbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "shoppogen.db";

    private static final int DATABASE_VERSION = 2;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        /**
         * Setup Coupons Table
         */
        String SQL_CREATE_COUPONS_TABLE = "CREATE TABLE " + CouponEntry.TABLE_NAME + " (" +
            CouponEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CouponEntry.COLUMN_COUPON_DISCOUNT + " DECIMAL(5, 2) NOT NULL" +
            ");";
        sqLiteDatabase.execSQL(SQL_CREATE_COUPONS_TABLE);

        Log.d(TAG, SQL_CREATE_COUPONS_TABLE);

        /**
         * Setup Products Table
         */
        String SQL_CREATE_PRODUCTS_TABLE = "CREATE TABLE " + ProductEntry.TABLE_NAME + " (" +
            ProductEntry.COLUMN_PRODUCT_NAME + " TEXT PRIMARY KEY, " +
            ProductEntry.COLUMN_PRODUCT_PRICE + " DECIMAL(5, 2) NOT NULL" +
            ");";
        sqLiteDatabase.execSQL(SQL_CREATE_PRODUCTS_TABLE);

        Log.d(TAG, SQL_CREATE_PRODUCTS_TABLE);

        /**
         * Setup CouponsProducts Table
         */
        String SQL_CREATE_COUPONS_PRODUCTS_TABLE = "CREATE TABLE " + CouponProductEntry.TABLE_NAME + " (" +
            CouponProductEntry.COLUMN_COUPON_ID + " INTEGER NOT NULL, " +
            CouponProductEntry.COLUMN_PRODUCT_ID + " TEXT NOT NULL, " +
            "FOREIGN KEY (" + CouponProductEntry.COLUMN_COUPON_ID + ") REFERENCES " + CouponEntry.TABLE_NAME + "(" + CouponEntry._ID + ")," +
            "FOREIGN KEY (" + CouponProductEntry.COLUMN_PRODUCT_ID + ") REFERENCES " + ProductEntry.TABLE_NAME + "(" + ProductEntry.COLUMN_PRODUCT_NAME + ")" +
            ");";
        sqLiteDatabase.execSQL(SQL_CREATE_COUPONS_PRODUCTS_TABLE);

        Log.d(TAG, SQL_CREATE_COUPONS_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        String SQL_DROP_COUPONS_TABLE = "DROP TABLE IF EXISTS " + CouponEntry.TABLE_NAME + ";";
        String SQL_DROP_PRODUCTS_TABLE = "DROP TABLE IF EXISTS " + ProductEntry.TABLE_NAME + ";";
        String SQL_DROP_COUPONS_PRODUCTS_TABLE = "DROP TABLE IF EXISTS " + CouponProductEntry.TABLE_NAME + ";";

        sqLiteDatabase.execSQL(SQL_DROP_COUPONS_TABLE);
        sqLiteDatabase.execSQL(SQL_DROP_PRODUCTS_TABLE);
        sqLiteDatabase.execSQL(SQL_DROP_COUPONS_PRODUCTS_TABLE);

        onCreate(sqLiteDatabase);
    }
}
