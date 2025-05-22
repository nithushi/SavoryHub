package lk.javainstitute.savoryhub.adapters;

import android.content.Context;
import android.content.Intent;
import android.opengl.GLES30;
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


public class BestFoodsAdapter extends RecyclerView.Adapter<BestFoodsAdapter.BestFoodsViewHolder> {

    private ArrayList<Foods> foodItems;
    private Context context;

    public BestFoodsAdapter(ArrayList<Foods> foodItems) {
        this.foodItems = foodItems;
    }

    @NonNull
    @Override
    public BestFoodsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.best_product_item, parent, false);
        return new BestFoodsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BestFoodsViewHolder holder, int position) {
        Foods food = foodItems.get(position);

        Log.d("BestFoodsAdapter", "Binding data for position: " + position + ", title: " + food.getTitle() + ", price: " + food.getPrice() + ", star: " + food.getStar() + ", time: " + food.getTimeValue() + ", image: " + food.getImagePath());

        holder.titleText.setText(food.getTitle());
        holder.priceText.setText("$" + food.getPrice());
        holder.timeText.setText(food.getTimeValue() + " mins");
        holder.startText.setText(""+food.getStar());


        int drawableResourceId = context.getResources().getIdentifier(food.getImagePath(), "drawable", context.getPackageName());

        Glide.with(context)
                .load(drawableResourceId)
                .transform(new CenterCrop(), new RoundedCorners(20))
                .into(holder.pic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("object", food);
                context.startActivity(intent);
            }
        });

    }

        public void setItems(ArrayList<Foods> items) {
        this.foodItems.clear();
        if(items !=null){
            this.foodItems.addAll(items);
        }
        //this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    public static class BestFoodsViewHolder extends RecyclerView.ViewHolder {

        TextView titleText, priceText, startText, timeText;
        ImageView pic;

        public BestFoodsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.food_title_txt);
            priceText = itemView.findViewById(R.id.food_price_txt);
            startText = itemView.findViewById(R.id.food_star_txt);
            timeText = itemView.findViewById(R.id.food_time_txt);
            pic = itemView.findViewById(R.id.food_pic);
        }
    }
}














//public class BestFoodsAdapter extends RecyclerView.Adapter<BestFoodsAdapter.BestFoodsViewHolder>{
//
//    ArrayList<Foods> items;
//    Context context;
//
//    public BestFoodsAdapter(ArrayList<Foods> items) {
//        this.items = items;
//    }
//
//    @NonNull
//    @Override
//    public BestFoodsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        context =parent.getContext();
//        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.best_product_item, parent, false);
//
//        return new BestFoodsViewHolder(inflate);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull BestFoodsViewHolder holder, int position) {
//        holder.titleText.setText(items.get(position).getTitle());
//        holder.priceText.setText("$"+items.get(position).getPrice());
//        holder.timeText.setText(items.get(position).getTimeValue());
//        holder.startText.setText(""+items.get(position).getStar());
//
////        int drawableResourceId = context.getResources().getIdentifier
////                (items.get(position).getImagePath(),"drawable", holder.itemView.getContext().getPackageName());
//
//        Glide.with(context)
//                .load(items.get(position).getImagePath())
//                .transform(new CenterCrop(), new RoundedCorners(30))
//                .into(holder.pic);
//
//    }
//
//    public void setItems(ArrayList<Foods> items) {
//        this.items.clear();
//        if(items !=null){
//            this.items.addAll(items);
//        }
//        //this.items = items;
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public int getItemCount() {
//        return items.size();
//    }
//
//    public class BestFoodsViewHolder extends RecyclerView.ViewHolder{
//
//        TextView titleText;
//        TextView priceText;
//        TextView startText;
//        TextView timeText;
//        ImageView pic;
//
//        public BestFoodsViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            titleText = itemView.findViewById(R.id.food_title_txt);
//            priceText = itemView.findViewById(R.id.food_price_txt);
//            startText = itemView.findViewById(R.id.food_star_txt);
//            timeText = itemView.findViewById(R.id.food_time_txt);
//            pic = itemView.findViewById(R.id.food_pic);
//
//
//        }
//    }
//}
//
