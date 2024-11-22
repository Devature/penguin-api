package dev.devature.penguin_api.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;


@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@AutoConfigureMockMvc
public abstract class RequestsTest {
    @Autowired
    protected WebApplicationContext context;

    protected MockMvc mockMvc;

    protected ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                        .apply(documentationConfiguration(restDocumentation))
                        .build();
    }
}
