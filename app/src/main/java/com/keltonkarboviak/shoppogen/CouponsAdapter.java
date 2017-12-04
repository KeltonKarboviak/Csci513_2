package com.keltonkarboviak.shoppogen;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.keltonkarboviak.shoppogen.Models.Coupon;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kelton on 12/2/17.
 */

public class CouponsAdapter extends RecyclerView.Adapter<CouponsAdapter.CouponsAdapterViewHolder>
{
    private Context mContext;

    private Cursor mCursor;

    private List<Coupon> mCouponList;

    private int lastSelectedPosition = -1;

    public CouponsAdapter(Context context, Cursor cursor)
    {
        this.mContext = context;

        swapCursor(cursor);
    }

    @Override
    public CouponsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.coupon_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(
            layoutIdForListItem,
            viewGroup,
            shouldAttachToParentImmediately
        );

        return new CouponsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CouponsAdapterViewHolder holder, int position)
    {
        holder.bind(position);
    }

    public void resetSelectedPosition()
    {
        this.lastSelectedPosition = -1;
    }

    @Override
    public int getItemCount()
    {
        return mCouponList.size();
    }

    public Coupon getSelectedCoupon()
    {
        return lastSelectedPosition != -1 && lastSelectedPosition < mCouponList.size()
            ? mCouponList.get(lastSelectedPosition)
            : null;
    }

    public void swapCursor(Cursor newCursor)
    {
        if (mCursor != null) {
            mCursor.close();
        }

        mCursor = newCursor;
        mCouponList = new ArrayList<>();

        if (mCursor != null) {
            while (mCursor.moveToNext()) {
                this.mCouponList.add(Coupon.fromCursor(mCursor));
            }
            this.notifyDataSetChanged();
        }
    }

    class CouponsAdapterViewHolder extends RecyclerView.ViewHolder
    {
        public final RadioButton mCouponRadioBtn;

        public final TextView mCouponIdTextView;

        public CouponsAdapterViewHolder(View itemView)
        {
            super(itemView);

            mCouponRadioBtn = (RadioButton) itemView.findViewById(R.id.rb_coupon_update);
            mCouponIdTextView = (TextView) itemView.findViewById(R.id.tv_coupon_id);

            mCouponRadioBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    lastSelectedPosition = CouponsAdapterViewHolder.this.getAdapterPosition();
                    CouponsAdapter.this.notifyDataSetChanged();
                }
            });
        }

        public void bind(int position)
        {
            if (position >= mCouponList.size()) {
                return;
            }

            final int pos = this.getAdapterPosition();

            Coupon coupon = mCouponList.get(pos);

            long id = coupon.getId();

            this.itemView.setTag(id);

            // Since only one radio button is allowed to be selected, this condition un-checks
            // previous selections
            mCouponRadioBtn.setChecked(lastSelectedPosition == position);

            mCouponIdTextView.setText(String.valueOf(id));
        }
    }
}
