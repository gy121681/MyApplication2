package com.example.heimagirl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.squareup.okhttp.OkHttpClient;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {


    @BindView(R.id.list_view)
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //sendSyncRequest();

    }

    private void sendSyncRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
               OkHttpClient okHttpClient = new OkHttpClient();


            }
        }).start();


    }


}
