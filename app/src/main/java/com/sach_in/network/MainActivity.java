package com.sach_in.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sach_in.network.Model.DataItem;
import com.sach_in.network.Service.MyService;
import com.sach_in.network.Service.Network;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {
        private static final String url="http://560057.youcanlearnit.net/services/json/itemsfeed.php";
    private static final String XMLurl="http://560057.youcanlearnit.net/services/xml/itemsfeed.php";
        private boolean  networkOK;
        TextView textView;
        TextView textView5;
        TextView textView4;
        TextView textView3;
        TextView textView6;
        Button button;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            String message = intent.getStringExtra(MyService.My_PAYLOAD);
            textView = (TextView) findViewById(R.id.textView);
            textView5 = (TextView) findViewById(R.id.textView5);
            textView4 = (TextView) findViewById(R.id.textView4);
            textView3 = (TextView) findViewById(R.id.textView3);
//            textView.append(message);
            if(intent.hasExtra(MyService.My_PAYLOAD)){
                DataItem[] message = (DataItem[]) intent.getParcelableArrayExtra(MyService.My_PAYLOAD);
               System.out.println("789");
                for (DataItem item: message
                        ) {
                    textView.append(item.getImage() + "\n");
                    textView5.append(item.getItemName() + "\n");
                    textView3.append(item.getCategory()+ "\n");
                    textView4.append(item.getDescription()+"\n");
                }
            }else if(intent.hasExtra(MyService.My_EXCEPTION)){
                System.out.println("123");
                String response = intent.getData().toString();
                Toast.makeText(getApplicationContext(),response,LENGTH_SHORT).show();
            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView6= (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               runClickHandler();

            }
        });


    }
    public void runClickHandler(){
        Intent intent = new Intent(this, MyService.class);
        intent.setData(Uri.parse(XMLurl));
        startService(intent);




        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(receiver, new IntentFilter(MyService.My_MESSAGE));

        networkOK = Network.hasNetworkAccess(this);
        textView6.append("\n"+"Network Status:" +networkOK + "\n"+"\n");

    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(receiver);
    }
}
