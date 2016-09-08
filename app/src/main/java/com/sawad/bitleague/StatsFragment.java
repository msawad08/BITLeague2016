package com.sawad.bitleague;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class StatsFragment extends Fragment {
    TableLayout tableLayout;

    public StatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_stats, container, false);
        LinearLayout holder = (LinearLayout) rootView.findViewById(R.id.holder);
        tableLayout = (TableLayout) inflater.inflate(R.layout.stats_title, holder, false);
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("player");
        query.orderByDescending("goals");
        query.setLimit(5);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if(e==null);
                    addRows(list);
            }
        });
        holder.addView(tableLayout);
        return rootView;
    }


    private void addRows(List<ParseObject> list) {
        for (ParseObject ob : list) {
            if (ob.getInt("goals")== 0)
                break;

                TableRow tr = (TableRow) LayoutInflater.from(getActivity()).inflate(R.layout.stats_layout, tableLayout, false);
                TextView tv = (TextView) tr.findViewById(R.id.name);
                tv.setText(Teams.players[ob.getInt("playerid")]);
                tv = (TextView) tr.findViewById(R.id.team_name);
                tv.setText(Teams.teamName[ob.getInt("teamid")]);
                tv = (TextView) tr.findViewById(R.id.goals);
                tv.setText(String.valueOf(ob.getInt("goals")));
                tableLayout.addView(tr);
        }
    }
}