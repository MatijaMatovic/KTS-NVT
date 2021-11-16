package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.converters.SittingTableDTOToSittingTable;
import com.rokzasok.serveit.converters.SittingTableToSittingTableDTO;
import com.rokzasok.serveit.dto.SittingTableDTO;
import com.rokzasok.serveit.model.SittingTable;
import com.rokzasok.serveit.service.ISittingTableService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("api/sitting-tables")
public class SittingTableController {
    private final ISittingTableService sittingTableService;

    private final SittingTableDTOToSittingTable sittingTableDTOToSittingTableConverter;
    private final SittingTableToSittingTableDTO sittingTableToSittingTableDTOConverter;

    public SittingTableController(ISittingTableService sittingTableService, SittingTableDTOToSittingTable sittingTableDTOToSittingTableConverter, SittingTableToSittingTableDTO sittingTableToSittingTableDTOConverter) {
        this.sittingTableService = sittingTableService;
        this.sittingTableDTOToSittingTableConverter = sittingTableDTOToSittingTableConverter;
        this.sittingTableToSittingTableDTOConverter = sittingTableToSittingTableDTOConverter;
    }

    // TODO RADI
    /***
     * Creates new table
     * author: isidora-stanic
     * authorized: ADMINISTRATOR
     * CREATE
     *
     * @param sittingTableDTO dto from frontend
     * @return true if successful, false otherwise
     */
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> create(@RequestBody SittingTableDTO sittingTableDTO) {
        SittingTable sittingTable = sittingTableDTOToSittingTableConverter.convert(sittingTableDTO);
        SittingTable sittingTableSaved = sittingTableService.save(sittingTable);

        if (sittingTableSaved == null) {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    // TODO RADI
    /***
     * Gets one table by id
     * author: isidora-stanic
     * authorized: ADMINISTRATOR, WAITER
     * READ (ONE)
     *
     * @param id id of table
     * @return sittingTableDTO if found, null otherwise
     */
    @GetMapping(value = "/one/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SittingTableDTO> one(@PathVariable Integer id) {
        SittingTable sittingTable = sittingTableService.findOne(id);

        SittingTableDTO sittingTableDTO = sittingTableToSittingTableDTOConverter.convert(sittingTable);
        if (sittingTableDTO == null) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return new ResponseEntity<>(sittingTableDTO, HttpStatus.OK);
    }

    // TODO RADI
    /***
     * Gets all tables
     * author: isidora-stanic
     * authorized: ADMINISTRATOR, WAITER
     * READ (ALL)
     *
     * @return list of sittingTableDTOs
     */
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SittingTableDTO>> all() {
        List<SittingTable> sittingTables = sittingTableService.findAll();

        List<SittingTableDTO> sittingTableDTOs = sittingTableToSittingTableDTOConverter.convert(sittingTables);
        return new ResponseEntity<>(sittingTableDTOs, HttpStatus.OK);
    }

    // TODO RADI
    /***
     * Edits one table
     * author: isidora-stanic
     * authorized: ADMINISTRATOR
     * UPDATE (EDIT)
     *
     * @return true if successful, false otherwise
     */
    @PutMapping(value = "/edit/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SittingTableDTO> edit(@PathVariable Integer id, @RequestBody SittingTableDTO sittingTableDTO) {
        SittingTable table;
        try {
            table = sittingTableService.edit(id, sittingTableDTO);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(sittingTableToSittingTableDTOConverter.convert(table), HttpStatus.OK);
    }

    // TODO RADI
    /***
     * Deletes one table
     * author: isidora-stanic
     * authorized: ADMINISTRATOR
     * DELETE
     *
     * @return true if successful, false otherwise
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        Boolean success;
        try {
            success = sittingTableService.deleteOne(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(success, HttpStatus.OK);
    }


}
