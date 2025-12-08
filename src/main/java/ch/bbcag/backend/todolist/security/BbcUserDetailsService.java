package ch.bbcag.backend.todolist.security;


import ch.bbcag.backend.todolist.person.Person;
import ch.bbcag.backend.todolist.person.PersonRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Collections.emptyList;

/**
 * Is used by AuthenticationManager for authenticate method
 */
@Service
public class BbcUserDetailsService implements UserDetailsService {
    private final PersonRepository personRepository;

    public BbcUserDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> optPerson = personRepository.findByUsername(username);

        if (optPerson.isPresent()) {
            Person person = optPerson.get();
            return new User(person.getUsername(), person.getPassword(), emptyList());
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

}
