package dev.devature.penguin_api.service;

import dev.devature.penguin_api.entity.Issue;
import dev.devature.penguin_api.entity.Member;
import dev.devature.penguin_api.enums.IssueResult;
import dev.devature.penguin_api.repository.IssueRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class IssueService {
    private final IssueRepository issueRepository;
    private final MemberService memberService;

    @Autowired
    public IssueService(IssueRepository issueRepository, MemberService memberService){
        this.issueRepository = issueRepository;
        this.memberService = memberService;
    }

    /**
     * @param issue Takes in an {@code Issue} that will be created in the database.
     * @param authClaims Take in a claim to check authorization.
     * @return The result of the request to the service layer as a {@code IssueResult}.
     */
    @Transactional
    public IssueResult createIssue(Issue issue, Claims authClaims){
        if(checkIfBadData(issue)){
            return IssueResult.BAD_DATA;
        }

        if(checkAuthorization(issue, authClaims)){
            return IssueResult.NOT_AUTHORIZED;
        }

        issueRepository.save(issue);

        return IssueResult.CREATED;
    }

    /**
     * @param issue Take in a {@code Issue} object to patch an issue by the ID.
     * @param id Take in a {@code Long} id that will be used to find and update an issue.
     * @param authClaims Take in a claim to check authorization.
     * @return {@code IssueResult} enum that determines what happened during the service call.
     */
    @Transactional
    public IssueResult updateIssue(Issue issue, Long id, Claims authClaims){
        Issue returnIssue = checkIfIssueExist(id);

        if(checkAuthorization(issue, authClaims)){
            return IssueResult.NOT_AUTHORIZED;
        }

        if(returnIssue == null || checkIfBadData(issue)){
            return IssueResult.BAD_DATA;
        }

        returnIssue.setColumn_id(issue.getColumn_id());
        returnIssue.setTitle(issue.getTitle());
        returnIssue.setSummary(issue.getSummary());
        returnIssue.setStory_points(issue.getStory_points());
        returnIssue.setStatus_id(issue.getStatus_id());
        returnIssue.setAssignee_id(issue.getAssignee_id());
        returnIssue.setDue_date(issue.getDue_date());
        returnIssue.setCreated_at(issue.getCreated_at());
        returnIssue.setParent_issue_id(issue.getParent_issue_id());
        returnIssue.setUpdated_at(issue.getUpdated_at());

        Issue returnDatabaseIssue = issueRepository.save(returnIssue);

        boolean isValid = returnDatabaseIssue.getId() != null
                && !returnDatabaseIssue.getTitle().isEmpty();

        return isValid ? IssueResult.SUCCESS : IssueResult.UNKNOWN_ERROR;
    }

    /**
     * @param id Take in an ID to return an issue from
     * @return Returns a specific issue that is requested.
     */
    public Issue getByIdIssue(Long id){
        return checkIfIssueExist(id);
    }

    /**
     * Used to delete a singular issue.
     * @param id Used to find the issue.
     * @param authClaims Take in a claim to check authorization.
     * @return The result of the operation.
     */
    @Transactional
    public IssueResult deleteIssue(Long id, Claims authClaims){
        Optional<Issue> issue = issueRepository.findById(id);

        if(issue.isPresent()){
            if(checkAuthorization(issue.get(), authClaims)){
                return IssueResult.NOT_AUTHORIZED;
            }
        }

        long rowsDelete = issueRepository.removeById(id);

        if(rowsDelete == 0){
            return IssueResult.NOT_FOUND;
        }

        return IssueResult.SUCCESS;
    }

    /**
     * @param issue Take in a {@code Issue} object to process the request.
     * @return {@code True} there is an issue with the input of the data or {@code False} if the input of data
     * is complete.
     */
    public boolean checkIfBadData(Issue issue){
        return     issue == null
                || issue.getTitle() == null
                || issue.getTitle().isEmpty()
                || issue.getStatus_id() == null
                || issue.getColumn_id() == null;
    }

    /**
     * @param id Take in the ID to search the database.
     * @return Issue if the {@code Issue} exist or return {@code null} if there isn't an issue.
     */
    private Issue checkIfIssueExist(Long id){
        Optional<Issue> issueOptional = issueRepository.findById(id);
        return issueOptional.orElse(null);
    }

    /**
     * @param issue Take in an issue with all the information to process permission levels.
     * @param authClaims Take in a claims to be process and verify if person who they say they are.
     * @return {@code True} if the person is authorized to create issue, or {@code False} if they do not
     * have authorization to create an issue.
     */
    private boolean checkAuthorization(Issue issue, Claims authClaims){
        Optional<Member> member = memberService.getMemberByUserId(authClaims.getSubject(), issue.getOrganization_id());
        return member.isPresent();
    }
}
