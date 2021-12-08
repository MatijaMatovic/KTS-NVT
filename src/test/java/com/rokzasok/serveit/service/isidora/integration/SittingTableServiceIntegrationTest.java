package com.rokzasok.serveit.service.isidora.integration;

import com.rokzasok.serveit.converters.SittingTableToSittingTableDTO;
import com.rokzasok.serveit.model.SittingTable;
import com.rokzasok.serveit.service.impl.SittingTableService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.rokzasok.serveit.constants.TableConstants.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class SittingTableServiceIntegrationTest {

    @Autowired
    private SittingTableService sittingTableService;
    @Autowired
    private SittingTableToSittingTableDTO converter;

    @Test
    public void testFindAll() {
        List<SittingTable> found = sittingTableService.findAll();
        assertEquals(FIND_ALL_NUMBER_OF_ITEMS, found.size());
    }

    @Test
    public void testFindById() {
        SittingTable found = sittingTableService.findOne(ID1);
        assertEquals(ID1, found.getId());
    }

    @Test
    public void testSave(){
        SittingTable sittingTable = SittingTable.builder().id(null).name("Table test").x(X3).y(Y3).isDeleted(false).build();
        List<SittingTable> oldAll = sittingTableService.findAll();
        Integer lastOldId = oldAll.get(oldAll.size() - 1).getId();
        SittingTable created = sittingTableService.save(sittingTable);

        assertNotEquals(lastOldId, created.getId());
    }

    @Test
    public void testDelete(){
        sittingTableService.deleteOne(ID_TO_DELETE);
        SittingTable sittingTable = sittingTableService.findOne(ID_TO_DELETE);
        assertNull(sittingTable);
    }

    @Test
    public void testEdit() {
        SittingTable toEdit = sittingTableService.findOne(ID_TO_EDIT);
        String beforeEditName = toEdit.getName();
        toEdit.setName(beforeEditName + " test");
        SittingTable edited = sittingTableService.edit(toEdit.getId(), converter.convert(toEdit));
        assertEquals(ID_TO_EDIT, edited.getId());
        assertNotEquals(beforeEditName, edited.getName());
    }

}
