package ch.bbcag.backend.todolist.controllers;

import ch.bbcag.backend.todolist.item.*;
import ch.bbcag.backend.todolist.person.Person;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ItemService itemService;


    @Test
    public void checkPost_whenValidNewItem_thenIsCreated() throws Exception {

        // 1. Mockito Mocking
        Item item = new Item();
        item.setId(1);
        item.setName("Item1");
        item.setPerson(new Person(2));
        Mockito.when(itemService.insert(any(Item.class))).thenReturn(item);

        // 2. Ausführung des Tests mit mockMvc
        mockMvc.perform(post(ItemController.PATH)
                        .contentType("application/json")
                        .content("""
                                {
                                    "name": "Item1",
                                    "personId": "2"
                                }
                        """))
                // 3. Überprüfung der Ergebnisse
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Item1")))
                .andExpect(jsonPath("$.personId", is(2)));
    }

    @Test
    public void checkPost_whenInvalidItem_thenIsConflict() throws Exception {

        // 1. Mockito Mocking
        Mockito.when(itemService.insert(any(Item.class))).thenThrow(DataIntegrityViolationException.class);

        // 2. Ausführung des Tests mit mockMvc
        mockMvc.perform(post(ItemController.PATH)
                        .contentType("application/json")
                        .content("""
                                {
                                    "wrongItemName": "ItemTestName"
                                }
                        """))
                // 3. Überprüfung der Ergebnisse
                .andExpect(status().isBadRequest());
    }

    @Test
    public void checkGet_whenFoundById_thenIsFound() throws Exception {
        Item item = new Item();
        Integer itemId = 1;
        ItemResponseDTO itemResponseDTO = new ItemResponseDTO();

        Mockito.when(itemService.findById(itemId)).thenReturn(item);

        try (MockedStatic<ItemMapper> itemMapper = Mockito.mockStatic(ItemMapper.class)) {
            itemMapper.when(() -> ItemMapper.toResponseDTO(item)).thenReturn(itemResponseDTO);

            mockMvc.perform(get(ItemController.PATH + "/" + itemId))
                    .andExpect(status().isOk());
        }
    }

    @Test
    public void checkGet_whenNotFoundById_thenIsNotFound() throws Exception {
        Integer itemId = 0;
        Mockito.when(itemService.findById(itemId))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));

        mockMvc.perform(get(ItemController.PATH + "/" + itemId))
                .andExpect(status().isNotFound());
    }

}
