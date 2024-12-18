package dev.devature.penguin_api.controller;

import dev.devature.penguin_api.entity.Issue;
import dev.devature.penguin_api.enums.IssueResult;
import dev.devature.penguin_api.service.IssueService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/organizations/{orgID}/issues")
public class IssueController {

    private final IssueService issueService;

    @Autowired
    public IssueController(IssueService issueService){
        this.issueService = issueService;
    }

    /**
     * @param issue Take in a RequestBody with the issue object created.
     * @return ResponseEntity with status and body based on the result of service.
     */
    @PostMapping
    public ResponseEntity<String> postIssue(@RequestAttribute("authClaims") Claims authClaims,
                                            @RequestBody Issue issue){
        IssueResult issueResult = issueService.createIssue(issue, authClaims);
        return stringResponseEntity(issueResult);
    }

    /**
     * @param issue Take in a RequestBody with the issue object created.
     * @param id Takes in a Path variable with the issue ID to be updated.
     * @return ResponseEntity with status and body based on the result of service.
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> putIssue(
            @RequestAttribute("authClaims") Claims authClaims,
            @RequestBody Issue issue,
            @PathVariable Long id){
        IssueResult issueResult = issueService.updateIssue(issue, id, authClaims);
        return stringResponseEntity(issueResult);
    }

    /**
     * @param id Take in an ID as a PathVariable.
     * @return The issue object if it request works or {@code null} if
     * something happened.
     */
    /*
    @GetMapping("/{id}")
    public ResponseEntity<Issue> getIssue(@PathVariable Long id){
        Issue issue = issueService.getByIdIssue(id);

        if(issue == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(issue);
    }
    */

    /**
     * @param id Takes in a Path variable with the issue ID to be updated.
     * @return ResponseEntity with status and body based on the result of service.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteIssue(@RequestAttribute("authClaims") Claims authClaims,
                                              @PathVariable Long id){
        IssueResult issueResult = issueService.deleteIssue(id, authClaims);
        return stringResponseEntity(issueResult);
    }

    /**
     * @param result Takes in a IssueResult enum to determine the result.
     * @return ResponseEntity based on the enum with the correct status code and body.
     */
    private ResponseEntity<String> stringResponseEntity(IssueResult result){
        switch (result){
            case CREATED -> {
                return ResponseEntity.status(HttpStatus.CREATED).body("Issue successfully created.");
            }
            case NOT_FOUND -> {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to find issue.");
            }
            case SUCCESS -> {
                return ResponseEntity.ok("Issue successfully updated.");
            }
            case BAD_DATA -> {
                return ResponseEntity.badRequest().body("Issue information is invalid.");
            }
            case NOT_AUTHORIZED -> {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("You do not have permission for that.");
            }
            default -> {
                return ResponseEntity.status(422)
                        .body("Invalid issue data. Please review your input and try again.");
            }
        }
    }
}
