package com.mozia.VmusicBox.dataMng;

import com.mozia.VmusicBox.constants.ICloudMusicPlayerConstants;
import com.mozia.VmusicBox.soundclound.ISoundCloundConstants;
import com.ypyproductions.utils.StringUtils;
import com.ypyproductions.webservice.DownloadUtils;

import org.json.JSONObject;

public class YPYNetUtils implements ISoundCloundConstants, ICloudMusicPlayerConstants {
    private static final String TAG = YPYNetUtils.class.getSimpleName();

    public static String getLinkStreamFromSoundClound(long id) {
        String manualUrl = String.format(ISoundCloundConstants.FORMAT_URL_SONG, new Object[]{String.valueOf(id), SOUNDCLOUND_CLIENT_ID});
        String dataServer = DownloadUtils.downloadString(manualUrl);
        //DBLog.m25d(TAG, "=========>dataServer=" + dataServer);
        if (StringUtils.isEmptyString(dataServer)) {
            return null;
        }
        try {
            return new JSONObject(dataServer).getString("http_mp3_128_url");
        } catch (Exception e) {
            e.printStackTrace();
            return manualUrl;
        }
    }
}
