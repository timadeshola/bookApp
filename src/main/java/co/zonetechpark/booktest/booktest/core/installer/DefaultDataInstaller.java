package co.zonetechpark.booktest.booktest.core.installer;

import co.zonetechpark.booktest.booktest.jpa.entity.Role;
import co.zonetechpark.booktest.booktest.jpa.entity.User;
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

    @Autowired
    public DefaultDataInstaller(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
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

        final Set<Role> authorRole = new HashSet<>(Arrays.asList(author, reviewer, publisher));
        final Set<Role> reviewerRole = new HashSet<>(Arrays.asList(reviewer));
        final Set<Role> publisherRole = new HashSet<>(Collections.singletonList(publisher));

        User authorUser = createUserIfNotFound("author", "timadeshola@yahoo.com", "John", "Adeshola", "password", "07030239912", authorRole);
        User reviewerUser = createUserIfNotFound("reviewer", "jtestermails@gmail.com", "John", "Doe", "password", "07030239942", reviewerRole);
        User publisheruser = createUserIfNotFound("publisher", "timadeshola@gmail.com", "Eniola", "Jonathan", "password", "07030239942", publisherRole);

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
}
