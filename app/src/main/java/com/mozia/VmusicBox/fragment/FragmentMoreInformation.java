package com.mozia.VmusicBox.fragment;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mozia.VmusicBox.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMoreInformation extends Fragment implements View.OnClickListener {
    CardView cardViewAbout,cardViewReview,cardViewShare;
    ImageView imvCompany;

    public FragmentMoreInformation() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information,container,false);
        cardViewAbout=(CardView)view.findViewById(R.id.aboutcompany);
        cardViewReview=(CardView)view.findViewById(R.id.aboutsupportus);
        cardViewShare=(CardView)view.findViewById(R.id.share);
        imvCompany=(ImageView)view.findViewById(R.id.imv_company);
        cardViewAbout.setOnClickListener(this);
        cardViewReview.setOnClickListener(this);
        cardViewShare.setOnClickListener(this);
        Glide.with(getContext()).load(R.drawable.companypicture).into(imvCompany);
       return view;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.aboutcompany:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://moziasoft.com"));
                startActivity(browserIntent);
                break;

            case R.id.aboutsupportus:
                openAppRating(getContext());
                break;
            case R.id.share:
                Intent intent  = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareBody="https://play.google.com/store/apps/details?id=";
                String shareSub="Download MusicBox App:";
                intent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
                intent.putExtra(Intent.EXTRA_TEXT,shareBody);
                startActivity(Intent.createChooser(intent,"Share using"));

        }
    }
    public static void openAppRating(Context context) {
        // you can also use BuildConfig.APPLICATION_ID
        String appId = "com.globalapp.traceapp";
        Intent rateIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + appId));
        boolean marketFound = false;

        // find all applications able to handle our rateIntent
        final List<ResolveInfo> otherApps = context.getPackageManager()
                .queryIntentActivities(rateIntent, 0);
        for (ResolveInfo otherApp: otherApps) {
            // look for Google Play application
            if (otherApp.activityInfo.applicationInfo.packageName
                    .equals("com.android.vending")) {

                ActivityInfo otherAppActivity = otherApp.activityInfo;
                ComponentName componentName = new ComponentName(
                        otherAppActivity.applicationInfo.packageName,
                        otherAppActivity.name
                );
                // make sure it does NOT open in the stack of your activity
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // task reparenting if needed
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                // if the Google Play was already open in a search result
                //  this make sure it still go to the app page you requested
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // this make sure only the Google Play app is allowed to
                // intercept the intent
                rateIntent.setComponent(componentName);
                context.startActivity(rateIntent);
                marketFound = true;
                break;

            }
        }

        // if GP not present on device, open web browser
        if (!marketFound) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id="+appId));
            context.startActivity(webIntent);
        }
    }
}
