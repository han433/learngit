package com.asiainfo.boss.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.asiainfo.boss.domain.HotListUser;

import com.asiainfo.boss.repository.HotListUserRepository;
import com.asiainfo.boss.web.rest.errors.BadRequestAlertException;
import com.asiainfo.boss.web.rest.util.HeaderUtil;
import com.asiainfo.boss.service.dto.HotListUserDTO;
import com.asiainfo.boss.service.mapper.HotListUserMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing HotListUser.
 */
@RestController
@RequestMapping("/api")
public class HotListUserResource {

    private final Logger log = LoggerFactory.getLogger(HotListUserResource.class);

    private static final String ENTITY_NAME = "hotListUser";

    private final HotListUserRepository hotListUserRepository;

    private final HotListUserMapper hotListUserMapper;

    public HotListUserResource(HotListUserRepository hotListUserRepository, HotListUserMapper hotListUserMapper) {
        this.hotListUserRepository = hotListUserRepository;
        this.hotListUserMapper = hotListUserMapper;
    }

    /**
     * POST  /hot-list-users : Create a new hotListUser.
     *
     * @param hotListUserDTO the hotListUserDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new hotListUserDTO, or with status 400 (Bad Request) if the hotListUser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/hot-list-users")
    @Timed
    public ResponseEntity<HotListUserDTO> createHotListUser(@RequestBody HotListUserDTO hotListUserDTO) throws URISyntaxException {
        log.debug("REST request to save HotListUser : {}", hotListUserDTO);
        if (hotListUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new hotListUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HotListUser hotListUser = hotListUserMapper.toEntity(hotListUserDTO);
        hotListUser = hotListUserRepository.save(hotListUser);
        HotListUserDTO result = hotListUserMapper.toDto(hotListUser);
        return ResponseEntity.created(new URI("/api/hot-list-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /hot-list-users : Updates an existing hotListUser.
     *
     * @param hotListUserDTO the hotListUserDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated hotListUserDTO,
     * or with status 400 (Bad Request) if the hotListUserDTO is not valid,
     * or with status 500 (Internal Server Error) if the hotListUserDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/hot-list-users")
    @Timed
    public ResponseEntity<HotListUserDTO> updateHotListUser(@RequestBody HotListUserDTO hotListUserDTO) throws URISyntaxException {
        log.debug("REST request to update HotListUser : {}", hotListUserDTO);
        if (hotListUserDTO.getId() == null) {
            return createHotListUser(hotListUserDTO);
        }
        HotListUser hotListUser = hotListUserMapper.toEntity(hotListUserDTO);
        hotListUser = hotListUserRepository.save(hotListUser);
        HotListUserDTO result = hotListUserMapper.toDto(hotListUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, hotListUserDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hot-list-users : get all the hotListUsers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of hotListUsers in body
     */
    @GetMapping("/hot-list-users")
    @Timed
    public List<HotListUserDTO> getAllHotListUsers() {
        log.debug("REST request to get all HotListUsers");
        List<HotListUser> hotListUsers = hotListUserRepository.findAll();
        return hotListUserMapper.toDto(hotListUsers);
        }

    /**
     * GET  /hot-list-users/:id : get the "id" hotListUser.
     *
     * @param id the id of the hotListUserDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the hotListUserDTO, or with status 404 (Not Found)
     */
    @GetMapping("/hot-list-users/{id}")
    @Timed
    public ResponseEntity<HotListUserDTO> getHotListUser(@PathVariable Long id) {
        log.debug("REST request to get HotListUser : {}", id);
        HotListUser hotListUser = hotListUserRepository.findOne(id);
        HotListUserDTO hotListUserDTO = hotListUserMapper.toDto(hotListUser);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(hotListUserDTO));
    }

    /**
     * DELETE  /hot-list-users/:id : delete the "id" hotListUser.
     *
     * @param id the id of the hotListUserDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/hot-list-users/{id}")
    @Timed
    public ResponseEntity<Void> deleteHotListUser(@PathVariable Long id) {
        log.debug("REST request to delete HotListUser : {}", id);
        hotListUserRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
