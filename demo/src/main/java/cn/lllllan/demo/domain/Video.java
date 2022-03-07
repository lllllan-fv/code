package cn.lllllan.demo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Video implements Serializable {

    private int id;

    private String title;

    private String summary;

    private int price;

    private String coverImg;

    @JsonFormat(pattern = "yyyy-mm-dd hh:mm:ss", locale = "zh", timezone = "GNT+8")
    private Date date;

    private List<Chapter> chapterList;

    public Video() {
        this.date = new Date();
    }

    public Video(int id, String title) {
        this.id = id;
        this.title = title;
        this.date = new Date();
    }

    public Video(int id, String title, List<Chapter> chapterList) {
        this.id = id;
        this.title = title;
        this.chapterList = chapterList;
        this.date = new Date();
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
                ", date=" + date +
                ", chapterList=" + chapterList +
                '}';
    }
}
