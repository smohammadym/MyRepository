package ch.bbcag.backend.todolist.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    @Query("SELECT i FROM Item i WHERE i.name LIKE CONCAT('%', :name, '%')")
    List<Item> findByName(String name);

    // Die Methode ist gleich wie die obere, aber Spring generiert die Query basierend auf dem Methodennamen
    List<Item> findByNameContains(String name);

    @Query("SELECT i FROM Item i " +
            "JOIN i.linkedTags t " +
            "WHERE t.name LIKE CONCAT('%', :tagName, '%')"
    )
    List<Item> findByTagName(String tagName);


    @Query("SELECT i FROM Item i " +
            "JOIN i.linkedTags t " +
            "WHERE i.name LIKE CONCAT('%', :name, '%') " +
            "AND t.name LIKE CONCAT('%', :tagName, '%') ")
    List<Item> findByNameAndTagName(String name, String tagName);
}
