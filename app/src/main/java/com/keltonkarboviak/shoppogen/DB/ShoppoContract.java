package com.keltonkarboviak.shoppogen.DB;

import android.provider.BaseColumns;


/**
 * Created by kelton on 12/3/17.
 */

public class ShoppoContract
{
    private ShoppoContract() { }

    public static final class ProductEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "products";

        public static final String COLUMN_PRODUCT_NAME = "name";

        public static final String COLUMN_PRODUCT_PRICE = "price";
    }


    public static final class CouponEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "coupons";

        public static final String COLUMN_COUPON_DISCOUNT = "discount";
    }


    public static final class CouponProductEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "coupons_products";

        public static final String COLUMN_COUPON_ID = "coupon_id";

        public static final String COLUMN_PRODUCT_ID = "product_id";
    }
}
