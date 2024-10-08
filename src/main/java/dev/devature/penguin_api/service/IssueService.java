package dev.devature.penguin_api.service;

import dev.devature.penguin_api.entity.Issue;
import dev.devature.penguin_api.enums.IssueResult;
import dev.devature.penguin_api.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class IssueService {
    private IssueRepository issueRepository;

    @Autowired
    public IssueService(IssueRepository issueRepository){
        this.issueRepository = issueRepository;
    }

    /**
     * @param issue Takes in an {@code Issue} that will be created in the database.
     * @return The result of the request to the service layer as a {@code IssueResult}.
     */
    public IssueResult createIssue(Issue issue){
        if(checkIfBadData(issue)){
            return IssueResult.BAD_DATA;
        }

        issueRepository.save(issue);

        return IssueResult.CREATED;
    }

    /**
     * @param issue Take in a {@code Issue} object to patch an issue by the ID.
     * @param id Take in a {@code Long} id that will be used to find and update an issue.
     * @return {@code IssueResult} enum that determines what happened during the service call.
     */
    public IssueResult updateIssue(Issue issue, Long id){
        Issue returnIssue = checkIfIssueExist(id);

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
        returnIssue.setCreated_by(issue.getCreated_by());

        Issue returnDatabaseIssue = issueRepository.save(returnIssue);

        boolean isValid = returnDatabaseIssue.getId() != null
                && !returnDatabaseIssue.getTitle().isEmpty();

        return isValid ? IssueResult.SUCCESS : IssueResult.UNKNOWN_ERROR;
    }

    /**
     * @param id Take in an ID to return an issue from
     * @return The issue
     */
    public Issue getIssue(Long id){
        return checkIfIssueExist(id);
    }

    public IssueResult deleteIssue(Long id){
        int rowsDelete = issueRepository.removeById(id);

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
    private boolean checkIfBadData(Issue issue){
        if(issue.getTitle().isEmpty() || issue.getStatus_id() == null || issue.getColumn_id() != null){
            return true;
        }
        return false;
    }

    /**
     * @param id Take in the ID to search the database.
     * @return Issue if the {@code Issue} exist or return {@code null} if there isn't an issue.
     */
    private Issue checkIfIssueExist(Long id){
        Optional<Issue> issueOptional = issueRepository.findById(id);
        return issueOptional.orElse(null);
    }
}