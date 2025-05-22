package lk.javainstitute.savoryhub.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;

import lk.javainstitute.savoryhub.R;
import lk.javainstitute.savoryhub.model.Foods;
import lk.javainstitute.savoryhub.ui.DetailsActivity;
import lk.javainstitute.savoryhub.ui.ListFoodsActivity;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.viewHolder> {

    ArrayList<Foods> items;
    Context context;

    public FoodListAdapter(ArrayList<Foods>items) {
        this.items=items;
    }

    @NonNull
    @Override
    public FoodListAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.viewholder_list_foods, parent, false);
        return new viewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodListAdapter.viewHolder holder, int position) {
        Foods foodListItem = items.get(position);
        Log.i("ListFoodAdapter", "Binding data for position: " + position + ", title: " + foodListItem.getTitle() + ", price: " + foodListItem.getPrice() + ", star: " + foodListItem.getStar() + ", time: " + foodListItem.getTimeValue() + ", image: " + foodListItem.getImagePath());

        holder.titleTxt.setText(foodListItem.getTitle());
        holder.timeTxt.setText(foodListItem.getTimeValue() +" min");
        holder.priceTxt.setText("$"+ foodListItem.getPrice());
        holder.startTxt.setText(""+foodListItem.getStar());

        int drawableResourceId = context.getResources().getIdentifier(items.get(position).getImagePath(), "drawable", context.getPackageName());

        Glide.with(context)
                .load(drawableResourceId)
                .transform(new CenterCrop(), new RoundedCorners(20))
                .into(holder.pic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ListFoodAdapter", "Passing object: " + foodListItem.getTitle());
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("object", foodListItem);
                context.startActivity(intent);
            }
        });
    }

    public void setItems(ArrayList<Foods> items) {
        this.items.clear();
        if (items != null) {
            this.items.addAll(items);
        }
        //this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        TextView titleTxt;
        TextView priceTxt;
        TextView startTxt;
        TextView timeTxt;
        ImageView pic;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            titleTxt = itemView.findViewById(R.id.list_food_titleTxt);
            priceTxt = itemView.findViewById(R.id.list_food_priceTxt);
            startTxt = itemView.findViewById(R.id.list_food_rateTxt);
            timeTxt = itemView.findViewById(R.id.list_food_timeTxt);
            pic = itemView.findViewById(R.id.list_food_img);
        }
    }
}

