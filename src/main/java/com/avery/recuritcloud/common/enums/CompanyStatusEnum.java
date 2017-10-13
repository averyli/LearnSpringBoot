package com.avery.recuritcloud.common.enums;

public enum CompanyStatusEnum {
    CREATED(10),
    IN_POOL(20);
    
    private Integer status;
    
    CompanyStatusEnum(Integer status) {
        this.status = status;
    }
    
    public Integer getStatus() {
        return status;
    }
}
