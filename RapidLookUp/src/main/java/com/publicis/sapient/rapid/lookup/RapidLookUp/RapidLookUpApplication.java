package com.publicis.sapient.rapid.lookup.RapidLookUp;

import com.publicis.sapient.rapid.lookup.RapidLookUp.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RapidLookUpApplication {
	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(RapidLookUpApplication.class, args);
	}

	@PostConstruct
	public void loadUsersFromExternalAPI() {
		userService.loadDataFromApi();
	}
}
