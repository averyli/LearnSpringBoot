package com.avery.recuritcloud.common.enums;

public enum StatusEnum {
    
    VALID(10),
    INVALID(100);
    
    private Integer status;
    
    StatusEnum(Integer status){this.status = status;}
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
}
