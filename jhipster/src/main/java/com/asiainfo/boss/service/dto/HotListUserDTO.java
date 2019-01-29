package com.asiainfo.boss.service.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the HotListUser entity.
 */
public class HotListUserDTO implements Serializable {

    private Long id;

    private Long hotListId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHotListId() {
        return hotListId;
    }

    public void setHotListId(Long hotListId) {
        this.hotListId = hotListId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HotListUserDTO hotListUserDTO = (HotListUserDTO) o;
        if(hotListUserDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), hotListUserDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HotListUserDTO{" +
            "id=" + getId() +
            ", hotListId=" + getHotListId() +
            "}";
    }
}
