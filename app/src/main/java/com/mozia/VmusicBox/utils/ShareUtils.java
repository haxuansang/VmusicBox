package com.mozia.VmusicBox.utils;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.mozia.VmusicBox.R;

import java.util.Arrays;
import java.util.Random;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by moziasoft on 2/13/17.
 */

public class ShareUtils {
    private static final String TAG = ShareUtils.class.getSimpleName();

    public interface OnCallbackShareListener {
        void cancel();

        void failed(String str);

        void success();
    }

    public static void shareFacebook(Uri linkImage, Activity activity, String title, String description, final OnCallbackShareListener onShareListener) {
        try {

            ShareDialog shareDialog = new ShareDialog(activity);
            shareDialog.registerCallback(CallbackManager.Factory.create(), new FacebookCallback<Sharer.Result>() {
                public void onSuccess(Sharer.Result result) {

                    if (onShareListener != null) {
                        onShareListener.success();
                    }
                }

                public void onCancel() {

                    if (onShareListener != null) {
                        onShareListener.cancel();
                    }
                }

                public void onError(FacebookException error) {

                    if (onShareListener != null) {
                        onShareListener.failed(error.getMessage());
                    }
                }
            });
            ShareLinkContent.Builder builder = new ShareLinkContent.Builder();
            builder.setContentTitle(title);
            builder.setContentDescription(activity.getString(R.string.app_name));
            //builder.setQuote(activity.getString(R.string.cast_ad_label) + "\n" + activity.getString(R.string.app_name));
            builder.setContentUrl(linkImage);
            builder.setImageUrl(linkImage);
            ShareContent shareLinkContent = builder.build();
            boolean canShow = shareDialog.canShow(shareLinkContent, ShareDialog.Mode.AUTOMATIC);

            if (canShow) {
                shareDialog.show(shareLinkContent, ShareDialog.Mode.AUTOMATIC);
            } else if (onShareListener != null) {
                onShareListener.failed("cant show");
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (onShareListener != null) {
                onShareListener.failed(e.getMessage());
            }
        }
    }

    public static boolean shareAppFacebook(String urlImage, String url, Activity activity, final OnCallbackShareListener onShareListener) {
        try {
            ShareDialog shareDialog = new ShareDialog(activity);
            shareDialog.registerCallback(CallbackManager.Factory.create(), new FacebookCallback<Sharer.Result>() {
                public void onSuccess(Sharer.Result result) {
                    if (onShareListener != null) {
                        onShareListener.success();
                    }
                }

                public void onCancel() {
                    if (onShareListener != null) {
                        onShareListener.cancel();
                    }
                }

                public void onError(FacebookException error) {
                    if (onShareListener != null) {
                        onShareListener.failed(error.getMessage());
                    }
                }
            });
            ShareLinkContent.Builder builder = new ShareLinkContent.Builder();
            builder.setContentUrl(Uri.parse(url));
            builder.setImageUrl(Uri.parse(urlImage));
            ShareContent shareLinkContent = builder.build();
            boolean canShow = shareDialog.canShow(shareLinkContent, ShareDialog.Mode.AUTOMATIC);

            if (canShow) {
                shareDialog.show(shareLinkContent, ShareDialog.Mode.AUTOMATIC);
                return  true;
            } else if (onShareListener != null) {
                onShareListener.failed("cant show");

            }
        } catch (Exception e) {
            e.printStackTrace();
            if (onShareListener != null) {
                onShareListener.failed(e.getMessage());

            }
        }
        return false;
    }
    public  static  String getMessageForFanpage()
    {
        Random rand = new Random();
        int checkRank = rand.nextInt(5);
        if(checkRank == 0)
            return "Wow Wow Wow bản nhạc  chất quá";
        else  if (checkRank == 1)
            return "LOL LOL LOL bản nhạc tuyệt ";
        else  if (checkRank == 2)
            return "Ha Ha Ha còn gì tuyệt vời ";
        else
          return "bản nhạc chất quá";

    }
    public static void postWallPage(String urlLink)
    {

        Bundle params = new Bundle();
        params.putString("name", "LuSo MP3 Online");
        params.putString("message",getMessageForFanpage());
        params.putString("link", urlLink);
        params.putString("display", "page");
        FacebookSdk.sdkInitialize(getApplicationContext(), new FacebookSdk.InitializeCallback() {
            @Override
            public void onInitialized() {
                if(AccessToken.getCurrentAccessToken() == null){
                    System.out.println("not logged in yet");
                } else {
                    System.out.println("Logged in");
                }
            }
        });
        AccessToken accessToken = new AccessToken("EAACEdEose0cBABTua5fvEQqNRf7v5XXWSlygylsdnNTxFvfLwEiYrV8VtUkqp5gJLhNRrqdfDejxZCCznAMHj2vFJhvgfZBnaUJsFvNMJcqwETwBAE0E6yn2b6BdaHfFXZA42uSLCFgc1gxQjFCuiL6OBZBBrmqizOOBqrfzXHcYHOCvvcUDTqKZBIIUg1CUZD", "377503705962708", "100006758161089", Arrays.asList("user_status","publish_actions","publish_pages","user_posts","user_managed_groups","pages_messaging"), null, null, null, null);
       if (accessToken == null)
       {
           Log.v("Fuck","111111111111111111");
       }
        new GraphRequest(
                accessToken,
                "1054397321338737/feed",
                params,
                HttpMethod.POST,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Toast.makeText(getApplicationContext(),"Posted on wall",Toast.LENGTH_SHORT).show();
                    }
                }
        ).executeAsync();
    }
}
