package com.asiainfo.boss.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Application_commission.
 */
@Entity
@Table(name = "application_commission")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Application_commission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "application_id")
    private Long application_id;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApplication_id() {
        return application_id;
    }

    public Application_commission application_id(Long application_id) {
        this.application_id = application_id;
        return this;
    }

    public void setApplication_id(Long application_id) {
        this.application_id = application_id;
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
        Application_commission application_commission = (Application_commission) o;
        if (application_commission.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), application_commission.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Application_commission{" +
            "id=" + getId() +
            ", application_id=" + getApplication_id() +
            "}";
    }
}
