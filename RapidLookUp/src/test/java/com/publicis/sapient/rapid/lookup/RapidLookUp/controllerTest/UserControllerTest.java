package com.publicis.sapient.rapid.lookup.RapidLookUp.controllerTest;

import com.publicis.sapient.rapid.lookup.RapidLookUp.RapidLookUpApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = RapidLookUpApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testSearchEndpointBadRequest() throws Exception {
        mockMvc.perform(get("/users/search?searchText=Jo")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testLoadEndpoint() throws Exception {
        mockMvc.perform(post("/users/load")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testFindByIdEndpoint() throws Exception {
        mockMvc.perform(get("/users/find?id=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testFindByEmailEndpoint() throws Exception {
        mockMvc.perform(post("/users/load")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/find?email=emily.johnson@x.dummyjson.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testSearchEndpointValid() throws Exception {
        mockMvc.perform(get("/users/search?searchText=Joh")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}