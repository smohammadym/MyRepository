package ch.bbcag.backend.todolist.item;

import ch.bbcag.backend.todolist.person.Person;
import ch.bbcag.backend.todolist.tag.Tag;

import java.util.Set;
import java.util.stream.Collectors;

public class ItemMapper {
    public static ItemResponseDTO toResponseDTO(Item item) {
        ItemResponseDTO itemResponseDTO = new ItemResponseDTO();

        itemResponseDTO.setId(item.getId());
        itemResponseDTO.setName(item.getName());
        itemResponseDTO.setDescription(item.getDescription());
        itemResponseDTO.setCreatedAt(item.getCreatedAt());
//        itemResponseDTO.setDone(item.getDone());

        if (item.getPerson() != null) {
            itemResponseDTO.setPersonId(item.getPerson().getId());
        }

        if (item.getLinkedTags() != null) {
            for (Tag tag : item.getLinkedTags()) {
                itemResponseDTO.getTagIds().add(tag.getId());
            }
        }

        return itemResponseDTO;
    }

    public static Item fromRequestDTO(ItemRequestDTO itemRequestDTO) {
        Item item = new Item();

        item.setName(itemRequestDTO.getName());
        item.setDescription(itemRequestDTO.getDescription());
//        item.setDone(itemRequestDTO.getDone());

        if (itemRequestDTO.getPersonId() != null) {
            item.setPerson(new Person(itemRequestDTO.getPersonId()));
        }

        if (itemRequestDTO.getTagIds() != null) {
            Set<Tag> linkedTags = itemRequestDTO
                    .getTagIds()
                    .stream()
                    .map(ItemMapper::createNewTagWithId)
                    .collect(Collectors.toSet());

            item.setLinkedTags(linkedTags);
        }

        return item;
    }

    private static Tag createNewTagWithId(Integer id) {
        Tag tag = new Tag();
        tag.setId(id);
        return tag;
    }
}
