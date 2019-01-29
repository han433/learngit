package com.asiainfo.boss.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A HotTalent.
 */
@Entity
@Table(name = "hot_talent")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HotTalent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hotid")
    private Long hotid;

    @Column(name = "talentid")
    private Long talentid;

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

    public HotTalent hotid(Long hotid) {
        this.hotid = hotid;
        return this;
    }

    public void setHotid(Long hotid) {
        this.hotid = hotid;
    }

    public Long getTalentid() {
        return talentid;
    }

    public HotTalent talentid(Long talentid) {
        this.talentid = talentid;
        return this;
    }

    public void setTalentid(Long talentid) {
        this.talentid = talentid;
    }

    public Hot getHot() {
        return hot;
    }

    public HotTalent hot(Hot hot) {
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
        HotTalent hotTalent = (HotTalent) o;
        if (hotTalent.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), hotTalent.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HotTalent{" +
            "id=" + getId() +
            ", hotid=" + getHotid() +
            ", talentid=" + getTalentid() +
            "}";
    }
}
