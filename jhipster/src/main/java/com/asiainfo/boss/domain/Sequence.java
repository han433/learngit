package com.asiainfo.boss.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Sequence.
 */
@Entity
@Table(name = "sequence")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Sequence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "current_value")
    private String currentValue;

    @Column(name = "jhi_increment")
    private String increment;

    @Column(name = "max_value")
    private String maxValue;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Sequence name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public Sequence currentValue(String currentValue) {
        this.currentValue = currentValue;
        return this;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    public String getIncrement() {
        return increment;
    }

    public Sequence increment(String increment) {
        this.increment = increment;
        return this;
    }

    public void setIncrement(String increment) {
        this.increment = increment;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public Sequence maxValue(String maxValue) {
        this.maxValue = maxValue;
        return this;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
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
        Sequence sequence = (Sequence) o;
        if (sequence.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sequence.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Sequence{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", currentValue='" + getCurrentValue() + "'" +
            ", increment='" + getIncrement() + "'" +
            ", maxValue='" + getMaxValue() + "'" +
            "}";
    }
}
