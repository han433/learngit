package com.asiainfo.boss.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A HotUser.
 */
@Entity
@Table(name = "hot_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HotUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hotid")
    private Long hotid;

    @Column(name = "userid")
    private Long userid;

    @ManyToOne
    private Hot hot;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHotid() {
        return hotid;
    }

    public HotUser hotid(Long hotid) {
        this.hotid = hotid;
        return this;
    }

    public void setHotid(Long hotid) {
        this.hotid = hotid;
    }

    public Long getUserid() {
        return userid;
    }

    public HotUser userid(Long userid) {
        this.userid = userid;
        return this;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Hot getHot() {
        return hot;
    }

    public HotUser hot(Hot hot) {
        this.hot = hot;
        return this;
    }

    public void setHot(Hot hot) {
        this.hot = hot;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HotUser hotUser = (HotUser) o;
        if (hotUser.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), hotUser.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HotUser{" +
            "id=" + getId() +
            ", hotid=" + getHotid() +
            ", userid=" + getUserid() +
            "}";
    }
}
