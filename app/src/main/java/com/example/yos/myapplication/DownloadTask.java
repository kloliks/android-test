package com.example.yos.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

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
            Log.e("sUrl", sUrl[0]);
            URL url = new URL(sUrl[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.e("Error: ", "code ("+ connection.getResponseCode()
                        +"); message ("+ connection.getResponseMessage() +")");
                if (connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
                    String new_sUrl = connection.getHeaderField("Location");
                    Log.e("new_sUrl", new_sUrl);

                    connection.disconnect();

                    url = new URL(new_sUrl);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                }
            }

            String output_file_name =
                    Environment.getExternalStorageDirectory().getPath()
                    + "/city.list.us.json.gz";

            int fileLength = connection.getContentLength();
            Log.e("BLA", "fileLength: "+ fileLength);

            input = connection.getInputStream();
            output = new FileOutputStream(output_file_name);

            int count;
            byte data[] = new byte[4096];
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
//            Toast.makeText(mContext, "Error: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
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
    protected void onPostExecute(Void result) {
        mView.clearAnimation();
    }
}
