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

import com.parse.ParseObject;
import com.sawad.bitleague.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PointsFragment extends Fragment {
    TableLayout tableLayout;


    public PointsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        tableLayout= (TableLayout) inflater.inflate(R.layout.fragment_points, container, false);
        tableLayout.post(new Runnable() {
            @Override
            public void run() {
               addViews();
            }
        });
        return tableLayout;

    }

    private void addViews() {
        List<Points> list =new ArrayList<>();
        list.add(new Points(0));
        list.add(new Points(1));
        list.add(new Points(2));
        list.add(new Points(3));
        list.add(new Points(4));
        list.add(new Points(5));
        List<ParseObject> matchList= MyApp.matchList;
        //calc points
        for(ParseObject ob: matchList)
        {
            if (ob.getString("state").equalsIgnoreCase("FT") && ob.getBoolean("league")) {
                int gd= ob.getInt("team1goals") - ob.getInt("team2goals");
                list.get(ob.getInt("team1")).cards+=ob.getInt("card");
                if (ob.getInt("win") == 1) {
                    list.get(ob.getInt("team1")).won(gd);
                    list.get(ob.getInt("team2")).lost(-gd);
                }else if (ob.getInt("win") == 2) {
                    list.get(ob.getInt("team2")).won(-gd);
                    list.get(ob.getInt("team1")).lost(gd);
                }else if (ob.getInt("win") == 0){
                    list.get(ob.getInt("team1")).drew();
                    list.get(ob.getInt("team2")).drew();
                }


            }
        }
        //sort points
        Collections.sort(list, new Comparator<Points>() {
            @Override
            public int compare(Points points, Points t1) {
                if ((points.points-t1.points)!=0)
                    return t1.points-points.points;
                else if((t1.gd-points.gd)!=0)
                    return t1.gd-points.gd;
                else
                    return points.cards-t1.cards;
            }
        });
        LayoutInflater li= LayoutInflater.from(getActivity());
        for (Points t :list)
        {
            if(t.team==0)
                continue;
            TableRow tr= (TableRow) li.inflate(R.layout.points_layout,tableLayout,false);
            TextView tv = (TextView) tr.findViewById(R.id.team_name);
            tv.setText(Teams.teamName[t.team]);
            tv = (TextView) tr.findViewById(R.id.plyd);
            tv.setText(String.valueOf(t.played));
            tv = (TextView) tr.findViewById(R.id.won);
            tv.setText(String.valueOf(t.win));
            tv = (TextView) tr.findViewById(R.id.lost);
            tv.setText(String.valueOf(t.loss));
            tv = (TextView) tr.findViewById(R.id.draw);
            tv.setText(String.valueOf(t.draw));
            tv = (TextView) tr.findViewById(R.id.gd);
            tv.setText(String.valueOf(t.gd));
            tv = (TextView) tr.findViewById(R.id.pts);
            tv.setText(String.valueOf(t.points));
            tableLayout.addView(tr);

        }

    }
    class Points{
        public int points,gd,win,loss,draw,played,team;
        public int cards;

        public Points(int i){
            points=gd=win=loss=draw=0;
            team=i;
            cards=0;

        }
        public void won(int gdc){
            points+=3;
            win++;
            played++;
            gd+=gdc;
        }
        public void lost(int gdc){
            loss++;
            played++;
            gd+=gdc;
        }
        public void drew(){
            draw++;
            played++;
            points++;
        }


    }

}
