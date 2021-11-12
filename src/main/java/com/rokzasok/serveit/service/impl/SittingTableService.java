package com.rokzasok.serveit.service.impl;

import com.rokzasok.serveit.dto.SittingTableDTO;
import com.rokzasok.serveit.model.SittingTable;
import com.rokzasok.serveit.repository.SittingTableRepository;
import com.rokzasok.serveit.service.ISittingTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SittingTableService implements ISittingTableService {
    @Autowired
    SittingTableRepository sittingTableRepository;

    @Override
    public List<SittingTable> findAll() {
        return sittingTableRepository.findAll()
                .stream()
                .filter(table -> !table.getIsDeleted())
                .collect(Collectors.toList());
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
    public Boolean deleteOne(Integer id) throws EntityNotFoundException {
        SittingTable toDelete = findOne(id);
        if (toDelete == null)
            throw new EntityNotFoundException("Table with given ID not found");
        sittingTableRepository.delete(toDelete);
        return true;
    }

    @Override
    public SittingTable edit(Integer id, SittingTableDTO sittingTableDTO) throws EntityNotFoundException {
        SittingTable toEdit = findOne(id);
        if (toEdit == null)
            throw new EntityNotFoundException("Table with given ID not found");
        toEdit.setName(sittingTableDTO.getName());
        toEdit.setX(sittingTableDTO.getX());
        toEdit.setY(sittingTableDTO.getY());
        return save(toEdit);
    }
}
