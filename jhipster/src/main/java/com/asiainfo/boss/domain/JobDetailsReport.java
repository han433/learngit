package com.asiainfo.boss.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A JobDetailsReport.
 */
@Entity
@Table(name = "job_details_report")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class JobDetailsReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "batch_id")
    private String batchId;

    @OneToMany(mappedBy = "n")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<N> ns = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBatchId() {
        return batchId;
    }

    public JobDetailsReport batchId(String batchId) {
        this.batchId = batchId;
        return this;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public Set<N> getNs() {
        return ns;
    }

    public JobDetailsReport ns(Set<N> ns) {
        this.ns = ns;
        return this;
    }

    public JobDetailsReport addN(N n) {
        this.ns.add(n);
        n.setN(this);
        return this;
    }

    public JobDetailsReport removeN(N n) {
        this.ns.remove(n);
        n.setN(null);
        return this;
    }

    public void setNs(Set<N> ns) {
        this.ns = ns;
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
        JobDetailsReport jobDetailsReport = (JobDetailsReport) o;
        if (jobDetailsReport.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), jobDetailsReport.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "JobDetailsReport{" +
            "id=" + getId() +
            ", batchId='" + getBatchId() + "'" +
            "}";
    }
}
