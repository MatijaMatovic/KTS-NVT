package com.rokzasok.serveit.service.isidora.unit;

import com.rokzasok.serveit.converters.SittingTableToSittingTableDTO;
import com.rokzasok.serveit.dto.SittingTableDTO;
import com.rokzasok.serveit.exceptions.SittingTableNotFoundException;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.rokzasok.serveit.constants.TableConstants.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
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


    @Test
    public void testFindOne_IdExisting_ShouldReturn_Table() {
        SittingTable table1 = SittingTable.builder()
                .id(ID1)
                .name(NAME1)
                .x(X1)
                .y(Y1)
                .isDeleted(IS_DELETED1)
                .build();

        when(sittingTableRepository.findById(ID1)).thenReturn(Optional.ofNullable(table1));

        SittingTable found = sittingTableService.findOne(ID1);

        assertNotNull(found);
        assertEquals(ID1, found.getId());
        assertEquals(NAME1, found.getName());
        assertEquals(X1, found.getX());
        assertEquals(Y1, found.getY());
    }

    @Test
    public void testFindOne_IdNotExisting_ShouldReturn_Null() {
        when(sittingTableRepository.findById(ID1)).thenReturn(Optional.empty());

        SittingTable found = sittingTableService.findOne(ID1);

        assertNull(found);
    }

    // TODO maybe unnecessary everywhere
    @Test
    public void testFindAll_ShouldReturn_List() {
        SittingTable table1 = SittingTable.builder()
                .id(ID1)
                .name(NAME1)
                .x(X1)
                .y(Y1)
                .isDeleted(IS_DELETED1)
                .build();

        SittingTable table2 = SittingTable.builder()
                .id(ID2)
                .name(NAME2)
                .x(X2)
                .y(Y2)
                .isDeleted(IS_DELETED2)
                .build();

        List<SittingTable> tables = new ArrayList<>();
        tables.add(table1);
        tables.add(table2);

        when(sittingTableRepository.findAll()).thenReturn(tables);

        List<SittingTable> found = sittingTableService.findAll();

        verify(sittingTableRepository, times(1)).findAll();
        assertEquals(2, found.size());
    }

    // TODO unnecessary, ima smisla samo kao integracioni test
    @Test
    public void testDelete_IdExisting_ShouldReturn_True() throws Exception {
        SittingTable table1 = SittingTable.builder()
                .id(ID1)
                .name(NAME1)
                .x(X1)
                .y(Y1)
                .isDeleted(IS_DELETED1)
                .build();

        when(sittingTableRepository.findById(ID1)).thenReturn(Optional.ofNullable(table1));

        Boolean deleted = sittingTableService.deleteOne(ID1);

        assertEquals(true, deleted);
    }

    @Test(expected = SittingTableNotFoundException.class)
    public void testDelete_IdNotExisting_ShouldThrow_TableNotFound() throws Exception {
        when(sittingTableRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        sittingTableService.deleteOne(NON_EXISTING_ID);
    }

    @Test
    public void testEdit_IdExisting_ShouldReturnChanged() throws Exception {
        SittingTable table2 = SittingTable.builder()
                .id(ID2)
                .name(NAME2)
                .x(X2)
                .y(Y2)
                .isDeleted(IS_DELETED2)
                .build();

        when(sittingTableRepository.findById(ID2)).thenReturn(Optional.ofNullable(table2));

        SittingTableDTO table2ChangedDTO = SittingTableDTO.builder()
                .id(ID2)
                .name(NAME2 + " changed")
                .x(X2)
                .y(Y2)
                .build();

        SittingTable table2Changed = SittingTable.builder()
                .id(ID2)
                .name(NAME2 + " changed")
                .x(X2)
                .y(Y2)
                .isDeleted(IS_DELETED2)
                .build();

        when(sittingTableRepository.save(any(SittingTable.class))).thenReturn(table2Changed);

        SittingTable editedSittingTable = sittingTableService.edit(ID2, table2ChangedDTO);

        verify(sittingTableRepository, times(1)).findById(ID2);
        assertEquals(table2.getId(), editedSittingTable.getId());
        assertEquals(table2Changed.getName(), editedSittingTable.getName());
    }

    @Test(expected = SittingTableNotFoundException.class)
    public void testEdit_IdNotExisting_ShouldThrow_TableNotFound() throws Exception {
        when(sittingTableRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        SittingTableDTO empty = SittingTableDTO.builder()
                .id(NON_EXISTING_ID)
                .build();

        sittingTableService.edit(NON_EXISTING_ID, empty);
    }

}
