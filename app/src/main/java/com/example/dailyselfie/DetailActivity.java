package com.example.dailyselfie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

public class DetailActivity extends Activity {
    /*
    private ImageView imageView;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent i = new Intent();
        imagePath = i.getStringExtra(Intent.EXTRA_TEXT);

        imageView = (ImageView) findViewById(R.id.imageView);

        ViewTreeObserver viewTreeObserver = imageView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ImageHelper.setImageFromFilePath(imagePath, imageView, imageView.getWidth(), imageView.getHeight());
                imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

     */
    private ImageView imageView;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        imagePath = intent.getStringExtra(Intent.EXTRA_TEXT);

        imageView = (ImageView) findViewById(R.id.imageView);

        ViewTreeObserver vto = imageView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ImageHelper.setImageFromFilePath(imagePath, imageView, imageView.getWidth(), imageView.getHeight());
                imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
