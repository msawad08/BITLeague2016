package com.sawad.bitleague;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {
    TextView tv;

    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =inflater.inflate(R.layout.fragment_about, container, false);
//        pg= new ProgressDialog(getActivity());
//        pg.show();
//        ParseQuery<ParseObject> query =new ParseQuery<ParseObject>("news");
//        query.orderByDescending("createdAt");
//        query.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> list, ParseException e) {
//                pg.dismiss();
//                if(e==null)
//                    addViews(list);
//            }
//        });
        tv = (TextView) rootView.findViewById(R.id.text);
//        SntpClient time= new SntpClient();
//        if (time.requestTime("time-c.nist.gov",10000))
//            tv.setText(getDate(time.getNtpTime(),"dd/MM/yyyy hh:mm:ss.SSS"));
//        else
//            Toast.makeText(getActivity(),"fail",Toast.LENGTH_LONG).show();
        String[] strings={"nist1-lnk.binary.net","nist.netservicesgroup.com\t"};
        LongOperation tast =new LongOperation();
        tast.execute(strings);
        return rootView;
    }
    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
    private void addViews(List<ParseObject> list) {
    }
    private class LongOperation extends AsyncTask<String, Void, String> {
        ProgressDialog pg;
        SntpClient time;
        @Override
        protected String doInBackground(String... params) {
            for (String host:params) {
                if (time.requestTime(host,10000))
                    return "Executed";
            }
            return "not Exec";
        }

        @Override
        protected void onPostExecute(String result) {
            pg.dismiss();
            Toast.makeText(getActivity(),result,Toast.LENGTH_LONG).show();
            tv.setText(getDate(time.getNtpTime(),"dd/MM/yyyy hh:mm:ss.SSS"));
        }

        @Override
        protected void onPreExecute() {
            time=new SntpClient();
            pg = ProgressDialog.show(getActivity(), "Autenticando", "Contactando o servidor, por favor, aguarde alguns instantes.", true, false);
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

}