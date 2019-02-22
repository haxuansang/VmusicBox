package com.mozia.VmusicBox.object;

import android.util.Log;

public class GenreObject {
    private String id;
    private String keyword;
    private String name;

    public GenreObject(String id, String name, String keyword) {
        this.id = id;
        Log.d("keyyyyyyyyyyy:111111",this.id);
        this.name = name;
        this.keyword = keyword;
        Log.d("key222222" ,keyword);
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
