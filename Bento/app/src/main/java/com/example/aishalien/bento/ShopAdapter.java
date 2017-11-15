package com.example.aishalien.bento;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 *  控制 recycler view 用來顯示店家
 */

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {

    private List<ShopItemData> itemsData;

    // 這邊是接 main_menu 傳進來的值
    public ShopAdapter(List<ShopItemData> ItemData) {
        this.itemsData = ItemData;
    }


    // 初始化自定义 ViewHolder 时调用此方法。
    // 我们指定 RecyclerView 每个条目应使用的布局
    @Override
    public ShopAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_main_card_item, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }


    // 指定 RecyclerView 的每个项目的内容
    // 在此方法内设置 CardView 的名称、照片字段的值
    @Override
    public void onBindViewHolder(ShopAdapter.ViewHolder holder, int position) {

        // 設定 textView 的文字由 itemsData 取出，而 itemsData 的內容已經在 ItemData.java 設定好了
        holder.shopTittle.setText(itemsData.get(position).getTitle());
        final String mStoreName = itemsData.get(position).getTitle();
        final int mStoreID = itemsData.get(position).storeId;

        // 設定 imgview 的圖片由 itemsData 取出
        holder.shopImg.setImageResource(itemsData.get(position).imgId);
        final int mStoreImg = itemsData.get(position).imgId;

        // 點擊 shop 項目時
        holder.shopCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 建立一個Bundle
                Bundle bundle = new Bundle();
                bundle.putString("store_name", mStoreName);
                bundle.putInt("store_img", mStoreImg);
                bundle.putInt("store_id", mStoreID);

                // 將bundle傳入
                // 換頁
                Intent intent = new Intent(view.getContext(), store_menu.class);
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);
            }
        });

    }

    // 返回数据中现存的条目(item)数
    @Override
    public int getItemCount() {
        return itemsData.size();
    }


    // 使用代表店家的 CardView 布局
    // 在自定义 ViewHolder 的构造函数内，初始化属于 RecyclerView 条目的视图
    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView shopCard;
        TextView shopTittle;
        ImageView shopImg;

        public ViewHolder(final View itemView) {
            super(itemView);
            shopCard = itemView.findViewById(R.id.shop_item_card);

            // 設定 textView 為 shop_item_title 這個 layout 物件
            shopTittle = itemView.findViewById(R.id.shop_item_tittle);
            // 設定圖片
            shopImg = itemView.findViewById(R.id.shop_item_img);

        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}

