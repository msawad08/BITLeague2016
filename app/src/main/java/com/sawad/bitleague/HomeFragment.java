package com.sawad.bitleague;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.DateFormat;
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

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    ProgressDialog pg;
    LayoutInflater li;
    LinearLayout matchHolder,newsHolder,predictionHolder;
    ParseObject currentMatch=null,nextMatch=null,prevMatch=null;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        li=inflater;
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        matchHolder = (LinearLayout) rootView.findViewById(R.id.match_holder);
        newsHolder = (LinearLayout) rootView.findViewById(R.id.news_holder);
        predictionHolder = (LinearLayout) rootView.findViewById(R.id.prediction_holder);
        pg= new ProgressDialog(getActivity(),R.style.MyTheme);
        pg.setProgressStyle(android.R.style.Widget_ProgressBar_Large_Inverse);
        pg.show();

        ParseQuery<ParseObject> query =new ParseQuery<ParseObject>("game");
        query.fromLocalDatastore();
        query.orderByAscending("date");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if(e==null){
                    MyApp.matchList=list;
                    addViews();

                }
                addNewsOf();
                getOnline();


            }
        });





        return rootView;
    }

    private void getOnline() {
        ParseQuery<ParseObject> query =new ParseQuery<ParseObject>("game");
        query.orderByAscending("date");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                pg.dismiss();
                if(e==null){
                    MyApp.matchList=list;
                    for(ParseObject ob:list)
                        ob.pinInBackground();
                    addViews();

                }else
                {
                    Snackbar.make(matchHolder,"Error connecting to Internet",Snackbar.LENGTH_LONG)
                            .setAction("Try Again", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getOnline();
                                }
                            })
                            .show();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getOnline();
                    }
                }, 10000);


            }
        });
    }

    private void addViews() {
        List<ParseObject> matchList= MyApp.matchList;
        for (ParseObject match:matchList)
        {
            if(match.getString("state").equalsIgnoreCase("FT"))
                prevMatch=match;
            else if(match.getString("state").equalsIgnoreCase("NS")) {
                nextMatch = match;
                break;
            }else {
                currentMatch=match;
            }

        }
        matchHolder.removeAllViews();
        if(currentMatch!=null)
            addMatches("Current Match",currentMatch);
        if(prevMatch!=null)
            addMatches("Previous Match",prevMatch);
        if (nextMatch!=null)
            addMatches("Next Match",nextMatch);
        //pg.dismiss();

    }

    private void addNewsOf() {
        ParseQuery<ParseObject> query =new ParseQuery<ParseObject>("news");
        query.fromLocalDatastore();
        query.orderByDescending("createdAt");
        query.setLimit(3);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if(e==null){
                    newsHolder.removeAllViews();
                    for (final ParseObject ob:list)
                    {
                        View v =li.inflate(R.layout.news_layout,newsHolder,false);
                        TextView tv = (TextView) v.findViewById(R.id.title);
                        tv.setText(ob.getString("title"));
                        tv = (TextView) v.findViewById(R.id.text);
                        tv.setText(ob.getString("text"));
                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MyApp.selectedNews = ob;
                                startActivity(new Intent(getActivity(),NewsActivity.class));
                            }
                        });
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
        query.setLimit(3);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if(e==null){
                    newsHolder.removeAllViews();
                    for (final ParseObject ob:list)
                    {
                        View v =li.inflate(R.layout.news_layout,newsHolder,false);
                        TextView tv = (TextView) v.findViewById(R.id.title);
                        tv.setText(ob.getString("title"));
                        tv = (TextView) v.findViewById(R.id.text);
                        tv.setText(ob.getString("text"));
                        ImageView iv = (ImageView) v.findViewById(R.id.image);
                        iv.setImageBitmap(NewsFragment.getBitmapFromBytes(ob.getBytes("thumb")));
                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MyApp.selectedNews = ob;
                                startActivity(new Intent(getActivity(),NewsActivity.class));
                            }
                        });
                        newsHolder.addView(v);
                        ob.pinInBackground();
                    }

                }

            addPrediction();
            }
        });
    }

    private void addPrediction() {
        ParseQuery<ParseObject> query =new ParseQuery<ParseObject>("prediction");
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if(e==null){
                    predictionHolder.removeAllViews();
                    for (final ParseObject ob:list)
                    {
                        View v =li.inflate(R.layout.prediction_layout,predictionHolder,false);
                        TextView tv = (TextView) v.findViewById(R.id.title);
                        tv.setText(ob.getString("title"));
                        tv = (TextView) v.findViewById(R.id.question1);
                        tv.setText(ob.getString("question1"));
                        tv = (TextView) v.findViewById(R.id.question2);
                        tv.setText(ob.getString("question2"));
                        tv = (TextView) v.findViewById(R.id.question3);
                        tv.setText(ob.getString("question3"));

                        predictionHolder.addView(v);
                    }

                }

            }
        });
    }

    private void addMatches(String match, final ParseObject matchOb)
    {
            View view = li.inflate(R.layout.match_layout,matchHolder,false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyApp.selectedMatch = matchOb;
                    startActivity(new Intent(getActivity(),MatchActivity.class));
                }
            });
            TextView tv = (TextView) view.findViewById(R.id.match);
            tv.setText(match);
            int team1 = matchOb.getInt("team1");
            int team2 = matchOb.getInt("team2");
            tv = (TextView) view.findViewById(R.id.team1_name);
            tv.setText(Teams.teamName[team1]);
            tv = (TextView) view.findViewById(R.id.team2_name);
            tv.setText(Teams.teamName[team2]);
            ImageView iv = (ImageView) view.findViewById(R.id.team1_logo);
            iv.setImageResource(Teams.teamLogo[team1]);
            iv = (ImageView) view.findViewById(R.id.team2_logo);
            iv.setImageResource(Teams.teamLogo[team2]);
            if (matchOb.getString("state").equalsIgnoreCase("NS"))
            {
                tv = (TextView) view.findViewById(R.id.match_state);
                tv.setText(DateFormat.format("dd/MM/yy",matchOb.getDate("date")));
            }else
            {
                tv = (TextView) view.findViewById(R.id.match_state);
                tv.setText(matchOb.getString("state"));
                String goals = matchOb.getInt("team1goals") + ":" + matchOb.getInt("team2goals");
                tv = (TextView) view.findViewById(R.id.goals);
                tv.setText(goals);
                tv = (TextView) view.findViewById(R.id.penaltygoals);
                tv.setText(matchOb.getString("penaltygoals"));
            }
            matchHolder.addView(view);
    }


}
