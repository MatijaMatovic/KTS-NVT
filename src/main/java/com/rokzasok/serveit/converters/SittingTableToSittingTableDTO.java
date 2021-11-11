package com.rokzasok.serveit.converters;

import com.rokzasok.serveit.dto.SittingTableDTO;
import com.rokzasok.serveit.model.SittingTable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Model -> DTO
 */
@Component
public class SittingTableToSittingTableDTO implements Converter<SittingTable, SittingTableDTO> {
    @Override
    public SittingTableDTO convert(SittingTable source) {
        return new SittingTableDTO(source.getId(), source.getName(), source.getX(), source.getY());
    }

    public List<SittingTableDTO> convert(List<SittingTable> source) {
        List<SittingTableDTO> result = new ArrayList<>();
        for (SittingTable table : source) {
            result.add(convert(table));
        }
        return result;
    }


}
