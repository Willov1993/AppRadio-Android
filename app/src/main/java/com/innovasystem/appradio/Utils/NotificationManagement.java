package com.innovasystem.appradio.Utils;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import com.innovasystem.appradio.R;

public class NotificationManagement {

    public static void notificarError(String titulo, String mensaje,Context context){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,"APPRADIO_CHANNEL");
        mBuilder.setSmallIcon(R.drawable.radio_icon_48);
        mBuilder.setContentTitle(titulo);
        mBuilder.setContentText(mensaje);
        NotificationManager manager=  (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        manager.notify(001,mBuilder.build());
    }
}
