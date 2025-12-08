package ch.bbcag.backend.todolist.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemRequestDTO {
    @NotBlank(message = "Item Name should not be empty")
    @Size(min = 1, max = 256)
    private String name;
    private String description;
    @NotNull(message = "An item without person doesn't exist")
    private Integer personId;
    private List<Integer> tagIds = new ArrayList<>();
    private Boolean done;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemRequestDTO that = (ItemRequestDTO) o;
        return Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(personId, that.personId) && Objects.equals(tagIds, that.tagIds) && Objects.equals(done, that.done);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, personId, tagIds, done);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public List<Integer> getTagIds() {
        return tagIds;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }
}
