package com.example.borgo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.borgo.R;
import com.example.borgo.data.model.MenuItem;
import com.example.borgo.utils.RecycleViewInterface;

import org.jspecify.annotations.Nullable;

import java.text.NumberFormat;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder>{

    private static final String TAG = "FoodAdapter";
    public List<MenuItem> foodList;
    Context context;

    private final RecycleViewInterface recycleViewInterface;

    public FoodAdapter(Context context, @Nullable List<MenuItem> list, RecycleViewInterface recycleViewInterface) {
        this.foodList = list;
        this.context = context;
        this.recycleViewInterface = recycleViewInterface;
    }


    public static class FoodViewHolder extends RecyclerView.ViewHolder{

        TextView foodName,foodPrice;
        ImageView foodImage;

        public FoodViewHolder(@NonNull View itemView, RecycleViewInterface recycleViewInterface) {
            super(itemView);

            foodName = itemView.findViewById(R.id.food_name);
            foodPrice = itemView.findViewById(R.id.food_price);
            foodImage = itemView.findViewById(R.id.food_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recycleViewInterface != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            recycleViewInterface.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item,parent,false);
        return new FoodViewHolder(view,recycleViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.FoodViewHolder holder, int position) {
        MenuItem food = foodList.get(position);
        holder.foodName.setText(food.getName());

        double price = food.getPrice();
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
        String formattedPrice = currencyFormatter.format(price);
        holder.foodPrice.setText(formattedPrice);


        if (food.getImage() != null && !food.getImage().isEmpty()) {
            String imageUrl = food.getImage();
            Log.d(TAG, "Loading image from URL: " + imageUrl);

            Glide.with(holder.foodImage.getContext())
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(R.drawable.burger_image)
                    .error(R.drawable.burger_1)
                    .listener(new com.bumptech.glide.request.RequestListener<android.graphics.drawable.Drawable>() {
                        @Override
                        public boolean onLoadFailed(
                                @Nullable GlideException e,
                                Object model,
                                com.bumptech.glide.request.target.Target<android.graphics.drawable.Drawable> target,
                                boolean isFirstResource) {
                            Log.e(TAG, "Glide failed to load image: " + imageUrl, e);
                            return false; // false means Glide will still set the error drawable
                        }

                        @Override
                        public boolean onResourceReady(
                                android.graphics.drawable.Drawable resource,
                                Object model,
                                com.bumptech.glide.request.target.Target<android.graphics.drawable.Drawable> target,
                                com.bumptech.glide.load.DataSource dataSource,
                                boolean isFirstResource) {
                            Log.d(TAG, "Glide successfully loaded: " + imageUrl);
                            return false;
                        }
                    })
                    .into(holder.foodImage);
        } else {
            holder.foodImage.setImageResource(R.drawable.burger_image);
        }
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public void updateList(List<MenuItem> filteredList) {
        foodList = filteredList;
        notifyDataSetChanged();
    }

}
