package com.example.yos.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.URLUtil;
import android.widget.Toast;

import org.xml.sax.InputSource;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class DownloadTask extends AsyncTask<Void, Void, Void> {
    private Context mContext;
    private View mView;

    public DownloadTask(Context context, View view) {
        mContext = context;
        mView = view;
    }

    @Override
    protected Void doInBackground(Void... ignore) {
        try {
            String output_file_name =
                    Environment.getExternalStorageDirectory().getPath()
                            +"/"+ mContext.getResources().getString(R.string.city_file_name);

            File json = new File(output_file_name);
            if (json.exists()) {
                long lastTime = json.lastModified();
                Toast.makeText(mContext, "last time: "+lastTime, Toast.LENGTH_SHORT).show();
            }

            URL url = new URL(mContext.getResources().getString(R.string.url_gzip_city_archive));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.e("Error", "code ("+ connection.getResponseCode()
                        +"); message ("+ connection.getResponseMessage() +")");
                return null;
            }

            InputStream stream = new GZIPInputStream(connection.getInputStream());
            InputSource source = new InputSource(stream);
            InputStream input = new BufferedInputStream(source.getByteStream());
            OutputStream output = new FileOutputStream(output_file_name);

            int count;
            byte data[] = new byte[4096];
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }
            output.flush();

            output.close();
            input.close();
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
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
