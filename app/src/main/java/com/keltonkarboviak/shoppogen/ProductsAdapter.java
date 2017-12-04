package com.keltonkarboviak.shoppogen;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.keltonkarboviak.shoppogen.Models.Product;

import java.util.List;


/**
 * Created by kelton on 11/28/17.
 */

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsAdapterViewHolder>
{
    private List<Product> mProductList;

    public ProductsAdapter()
    {
    }

    @Override
    public ProductsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.product_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(
            layoutIdForListItem,
            viewGroup,
            shouldAttachToParentImmediately
        );
        ProductsAdapterViewHolder viewHolder = new ProductsAdapterViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProductsAdapterViewHolder holder, int position)
    {
        holder.bind(position);
    }

    @Override
    public int getItemCount()
    {
        return mProductList == null
            ? 0
            : mProductList.size();
    }

    public void setProductData(List<Product> products)
    {
        mProductList = products;
        notifyDataSetChanged();
    }

    class ProductsAdapterViewHolder extends RecyclerView.ViewHolder
    {
        public final CheckBox mProductCheckBox;

        public final TextView mProductNameTextView;

        public final EditText mProductPriceEditText;

        public ProductsAdapterViewHolder(View itemView)
        {
            super(itemView);

            mProductCheckBox = (CheckBox) itemView.findViewById(R.id.cb_product_update);
            mProductNameTextView = (TextView) itemView.findViewById(R.id.tv_product_name);
            mProductPriceEditText = (EditText) itemView.findViewById(R.id.et_product_price);
        }

        public void bind(int position)
        {
            Product product = mProductList.get(position);

            mProductNameTextView.setText(product.getName());
            mProductPriceEditText.setText(String.format("%01.2f", product.getPrice()));
        }
    }
}
