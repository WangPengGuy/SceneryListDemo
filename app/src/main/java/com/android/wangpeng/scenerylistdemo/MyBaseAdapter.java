package com.android.wangpeng.scenerylistdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mr_wang on 2016/12/17.
 */


/**
  * listview的适配器，包括上下文对象和数据源
  * 提高listview的效率：当listview滚动时不去加载可见项图片，停止滚动后再开始加载
  */

public class MyBaseAdapter extends BaseAdapter  {

    private LayoutInflater mInflater;
    private List<ContentListItem> listItems;
    private ImageLoader imageLoader;
    private int mStart, mEnd; //listview屏幕可视范围内的第一条item和最后一个item
    public static String URLS[]; //设置一个数组，用来保存所有图片的URL
    private boolean mFirstIn;  //判断是否是第一次启动程序

    public MyBaseAdapter(Context context, List<ContentListItem> data, RefreshListView listView){
        mInflater = LayoutInflater.from(context);
        this.listItems = data;
        imageLoader = new ImageLoader(listView); //在这里初始化，能够保证只有一个imageloader的实例，即只有一个LruCache的实例
        URLS = new String[data.size()];
        for(int i=0; i<data.size(); i++){
            URLS[i] = data.get(i).getSceneryImageUrl();
            imageLoader.loadSingerImages(i);
        }
        mFirstIn = true;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int i) {
        return listItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view == null){
            view = mInflater.inflate(R.layout.singer_list_item, null);
            holder = new ViewHolder();
            holder.sceneryImageVH = (ImageView) view.findViewById(R.id.listImageView);
            holder.sceneryTitleVH = (TextView) view.findViewById(R.id.list_item_title);
            holder.sceneryGradeVH = (TextView) view.findViewById(R.id.grade);
            holder.sceneryAddressVH = (TextView) view.findViewById(R.id.address);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        String url = listItems.get(i).getSceneryImageUrl();
        holder.sceneryImageVH.setTag(url);
        imageLoader.showImages(holder.sceneryImageVH, url);


        holder.sceneryTitleVH.setText(listItems.get(i).getSceneryTitle());
        holder.sceneryGradeVH.setText(listItems.get(i).getSceneryGrade());
        holder.sceneryAddressVH.setText(listItems.get(i).getSceneryAddress());

        return view;
    }

    class ViewHolder{
        public ImageView sceneryImageVH;
        public TextView sceneryTitleVH, sceneryGradeVH, sceneryAddressVH;
    }

}
