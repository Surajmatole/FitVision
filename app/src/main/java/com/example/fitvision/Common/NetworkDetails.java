package com.example.fitvision.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

//this java class is used to internet connect or not
public class NetworkDetails {
    
    public  static boolean isConnectedToInternet(Context context)
    {
        // (ConnectivityManager) is the typecast it is used to find connected network..
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager!=null)
        {
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null)
            {
               for (int i=0; i<networkInfo.length;i++)
               {
                   if (networkInfo[i].getState()==NetworkInfo.State.CONNECTED)
                   {
                       return true;
                   }
               }
            }
        }

        return false;
    }
}
