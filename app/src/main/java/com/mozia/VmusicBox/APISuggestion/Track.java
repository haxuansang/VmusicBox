package com.mozia.VmusicBox.APISuggestion;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Track {

    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("duration")
    @Expose
    private Integer duration;
    @SerializedName("commentable")
    @Expose
    private Boolean commentable;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("original_content_size")
    @Expose
    private Integer originalContentSize;
    @SerializedName("last_modified")
    @Expose
    private String lastModified;
    @SerializedName("sharing")
    @Expose
    private String sharing;
    @SerializedName("tag_list")
    @Expose
    private String tagList;
    @SerializedName("permalink")
    @Expose
    private String permalink;
    @SerializedName("streamable")
    @Expose
    private Boolean streamable;
    @SerializedName("embeddable_by")
    @Expose
    private String embeddableBy;
    @SerializedName("downloadable")
    @Expose
    private Boolean downloadable;
    @SerializedName("purchase_url")
    @Expose
    private Object purchaseUrl;
    @SerializedName("label_id")
    @Expose
    private Object labelId;
    @SerializedName("purchase_title")
    @Expose
    private Object purchaseTitle;
    @SerializedName("genre")
    @Expose
    private String genre;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("label_name")
    @Expose
    private String labelName;
    @SerializedName("release")
    @Expose
    private Object release;
    @SerializedName("track_type")
    @Expose
    private Object trackType;
    @SerializedName("key_signature")
    @Expose
    private Object keySignature;
    @SerializedName("isrc")
    @Expose
    private Object isrc;
    @SerializedName("video_url")
    @Expose
    private Object videoUrl;
    @SerializedName("bpm")
    @Expose
    private Object bpm;
    @SerializedName("release_year")
    @Expose
    private Object releaseYear;
    @SerializedName("release_month")
    @Expose
    private Object releaseMonth;
    @SerializedName("release_day")
    @Expose
    private Object releaseDay;
    @SerializedName("original_format")
    @Expose
    private String originalFormat;
    @SerializedName("license")
    @Expose
    private String license;
    @SerializedName("uri")
    @Expose
    private String uri;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("attachments_uri")
    @Expose
    private String attachmentsUri;
    @SerializedName("permalink_url")
    @Expose
    private String permalinkUrl;
    @SerializedName("artwork_url")
    @Expose
    private String artworkUrl;
    @SerializedName("waveform_url")
    @Expose
    private String waveformUrl;
    @SerializedName("stream_url")
    @Expose
    private String streamUrl;
    @SerializedName("playback_count")
    @Expose
    private Integer playbackCount;
    @SerializedName("download_count")
    @Expose
    private Integer downloadCount;
    @SerializedName("favoritings_count")
    @Expose
    private Integer favoritingsCount;
    @SerializedName("comment_count")
    @Expose
    private Integer commentCount;
    @SerializedName("likes_count")
    @Expose
    private Integer likesCount;
    @SerializedName("reposts_count")
    @Expose
    private Integer repostsCount;
    @SerializedName("policy")
    @Expose
    private String policy;
    @SerializedName("monetization_model")
    @Expose
    private String monetizationModel;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Boolean getCommentable() {
        return commentable;
    }

    public void setCommentable(Boolean commentable) {
        this.commentable = commentable;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getOriginalContentSize() {
        return originalContentSize;
    }

    public void setOriginalContentSize(Integer originalContentSize) {
        this.originalContentSize = originalContentSize;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getSharing() {
        return sharing;
    }

    public void setSharing(String sharing) {
        this.sharing = sharing;
    }

    public String getTagList() {
        return tagList;
    }

    public void setTagList(String tagList) {
        this.tagList = tagList;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public Boolean getStreamable() {
        return streamable;
    }

    public void setStreamable(Boolean streamable) {
        this.streamable = streamable;
    }

    public String getEmbeddableBy() {
        return embeddableBy;
    }

    public void setEmbeddableBy(String embeddableBy) {
        this.embeddableBy = embeddableBy;
    }

    public Boolean getDownloadable() {
        return downloadable;
    }

    public void setDownloadable(Boolean downloadable) {
        this.downloadable = downloadable;
    }

    public Object getPurchaseUrl() {
        return purchaseUrl;
    }

    public void setPurchaseUrl(Object purchaseUrl) {
        this.purchaseUrl = purchaseUrl;
    }

    public Object getLabelId() {
        return labelId;
    }

    public void setLabelId(Object labelId) {
        this.labelId = labelId;
    }

    public Object getPurchaseTitle() {
        return purchaseTitle;
    }

    public void setPurchaseTitle(Object purchaseTitle) {
        this.purchaseTitle = purchaseTitle;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public Object getRelease() {
        return release;
    }

    public void setRelease(Object release) {
        this.release = release;
    }

    public Object getTrackType() {
        return trackType;
    }

    public void setTrackType(Object trackType) {
        this.trackType = trackType;
    }

    public Object getKeySignature() {
        return keySignature;
    }

    public void setKeySignature(Object keySignature) {
        this.keySignature = keySignature;
    }

    public Object getIsrc() {
        return isrc;
    }

    public void setIsrc(Object isrc) {
        this.isrc = isrc;
    }

    public Object getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(Object videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Object getBpm() {
        return bpm;
    }

    public void setBpm(Object bpm) {
        this.bpm = bpm;
    }

    public Object getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Object releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Object getReleaseMonth() {
        return releaseMonth;
    }

    public void setReleaseMonth(Object releaseMonth) {
        this.releaseMonth = releaseMonth;
    }

    public Object getReleaseDay() {
        return releaseDay;
    }

    public void setReleaseDay(Object releaseDay) {
        this.releaseDay = releaseDay;
    }

    public String getOriginalFormat() {
        return originalFormat;
    }

    public void setOriginalFormat(String originalFormat) {
        this.originalFormat = originalFormat;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAttachmentsUri() {
        return attachmentsUri;
    }

    public void setAttachmentsUri(String attachmentsUri) {
        this.attachmentsUri = attachmentsUri;
    }

    public String getPermalinkUrl() {
        return permalinkUrl;
    }

    public void setPermalinkUrl(String permalinkUrl) {
        this.permalinkUrl = permalinkUrl;
    }

    public String getArtworkUrl() {
        return artworkUrl;
    }

    public void setArtworkUrl(String artworkUrl) {
        this.artworkUrl = artworkUrl;
    }

    public String getWaveformUrl() {
        return waveformUrl;
    }

    public void setWaveformUrl(String waveformUrl) {
        this.waveformUrl = waveformUrl;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public Integer getPlaybackCount() {
        return playbackCount;
    }

    public void setPlaybackCount(Integer playbackCount) {
        this.playbackCount = playbackCount;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    public Integer getFavoritingsCount() {
        return favoritingsCount;
    }

    public void setFavoritingsCount(Integer favoritingsCount) {
        this.favoritingsCount = favoritingsCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public Integer getRepostsCount() {
        return repostsCount;
    }

    public void setRepostsCount(Integer repostsCount) {
        this.repostsCount = repostsCount;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getMonetizationModel() {
        return monetizationModel;
    }

    public void setMonetizationModel(String monetizationModel) {
        this.monetizationModel = monetizationModel;
    }

}