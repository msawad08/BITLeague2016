package com.sawad.bitleague;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.AsyncListUtil;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class NewsActivity extends AppCompatActivity {
    ParseObject newsOb;
    ImageView iv;
    View progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.bringToFront();
        setSupportActionBar(toolbar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        newsOb = MyApp.selectedNews;
        progress = findViewById(R.id.progressBar1);
        TextView tv = (TextView) findViewById(R.id.title);
        tv.setText(newsOb.getString("title"));
        tv = (TextView) findViewById(R.id.text);
        tv.setText(newsOb.getString("text"));
        if(newsOb.getBoolean("image")) {
            iv = (ImageView) findViewById(R.id.image);
            iv.setImageBitmap(NewsFragment.getBitmapFromBytes(newsOb.getBytes("thumb")));
            setImage();
        }
    }

    private void setImage() {
        ParseQuery<ParseObject> query= new ParseQuery<ParseObject>("newsimage");
        query.getInBackground(newsOb.getString("imageid"), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(e==null){
                    ParseFile file =parseObject.getParseFile("image");
                    file.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] bytes, ParseException e) {
                            progress.setVisibility(View.GONE);
                            if(e==null)
                                iv.setImageBitmap(NewsFragment.getBitmapFromBytes(bytes));
                        }
                    });
                }
            }
        });
    }
}
