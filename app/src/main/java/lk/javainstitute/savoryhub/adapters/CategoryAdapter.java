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

import java.util.ArrayList;

import lk.javainstitute.savoryhub.R;
import lk.javainstitute.savoryhub.model.Category;
import lk.javainstitute.savoryhub.ui.ListFoodsActivity;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.BestFoodsViewHolder>{

    ArrayList<Category> items;
    Context context;

    public CategoryAdapter(ArrayList<Category> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public BestFoodsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context =parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category_item, parent, false);

        return new BestFoodsViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull BestFoodsViewHolder holder, int position) {

        holder.titleText.setText(items.get(position).getName());

        switch (position){
            case 0:{
                holder.pic.setBackgroundResource(R.drawable.cat0_background);
                break;
            }
            case 1:{
                holder.pic.setBackgroundResource(R.drawable.cat1_background);
                break;
            }
            case 2:{
                holder.pic.setBackgroundResource(R.drawable.cat2_background);
                break;
            }
            case 3:{
                holder.pic.setBackgroundResource(R.drawable.cat3_background);
                break;
            }
            case 4:{
                holder.pic.setBackgroundResource(R.drawable.cat4_background);
                break;
            }
            case 5:{
                holder.pic.setBackgroundResource(R.drawable.cat5_background);
                break;
            }
            case 6:{
                holder.pic.setBackgroundResource(R.drawable.cat6_background);
                break;
            }
            case 7:{
                holder.pic.setBackgroundResource(R.drawable.cat7_background);
                break;
            }
        }

        int drawableResourceId = context.getResources().getIdentifier
                (items.get(position).getImagePath(),"drawable", holder.itemView.getContext().getPackageName());

        Glide.with(context)
                .load(drawableResourceId)
                .into(holder.pic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CategoryAdapter", "Category ID: " + position + ", Category Name: " + items.get(position).getName());
                Intent intent = new Intent(context, ListFoodsActivity.class);
                intent.putExtra("CategoryId", position);
                intent.putExtra("CategoryName", items.get(position).getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class BestFoodsViewHolder extends RecyclerView.ViewHolder{

        TextView titleText;
        ImageView pic;
        public BestFoodsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.catNameTxt);
            pic = itemView.findViewById(R.id.imgCat);
        }
    }
}
