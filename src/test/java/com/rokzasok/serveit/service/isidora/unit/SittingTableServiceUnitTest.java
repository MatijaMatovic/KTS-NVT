package com.rokzasok.serveit.service.isidora.unit;

import com.rokzasok.serveit.converters.SittingTableToSittingTableDTO;
import com.rokzasok.serveit.model.SittingTable;
import com.rokzasok.serveit.repository.SittingTableRepository;
import com.rokzasok.serveit.service.impl.SittingTableService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.rokzasok.serveit.constants.TableConstants.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@PropertySource("classpath:application-test.properties")
public class SittingTableServiceUnitTest {

    @Autowired
    SittingTableService sittingTableService;

    @MockBean
    SittingTableRepository sittingTableRepository;

    @Autowired
    SittingTableToSittingTableDTO converter;

    SittingTable table1;
    SittingTable table2;
    SittingTable table3;
    SittingTable empty;

    @PostConstruct
    public void setup() {
        table1 = SittingTable.builder()
                .id(ID1)
                .name(NAME1)
                .x(X1)
                .y(Y1)
                .isDeleted(IS_DELETED1)
                .build();

        table2 = SittingTable.builder()
                .id(ID2)
                .name(NAME2)
                .x(X2)
                .y(Y2)
                .isDeleted(IS_DELETED2)
                .build();

        table3 = SittingTable.builder()
                .id(ID3)
                .name(NAME3)
                .x(X3)
                .y(Y3)
                .isDeleted(IS_DELETED3)
                .build();

        empty = SittingTable.builder()
                .id(NON_EXISTING_ID)
                .build();

        List<SittingTable> tables = new ArrayList<>();
        tables.add(table1);
        tables.add(table2);

        // testFindAll
        given(sittingTableRepository.findAll()).willReturn(tables);

        // testFindOne
        given(sittingTableRepository.findById(ID1)).willReturn(Optional.ofNullable(table1));
        // testFindOne
        given(sittingTableRepository.findById(ID2)).willReturn(Optional.ofNullable(table2));

        // testFindOne_NonExistingID
        Optional<SittingTable> sittingTableNull = Optional.empty(); //Da bi orElse mogao da vrati Null
        given(sittingTableRepository.findById(NON_EXISTING_ID)).willReturn(sittingTableNull);
        doNothing().when(sittingTableRepository).delete(table1);

        // testEdit
        given(sittingTableRepository.findById(ID2)).willReturn(Optional.ofNullable(table2));
        given(sittingTableRepository.save(any())).willReturn(table3);
    }

    @Test
    public void testFindAll() {
        List<SittingTable> found = sittingTableService.findAll();

        verify(sittingTableRepository, times(1)).findAll();
        assertEquals(2, found.size());
    }

    @Test
    public void testDelete() {
        Boolean deleted = sittingTableService.deleteOne(ID1);

        verify(sittingTableRepository, times(1)).findById(ID1);
        assertEquals(true, deleted);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDelete_NonExistingID() {
        Boolean deleted = sittingTableService.deleteOne(NON_EXISTING_ID);

        assertEquals(false, deleted);
    }

    @Test
    public void testEdit() {
        SittingTable editedSittingTable = sittingTableService.edit(table2.getId(), converter.convert(table2));

        verify(sittingTableRepository, times(1)).findById(ID2);
        assertNotEquals(editedSittingTable.getId(), table2.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testEdit_NonExistingID() {
        SittingTable editedSittingTable = sittingTableService.edit(empty.getId(), converter.convert(empty));

        verify(sittingTableRepository, times(1)).findById(ID2);
        assertEquals(editedSittingTable.getId(), table2.getId());
    }
    
}
