package com.mozia.VmusicBox.APISuggestion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseSuggestion {


        @SerializedName("duration")
        @Expose
        private Integer duration;
        @SerializedName("release_day")
        @Expose
        private Object releaseDay;
        @SerializedName("permalink_url")
        @Expose
        private String permalinkUrl;
        @SerializedName("reposts_count")
        @Expose
        private Integer repostsCount;
        @SerializedName("genre")
        @Expose
        private Object genre;
        @SerializedName("permalink")
        @Expose
        private String permalink;
        @SerializedName("purchase_url")
        @Expose
        private Object purchaseUrl;
        @SerializedName("release_month")
        @Expose
        private Object releaseMonth;
        @SerializedName("description")
        @Expose
        private Object description;
        @SerializedName("uri")
        @Expose
        private String uri;
        @SerializedName("label_name")
        @Expose
        private Object labelName;
        @SerializedName("tag_list")
        @Expose
        private String tagList;
        @SerializedName("release_year")
        @Expose
        private Object releaseYear;
        @SerializedName("track_count")
        @Expose
        private Integer trackCount;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("last_modified")
        @Expose
        private String lastModified;
        @SerializedName("license")
        @Expose
        private String license;
        @SerializedName("tracks")
        @Expose
        private List<Track> tracks = null;
        @SerializedName("playlist_type")
        @Expose
        private Object playlistType;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("downloadable")
        @Expose
        private Object downloadable;
        @SerializedName("sharing")
        @Expose
        private String sharing;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("release")
        @Expose
        private Object release;
        @SerializedName("likes_count")
        @Expose
        private Integer likesCount;
        @SerializedName("kind")
        @Expose
        private String kind;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("type")
        @Expose
        private Object type;
        @SerializedName("purchase_title")
        @Expose
        private Object purchaseTitle;
        @SerializedName("artwork_url")
        @Expose
        private Object artworkUrl;
        @SerializedName("ean")
        @Expose
        private Object ean;
        @SerializedName("streamable")
        @Expose
        private Boolean streamable;
        @SerializedName("user")
        @Expose
        private User_ user;
        @SerializedName("embeddable_by")
        @Expose
        private String embeddableBy;
        @SerializedName("label_id")
        @Expose
        private Object labelId;

        public Integer getDuration() {
            return duration;
        }

        public void setDuration(Integer duration) {
            this.duration = duration;
        }

        public Object getReleaseDay() {
            return releaseDay;
        }

        public void setReleaseDay(Object releaseDay) {
            this.releaseDay = releaseDay;
        }

        public String getPermalinkUrl() {
            return permalinkUrl;
        }

        public void setPermalinkUrl(String permalinkUrl) {
            this.permalinkUrl = permalinkUrl;
        }

        public Integer getRepostsCount() {
            return repostsCount;
        }

        public void setRepostsCount(Integer repostsCount) {
            this.repostsCount = repostsCount;
        }

        public Object getGenre() {
            return genre;
        }

        public void setGenre(Object genre) {
            this.genre = genre;
        }

        public String getPermalink() {
            return permalink;
        }

        public void setPermalink(String permalink) {
            this.permalink = permalink;
        }

        public Object getPurchaseUrl() {
            return purchaseUrl;
        }

        public void setPurchaseUrl(Object purchaseUrl) {
            this.purchaseUrl = purchaseUrl;
        }

        public Object getReleaseMonth() {
            return releaseMonth;
        }

        public void setReleaseMonth(Object releaseMonth) {
            this.releaseMonth = releaseMonth;
        }

        public Object getDescription() {
            return description;
        }

        public void setDescription(Object description) {
            this.description = description;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public Object getLabelName() {
            return labelName;
        }

        public void setLabelName(Object labelName) {
            this.labelName = labelName;
        }

        public String getTagList() {
            return tagList;
        }

        public void setTagList(String tagList) {
            this.tagList = tagList;
        }

        public Object getReleaseYear() {
            return releaseYear;
        }

        public void setReleaseYear(Object releaseYear) {
            this.releaseYear = releaseYear;
        }

        public Integer getTrackCount() {
            return trackCount;
        }

        public void setTrackCount(Integer trackCount) {
            this.trackCount = trackCount;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getLastModified() {
            return lastModified;
        }

        public void setLastModified(String lastModified) {
            this.lastModified = lastModified;
        }

        public String getLicense() {
            return license;
        }

        public void setLicense(String license) {
            this.license = license;
        }

        public List<Track> getTracks() {
            return tracks;
        }

        public void setTracks(List<Track> tracks) {
            this.tracks = tracks;
        }

        public Object getPlaylistType() {
            return playlistType;
        }

        public void setPlaylistType(Object playlistType) {
            this.playlistType = playlistType;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Object getDownloadable() {
            return downloadable;
        }

        public void setDownloadable(Object downloadable) {
            this.downloadable = downloadable;
        }

        public String getSharing() {
            return sharing;
        }

        public void setSharing(String sharing) {
            this.sharing = sharing;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public Object getRelease() {
            return release;
        }

        public void setRelease(Object release) {
            this.release = release;
        }

        public Integer getLikesCount() {
            return likesCount;
        }

        public void setLikesCount(Integer likesCount) {
            this.likesCount = likesCount;
        }

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Object getType() {
            return type;
        }

        public void setType(Object type) {
            this.type = type;
        }

        public Object getPurchaseTitle() {
            return purchaseTitle;
        }

        public void setPurchaseTitle(Object purchaseTitle) {
            this.purchaseTitle = purchaseTitle;
        }

        public Object getArtworkUrl() {
            return artworkUrl;
        }

        public void setArtworkUrl(Object artworkUrl) {
            this.artworkUrl = artworkUrl;
        }

        public Object getEan() {
            return ean;
        }

        public void setEan(Object ean) {
            this.ean = ean;
        }

        public Boolean getStreamable() {
            return streamable;
        }

        public void setStreamable(Boolean streamable) {
            this.streamable = streamable;
        }

        public User_ getUser() {
            return user;
        }

        public void setUser(User_ user) {
            this.user = user;
        }

        public String getEmbeddableBy() {
            return embeddableBy;
        }

        public void setEmbeddableBy(String embeddableBy) {
            this.embeddableBy = embeddableBy;
        }

        public Object getLabelId() {
            return labelId;
        }

        public void setLabelId(Object labelId) {
            this.labelId = labelId;
        }

    }

