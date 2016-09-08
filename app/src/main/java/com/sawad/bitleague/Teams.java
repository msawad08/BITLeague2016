package com.sawad.bitleague;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;


import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class Teams {
    public static final String[] teamName = {
            "TBD",
            "ALPHA SC",
            "CAVALIERS FC",
            "INVADERS FC",
            "LEGEND KILLERS FC",
            "ROYAL SHOOTERS"
    };
    public static final int[] teamLogo ={
            R.mipmap.ic_launcher,
            R.mipmap.logo1,
            R.mipmap.logo2,
            R.mipmap.logo3,
            R.mipmap.logo4,
            R.mipmap.logo5
    };

    public static final String[] players ={
            "Anas Bachi",
                    "Hisham Ibrahim",
                    "Shuhaib K P",
                    "Mohammed Rizwan",
                    "Irshad U M",
                    "Shahshoor Ali",
                    "Noufal",
                    "Fayaz",
                    "Ajmal Hamid",
                    "Mubeel C P",
                    "Azeem",
                    "Mohammed Jasim",
                    "Ahmed Mubeen",
                    "Azarudheen",

            "Nabeel P I",
            "Mohammed Irfan",
            "Akbar",
            "Suhail SAP",
            "Muhammed M K",
            "Shafeeque",
            "Ommer Mukthar",
            "Sabir",
            "Favaz k",
            "Rahees",
            "Abdul Basith",
            "Kousar",
            "Mohammed Sadiq",
            "Aadil Tansi",

            "Abdul Azeez Farhad",
                    "Illyas",
                    "Sarfraz",
                    "Ajmal",
                    "Shabir",
                    "Jaseem",
                    "Khais V P",
                    "Mohammad Shammas",
                    "Akhil",
                    "Ahmed Safad",
                    "Muneer",
                    "Mahmood Suhail",
                    "Mohammed Farhaz",
                    "Mohammed Musthafa",

            "Milton Disouza",
                    "Zainudeen",
                    "Ajilan",
                    "Munahin",
                    "Al Ameen",
                    "Shamil",
                    "Hamza",
                    "Zulkarnain",
                    "Shakir Hussain",
                    "Aamir Suhail",
                    "Hareesh",
                    "Hisham",
                    "Moideen Aliyul Rilah",
                    "Hafeez",

            "Mousin",
                    "Nawaz",
                    "Niyas",
                    "Abdul Hakeem",
                    "Shaloob",
                    "Aboobaker Azwan",
                    "Mohammed Irfan",
                    "Hassan Rizwan",
                    "Azeem C M",
                    "Waqar Younus",
                    "Yasar Arafath",
                    "Mihraz",
                    "Rahman Sharbaz",
                    "Rashik"

    };


    public static Drawable getTeamLogo(Context c, int i) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return c.getResources().getDrawable(teamLogo[i-1],c.getTheme());
        }else{
            return c.getResources().getDrawable(teamLogo[i-1]);
        }
    }


}