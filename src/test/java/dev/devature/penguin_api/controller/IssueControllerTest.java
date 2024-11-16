package dev.devature.penguin_api.controller;

import dev.devature.penguin_api.entity.Issue;
import dev.devature.penguin_api.enums.IssueResult;
import dev.devature.penguin_api.interceptor.AuthenticationInterceptor;
import dev.devature.penguin_api.service.IssueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class IssueControllerTest extends RequestsTest {
    @MockBean
    private IssueService issueService;

    @MockBean
    private AuthenticationInterceptor authInterceptor;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
        try {
            when(authInterceptor.preHandle(any(), any(), any())).thenReturn(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testIssue_CreateSuccess() throws Exception {
        Issue issue = new Issue(0L, 2L, "Test Data");

        when(issueService.createIssue(issue)).thenReturn(IssueResult.CREATED);

        this.mockMvc.perform(post("/api/v1/issue/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(issue)))
                .andExpect(status().is(201))
                .andExpect(content()
                        .string(containsString("Issue successfully created.")))
                .andDo(document("issue/success"));
    }

    @Test
    public void testIssue_CreateFailed() throws Exception {
        Issue issue = new Issue(0L, 2L, "Test Data");

        when(issueService.createIssue(issue)).thenReturn(IssueResult.BAD_DATA);

        this.mockMvc.perform(post("/api/v1/issue/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(issue)))
                .andExpect(status().is(400))
                .andExpect(content()
                        .string(containsString("Issue information is invalid.")))
                .andDo(document("issue/failed"));
    }

    @Test
    public void testIssue_UpdateSuccess() throws Exception {
        Issue issue = new Issue();
        issue.setTitle("Updated Title");

        when(issueService.updateIssue(any(Issue.class), eq(1L))).thenReturn(IssueResult.SUCCESS);

        this.mockMvc.perform(patch("/api/v1/issue/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(issue)))
                .andExpect(status().isOk())
                .andExpect(content()
                        .string(containsString("Issue successfully updated.")))
                .andDo(document("issue/success"));
    }

    @Test
    public void testIssue_UpdateFailed() throws Exception {
        Issue issue = new Issue();
        issue.setTitle("");

        when(issueService.updateIssue(any(Issue.class), eq(1L))).thenReturn(IssueResult.BAD_DATA);

        this.mockMvc.perform(patch("/api/v1/issue/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(issue)))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .string(containsString("Issue information is invalid.")))
                .andDo(document("issue/failed"));
    }

    @Test
    public void testIssue_GetSuccess() throws Exception {
        Long issueId = 1L;
        Issue mockIssue = new Issue();
        mockIssue.setId(issueId);
        mockIssue.setTitle("Test Issue");

        when(issueService.getIssue(issueId)).thenReturn(mockIssue);

        mockMvc.perform(get("/api/v1/issue/{id}", issueId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(issueId))
                .andExpect(jsonPath("$.title").value("Test Issue"))
                .andDo(document("issue/success"));

        verify(issueService, times(1)).getIssue(issueId);
    }

    @Test
    public void testIssue_GetNotFound() throws Exception {
        Long issueId = 1L;

        when(issueService.getIssue(issueId)).thenReturn(null);

        mockMvc.perform(get("/api/v1/issue/{id}", issueId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""))
                .andDo(document("issue/failed"));

        verify(issueService, times(1)).getIssue(issueId);
    }

    @Test
    public void testIssue_DeleteSuccess() throws Exception {
        Long issueId = 1L;

        when(issueService.deleteIssue(issueId)).thenReturn(IssueResult.SUCCESS);

        mockMvc.perform(delete("/api/v1/issue/{id}", issueId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Issue successfully updated."))
                .andDo(document("issue/success"));

        verify(issueService, times(1)).deleteIssue(issueId);
    }

    @Test
    public void testIssue_DeleteFailed() throws Exception {
        Long issueId = 1L;

        when(issueService.deleteIssue(issueId)).thenReturn(IssueResult.NOT_FOUND);

        mockMvc.perform(delete("/api/v1/issue/{id}", issueId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Failed to find issue."))
                .andDo(document("issue/failed"));

        verify(issueService, times(1)).deleteIssue(issueId);
    }
}
