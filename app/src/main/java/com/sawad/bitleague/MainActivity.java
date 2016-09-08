package com.sawad.bitleague;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FragmentManager fm;
    ProgressDialog pg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.place_holder, new HomeFragment());
        ft.commit();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
       // navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void putPlayer(final int i) {
        pg.setProgress(i);
        ParseObject ob = new ParseObject("player");
        ob.put("playerid",i);
        ob.put("goals",0);
        if(i<14)
            ob.put("teamid",2);
        else if (i<28)
            ob.put("teamid",3);
        else if (i<42)
            ob.put("teamid",1);
        else if (i<56)
            ob.put("teamid",4);
        else if (i<70)
            ob.put("teamid",5);
        else{
            pg.dismiss();
            Toast.makeText(this,"done",Toast.LENGTH_LONG).show();
            return;
        }
        ob.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                putPlayer(i+1);
            }
        });


    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_home) {
            setTitle("BIT LEAGUE 2016");
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.place_holder, new HomeFragment());
            ft.commit();

        } else if (id == R.id.nav_fixtures) {
            setTitle("Fixtures & Results");
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.place_holder, new FixturesFragment());
            ft.commit();

        } else if (id == R.id.nav_news) {
            setTitle("NEWS");
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.place_holder, new NewsFragment());
            ft.commit();

        } else if (id == R.id.nav_stats) {
            setTitle("Stats");
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.place_holder, new StatsFragment());
            ft.commit();

        } else if (id == R.id.nav_points) {
            setTitle("Points Table");
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.place_holder, new PointsFragment());
            ft.commit();

        } else if (id == R.id.nav_team1) {
            Intent intent= new Intent(this,TeamActivity.class);
            intent.putExtra("teamid",1);
            startActivity(intent);

        } else if (id == R.id.nav_team2) {
            Intent intent= new Intent(this,TeamActivity.class);
            intent.putExtra("teamid",2);
            startActivity(intent);

        } else if (id == R.id.nav_team3) {
            Intent intent= new Intent(this,TeamActivity.class);
            intent.putExtra("teamid",3);
            startActivity(intent);

        } else if (id == R.id.nav_team4) {
            Intent intent= new Intent(this,TeamActivity.class);
            intent.putExtra("teamid",4);
            startActivity(intent);

        } else if (id == R.id.nav_team5) {
            Intent intent= new Intent(this,TeamActivity.class);
            intent.putExtra("teamid",5);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
