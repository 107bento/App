package com.example.aishalien.bento;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.EdgeEffectCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by User on 2017/11/22.
 */

public class cartAdapter  extends BaseAdapter {

    private ArrayList<HashMap<String, Object>> mAppList;
    private LayoutInflater mInflater;
    private Context mContext;
    Activity activity ;
    private String[] keyString;
    private int[] valueViewID;
    GlobalVariable User;
    private ItemView itemView;



    private class ItemView {
        ImageView ItemImage;
        TextView StoreName;
        TextView MealName;
        TextView Details;
        Button ItemButton;
    }

    public cartAdapter(Context c, ArrayList<HashMap<String, Object>> appList, int resource, String[] from, int[] to,GlobalVariable User_input) {
        mAppList = appList;
        mContext = c;
        activity = (Activity) mContext;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        keyString = new String[from.length];
        valueViewID = new int[to.length];
        System.out.println(keyString);
        User= User_input;
        System.arraycopy(from, 0, keyString, 0, from.length);
        System.arraycopy(to, 0, valueViewID, 0, to.length);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        //return 0;
        return mAppList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        //return null;
        return mAppList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        //return 0;
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        //return null;

        if (convertView != null) {
            itemView = (ItemView) convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.list_item_cart, null);
            itemView = new ItemView();
            itemView.ItemImage = (ImageView)convertView.findViewById(valueViewID[0]);
            itemView.StoreName = (TextView)convertView.findViewById(valueViewID[1]);
            itemView.MealName = (TextView)convertView.findViewById(valueViewID[2]);
            itemView.Details = (TextView)convertView.findViewById(valueViewID[3]);
            itemView.ItemButton = (Button)convertView.findViewById(valueViewID[4]);
            convertView.setTag(itemView);
        }

        HashMap<String, Object> appInfo = mAppList.get(position);
        if (appInfo != null) {
            int mid = (Integer)appInfo.get(keyString[0]);
            String store_name = (String) appInfo.get(keyString[1]);
            String meal_name = (String) appInfo.get(keyString[2]);
            String info = (String) appInfo.get(keyString[3]);
            int bid = (Integer)appInfo.get(keyString[4]);
            itemView.StoreName.setText(store_name);
            itemView.MealName.setText(meal_name);
            itemView.Details.setText(info);
            itemView.ItemImage.setImageDrawable(itemView.ItemImage.getResources().getDrawable(mid));
            //itemView.ItemButton.setBackgroundDrawable(itemView.ItemButton.getResources().getDrawable(bid));
            itemView.ItemButton.setOnClickListener(new ItemButton_Click(position));
        }
        return convertView;
    }

    class ItemButton_Click implements View.OnClickListener {
        private int position;
        ItemButton_Click(int pos) {
            position = pos;
        }
        @Override
        public void onClick(View v) {
            int vid=v.getId();
            if (vid == itemView.ItemButton.getId()) {
//                Log.v("ola_log", String.valueOf(position));
                //產生視窗物件
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("Title"); //設定dialog 的title顯示內容
                dialog.setIcon(android.R.drawable.ic_dialog_alert);//設定dialog 的ICON
                dialog.setCancelable(false); //關閉 Android 系統的主要功能鍵(menu,home等...)
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 按下"是"以後要做的事情
                        User.remCart(position);
                        Toast.makeText(mContext, "已刪除第"+position+"號單",
                                Toast.LENGTH_LONG).show();

                        activity.finish();
                        Intent intent = activity.getIntent();
                        mContext.startActivity(intent);
                    }

                });
                dialog.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Toast.makeText(mContext, "已取消",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                dialog.show();
            }
        }
    }
}