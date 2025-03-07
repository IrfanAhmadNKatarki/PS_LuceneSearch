package com.publicis.sapient.rapid.lookup.RapidLookUp.service;

import com.publicis.sapient.rapid.lookup.RapidLookUp.entity.User;
import com.publicis.sapient.rapid.lookup.RapidLookUp.entity.UserResponse;
import com.publicis.sapient.rapid.lookup.RapidLookUp.repo.UserRepository;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private RestTemplate restTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MassIndexerConfig massIndexerConfig;


    public User findUserByEmailOrId(Long id, String email) {
        if (id != null) {
            return userRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        } else if (email != null) {
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Either id or email must be provided");
    }

    @Transactional
    public List<User> searchUsers(String searchText) {
        logger.info("Searching users with text: {}", searchText);
        SearchSession searchSession = Search.session(entityManager);
        return searchSession.search(User.class)
                .where(f -> f.bool()
                        .should(f.match().field("firstName").matching(searchText).fuzzy(1))
                        .should(f.match().field("lastName").matching(searchText).fuzzy(1))
                        .should(f.match().field("ssn").matching(searchText)))
                .fetchHits(20);
    }

    @Value("${external.user-api-url}")
    private String userApiUrl;

    // Load users from external API into H2 DB and rebuild search index after loading
    @Retryable(value = {RestClientException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public void loadDataFromApi() {
        try {
            logger.info("Fetching users from external API: {}", userApiUrl);
            UserResponse userResponse = restTemplate.getForObject(userApiUrl, UserResponse.class);

            if (userResponse == null || userResponse.getUsers() == null || userResponse.getUsers().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No users found in external API");
            }
             userRepository.saveAll(userResponse.getUsers());
            logger.info("Saved {} users into the database", userResponse.getUsers().size());
            massIndexerConfig.rebuildIndex();
        } catch (RestClientException e) {
            logger.error("Error fetching data from external API", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Failed to fetch data from external API", e);
        }
    }


}
