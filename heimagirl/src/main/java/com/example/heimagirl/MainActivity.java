package com.example.heimagirl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.list_view)
    ListView mlistView;
    private Gson mGson = new Gson();
    private List<GsonBean.ResultsBean> mListData = new ArrayList<GsonBean.ResultsBean>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        sendSyncRequest();
        // sendAsyncRequest();

        mlistView.setAdapter(mBaseAdapter);



    }

    //同步请求
    private void sendAsyncRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                String url = "http://gank.io/api/data/%E7%A6%8F%E5%88%A9/10/1";
                Request request = new Request.Builder().get().url(url).build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    Log.d(TAG, "sendAsyncRequest: " + response);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();


    }

    //异步发送请求
    private void sendSyncRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                String url = "http://gank.io/api/data/%E7%A6%8F%E5%88%A9/10/1";
                Request request = new Request.Builder().get().url(url).build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {

                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        String result = response.body().string();
                        final GsonBean gsonBean = mGson.fromJson(result, GsonBean.class);


                        Log.d(TAG, "onResponse: " + gsonBean.getResults().get(0).getUrl());
                        //在主线程上面刷新Ui
                        mListData.addAll(gsonBean.getResults());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                mBaseAdapter.notifyDataSetChanged();
                                //将网络结果加入数据集合

                            }
                        });

                    }
                });
            }
        }).start();


    }

    public BaseAdapter mBaseAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(MainActivity.this, R.layout.list_view, null);
                holder = new ViewHolder();
                holder.mImageView = (ImageView) convertView.findViewById(R.id.image);
                holder.mTextView = (TextView) convertView.findViewById(R.id.publish_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
                GsonBean.ResultsBean resultsBean = mListData.get(position);
                holder.mTextView.setText(resultsBean.getPublishedAt());
                String url = resultsBean.getUrl();

                Glide.with(MainActivity.this).load(url).into(holder.mImageView);


            }

            return convertView;

        }


    };


    public static class ViewHolder {
        ImageView mImageView;
        TextView mTextView;
    }
}




