package com.avery.recuritcloud.entity.model;

import org.joda.time.DateTime;

public class PublisherModel {
    
    private String url;
    
    private DateTime createDate;
    
    public PublisherModel(String url, DateTime createDate) {
        this.url = url;
        this.createDate = createDate;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public DateTime getCreateDate() {
        return createDate;
    }
    
    public void setCreateDate(DateTime createDate) {
        this.createDate = createDate;
    }
}
