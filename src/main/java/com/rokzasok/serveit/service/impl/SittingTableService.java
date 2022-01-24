package com.rokzasok.serveit.service.impl;

import com.rokzasok.serveit.dto.SittingTableDTO;
import com.rokzasok.serveit.exceptions.SittingTableNotFoundException;
import com.rokzasok.serveit.model.SittingTable;
import com.rokzasok.serveit.repository.SittingTableRepository;
import com.rokzasok.serveit.service.ISittingTableService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SittingTableService implements ISittingTableService {
    private final SittingTableRepository sittingTableRepository;

    public SittingTableService(SittingTableRepository sittingTableRepository) {
        this.sittingTableRepository = sittingTableRepository;
    }

    @Override
    public List<SittingTable> findAll() {
        return sittingTableRepository.findAll();
    }

    @Override
    public SittingTable findOne(Integer id) {
        return sittingTableRepository.findById(id).orElse(null);
    }

    @Override
    public SittingTable save(SittingTable entity) {
        return sittingTableRepository.save(entity);
    }

    @Override
    public Boolean deleteOne(Integer id) throws Exception {
        SittingTable toDelete = sittingTableRepository.findById(id).orElseThrow(() -> new SittingTableNotFoundException("Table with provided ID does not exist"));
        sittingTableRepository.delete(toDelete);
        return true;
    }

    @Override
    public SittingTable edit(Integer id, SittingTableDTO sittingTableDTO) throws Exception {
        SittingTable toEdit = sittingTableRepository.findById(id).orElseThrow(() -> new SittingTableNotFoundException("Table with provided ID does not exist"));
        toEdit.setName(sittingTableDTO.getName());
        toEdit.setX(sittingTableDTO.getX());
        toEdit.setY(sittingTableDTO.getY());
        return save(toEdit);
    }
}
