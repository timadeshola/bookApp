package co.zonetechpark.booktest.booktest.core.installer;

import co.zonetechpark.booktest.booktest.jpa.entity.Book;
import co.zonetechpark.booktest.booktest.jpa.entity.Role;
import co.zonetechpark.booktest.booktest.jpa.entity.User;
import co.zonetechpark.booktest.booktest.jpa.repos.BookRepository;
import co.zonetechpark.booktest.booktest.jpa.repos.RoleRepository;
import co.zonetechpark.booktest.booktest.jpa.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class DefaultDataInstaller implements ApplicationListener<ContextRefreshedEvent> {

    private Boolean alreadySetup = Boolean.FALSE;

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private BookRepository bookRepository;

    @Autowired
    public DefaultDataInstaller(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.bookRepository = bookRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        final Role author = createRoleIfNotFound("AUTHOR");
        final Role reviewer = createRoleIfNotFound("REVIEWER");
        final Role publisher = createRoleIfNotFound("PUBLISHER");
        final Role editor = createRoleIfNotFound("EDITOR");

        final Set<Role> authorRole = new HashSet<>(Arrays.asList(author, reviewer, publisher));
        final Set<Role> reviewerRole = new HashSet<>(Arrays.asList(reviewer));
        final Set<Role> publisherRole = new HashSet<>(Collections.singletonList(publisher));
        final Set<Role> editorRole = new HashSet<>(Collections.singletonList(editor));

        User authorUser = createUserIfNotFound("author", "author@example.com", "Peter", "Verhas", "Password@123", "+2347012345675", authorRole);
        User reviewerUser = createUserIfNotFound("reviewer", "reviewer@example.com", "Jeff", "Friesen", "Password@123", "+2347012345676", reviewerRole);
        User publisherUser = createUserIfNotFound("publisher", "publisher@example.com", "Denim", "Pinto", "Password@123", "+2347012345677", publisherRole);
        User editorUser = createUserIfNotFound("editor", "editor@example.com", "Kunal", "Parikh", "Password@123", "+2347012345678", editorRole);

        createBookIfNotExist("Peter Verhas", "Java 9 Programming By Example");
        createBookIfNotExist("Ken Arnold, James Gosling, David Holmes", "Java Programming Language");
        createBookIfNotExist("Kathy Sierra, Bert Bates", "Head First Java");
        createBookIfNotExist("Bruce Eckel", "Thinking In Java");

        alreadySetup = Boolean.TRUE;
    }

    @Transactional
    protected Role createRoleIfNotFound(final String name) {
        Optional<Role> roleCheck = roleRepository.findRoleByName(name);
        if (roleCheck.isPresent()) {
            return roleCheck.get();
        }
        Role role = new Role();
        role.setName(name);
        role.setStatus(true);
        role = roleRepository.save(role);
        return role;
    }

    @Transactional
    protected User createUserIfNotFound(final String username, final String email, final String firstName, final String lastName, final String password, final String phoneNumber, final Set<Role> roles) {
        Optional<User> userOptional = userRepository.findUserByUsername(username);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        User user = new User();
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setStatus(true);
        user.setRoles(roles);
        user.setStatus(true);
        user = userRepository.save(user);
        return user;
    }

    @Transactional
    protected Book createBookIfNotExist(String author, String title) {
        Optional<Book> optionalBook = bookRepository.findByTitle(title);
        if(optionalBook.isPresent()) {
            return optionalBook.get();
        }
        Book book = new Book();
        book.setAuthor(author);
        book.setTitle(title);
        book.setStatus(true);
        bookRepository.save(book);
        return book;
    }
}
