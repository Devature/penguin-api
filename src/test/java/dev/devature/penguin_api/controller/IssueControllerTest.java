package dev.devature.penguin_api.controller;

import dev.devature.penguin_api.entity.Issue;
import dev.devature.penguin_api.enums.IssueResult;
import dev.devature.penguin_api.model.JwtToken;
import dev.devature.penguin_api.service.IssueService;
import dev.devature.penguin_api.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;
import java.util.Optional;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
//import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
//import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class IssueControllerTest extends RequestsTest {
    @MockBean
    private IssueService issueService;

    @MockBean
    private JwtService jwtService;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    private Claims createMockClaim() {
        return Jwts.claims()
                .subject("user123")
                .issuer("test-issuer")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .build();
    }

    @Test
    public void testIssue_CreateSuccess() throws Exception {
        Long orgId = 1L;
        Issue issue = new Issue(0L, 2L, "Test Data");
        Claims mockClaims = createMockClaim();

        when(jwtService.verifyToken(any(JwtToken.class)))
                .thenReturn(Optional.of(mockClaims));

        when(issueService.createIssue(any(Issue.class), eq(mockClaims)))
                .thenReturn(IssueResult.CREATED);

        this.mockMvc.perform(post("/api/v1/organizations/{orgId}/issues", orgId)
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(issue)))
                .andExpect(status().is(201))
                .andExpect(content().string(containsString("Issue successfully created.")))
                .andDo(document("issue/success"));
    }

    @Test
    public void testIssue_CreateFailed() throws Exception {
        Long orgId = 1L;
        Issue issue = new Issue(0L, 2L, "Test Data");
        Claims mockClaims = createMockClaim();

        when(jwtService.verifyToken(any(JwtToken.class)))
                .thenReturn(Optional.of(mockClaims));

        when(issueService.createIssue(any(Issue.class), eq(mockClaims)))
                .thenReturn(IssueResult.BAD_DATA);

        this.mockMvc.perform(post("/api/v1/organizations/{orgId}/issues", orgId)
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(issue)))
                .andExpect(status().is(400))
                .andExpect(content()
                        .string(containsString("Issue information is invalid.")))
                .andDo(document("issue/failed"));
    }

    @Test
    public void testIssue_UpdateSuccess() throws Exception {
        Long orgId = 1L;
        Issue issue = new Issue();
        issue.setTitle("Updated Title");
        Claims mockClaims = createMockClaim();

        when(jwtService.verifyToken(any(JwtToken.class)))
                .thenReturn(Optional.of(mockClaims));

        when(issueService.updateIssue(any(Issue.class), eq(1L), eq(mockClaims))).thenReturn(IssueResult.SUCCESS);

        this.mockMvc.perform(put("/api/v1/organizations/{orgId}/issues/{id}", orgId, 1L)
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(issue)))
                .andExpect(status().isOk())
                .andExpect(content()
                        .string(containsString("Issue successfully updated.")))
                .andDo(document("issue/success"));
    }

    @Test
    public void testIssue_UpdateFailed() throws Exception {
        Long orgId = 1L;
        Issue issue = new Issue();
        issue.setTitle("");
        Claims mockClaims = createMockClaim();

        when(jwtService.verifyToken(any(JwtToken.class)))
                .thenReturn(Optional.of(mockClaims));

        when(issueService.updateIssue(any(Issue.class), eq(1L), eq(mockClaims))).thenReturn(IssueResult.BAD_DATA);

        this.mockMvc.perform(put("/api/v1/organizations/{orgId}/issues/{id}", orgId, 1L)
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(issue)))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .string(containsString("Issue information is invalid.")))
                .andDo(document("issue/failed"));
    }
/*
    @Test
    public void testIssue_GetSuccess() throws Exception {
        Long orgId = 1L;
        Long issueId = 1L;
        Issue mockIssue = new Issue();
        mockIssue.setId(issueId);
        mockIssue.setTitle("Test Issue");
        Claims mockClaims = createMockClaim();

        when(jwtService.verifyToken(any(JwtToken.class)))
                .thenReturn(Optional.of(mockClaims));

        when(issueService.getByIdIssue(issueId)).thenReturn(mockIssue);

        mockMvc.perform(get("/api/v1/organizations/{orgId}/issues/{id}", orgId, issueId)
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(issueId))
                .andExpect(jsonPath("$.title").value("Test Issue"))
                .andDo(document("issue/success"));

        verify(issueService, times(1)).getByIdIssue(issueId);
    }

    @Test
    public void testIssue_GetNotFound() throws Exception {
        Long orgId = 1L;
        Long issueId = 1L;
        Claims mockClaims = createMockClaim();

        when(jwtService.verifyToken(any(JwtToken.class)))
                .thenReturn(Optional.of(mockClaims));

        when(issueService.getByIdIssue(issueId)).thenReturn(null);

        mockMvc.perform(get("/api/v1/organizations/{orgId}/issues/{id}", orgId, issueId)
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""))
                .andDo(document("issue/failed"));

        verify(issueService, times(1)).getByIdIssue(issueId);
    }
*/
    @Test
    public void testIssue_DeleteSuccess() throws Exception {
        Long orgId = 1L;
        Long issueId = 1L;
        Claims mockClaims = createMockClaim();

        when(jwtService.verifyToken(any(JwtToken.class)))
                .thenReturn(Optional.of(mockClaims));

        when(issueService.deleteIssue(issueId, mockClaims)).thenReturn(IssueResult.SUCCESS);

        mockMvc.perform(delete("/api/v1/organizations/{orgId}/issues/{id}", orgId, issueId)
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Issue successfully updated."))
                .andDo(document("issue/success"));

        verify(issueService, times(1)).deleteIssue(issueId, mockClaims);
    }

    @Test
    public void testIssue_DeleteFailed() throws Exception {
        Long orgId = 1L;
        Long issueId = 1L;
        Claims mockClaims = createMockClaim();

        when(jwtService.verifyToken(any(JwtToken.class)))
                .thenReturn(Optional.of(mockClaims));

        when(issueService.deleteIssue(issueId, mockClaims)).thenReturn(IssueResult.NOT_FOUND);

        mockMvc.perform(delete("/api/v1/organizations/{orgId}/issues/{id}", orgId, issueId)
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Failed to find issue."))
                .andDo(document("issue/failed"));

        verify(issueService, times(1)).deleteIssue(issueId, mockClaims);
    }
}
