package ch.bbcag.backend.todolist.tag;

import ch.bbcag.backend.todolist.FailedValidationException;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TagService {
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    public Tag findById(Integer id) {
        return tagRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<Tag> findByName(String name) {
        return tagRepository.findByName(name);
    }

    public Tag insert(Tag tag) {
        return tagRepository.save(tag);
    }

    public Tag update(Tag changingTag, Integer id) {
        Tag existingTag = this.findById(id);
        mergeTags(existingTag, changingTag);
        return tagRepository.save(existingTag);
    }

    public void deleteById(Integer id) {
        findById(id); // throws exception if not found
        tagRepository.deleteById(id);
    }


    private void mergeTags(Tag existingTag, Tag changingTag) {
        Map<String, List<String>> error  = new HashMap<>();

        if (changingTag.getName() != null) {
            if (StringUtils.isNotBlank(changingTag.getName())) {
                existingTag.setName(changingTag.getName());
            } else {
                error.put("name", List.of("Tag name must not be empty"));
            }
        }

        if (!error.isEmpty()) {
            throw new FailedValidationException(error);
        }
    }
}
