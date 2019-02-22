package com.mozia.VmusicBox.object;

public class SongOfAlbum {
    String image;
    String tittle;
    String link;
    String album;

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public SongOfAlbum(String image, String tittle, String link, String album) {
        this.image = image;
        this.tittle = tittle;
        this.link = link;
        this.album = album;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
