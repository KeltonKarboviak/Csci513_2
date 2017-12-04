package com.keltonkarboviak.shoppogen;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.keltonkarboviak.shoppogen.Models.Coupon;

import java.util.List;


/**
 * Created by kelton on 12/2/17.
 */

public class CouponsAdapter extends RecyclerView.Adapter<CouponsAdapter.CouponsAdapterViewHolder>
{
    private List<Coupon> mCouponList;

    private int lastSelectedPosition = -1;

    public CouponsAdapter()
    {
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
        CouponsAdapterViewHolder viewHolder = new CouponsAdapterViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CouponsAdapterViewHolder holder, int position)
    {
        holder.bind(position);
    }

    @Override
    public int getItemCount()
    {
        return mCouponList == null
            ? 0
            : mCouponList.size();
    }

    public void setCouponData(List<Coupon> coupons)
    {
        mCouponList = coupons;
        notifyDataSetChanged();
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
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }

        public void bind(int position)
        {
            Coupon coupon = mCouponList.get(position);

            // Since only one radio button is allowed to be selected, this condition un-checks
            // previous selections
            mCouponRadioBtn.setChecked(lastSelectedPosition == position);

            mCouponIdTextView.setText(
                String.valueOf(coupon.getId())
            );
        }
    }
}
