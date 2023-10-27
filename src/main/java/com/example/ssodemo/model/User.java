package com.example.ssodemo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Table(name = "user")
@EntityListeners(AuditingEntityListener.class)
public class User extends Account {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Transient
    private String image;


    @Transient
    private Boolean isManager = false;

    @Transient
    private Integer buId;

    @Transient
    private String idPath;

    @Transient
    private Date passwordExpireDate;

    /**
     *
     */
    public User() {
        super();
    }

    public User(User cus) {
        super(cus);
        this.setPhoneNumber( cus.getPhoneNumber());
        this.image = cus.getImage();

    }

    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Date getPasswordExpireDate() {
        return passwordExpireDate;
    }

    public void setPasswordExpireDate(Date passwordExpireDate) {
        this.passwordExpireDate = passwordExpireDate;
    }

    /**
     * @return the image
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getManager() {
        return isManager;
    }

    public void setManager(Boolean manager) {
        isManager = manager;
    }

    public Integer getBuId() {
        return buId;
    }

    public void setBuId(Integer buId) {
        this.buId = buId;
    }

    public String getIdPath() {
        return idPath;
    }

    public void setIdPath(String idPath) {
        this.idPath = idPath;
    }
}
