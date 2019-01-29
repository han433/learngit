package com.asiainfo.boss.service.mapper;

import com.asiainfo.boss.domain.*;
import com.asiainfo.boss.service.dto.HotListUserDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity HotListUser and its DTO HotListUserDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface HotListUserMapper extends EntityMapper<HotListUserDTO, HotListUser> {

    

    

    default HotListUser fromId(Long id) {
        if (id == null) {
            return null;
        }
        HotListUser hotListUser = new HotListUser();
        hotListUser.setId(id);
        return hotListUser;
    }
}
