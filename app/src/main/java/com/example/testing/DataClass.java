package com.example.testing;

public class DataClass {
    private String imageURL, caption,location;
    public DataClass(){
    }
    public String getImageURL() {
        return imageURL;
    }
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
    public String getCaption() {
        return caption;
    }
    public String getLocation(){return location;}
    public void setCaption(String caption) {
        this.caption = caption;
    }
    public DataClass(String imageURL, String caption, String location) {
        this.imageURL = imageURL;
        this.caption = caption;
        this.location =location;
    }
}