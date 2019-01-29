package com.asiainfo.boss.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Vc_agreement.
 */
@Entity
@Table(name = "vc_agreement")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Vc_agreement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sign_date")
    private LocalDate sign_date;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getSign_date() {
        return sign_date;
    }

    public Vc_agreement sign_date(LocalDate sign_date) {
        this.sign_date = sign_date;
        return this;
    }

    public void setSign_date(LocalDate sign_date) {
        this.sign_date = sign_date;
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
        Vc_agreement vc_agreement = (Vc_agreement) o;
        if (vc_agreement.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), vc_agreement.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Vc_agreement{" +
            "id=" + getId() +
            ", sign_date='" + getSign_date() + "'" +
            "}";
    }
}
