package dev.devature.penguin_api.controller;

import dev.devature.penguin_api.entity.Issue;
import dev.devature.penguin_api.enums.IssueResult;
import dev.devature.penguin_api.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/issue")
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
    @PostMapping("")
    public ResponseEntity<String> postIssue(@RequestBody Issue issue){
        IssueResult issueResult = issueService.createIssue(issue);
        return stringResponseEntity(issueResult);
    }

    /**
     * @param issue Take in a RequestBody with the issue object created.
     * @param id Takes in a Path variable with the issue ID to be updated.
     * @return ResponseEntity with status and body based on the result of service.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<String> patchIssue(@RequestBody Issue issue, @PathVariable Long id){
        IssueResult issueResult = issueService.updateIssue(issue, id);
        return stringResponseEntity(issueResult);
    }

    /**
     * @param id Take in an ID as a PathVariable.
     * @return The issue object if it request works or {@code null} if
     * something happened.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Issue> getIssue(@PathVariable Long id){
        Issue issue = issueService.getIssue(id);

        if(issue == null){
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(issue);
    }

    /**
     * @param result Takes in a IssueResult enum to determine the result.
     * @return ResponseEntity based on the enum with the correct status code and body.
     */
    private ResponseEntity<String> stringResponseEntity(IssueResult result){
        switch (result){
            case SUCCESS -> {
                return ResponseEntity.status(HttpStatus.CREATED).body("Issue successfully created.");
            }
            case SUCCESS_UPDATE -> {
                return ResponseEntity.ok("Issue successfully updated.");
            }
            case BAD_DATA -> {
                return ResponseEntity.badRequest().body("Issue information is invalid.");
            }
            default -> {
                return ResponseEntity.status(422)
                        .body("Invalid issue data. Please review your input and try again.");
            }
        }
    }
}
