package com.android.wangpeng.scenerylistdemo;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;
import cn.qqtheme.framework.util.ConvertUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements OnRefreshListener {

    private RefreshListView mListView;
    private MyBaseAdapter adapter;
    private String baseJsonUrl = "http://apis.haoservice.com/lifeservice/travel/scenery?key=63d6e291a49744b79f1e825b11ce9908";
    private StringBuffer extendJsonUrl_Province;
    private StringBuffer extendJsonUrl_City;
    private StringBuffer extendJsonUrl_Page;
    private int provinceID = 0;
    private int[] provinceIDArray;
    private String[] provinceStringArray;
    private int cityID = 0;
    private int[][] cityIDArray;
    private String[][] cityStringArray;
    private int page = 1;
    private String json;
    private OkHttpClient okHttpClient;
    private List<ContentListItem> lists ;
    private ArrayList<String> listsAddress;     // 将想去的景点的地址添加到这个List中，可以直接返回并定位
    private String address;
    private String addressTitle;
    private String sceneryDetailUrl;
    final int MENU_DETAIL = 0x111;
    final int MENU_SELECTOR = 0x112;
    final int MENU_CANCLE = 0x113;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (RefreshListView) findViewById(R.id.listView);
        extendJsonUrl_Page = new StringBuffer(baseJsonUrl);
        extendJsonUrl_Province = new StringBuffer(baseJsonUrl);

        listsAddress = new ArrayList<String>();

        lists = intiLists();

        adapter = new MyBaseAdapter(MainActivity.this, lists, mListView);

        mListView.setAdapter(adapter);

        mListView.setOnRefreshListener(MainActivity.this);

        registerForContextMenu(mListView);

        okHttpClient = new OkHttpClient();

        new Thread(new MyThread()).start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.main_aty_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        showAddressDialog();
        return true;

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        item.setChecked(true);

        switch (item.getItemId()){
            case MENU_DETAIL :
                Intent intent = new Intent(MainActivity.this, WebviewDetail.class);
                intent.putExtra("detail_url",sceneryDetailUrl);
                startActivity(intent);
                break;
            case MENU_SELECTOR :
                listsAddress.add(address);
                Toast.makeText(MainActivity.this, addressTitle+"已被添加到行程中！ ", Toast.LENGTH_LONG).show();
                break;
            case MENU_CANCLE :
                Iterator<String> iterator = listsAddress.iterator();
                while(iterator.hasNext()){
                    String address1 = iterator.next();
                    if(address1.equals(address)){
                        iterator.remove();
                        Toast.makeText(MainActivity.this, addressTitle+"已从行程中删除！ ", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            default:
                return  super.onOptionsItemSelected(item);
        }
        return true;
    }

    /**
     * 初始化省份、城市矩阵
     */
    public void initProvince$City$ID$StringArray(){
        provinceIDArray = ProvinceCityID.getProvinceID();
        cityIDArray = ProvinceCityID.getCityID();
        provinceStringArray = getResources().getStringArray(R.array.arrayProvinceString);
        //cityStringArray = getTwoDimensionalArray(R.array.arrayCityString);
        //System.out.println(cityStringArray[0][2]);
    }

    /**
     * 按设定规则解析xml文件中一维数组为二维数组（---------还没完成-------）
     * @param id
     * @return
     */
    private String[][] getTwoDimensionalArray(int id) {
        String[][] twoDimensionalArray = null;
        String[] array = getResources().getStringArray(id);
        System.out.println(array.length);

        TypedArray typedArray1 = getResources().obtainTypedArray(id);

        for (int i=0; i<array.length; i++){

            TypedArray typedArray2 = getResources().obtainTypedArray(typedArray1.getResourceId(i,1));

            System.out.println(typedArray2);

            for (int j=0; j<typedArray2.length(); j++){
                twoDimensionalArray[i] = getResources().getStringArray(typedArray2.getResourceId(j,0));
            }

            typedArray2.recycle();

        }

        typedArray1.recycle();

        return twoDimensionalArray;
    }

    /**
     * 显示地址选择器
     */
    public void showAddressDialog(){
        try {
            System.out.println("点击了地址选择器----");
            ArrayList<Province> data = new ArrayList<Province>();
            String json = ConvertUtils.toString(getAssets().open("city.json"));
            data.addAll(JSON.parseArray(json, Province.class));
            AddressPicker picker = new AddressPicker(this, data);
            //picker.setCycleDisable(true);
            //picker.setHideProvince(true);
            //picker.setSelectedItem("贵州", "贵阳", "花溪");
            //picker.setHalfScreen(true);
            //picker.setHalfScreen(true);
            picker.setGravity(Gravity.CENTER_VERTICAL);

            //点击地址选择器的“确定”时，重绘ListView
            picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
                @Override
                public void onAddressPicked(Province province, City city, County county) {
                    if (county == null) {
                        System.out.println("province : " + province + ", city: " + city);
                        getUrl(province.getAreaName().toString(),city.getAreaName().toString());
                        start(extendJsonUrl_Province.toString(),okHttpClient);
                        //System.out.println(extendJsonUrl_Province);
                        extendJsonUrl_Province.delete(0,extendJsonUrl_Province.length());
                        extendJsonUrl_Province.replace(0,baseJsonUrl.length(),baseJsonUrl);
                        //System.out.println(extendJsonUrl_Province);
                    } else {
                        System.out.println("province : " + province + ", city: " + city + ", county: " + county);
                        getUrl(province.getAreaName().toString(),city.getAreaName().toString());
                        //System.out.println(extendJsonUrl_Province);
                        start(extendJsonUrl_Province.toString(),okHttpClient);
                        extendJsonUrl_Province.delete(0,extendJsonUrl_Province.length());
                        extendJsonUrl_Province.replace(0,baseJsonUrl.length(),baseJsonUrl);
                        //System.out.println(extendJsonUrl_Province);
                    }
                }
            });
            picker.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getUrl(String province, String city){

        for (int i=0; i<provinceStringArray.length; i++){
            if(province.equals(provinceStringArray[i])){
                provinceID = provinceIDArray[i];
                page = 1;
            }
        }
        extendJsonUrl_Province = extendJsonUrl_Province.append("&pid=").append(String.valueOf(provinceID));
    }

    /**
     * 第一次进入时初始化List
     * @return
     */
    public List<ContentListItem> intiLists(){

        List<ContentListItem> contentListItems = new ArrayList<ContentListItem>();
        ContentListItem contentListItem = new ContentListItem("http://7xvlr6.com1.z0.glb.clouddn.com/QQ%E5%9B%BE%E7%89%8720160624155145.jpg","","","","");
        contentListItems.add(contentListItem);
        return contentListItems;

    }

    /**
     * 第一次进入时的初始化界面
     */

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){

                json = (String) msg.obj;
                //System.out.println(json);

                lists = getJsonData(baseJsonUrl,json);
                //System.out.println(lists);

                adapter = new MyBaseAdapter(MainActivity.this, lists, mListView);
                adapter.notifyDataSetChanged();

                mListView.setAdapter(adapter);
                mListView.setOnRefreshListener(MainActivity.this);
                registerForContextMenu(mListView);

                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        ListView listView = (ListView) adapterView;
                        ContentListItem contentListItem = (ContentListItem) listView.getItemAtPosition(i);
                        sceneryDetailUrl = contentListItem.getSceneryDetailUrl();
                        Intent intent = new Intent(MainActivity.this, WebviewDetail.class);
                        intent.putExtra("detail_url",sceneryDetailUrl);
                        startActivity(intent);

                    }
                });

     /*           mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        ListView listView = (ListView) adapterView;
                        registerForContextMenu(listView);
                        ContentListItem contentListItem = (ContentListItem) listView.getItemAtPosition(i);
                        address = contentListItem.getSceneryAddress();
                        sceneryDetailUrl = contentListItem.getSceneryDetailUrl();
                        addressTitle = contentListItem.getSceneryTitle();
                        System.out.println(address);
                        return true;
                    }
                });*/

                mListView.setOnCreateContextMenuListener(new AdapterView.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

                        //System.out.println(contextMenuInfo.getClass());
                        AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) contextMenuInfo;

                        ListView listView = (ListView) view;
                        contextMenu.add(Menu.NONE, MENU_DETAIL, 0, "查看景点详情");
                        contextMenu.add(Menu.NONE, MENU_SELECTOR, 0, "添加到行程中");
                        contextMenu.add(Menu.NONE, MENU_CANCLE, 0, "从行程中取消");
                        contextMenu.setGroupCheckable(0,true,true);
                        contextMenu.setHeaderTitle("请选择：");

                        ContentListItem contentListItem = (ContentListItem) listView.getItemAtPosition(adapterContextMenuInfo.position);
                        address = contentListItem.getSceneryAddress();
                        sceneryDetailUrl = contentListItem.getSceneryDetailUrl();
                        addressTitle = contentListItem.getSceneryTitle();
                        //System.out.println(address);
                        //super.onCreateContextMenu(menu, v, menuInfo);
                    }
                });

                showAddressDialog();

            }
        }
    };

    public class MyThread implements Runnable{

        @Override
        public void run() {

            initProvince$City$ID$StringArray();

            Request request = new Request.Builder()
                    .get()
                    .url(baseJsonUrl)
                    .build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                json = response.body().string();
                Message msg = Message.obtain();
                msg.obj = json;
                msg.what = 1;
                handler.sendMessage(msg);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解析Json数据并存入List中
     * @param jsonUrl
     * @param json
     * @return
     */

    private List<ContentListItem> getJsonData(final String jsonUrl, String json) {

        List<ContentListItem> contentListItems = new ArrayList<ContentListItem>();
        try {

            ContentListItem contentListItem;

            JSONObject jb = new JSONObject(json);

            JSONArray ja = jb.getJSONArray("result");

            int ja_length = ja.length();
            System.out.println(ja_length);

            for (int i = 0; i < ja_length; i++) {

                JSONObject ja_result = new JSONObject((String) ja.get(i).toString());
                String title = ja_result.getString("title");
                String image_url = ja_result.getString("imgurl");
                String grade = ja_result.getString("grade");
                String address = ja_result.getString("address");
                String url = ja_result.getString("url");
                contentListItem = new ContentListItem(image_url,title,grade,address,url);
                contentListItems.add(contentListItem);
            }
        }catch (JSONException e)
        {
            e.printStackTrace();
        }

        return contentListItems;
    }

    /**
     * 数据变化时（上拉刷新、点击了地址选择等），重绘ListView
     * @param jsonUrl
     * @param okHttpClient
     */

    public void start(final String jsonUrl, OkHttpClient okHttpClient){

            Request request = new Request.Builder()
                .get()
                .url(jsonUrl)
                .build();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //Toast.makeText(OkHttpTest.this, "网络请求失败！ ", Toast.LENGTH_LONG).show();
                    System.out.println("网络请求失败！ " + e.getLocalizedMessage());
                }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                json = response.body().string();

                new ContentAsyncTask().execute(jsonUrl,json);

                System.out.println("--------数据获取及重绘ListView完成--------");
                if (response.body() != null) {
                    response.body().close();
                }

            }
        });

    }

/**
 * 构造一个AsyncTask，传入String类型的URL，返回一个NewsBean对象，每一个对象就是
 * listview中的每一行数据，包括一个icon,title,content
 */
        class ContentAsyncTask extends AsyncTask<String, Void, List<ContentListItem>> {

            @Override
            protected List<ContentListItem> doInBackground(String... strings) {

                return getJsonData(strings[0],strings[1]);

            }

            @Override
                protected void onPostExecute(List<ContentListItem> result) {
                    super.onPostExecute(result);
                    // 访问网络并解析json成功后返回结果，即我们设置的List<NewsBean>

                    lists = result;
                    System.out.println(lists);

                    adapter = new MyBaseAdapter(MainActivity.this, lists, mListView);

                    adapter.notifyDataSetChanged();

                    mListView.setAdapter(adapter);

                    mListView.setOnCreateContextMenuListener(new AdapterView.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                        ListView listView = (ListView) view;
                        AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) contextMenuInfo;

                        contextMenu.add(Menu.NONE, MENU_DETAIL, 0, "查看景点详情");
                        contextMenu.add(Menu.NONE, MENU_SELECTOR, 0, "添加到行程中");
                        contextMenu.add(Menu.NONE, MENU_CANCLE, 0, "从行程中取消");
                        contextMenu.setGroupCheckable(0,true,true);
                        contextMenu.setHeaderTitle("请选择：");

                        ContentListItem contentListItem = (ContentListItem) listView.getItemAtPosition(adapterContextMenuInfo.position);
                        address = contentListItem.getSceneryAddress();
                        sceneryDetailUrl = contentListItem.getSceneryDetailUrl();
                        addressTitle = contentListItem.getSceneryTitle();
                        //super.onCreateContextMenu(menu, v, menuInfo);
                    }
                });

                   // handlerChangeUI.sendEmptyMessage(0);

                    mListView.hideFooterView();

                }

        }

    private Handler handlerChangeUI = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter.notifyDataSetChanged();
        }
    };

/*
    @Override
    public void onDownPullRefresh() {

    }*/


    /**
     * 上拉刷新动作
     */
    @Override
    public void onLoadingMore() {

        page++;
        if(page > 0 ){
            extendJsonUrl_Province.delete(0,extendJsonUrl_Province.length());
            extendJsonUrl_Province.replace(0,baseJsonUrl.length(), String.valueOf(new StringBuffer(baseJsonUrl).append("&pid=").append(String.valueOf(provinceID))));
            System.out.println(extendJsonUrl_Province);
        }
        extendJsonUrl_Province = extendJsonUrl_Province.append("&page=").append(String.valueOf(page));
        System.out.println(extendJsonUrl_Province);
        //new ContentAsyncTask().execute(extendJsonUrl.toString(),json);
        start(extendJsonUrl_Province.toString(),okHttpClient);

    }

}
