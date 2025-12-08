package ch.bbcag.backend.todolist.security;

import ch.bbcag.backend.todolist.person.Person;

public class AuthMapper {

    /**
     * Maps an AuthDTO object to a Person object.
     *
     * @param source the AuthDTO object to be mapped
     * @return the mapped Person object
     */
    public static Person fromRequestDTO(AuthRequestDTO source) {
        Person person = new Person();
        person.setUsername(source.getUsername());
        person.setPassword(source.getPassword());
        return person;
    }

    /**
     * Maps a Person object to an AuthDTO object.
     *
     * @param source the Person object to be mapped
     * @return the mapped AuthDTO object
     */
    public static AuthResponseDTO toResponseDTO(Person source) {
        return new AuthResponseDTO(source.getId(), source.getUsername());
    }
}
