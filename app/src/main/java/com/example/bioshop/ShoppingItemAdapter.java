package com.example.bioshop;

import android.content.Context;
import android.util.Log;
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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ShoppingItemAdapter extends RecyclerView.Adapter<ShoppingItemAdapter.ViewHolder> implements Filterable {

    private ArrayList<ShoppingItem> mShoppingitemsData;
    private ArrayList<ShoppingItem> mShoppingitemsDataALL;
    private Context mContext;
    private int lastPosition=-1;

    ShoppingItemAdapter(Context context, ArrayList<ShoppingItem> itemsData){
this.mShoppingitemsData=itemsData;
this.mShoppingitemsDataALL=itemsData;
this.mContext=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_list,parent,false));
    }

    @Override
    public void onBindViewHolder( ShoppingItemAdapter.ViewHolder holder, int position) {
        ShoppingItem currentItem=mShoppingitemsData.get(position);
        holder.bindTo(currentItem);

        if (holder.getAdapterPosition() > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(mContext,R.anim.fade_slide);
            holder.itemView.startAnimation(animation);
            lastPosition=holder.getAdapterPosition();
        }

    }

    @Override
    public int getItemCount() {
        return mShoppingitemsData.size();
    }
private Filter shoppingFilter = new Filter() {
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        ArrayList<ShoppingItem> szurtlista =new ArrayList<>();
        FilterResults results=new FilterResults();
        if (constraint==null || constraint.length()==0){
            results.count=mShoppingitemsDataALL.size();
            results.values=mShoppingitemsDataALL;
        }else{
            String filterPattern = constraint.toString().toLowerCase().trim();
            for (ShoppingItem item:mShoppingitemsDataALL){
                if (item.getName().toLowerCase().contains(filterPattern)){
                    szurtlista.add(item);
                }
            }
            results.count= szurtlista.size();
            results.values= szurtlista;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        mShoppingitemsData=(ArrayList) results.values;
        notifyDataSetChanged();
    }
};
    @Override
    public Filter getFilter() {
        return shoppingFilter;
    }

    class  ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTitleText;
        private TextView mInfoText;
        private TextView mPriceText;
        private ImageView mItemImage;
        private RatingBar mRatingBar;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

             mTitleText=itemView.findViewById(R.id.termekcim);
             mInfoText=itemView.findViewById(R.id.termekleiras);
            mPriceText=itemView.findViewById(R.id.ar);
             mItemImage=itemView.findViewById(R.id.termekkep);
             mRatingBar=itemView.findViewById(R.id.ertekeles);
             itemView.findViewById(R.id.kosarba).setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Log.d("Activity", "Be lett zuzva a kosarba gomb");
                 }
             });
        }

        public void bindTo(ShoppingItem currentItem) {
            mTitleText.setText(currentItem.getName());
            mInfoText.setText(currentItem.getInfo());
            mPriceText.setText(currentItem.getPrice());
            mRatingBar.setRating(currentItem.getRateinfo());
            Glide.with(mContext).load(currentItem.getImageResource()).into(mItemImage);


        }
    };
}
