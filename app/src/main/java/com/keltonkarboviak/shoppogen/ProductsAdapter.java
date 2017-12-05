package com.keltonkarboviak.shoppogen;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.keltonkarboviak.shoppogen.Models.Product;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kelton on 11/28/17.
 */

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsAdapterViewHolder>
{
    private Context mContext;

    private Cursor mCursor;

    private List<Product> mProductList;

    private boolean mIsEditable;

    private boolean mIsSelectable;

    public ProductsAdapter(Context context)
    {
        this(context, null);
    }

    public ProductsAdapter(Context context, Cursor cursor)
    {
        this(context, cursor, false);
    }

    public ProductsAdapter(Context context, Cursor cursor, boolean editable)
    {
        this(context, cursor, editable, editable);
    }

    public ProductsAdapter(Context context, Cursor cursor, boolean editable, boolean selectable)
    {
        this.mContext = context;

        swapCursor(cursor);

        this.mIsEditable = editable;
        this.mIsSelectable = selectable;
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

        return new ProductsAdapterViewHolder(
            view,
            new PositionAwareEditTextListener(),
            new PositionAwareOnCheckChangeListener()
        );
    }

    @Override
    public void onBindViewHolder(ProductsAdapterViewHolder holder, int position)
    {
        holder.bind(position);
    }

    @Override
    public int getItemCount()
    {
        return mCursor.getCount();
    }

    public List<Product> getAllProducts()
    {
        return mProductList;
    }

    public List<Product> getCheckedProducts()
    {
        List<Product> productsChecked = new ArrayList<>();
        for (Product p : mProductList) {
            if (p.isChecked()) {
                productsChecked.add(p);
            }
        }
        return productsChecked;
    }

    public void swapCursor(Cursor newCursor)
    {
        if (mCursor != null) {
            mCursor.close();
        }

        mCursor = newCursor;
        mProductList = new ArrayList<>();

        if (mCursor != null) {
            while (mCursor.moveToNext()) {
                mProductList.add(Product.fromCursor(mCursor));
            }
            this.notifyDataSetChanged();
        }
    }

    class ProductsAdapterViewHolder extends RecyclerView.ViewHolder
    {
        public final CheckBox mProductCheckBox;

        public final TextView mProductNameTextView;

        public final EditText mProductPriceEditText;

        public final PositionAwareEditTextListener mPositionAwareEditTextListener;

        public final PositionAwareOnCheckChangeListener mPositionAwareOnCheckChangeListener;

        public ProductsAdapterViewHolder(
            View itemView,
            PositionAwareEditTextListener editTextListener,
            PositionAwareOnCheckChangeListener checkChangeListener)
        {
            super(itemView);

            mProductCheckBox = (CheckBox) itemView.findViewById(R.id.cb_product_update);
            mProductNameTextView = (TextView) itemView.findViewById(R.id.tv_product_name);
            mProductPriceEditText = (EditText) itemView.findViewById(R.id.et_product_price);

            mProductPriceEditText.setFocusable(mIsEditable);

            // Set visibility setting. Depends on the context whether this should be editable.
            if (!mIsSelectable) {
                mProductCheckBox.setVisibility(View.INVISIBLE);
            }

            mPositionAwareEditTextListener = editTextListener;
            mPositionAwareOnCheckChangeListener = checkChangeListener;

            mProductCheckBox.setOnCheckedChangeListener(mPositionAwareOnCheckChangeListener);
            mProductPriceEditText.addTextChangedListener(mPositionAwareEditTextListener);
        }

        public void bind(int position)
        {
            if (position >= mProductList.size()) {
                return;
            }

            final int pos = this.getAdapterPosition();

            mPositionAwareEditTextListener.updatePosition(pos);
            mPositionAwareOnCheckChangeListener.updatePosition(pos);

            Product product = mProductList.get(pos);

            String productName = product.getName();
            double productPrice = product.getPrice();

            mProductCheckBox.setChecked(product.isChecked());
            mProductNameTextView.setText(productName);
            mProductPriceEditText.setText(String.format("%01.2f", productPrice));
        }
    }


    private class PositionAwareEditTextListener implements TextWatcher
    {
        private int mPosition;

        public void updatePosition(int position)
        {
            this.mPosition = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void onTextChanged(
            CharSequence charSequence,
            int start,
            int before,
            int count)
        {
            mProductList.get(mPosition)
                        .setPrice(Double.parseDouble(charSequence.toString()));
        }

        @Override
        public void afterTextChanged(Editable editable) { }
    }


    private class PositionAwareOnCheckChangeListener
        implements CompoundButton.OnCheckedChangeListener
    {
        private int mPosition;

        public void updatePosition(int position)
        {
            this.mPosition = position;
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b)
        {
            mProductList.get(mPosition).setChecked(b);
        }
    }
}
