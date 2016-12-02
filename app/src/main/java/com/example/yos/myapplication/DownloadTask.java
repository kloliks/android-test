package com.example.yos.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.URLUtil;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class DownloadTask extends AsyncTask<String, Void, Void> {
    private Context mContext;
    private View mView;

    public DownloadTask(Context context, View view) {
        mContext = context;
        mView = view;
    }

    @Override
    protected Void doInBackground(String... sUrl) {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(sUrl[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.e("Error", "code ("+ connection.getResponseCode()
                        +"); message ("+ connection.getResponseMessage() +")");
                return null;
            }

            String output_file_name =
                    Environment.getExternalStorageDirectory().getPath()
                    +"/"+ URLUtil.guessFileName(sUrl[0], null, null);

            input = new BufferedInputStream(connection.getInputStream());
            output = new FileOutputStream(output_file_name);

            int count;
            byte data[] = new byte[4096];
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
        try {
            if (output != null) {
                output.flush();
                output.close();
            }
            if (input != null) {
                input.close();
            }
        } catch (IOException ignore) {
        }
        if (connection != null) {
            connection.disconnect();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        mView.setClickable(false);

        Animation rotation = AnimationUtils.loadAnimation(mContext, R.anim.rotation);
        rotation.setRepeatCount(Animation.INFINITE);

        mView.startAnimation(rotation);
    }

    @Override
    protected void onPostExecute(Void result) {
        mView.clearAnimation();
        mView.setClickable(true);
    }
}
