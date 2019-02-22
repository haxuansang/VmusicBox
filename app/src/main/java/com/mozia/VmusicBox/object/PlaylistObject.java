package com.mozia.VmusicBox.object;

import com.mozia.VmusicBox.DBAlertFragment;
import com.mozia.VmusicBox.dataMng.JsonParsingUtils;
import com.mozia.VmusicBox.soundclound.ISoundCloundConstants;
import com.mozia.VmusicBox.soundclound.object.TrackObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class PlaylistObject {
    private long id;
    private ArrayList<Long> listTrackIds;
    private ArrayList<TrackObject> listTrackObjects = new ArrayList();
    private String name;

    public PlaylistObject(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<TrackObject> getListTrackObjects() {
        return this.listTrackObjects;
    }

    public void setListTrackObjects(ArrayList<TrackObject> listTrackObjects) {
        this.listTrackObjects = listTrackObjects;
    }

    public ArrayList<Long> getListTrackIds() {
        return this.listTrackIds;
    }

    public void setListTrackIds(ArrayList<Long> listTrackIds) {
        this.listTrackIds = listTrackIds;
    }

    public void addTrackObject(TrackObject mTrackObject, boolean isAddId) {
        if (this.listTrackObjects != null && mTrackObject != null) {
            this.listTrackObjects.add(mTrackObject);
            if (isAddId) {
                this.listTrackIds.add(Long.valueOf(mTrackObject.getId()));
            }
        }
    }

    public void removeTrackObject(TrackObject mTrackObject) {
        if (this.listTrackObjects != null && mTrackObject != null) {
            Iterator<TrackObject> mIterator = this.listTrackObjects.iterator();
            while (mIterator.hasNext()) {
                if (((TrackObject) mIterator.next()).getId() == mTrackObject.getId()) {
                    mIterator.remove();
                    break;
                }
            }
            Iterator<Long> mTrackIdIterator = this.listTrackIds.iterator();
            while (mTrackIdIterator.hasNext()) {
                if (((Long) mTrackIdIterator.next()).longValue() == mTrackObject.getId()) {
                    mTrackIdIterator.remove();
                    return;
                }
            }
        }
    }

    public void removeTrackObject(long id) {
        if (this.listTrackObjects != null && this.listTrackObjects.size() > 0) {
            Iterator<TrackObject> mIterator = this.listTrackObjects.iterator();
            while (mIterator.hasNext()) {
                if (((TrackObject) mIterator.next()).getId() == id) {
                    mIterator.remove();
                    return;
                }
            }
        }
    }

    public TrackObject getTrackObject(long id) {
        if (this.listTrackObjects != null && this.listTrackObjects.size() > 0) {
            Iterator it = this.listTrackObjects.iterator();
            while (it.hasNext()) {
                TrackObject mTrackObject = (TrackObject) it.next();
                if (mTrackObject.getId() == id) {
                    return mTrackObject;
                }
            }
        }
        return null;
    }

    public boolean isSongAlreadyExited(long id) {
        if (this.listTrackIds != null && this.listTrackIds.size() > 0) {
            Iterator it = this.listTrackIds.iterator();
            while (it.hasNext()) {
                if (((Long) it.next()).longValue() == id) {
                    return true;
                }
            }
        }
        return false;
    }

    public JSONObject toJson() {
        try {
            JSONObject mJsonObject = new JSONObject();
            mJsonObject.put(DBAlertFragment.KEY_ID_DIALOG, this.id);
            mJsonObject.put(JsonParsingUtils.TAG_NAME, this.name);
            JSONArray mJsonArray = new JSONArray();
            if (this.listTrackObjects != null && this.listTrackObjects.size() > 0) {
                Iterator it = this.listTrackObjects.iterator();
                while (it.hasNext()) {
                    mJsonArray.put(((TrackObject) it.next()).getId());
                }
            }
            mJsonObject.put(ISoundCloundConstants.METHOD_TRACKS, mJsonArray);
            return mJsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
