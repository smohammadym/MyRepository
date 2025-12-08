package ch.bbcag.backend.todolist.item;

import ch.bbcag.backend.todolist.FailedValidationException;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public Item findById(Integer id) {
        return itemRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<Item> findByName(String name) {
        return itemRepository.findByName(name);
    }

    public List<Item> findByNameAndTagName(String name, String tagName) {
        return itemRepository.findByNameAndTagName(name, tagName);
    }

    public List<Item> findByTagName(String tagName) {
        return itemRepository.findByTagName(tagName);
    }

    public Item insert(Item item) {
        return itemRepository.save(item);
    }

    public Item update(Item changingItem, Integer id) {
        Item existingItem = this.findById(id);
        mergeItems(existingItem, changingItem);
        return itemRepository.save(existingItem);
    }

    public void deleteById(Integer id) {
        findById(id); // throws exception if not found!
        itemRepository.deleteById(id);
    }


    private void mergeItems(Item existingItem, Item changingItem) {
        Map<String, List<String>> errors = new HashMap<>();

        if (changingItem.getName() != null) {
            if (StringUtils.isNotBlank(changingItem.getName())) {
                existingItem.setName(changingItem.getName());
            } else {
                errors.put("name", List.of("Item name must not be empty"));
            }
        }
        if (changingItem.getDescription() != null) {
            existingItem.setDescription(changingItem.getDescription());
        }
//        if (changingItem.getDone() != null) {
//            existingItem.setDone(changingItem.getDone());
//        }
        if (changingItem.getPerson() != null) {
            existingItem.setPerson(changingItem.getPerson());
        }
        if (changingItem.getLinkedTags() != null) {
            existingItem.setLinkedTags(changingItem.getLinkedTags());
        }

        if (!errors.isEmpty()) {
            throw new FailedValidationException(errors);
        }
    }
}
