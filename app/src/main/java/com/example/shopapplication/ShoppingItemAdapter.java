package com.example.shopapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ShoppingItemAdapter extends RecyclerView.Adapter<ShoppingItemAdapter.ViewHolder> implements Filterable {
    private ArrayList<ShoppingItem> shoppingItemsData;
    private ArrayList<ShoppingItem> shoppingItemsDataAll;
    private Context context;
    private int lastPosition = -1;

    public ShoppingItemAdapter(Context context, ArrayList<ShoppingItem> itemsData) {
        this.shoppingItemsData = itemsData;
        this.shoppingItemsDataAll = itemsData;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ShoppingItemAdapter.ViewHolder holder, int position) {
        ShoppingItem currentItem = shoppingItemsData.get(position);
        holder.bindTo(currentItem);

        if(holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return shoppingItemsData.size();
    }

    @Override
    public Filter getFilter() {
        return shoppingFilter;
    }

    private Filter shoppingFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<ShoppingItem> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if (charSequence == null || charSequence.length() == 0) {
                results.count = shoppingItemsDataAll.size();
                results.values = shoppingItemsDataAll;
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (ShoppingItem item: shoppingItemsDataAll) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            shoppingItemsData = (ArrayList) results.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleText;
        private TextView infoText;
        private TextView priceText;
        private ImageView itemImage;
        private RatingBar ratingBar;
        public ViewHolder( View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.itemTitle);
            infoText = itemView.findViewById(R.id.subTitle);
            priceText = itemView.findViewById(R.id.price);
            itemImage = itemView.findViewById(R.id.itemImage);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }

        public void bindTo(ShoppingItem currentItem) {
            titleText.setText(currentItem.getName());
            infoText.setText(currentItem.getInfo());
            priceText.setText(currentItem.getPrice());
            ratingBar.setRating(currentItem.getRatedInfo());

//            Glide.with(context).load(currentItem.getImageResource()).into(itemImage);
            itemView.findViewById(R.id.add_to_cart).setOnClickListener(v -> ((ShopListActivity) context).updateAlertIcon(currentItem));
            itemView.findViewById(R.id.delete).setOnClickListener(v -> ((ShopListActivity) context).deleteItem(currentItem));
        }
    };

};

