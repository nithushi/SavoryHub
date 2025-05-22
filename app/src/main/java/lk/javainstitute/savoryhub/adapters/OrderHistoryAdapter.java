package lk.javainstitute.savoryhub.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import lk.javainstitute.savoryhub.R;
import lk.javainstitute.savoryhub.model.Orders;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    private ArrayList<Orders> orderList;

    public OrderHistoryAdapter(ArrayList<Orders> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderHistoryAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_order_item, parent,false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.OrderViewHolder holder, int position) {

        Orders order = orderList.get(position);
        holder.orderId.setText("#"+ order.getOrderId());
        holder.email.setText(order.getUser());
        holder.total.setText("$"+ order.getTotal());
        holder.date.setText(order.getDate());
        //holder.status.setText("Delivered");

        int status_id = order.getStatus_Id();
        if(status_id==2){
            holder.status.setText("Delivered");
            holder.status.setBackgroundResource(R.drawable.instock_background);
        } else if (status_id==4) {
            holder.status.setText("Shipped");
            holder.status.setBackgroundResource(R.drawable.shipped_background);
        }else{
            holder.status.setText("New Order");
            holder.status.setBackgroundResource(R.drawable.outstock_background);
        }

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView orderId,email,total,date,status;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.admin_orderId);
            email = itemView.findViewById(R.id.admin_orderEmail);
            total = itemView.findViewById(R.id.admin_orderTotal);
            date = itemView.findViewById(R.id.admin_orderDate);
            status = itemView.findViewById(R.id.admin_orderStatus);
        }
    }
}


