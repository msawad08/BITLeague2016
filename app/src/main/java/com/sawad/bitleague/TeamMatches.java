package com.sawad.bitleague;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.List;

/**
 *  FRAGMENT to display fixture in team activity
 */
public class TeamMatches extends Fragment{
    LinearLayout holder;
    public TeamMatches(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fixtures, container, false);
        holder = (LinearLayout) rootView.findViewById(R.id.holder);
        holder.post(new Runnable() {
            @Override
            public void run() {
                addViews();
            }
        });

        return rootView;
    }

    private void addViews() {
        List<ParseObject> matchList= MyApp.matchList;
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        for (final ParseObject matchOb:matchList)
        {
            if (matchOb.getInt("team1")==TeamActivity.teamId || matchOb.getInt("team2")==TeamActivity.teamId) {
                View view = inflater.inflate(R.layout.match_layout, holder, false);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Show Match Activity
                        MyApp.selectedMatch = matchOb;
                        startActivity(new Intent(getActivity(),MatchActivity.class));
                    }
                });
                TextView tv = (TextView) view.findViewById(R.id.match);
                tv.setText(matchOb.getString("match"));
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
                if (matchOb.getString("state").equalsIgnoreCase("NS")) {
                    tv = (TextView) view.findViewById(R.id.match_state);
                    tv.setText(DateFormat.format("dd/MM/yy", matchOb.getDate("date")));
                } else {
                    tv = (TextView) view.findViewById(R.id.match_state);
                    tv.setText(matchOb.getString("state"));
                    String goals = matchOb.getInt("team1goals") + ":" + matchOb.getInt("team2goals");
                    tv = (TextView) view.findViewById(R.id.goals);
                    tv.setText(goals);
                }
                holder.addView(view);
            }
        }
    }
}
