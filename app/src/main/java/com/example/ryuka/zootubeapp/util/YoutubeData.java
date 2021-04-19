package com.example.ryuka.zootubeapp.util;

public class YoutubeData {
    private String Thumbnail,Title,Content,VideoID,ChannelID;

    //コンストラクタ
    public YoutubeData(String thumbnail, String title, String content, String videoid, String channelid) {
        this.Thumbnail = thumbnail;
        this.Title = title;
        this.Content = content;
        this.VideoID = videoid;
        this.ChannelID = channelid;
    }

    public String getThumbnail() {
        return Thumbnail;
    }
    public String getTitle() {
        return Title;
    }
    public String getContent() {
        return Content;
    }
    public String getVideoID() {
        return VideoID;
    }
    public String getChannelID() {
        return ChannelID;
    }
}
