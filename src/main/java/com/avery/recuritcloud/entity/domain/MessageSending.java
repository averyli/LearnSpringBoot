package com.avery.recuritcloud.entity.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.scheduling.annotation.EnableScheduling;

@Entity
@Table(name = "AVERY_MESSAGE_SENDING")
public class MessageSending {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "send_to")
    private String sendTo;
    
    @Column(name = "copy_to")
    private String copyTo;
    
    @Column(name = "message")
    private String Message;
    
    @Column(name = "subject")
    private String subject;
    
    @Column(name = "record")
    private String record;
    
    @Column(name = "talent_info")
    private String talentInfo;
    
    @Column(name="status")
    private Integer status;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getSendTo() {
        return sendTo;
    }
    
    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }
    
    public String getCopyTo() {
        return copyTo;
    }
    
    public void setCopyTo(String copyTo) {
        this.copyTo = copyTo;
    }
    
    public String getMessage() {
        return Message;
    }
    
    public void setMessage(String message) {
        Message = message;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public String getRecord() {
        return record;
    }
    
    public void setRecord(String record) {
        this.record = record;
    }
    
    public String getTalentInfo() {
        return talentInfo;
    }
    
    public void setTalentInfo(String talentInfo) {
        this.talentInfo = talentInfo;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "MessageSending{" +
            "id=" + id +
            ", sendTo='" + sendTo + '\'' +
            ", copyTo='" + copyTo + '\'' +
            ", Message='" + Message + '\'' +
            ", subject='" + subject + '\'' +
            ", record='" + record + '\'' +
            ", talentInfo='" + talentInfo + '\'' +
            ", status=" + status +
            '}';
    }
}
