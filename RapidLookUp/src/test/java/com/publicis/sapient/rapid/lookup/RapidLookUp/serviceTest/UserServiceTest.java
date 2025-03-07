package com.publicis.sapient.rapid.lookup.RapidLookUp.serviceTest;

import com.publicis.sapient.rapid.lookup.RapidLookUp.entity.User;
import com.publicis.sapient.rapid.lookup.RapidLookUp.entity.UserResponse;
import com.publicis.sapient.rapid.lookup.RapidLookUp.repo.UserRepository;
import com.publicis.sapient.rapid.lookup.RapidLookUp.service.MassIndexerConfig;
import com.publicis.sapient.rapid.lookup.RapidLookUp.service.UserService;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import jakarta.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
@ActiveProfiles("test")
public class UserServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private EntityManager entityManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MassIndexerConfig massIndexerConfig;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadDataFromApiSuccess() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setSsn("123-45-6789");
        user.setAge(30);

        UserResponse response = new UserResponse();
        response.setUsers(Arrays.asList(user));

        ReflectionTestUtils.setField(userService, "userApiUrl", "http://dummyurl.com/api/users");

        when(restTemplate.getForObject(anyString(), eq(UserResponse.class))).thenReturn(response);
        when(userRepository.saveAll(any(List.class))).thenReturn(Arrays.asList(user));

        userService.loadDataFromApi();

        verify(userRepository, times(1)).saveAll(any(List.class));
        verify(massIndexerConfig, times(1)).rebuildIndex();
    }

    @Test
    public void testLoadDataFromApiNoData() {
        UserResponse response = new UserResponse();
        response.setUsers(Arrays.asList());
        when(restTemplate.getForObject(anyString(), eq(UserResponse.class))).thenReturn(response);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userService.loadDataFromApi();
        });
        assertThat(exception.getStatusCode()).isEqualTo(org.springframework.http.HttpStatus.NO_CONTENT);
    }

    @Test
    public void testFindUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userService.findUserByEmailOrId(1L, null);
        });
        assertThat(exception.getStatusCode()).isEqualTo(org.springframework.http.HttpStatus.NOT_FOUND);
    }

    @Test
    public void testFindUserByEmail_NotFound() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userService.findUserByEmailOrId(null, "nonexistent@example.com");
        });
        assertThat(exception.getStatusCode()).isEqualTo(org.springframework.http.HttpStatus.NOT_FOUND);
    }

}