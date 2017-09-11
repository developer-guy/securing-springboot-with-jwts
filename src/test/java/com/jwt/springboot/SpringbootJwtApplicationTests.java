package com.jwt.springboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwt.springboot.domain.AccountCredentials;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(secure = false)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringbootJwtApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void contextLoads() {
        assertNotNull("Is context load successfully", mockMvc);
    }


    @Test
    public void should_Added_Token_To_Response() throws Exception {
        AccountCredentials accountCredentials = new AccountCredentials();
        accountCredentials.setUsername("admin");
        accountCredentials.setPassword("password");
        String requestBody = new ObjectMapper().writeValueAsString(accountCredentials);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        String token = response.getHeader("Authorization");
        assertNotNull(token);
    }

    @Test
    public void should_Return_Users_With_Token() throws Exception {
        AccountCredentials accountCredentials = new AccountCredentials();
        accountCredentials.setUsername("admin");
        accountCredentials.setPassword("password");
        String requestBody = new ObjectMapper().writeValueAsString(accountCredentials);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        String token = response.getHeader("Authorization");


        mockMvc.perform(MockMvcRequestBuilders.get("/users").
                header("Authorization", "Bearer " + token).
                accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).
                andExpect(MockMvcResultMatchers.jsonPath("$.users", hasSize(2))).andExpect(MockMvcResultMatchers.jsonPath("$.users[0].firstname", is("Richard")));

    }

    @Test
    public void should_Return_4xxClientError_When_AttempToGetUsers_Without_Token() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users").
                accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().is4xxClientError());

    }
}
