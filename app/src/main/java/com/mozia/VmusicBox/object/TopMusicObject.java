package com.mozia.VmusicBox.object;

public class TopMusicObject {
    private String artist;
    private String artwork;
    private String name;

    public TopMusicObject(){}
    public TopMusicObject(String name, String artist, String artwork) {
        this.name = name;
        this.artist = artist;
        this.artwork = artwork;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return this.artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtwork() {
        return this.artwork;
    }

    public void setArtwork(String artwork) {
        this.artwork = artwork;
    }
}
