package com.sawad.bitleague;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class NewsFragment extends Fragment {
    LinearLayout newsHolder;
    ProgressDialog pg;
    LayoutInflater li;



    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_fixtures, container, false);
        li = inflater;
        newsHolder = (LinearLayout) rootView.findViewById(R.id.holder);
        pg= new ProgressDialog(getActivity(),R.style.MyTheme);
        pg.setProgressStyle(android.R.style.Widget_ProgressBar_Large_Inverse);
        pg.show();
        addNewsOf();
        return rootView;
    }
    private void addNewsOf() {
        ParseQuery<ParseObject> query =new ParseQuery<ParseObject>("news");
        query.fromLocalDatastore();
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                pg.dismiss();
                if(e==null){
                    newsHolder.removeAllViews();
                    for (final ParseObject ob:list)
                    {
                        View v =li.inflate(R.layout.news_layout,newsHolder,false);
                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MyApp.selectedNews = ob;
                                startActivity(new Intent(getActivity(),NewsActivity.class));
                            }
                        });
                        TextView tv = (TextView) v.findViewById(R.id.title);
                        tv.setText(ob.getString("title"));
                        tv = (TextView) v.findViewById(R.id.text);
                        tv.setText(ob.getString("text"));
                        ImageView iv = (ImageView) v.findViewById(R.id.image);
                        iv.setImageBitmap(getBitmapFromBytes(ob.getBytes("thumb")));
                        newsHolder.addView(v);
                    }
                    addNews();
                }


            }
        });
    }

    private void addNews() {
        ParseQuery<ParseObject> query =new ParseQuery<ParseObject>("news");
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                pg.dismiss();
                if(e==null){
                    newsHolder.removeAllViews();
                    for (final ParseObject ob:list)
                    {
                        View v =li.inflate(R.layout.news_layout,newsHolder,false);
                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MyApp.selectedNews = ob;
                                startActivity(new Intent(getActivity(),NewsActivity.class));
                            }
                        });
                        TextView tv = (TextView) v.findViewById(R.id.title);
                        tv.setText(ob.getString("title"));
                        tv = (TextView) v.findViewById(R.id.text);
                        tv.setText(ob.getString("text"));
                        ImageView iv = (ImageView) v.findViewById(R.id.image);
                        iv.setImageBitmap(getBitmapFromBytes(ob.getBytes("thumb")));
                        newsHolder.addView(v);
                        ob.pinInBackground();
                    }
                }else
                {
                    Snackbar.make(newsHolder,"Error Connecting to Internet",Snackbar.LENGTH_LONG)
                            .setAction("Try Again", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    addNews();
                                }
                            })
                            .show();
                }


            }
        });
    }
    public static Bitmap getBitmapFromBytes(byte[] stringPicture) {
        Bitmap decodedByte = BitmapFactory.decodeByteArray(stringPicture, 0, stringPicture.length);
        return decodedByte;
    }



}