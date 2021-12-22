package com.rokzasok.serveit.service.isidora.integration;

import com.rokzasok.serveit.converters.SittingTableToSittingTableDTO;
import com.rokzasok.serveit.exceptions.SittingTableNotFoundException;
import com.rokzasok.serveit.model.SittingTable;
import com.rokzasok.serveit.repository.SittingTableRepository;
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
    private SittingTableRepository sittingTableRepository;
    @Autowired
    private SittingTableToSittingTableDTO converter;

    @Test
    public void testFindAll_ShouldReturn_List() {
        List<SittingTable> found = sittingTableService.findAll();
        assertEquals(sittingTableRepository.findAll().size(), found.size());
    }

    @Test
    public void testFindOne_IdExisting_ShouldReturn_Table() {
        SittingTable found = sittingTableService.findOne(3);
        assertEquals(3, (int) found.getId());
        assertEquals("Sto 3", found.getName());
        assertEquals(1, (int) found.getX());
        assertEquals(3, (int) found.getY());
        assertFalse(found.getIsDeleted());
    }

    @Test
    public void testFindOne_IdNotExisting_ShouldReturn_Null() {
        SittingTable found = sittingTableService.findOne(NON_EXISTING_ID);
        assertNull(found);
    }

    // TODO unnecessary
    @Test
    public void testSave(){
        SittingTable sittingTable = SittingTable.builder().id(null).name("Table test").x(X3).y(Y3).isDeleted(false).build();
        List<SittingTable> oldAll = sittingTableService.findAll();
        Integer lastOldId = oldAll.get(oldAll.size() - 1).getId();
        SittingTable created = sittingTableService.save(sittingTable);

        assertNotEquals(lastOldId, created.getId());
    }

    @Test
    public void testDelete_IdExisting_ShouldReturn_True() throws Exception {
        Boolean success = sittingTableService.deleteOne(ID_TO_DELETE);
        SittingTable sittingTable = sittingTableService.findOne(ID_TO_DELETE);
        assertNull(sittingTable);
        assertTrue(success);
    }

    @Test(expected = SittingTableNotFoundException.class)
    public void testDelete_IdNotExisting_ShouldThrow_TableNotFound() throws Exception {
        sittingTableService.deleteOne(NON_EXISTING_ID);
    }

    @Test
    public void testEdit_IdExisting_ShouldReturnChanged() throws Exception {
        SittingTable toEdit = sittingTableService.findOne(ID_TO_EDIT);
        String beforeEditName = toEdit.getName();
        toEdit.setName(beforeEditName + " test");
        SittingTable edited = sittingTableService.edit(toEdit.getId(), converter.convert(toEdit));
        assertEquals(ID_TO_EDIT, edited.getId());
        assertNotEquals(beforeEditName, edited.getName());
        assertEquals(beforeEditName + " test", edited.getName());
    }

    @Test(expected = SittingTableNotFoundException.class)
    public void testEdit_IdNotExisting_ShouldThrow_TableNotFound() throws Exception {
        SittingTable toEdit = sittingTableService.findOne(ID_TO_EDIT);
        sittingTableService.edit(NON_EXISTING_ID, converter.convert(toEdit));
    }

}