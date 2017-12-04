package com.keltonkarboviak.shoppogen.Models;

/**
 * Created by kelton on 12/3/17.
 */

public class Coupon
{
    private int id;

    private double discount;

    /**
     * A list of products that need to be purchased in order to redeem this Coupon.
     */
    private String[] products;

    public Coupon(int id, double discount, String[] products)
    {
        this.id = id;
        this.discount = discount;
        this.products = products;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
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

    public String[] getProducts()
    {
        return products;
    }

    public void setProducts(String[] products)
    {
        this.products = products;
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

        Coupon coupon = (Coupon) o;

        return getId() == coupon.getId();
    }

    @Override
    public String toString()
    {
        return "Coupon{" + "id=" + id + ", discount=" + discount + ", products=" + products + '}';
    }
}
