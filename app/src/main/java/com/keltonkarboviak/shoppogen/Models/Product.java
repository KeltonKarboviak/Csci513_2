package com.keltonkarboviak.shoppogen.Models;

/**
 * Created by kelton on 12/3/17.
 */

public class Product
{
    private String name;

    private double price;

    public Product(String name, double price)
    {
        this.name = name;
        this.price = price;
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
        return "Product{" + "name='" + name + '\'' + ", price=" + price + '}';
    }
}
