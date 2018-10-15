package co.zonetechpark.booktest.booktest.testcases.repository;

import co.zonetechpark.booktest.booktest.jpa.entity.Role;
import co.zonetechpark.booktest.booktest.jpa.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.HashSet;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void viewUserByUsername() {
        User user = entityManager.persistFlushFind(User.builder().id(10L).username("jadeshola").password("Password@123").firstName("John").lastName("Adeshola")
                .fullName("John Adeshola").email("timadeshola@gmail.com").status(true).phoneNumber("2347030239942")
                .dateUpdated(null).dateDeleted(null).roles(new HashSet<>(Collections.singletonList(new Role("AUTHOR")))).build());
        assertThat(user.getUsername()).isEqualTo("jadeshola");
    }
}
