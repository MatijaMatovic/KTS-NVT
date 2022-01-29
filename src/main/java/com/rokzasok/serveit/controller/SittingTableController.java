package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.converters.SittingTableDTOToSittingTable;
import com.rokzasok.serveit.converters.SittingTableToSittingTableDTO;
import com.rokzasok.serveit.dto.SittingTableDTO;
import com.rokzasok.serveit.model.SittingTable;
import com.rokzasok.serveit.service.ISittingTableService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/sitting-tables")
public class SittingTableController {
    private final ISittingTableService sittingTableService;

    private final SittingTableDTOToSittingTable sittingTableDTOToSittingTable;
    private final SittingTableToSittingTableDTO sittingTableToSittingTableDTO;

    public SittingTableController(ISittingTableService sittingTableService, SittingTableDTOToSittingTable sittingTableDTOToSittingTableConverter, SittingTableToSittingTableDTO sittingTableToSittingTableDTOConverter) {
        this.sittingTableService = sittingTableService;
        this.sittingTableDTOToSittingTable = sittingTableDTOToSittingTableConverter;
        this.sittingTableToSittingTableDTO = sittingTableToSittingTableDTOConverter;
    }

    /***
     * Creates new table
     * author: isidora-stanic
     * authorized: ADMINISTRATOR
     * CREATE
     *
     * @param sittingTableDTO dto from frontend
     * @return true if successful, false otherwise
     */
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SittingTableDTO> create(@RequestBody SittingTableDTO sittingTableDTO) {
        SittingTable table = sittingTableDTOToSittingTable.convert(sittingTableDTO);
        boolean exists = false;

        if (sittingTableDTO.getId() != null) {
            exists = sittingTableService.findOne(sittingTableDTO.getId()) != null;
        }

        if (exists) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            table = sittingTableService.save(table);

            return new ResponseEntity<>(sittingTableToSittingTableDTO.convert(table), HttpStatus.OK);
        } catch (Exception e) {
            // if names are same between new and existing table
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    /***
     * Gets one table by id
     * author: isidora-stanic
     * authorized: ADMINISTRATOR, WAITER
     * READ (ONE)
     *
     * @param id id of table
     * @return sittingTableDTO if found, null otherwise
     */
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR') || hasRole('ROLE_WAITER')")
    @GetMapping(value = "/one/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SittingTableDTO> one(@PathVariable Integer id) {
        SittingTable sittingTable = sittingTableService.findOne(id);

        if (sittingTable == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(sittingTableToSittingTableDTO.convert(sittingTable), HttpStatus.OK);
    }

    /***
     * Gets all tables
     * author: isidora-stanic
     * authorized: ADMINISTRATOR, WAITER
     * READ (ALL)
     *
     * @return list of sittingTableDTOs
     */
    //@PreAuthorize("hasRole('ROLE_ADMINISTRATOR') || hasRole('ROLE_WAITER')")
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SittingTableDTO>> all() {
        List<SittingTable> sittingTables = sittingTableService.findAll();

        List<SittingTableDTO> sittingTableDTOs = sittingTableToSittingTableDTO.convert(sittingTables);

        return new ResponseEntity<>(sittingTableDTOs, HttpStatus.OK);
    }

    /***
     * Edits one table
     * author: isidora-stanic
     * authorized: ADMINISTRATOR
     * UPDATE (EDIT)
     *
     * @return true if successful, false otherwise
     */
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @PutMapping(value = "/edit/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SittingTableDTO> edit(@PathVariable Integer id, @RequestBody SittingTableDTO sittingTableDTO) throws Exception {
        SittingTable table;

        table = sittingTableService.edit(id, sittingTableDTO);

        return new ResponseEntity<>(sittingTableToSittingTableDTO.convert(table), HttpStatus.OK);
    }

    /***
     * Deletes one table
     * author: isidora-stanic
     * authorized: ADMINISTRATOR
     * DELETE
     *
     * @return true if successful, false otherwise
     */
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) throws Exception {
        Boolean success;

        success = sittingTableService.deleteOne(id);

        return new ResponseEntity<>(success, HttpStatus.OK);
    }

}
