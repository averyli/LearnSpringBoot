package com.avery.recuritcloud.entity.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="AVERY_COMPANY")
public class Company {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    /**
     * 董事长
     */
    @OneToOne
    private User president;
    
    /**
     * 公司人数
     */
    @NotNull
    @Column(name="person_count")
    private Integer personCount;
    
    
    @Column(name = "address")
    private String address;
    
    /**
     * 公司名称
     */
    @NotNull
    @Column(name="name")
    private String name;
    
    @NotNull
    @Column(name="email")
    private String email;
    
    @NotNull
    @Column(name = "privilege")
    private Boolean privilege;
    
    @Column(name = "is_accept_push_talents")
    private boolean isAcceptPushTalents=Boolean.FALSE;
    
    @Column(name = "status")
    private Integer status;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getPresident() {
        return president;
    }
    
    public void setPresident(User president) {
        this.president = president;
    }
    
    public Integer getPersonCount() {
        return personCount;
    }
    
    public void setPersonCount(Integer personCount) {
        this.personCount = personCount;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public boolean isAcceptPushTalents() {
        return isAcceptPushTalents;
    }
    
    public void setAcceptPushTalents(boolean acceptPushTalents) {
        isAcceptPushTalents = acceptPushTalents;
    }
    
    public Boolean getPrivilege() {
        return privilege;
    }
    
    public void setPrivilege(Boolean privilege) {
        this.privilege = privilege;
    }
    
    public Integer isStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "Company{" +
            "id=" + id +
            ", president=" + president +
            ", personCount=" + personCount +
            ", address='" + address + '\'' +
            ", name='" + name + '\'' +
            ", email='" + email + '\'' +
            ", privilege=" + privilege +
            ", isAcceptPushTalents=" + isAcceptPushTalents +
            ", status=" + status +
            '}';
    }
}
