package com.keltonkarboviak.shoppogen.Models;

import android.content.ContentValues;
import android.database.Cursor;

import com.keltonkarboviak.shoppogen.DB.ShoppoContract;


/**
 * Created by kelton on 12/3/17.
 */

public class Product
{
    private long id;

    private String name;

    private double price;

    private boolean checked;

    public Product()
    {
        this(-1, "", -1.0);
    }

    public Product(String name, double price)
    {
        this(-1, name, price);
    }

    public Product(long id, String name, double price)
    {
        this.id = id;
        this.name = name;
        this.price = price;
        this.checked = false;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public double getPrice()
    {
        return price;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public boolean isChecked()
    {
        return checked;
    }

    public void setChecked(boolean checked)
    {
        this.checked = checked;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Product product = (Product) o;

        return getName().equals(product.getName());
    }

    @Override
    public String toString()
    {
        return "Product{" +
            "id=" + id + ", " +
            "name='" + name + '\'' + ", " +
            "price=" + String.format("%01.2f", price) +
            '}';
    }

    public ContentValues toContentValues()
    {
        ContentValues cv = new ContentValues();

        if (this.id >= 0) {
            cv.put(ShoppoContract.ProductEntry._ID, this.id);
        }

        cv.put(ShoppoContract.ProductEntry.COLUMN_PRODUCT_NAME, this.name);
        cv.put(ShoppoContract.ProductEntry.COLUMN_PRODUCT_PRICE, this.price);

        return cv;
    }

    public static Product fromCursor(Cursor cursor)
    {
        return new Product(
            cursor.getLong(cursor.getColumnIndex(ShoppoContract.ProductEntry._ID)),
            cursor.getString(cursor.getColumnIndex(ShoppoContract.ProductEntry.COLUMN_PRODUCT_NAME)),
            cursor.getDouble(cursor.getColumnIndex(ShoppoContract.ProductEntry.COLUMN_PRODUCT_PRICE))
        );
    }
}
