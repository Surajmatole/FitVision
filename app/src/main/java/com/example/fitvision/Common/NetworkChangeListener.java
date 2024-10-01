package com.example.fitvision.Common;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.example.fitvision.R;

public class NetworkChangeListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (!NetworkDetails.isConnectedToInternet(context))
        {
            AlertDialog.Builder ad = new AlertDialog.Builder(context);
            View layout_dialog = LayoutInflater.from(context).inflate(R.layout.check_internet_connection_dialog,null);
            ad.setView(layout_dialog);

            AppCompatButton btRetry   =layout_dialog.findViewById(R.id.btretry);
            AlertDialog alertDialog= ad.create();
            alertDialog.show();
            alertDialog.setCanceledOnTouchOutside(false);

            btRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    onReceive(context,intent);
                }
            });
        }
        else
        {
            
        }

    }
}
//fundamentals
//1.Activity
// 2.services = long running operation in background..
//3. Brodcast Recevier = System communication and app Communicate
// 4.content Provider =Data Store , Data pass
