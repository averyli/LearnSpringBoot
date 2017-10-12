package com.avery.recuritcloud.common.enums;

public enum  TalentStatusEnum {
    CREATED(10),
    IN_POOL(20);
    
    private Integer status;
    
    TalentStatusEnum(Integer status) {
        this.status = status;
    }
    
    public Integer getStatus() {
        return status;
    }
}
