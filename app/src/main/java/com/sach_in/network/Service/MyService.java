package com.sach_in.network.Service;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;
import com.sach_in.network.Model.DataItem;
import com.sach_in.network.Parsers.MyXMLParser;

import java.io.IOException;

/**
 * Created by vtsony on 4/3/2017.
 */

public class MyService extends IntentService {
    public static final String My_MESSAGE="my message";
    public static final String My_PAYLOAD="my payload";
    public static final String My_EXCEPTION="my exception";

    public MyService() {
        super("MyService");
        //name=null;

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Uri uri = intent.getData();
        Log.i("TAG","ServiceTest");
        String response = null;
        try {
            response = HttpHelper.downloadUrl(uri.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Intent messageintent = new Intent(My_MESSAGE);
            messageintent.putExtra(My_EXCEPTION,e.getMessage());
            System.out.println("456");
            LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());
            manager.sendBroadcast(messageintent);
            return;
        }
//        Gson gson = new Gson();
//        DataItem[] dataItems = gson.fromJson(response,DataItem[].class);
        //System.out.println(response+"\n");
        DataItem[] dataItems = MyXMLParser.parseFeed(response);

//        for (DataItem item: dataItems
//             ) {

//            System.out.println(item);
        //}
        Intent messageintent = new Intent(My_MESSAGE);
        messageintent.putExtra(My_PAYLOAD, dataItems);

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(messageintent);
    }

}
