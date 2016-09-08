package com.sawad.bitleague;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class MatchActivity extends AppCompatActivity {
    ParseObject matchOb;
    LinearLayout holder;
    String matchId;
    ProgressDialog pg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        matchOb = MyApp.selectedMatch;
        setTitle(matchOb.getString("match"));
        matchId = matchOb.getObjectId();
        if(true)
        {
            pg= new ProgressDialog(this,R.style.MyTheme);
            pg.setProgressStyle(android.R.style.Widget_ProgressBar_Large_Inverse);
            pg.show();
            holder = (LinearLayout) findViewById(R.id.holder);
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    showGoalsOf();
                }
            });
        }

        View view = findViewById(R.id.matchlay);
        int team1 = matchOb.getInt("team1");
        int team2 = matchOb.getInt("team2");
        TextView tv = (TextView) view.findViewById(R.id.team1_name);
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



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle(matchOb.getString("match"));
    }

    private void showGoalsOf() {
        final LayoutInflater li= LayoutInflater.from(this);
        ParseQuery<ParseObject> query =new ParseQuery<ParseObject>("goals");
        query.fromLocalDatastore();
        query.whereEqualTo("match",matchId);
        query.orderByAscending("time");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if(e==null){
                    //pg.dismiss();
                    holder.removeAllViews();
                    for (ParseObject ob:list)
                    {
                        if (ob.getBoolean("canceled"))
                            continue;
                        View v;
                        if(ob.getInt("team")==1)
                            v =li.inflate(R.layout.goal_team1,holder,false);
                        else
                            v =li.inflate(R.layout.goal_team2,holder,false);
                        TextView tv = (TextView) v.findViewById(R.id.name);
                        tv.setText(Teams.players[ob.getInt("player")]);
                        tv = (TextView) v.findViewById(R.id.time);
                        tv.setText(ob.getInt("time")+"'");
                        holder.addView(v);
                    }
                }

                showGoals();
            }
        });
    }

    private void showGoals() {
        final LayoutInflater li= LayoutInflater.from(this);
        ParseQuery<ParseObject> query =new ParseQuery<ParseObject>("goals");
        query.whereEqualTo("match",matchId);
        query.orderByAscending("time");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if(e==null){
                    pg.dismiss();
                    holder.removeAllViews();
                    for (ParseObject ob:list)
                    {
                        if (ob.getBoolean("canceled"))
                            continue;
                        View v;
                        if(ob.getInt("team")==1)
                            v =li.inflate(R.layout.goal_team1,holder,false);
                        else
                            v =li.inflate(R.layout.goal_team2,holder,false);
                        TextView tv = (TextView) v.findViewById(R.id.name);
                        tv.setText(Teams.players[ob.getInt("player")]);
                        tv = (TextView) v.findViewById(R.id.time);
                        tv.setText(ob.getInt("time")+"'");
                        holder.addView(v);
                    }
                }


            }
        });
    }

}
