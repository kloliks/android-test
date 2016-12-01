package com.example.yos.myapplication;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);

        MenuItem refresh_item = menu.findItem(R.id.action_refresh);
        onRefresh(refresh_item.getActionView());
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_refresh) {
//            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            ImageView iv = (ImageView) inflater.inflate(R.layout.refresh_action_view, null);
//
//            Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotation);
//            rotation.setRepeatCount(Animation.INFINITE);
//            iv.startAnimation(rotation);
//
//            item.setActionView(iv);
//
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    public void onRefresh(View view) {
        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotation);
        rotation.setRepeatCount(Animation.INFINITE);

        DownloadTask downloadTask = new DownloadTask(this, view);
        downloadTask.execute(
                "https://slack-redir.net/link?url=http%3A%2F%2Fbulk.openweathermap.org%2Fsample%2Fcity.list.us.json.gz");

        view.startAnimation(rotation);
    }
}
