package com.fun.parking.customer.orderHistory;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fun.parking.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    ArrayList<Order> mOrderList;
    Context mContext;

    public OrderAdapter(ArrayList<Order> orderList, Context context) {
        mOrderList = orderList;
        mContext = context;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.customer_history_order_list, null);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = mOrderList.get(position);
        String pattern = "dd/MM/yyyy HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        //binding the data with the viewholder views
        holder.textViewAddress.setText("Address: " + order.getAddress());
        holder.textViewHours.setText(String.format("Hours: %.1f" , order.getHours()));
        holder.textViewTotalPrice.setText(String.format("%.2f ₪" , order.getTotalPrice()));
        holder.textViewStartDate.setText("Start: " + simpleDateFormat.format(order.getStartDate()));
        holder.textViewEndDate.setText("End:   " +  simpleDateFormat.format(order.getEndDate()));
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }


    class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView textViewAddress, textViewHours, textViewTotalPrice,
                textViewStartDate, textViewEndDate;

        public OrderViewHolder(View view) {
            super(view);

            textViewAddress = view.findViewById(R.id.textViewAddress);
            textViewHours = view.findViewById(R.id.textViewHours);
            textViewTotalPrice = view.findViewById(R.id.textViewTotalPrice);
            textViewStartDate = view.findViewById(R.id.textViewStartDate);
            textViewEndDate = view.findViewById(R.id.textViewEndeDate);
        }
    }
}