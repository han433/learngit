package com.asiainfo.boss.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A UserCredit.
 */
@Entity
@Table(name = "user_credit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserCredit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "credit")
    private Integer credit;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public UserCredit userId(String userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getCredit() {
        return credit;
    }

    public UserCredit credit(Integer credit) {
        this.credit = credit;
        return this;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
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
        UserCredit userCredit = (UserCredit) o;
        if (userCredit.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userCredit.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserCredit{" +
            "id=" + getId() +
            ", userId='" + getUserId() + "'" +
            ", credit=" + getCredit() +
            "}";
    }
}
