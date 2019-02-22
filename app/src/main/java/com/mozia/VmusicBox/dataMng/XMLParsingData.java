package com.mozia.VmusicBox.dataMng;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class XMLParsingData {
    public static final String TAG = XMLParsingData.class.getSimpleName();
    public static final String TAG_COMPLETE_SUGGESTION = "CompleteSuggestion";
    public static final String TAG_SUGGESTION = "suggestion";
    public static final String TAG_TOP_LEVEL = "toplevel";

    public static ArrayList<String> parsingSuggestion(InputStream in) {
        if (in == null) {
            return null;
        }
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(in, "ISO-8859-1");
            ArrayList<String> listSuggestionStr = new ArrayList();
            for (int eventType = parser.getEventType(); eventType != 1; eventType = parser.next()) {
                switch (eventType) {
                    case 2:
                        if (parser.getName().equals(TAG_SUGGESTION)) {
                            listSuggestionStr.add(parser.getAttributeValue(0));
                            break;
                        }
                        break;
                }
            }
            if (in == null) {
                return listSuggestionStr;
            }
            try {
                in.close();
                return listSuggestionStr;
            } catch (IOException e) {
                e.printStackTrace();
                return listSuggestionStr;
            }
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        } catch (IOException e32) {
            e32.printStackTrace();
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e322) {
                    e322.printStackTrace();
                }
            }
        } catch (Throwable th) {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e3222) {
                    e3222.printStackTrace();
                }
            }
        }
        return null;
    }
}
