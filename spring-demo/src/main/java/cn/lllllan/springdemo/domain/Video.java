package cn.lllllan.springdemo.domain;

import java.util.Date;

public class Video {

    private int id;

    private String title;

    private String summary;

    private String coverImg;

    private int price;

    private Date createTime;

    private int cId;

    private double point;

    public Video() {
    }

    public Video(int id, String title, String summary, String coverImg, int price, Date createTime, int cId, double point) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.coverImg = coverImg;
        this.price = price;
        this.createTime = createTime;
        this.cId = cId;
        this.point = point;
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

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

    public double getPoint() {
        return point;
    }

    public void setPoint(double point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", coverImg='" + coverImg + '\'' +
                ", price=" + price +
                ", createTime=" + createTime +
                ", cId=" + cId +
                ", point=" + point +
                '}';
    }
}
