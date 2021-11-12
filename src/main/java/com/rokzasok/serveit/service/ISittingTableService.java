package com.rokzasok.serveit.service;

import com.rokzasok.serveit.dto.SittingTableDTO;
import com.rokzasok.serveit.model.SittingTable;

public interface ISittingTableService extends IGenericService<SittingTable>{
    SittingTable edit(Integer id, SittingTableDTO sittingTableDTO);
}
