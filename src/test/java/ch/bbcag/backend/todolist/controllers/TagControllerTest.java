package ch.bbcag.backend.todolist.controllers;

import ch.bbcag.backend.todolist.tag.*;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TagController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TagService tagService;

    @Test
    public void checkPost_whenValidNewTag_thenIsCreated() throws Exception {
        Tag tag = new Tag();
        tag.setId(1);
        tag.setName("Tag1");
        Mockito.when(tagService.insert(Mockito.any(Tag.class))).thenReturn(tag);

        mockMvc.perform(MockMvcRequestBuilders.post(TagController.PATH)
                .contentType("application/json")
                .content("""
                        {
                            "name": "Tag1"
                        }
                """))

        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("Tag1"))
        .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void checkPost_whenInvalidNewTag_thenIsNotFound() throws Exception {
        Mockito.when(tagService.insert(Mockito.any(Tag.class))).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(MockMvcRequestBuilders.post(TagController.PATH)
                .contentType("application/json")
                .content("""
                            {
                                "wrongTagName": "TagTestName"
                            }
                """))
        .andExpect(status().isBadRequest());
    }

    @Test
    public void checkGet_whenFoundById_thenIsFound() throws Exception {
        Tag tag = new Tag();
        Integer tagId = 1;
        TagResponseDTO tagResponseDTO = new TagResponseDTO();

        Mockito.when(tagService.findById(tagId)).thenReturn(tag);
        try (MockedStatic<TagMapper> tagMapper = Mockito.mockStatic(TagMapper.class)) {
            tagMapper.when(() -> TagMapper.toResponseDTO(tag)).thenReturn(tagResponseDTO);

            mockMvc.perform(get(TagController.PATH + "/" + tagId))
                    .andExpect(status().isOk());
        }
    }

    @Test
    public void checkGet_whenNotFoundById_thenNotFound() throws Exception {
        Integer tagId = 0;
        Mockito.when(tagService.findById(tagId))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));

        mockMvc.perform(get(TagController.PATH + "/" + tagId))
                .andExpect(status().isNotFound());
    }
}
