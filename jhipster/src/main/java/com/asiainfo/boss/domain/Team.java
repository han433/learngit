package com.asiainfo.boss.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Team.
 */
@Entity
@Table(name = "team")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Team implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "team_title")
    private String team_title;

    @Column(name = "user_id")
    private Integer user_id;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeam_title() {
        return team_title;
    }

    public Team team_title(String team_title) {
        this.team_title = team_title;
        return this;
    }

    public void setTeam_title(String team_title) {
        this.team_title = team_title;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public Team user_id(Integer user_id) {
        this.user_id = user_id;
        return this;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
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
        Team team = (Team) o;
        if (team.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), team.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Team{" +
            "id=" + getId() +
            ", team_title='" + getTeam_title() + "'" +
            ", user_id=" + getUser_id() +
            "}";
    }
}
