package com.rokzasok.serveit.converters;

import com.rokzasok.serveit.dto.SittingTableDTO;
import com.rokzasok.serveit.model.SittingTable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO -> Model
 */
@Component
public class SittingTableDTOToSittingTable implements Converter<SittingTableDTO, SittingTable> {

    @Override
    public SittingTable convert(SittingTableDTO source) {
        return new SittingTable(source.getId(), source.getName(), source.getX(), source.getY(), false);
    }

    public List<SittingTable> convert(List<SittingTableDTO> source) {
        List<SittingTable> result = new ArrayList<>();
        for (SittingTableDTO tableDTO : source) {
            result.add(convert(tableDTO));
        }
        return result;
    }
}
