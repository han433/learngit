package com.asiainfo.boss.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A TrendReport.
 */
@Entity
@Table(name = "trend_report")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TrendReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company")
    private String company;

    @Column(name = "hire")
    private String hire;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public TrendReport company(String company) {
        this.company = company;
        return this;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getHire() {
        return hire;
    }

    public TrendReport hire(String hire) {
        this.hire = hire;
        return this;
    }

    public void setHire(String hire) {
        this.hire = hire;
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
        TrendReport trendReport = (TrendReport) o;
        if (trendReport.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), trendReport.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TrendReport{" +
            "id=" + getId() +
            ", company='" + getCompany() + "'" +
            ", hire='" + getHire() + "'" +
            "}";
    }
}
