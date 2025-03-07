package com.publicis.sapient.rapid.lookup.RapidLookUp.repositoryTest;

import com.publicis.sapient.rapid.lookup.RapidLookUp.entity.User;
import com.publicis.sapient.rapid.lookup.RapidLookUp.repo.UserRepository;
import com.publicis.sapient.rapid.lookup.RapidLookUp.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setSsn("123-45-6789");
        user.setAge(30);
        User savedUser = userRepository.save(user);
        assertThat(savedUser.getId()).isNotNull();
    }

    @Test
    public void testFindByEmail() {
        User user = new User();
        user.setFirstName("Jane");
        user.setLastName("Doe");
        user.setEmail("jane.doe@example.com");
        user.setSsn("987-65-4321");
        user.setAge(28);
        userRepository.save(user);
        Optional<User> foundUser = userRepository.findByEmail("jane.doe@example.com");
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getFirstName()).isEqualTo("Jane");
    }
}
