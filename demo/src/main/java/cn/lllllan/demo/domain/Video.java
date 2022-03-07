package cn.lllllan.demo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Video implements Serializable {

    private int id;

    private String title;

    private String summary;

    private int price;

    @JsonProperty("cover_img")
    private String coverImg;

    @JsonProperty("creat_time")
    @JsonFormat(pattern = "yyyy-mm-dd hh:mm:ss", locale = "zh", timezone = "GNT+8")
    private Date createTime;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Chapter> chapterList;

    public Video() {
        this.createTime = new Date();
    }

    public Video(int id, String title) {
        this.id = id;
        this.title = title;
        this.createTime = new Date();
    }

    public Video(int id, String title, List<Chapter> chapterList) {
        this.id = id;
        this.title = title;
        this.chapterList = chapterList;
        this.createTime = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<Chapter> getChapterList() {
        return chapterList;
    }

    public void setChapterList(List<Chapter> chapterList) {
        this.chapterList = chapterList;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", price=" + price +
                ", coverImg='" + coverImg + '\'' +
                ", date=" + createTime +
                ", chapterList=" + chapterList +
                '}';
    }
}
