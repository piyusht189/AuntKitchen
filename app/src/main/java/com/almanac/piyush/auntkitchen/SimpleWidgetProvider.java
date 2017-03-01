package com.almanac.piyush.auntkitchen;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * Created by 1415044 on 28-02-2017.
 */

public class SimpleWidgetProvider  extends AppWidgetProvider {
    RemoteViews remoteViews;
    Cursor res = null;
    String name, phone, email;
    int widgetId;
    DBHelper db;
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;

        for (int i = 0; i < count; i++) {
            widgetId = appWidgetIds[i];

            remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.simple_widget);

        }

        Intent intent = new Intent(context, SimpleWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);




        appWidgetManager.updateAppWidget(widgetId, remoteViews);

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.simple_widget);



    remoteViews.setOnClickPendingIntent(R.id.button5, getPendingSelfIntent(context,
                "ham"));

    }

    public PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, AboutDev.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }


}


