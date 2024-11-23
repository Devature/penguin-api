package dev.devature.penguin_api.service;

import dev.devature.penguin_api.entity.Organization;
import dev.devature.penguin_api.entity.AppUser;
import dev.devature.penguin_api.exception.AccessForbiddenException;
import dev.devature.penguin_api.exception.OrgRequestException;
import dev.devature.penguin_api.exception.OrganizationNotFoundException;
import dev.devature.penguin_api.model.OrgRequest;
import dev.devature.penguin_api.repository.OrganizationRepository;
import dev.devature.penguin_api.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrganizationService(OrganizationRepository organizationRepository, UserRepository userRepository) {
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
    }

    public Organization createOrganization(OrgRequest orgRequest, Claims authClaims) throws OrgRequestException {
        if (orgRequest.getName() == null || orgRequest.getName().isBlank())
            throw new OrgRequestException("Invalid organization name");

        AppUser owner = orgRequest.getOwner_id() == null ?
                userRepository.findByEmail(authClaims.getSubject())
                : userRepository.findById(orgRequest.getOwner_id()).orElse(null);

        if (owner == null) throw new OrgRequestException("Invalid owner id");

        Organization organization = new Organization();
        organization.setName(orgRequest.getName());
        organization.setOwner(owner);
        organization.getMembers().add(owner);

       organization = this.organizationRepository.save(organization);
       return organization;
    }

    public Organization getOrganization(long organizationId, Claims authClaims)
            throws AccessForbiddenException, OrganizationNotFoundException {
        Optional<Organization> organization = this.organizationRepository.findById(organizationId);
        if (organization.isPresent()) {
            String subject = authClaims.getSubject();
            Organization org = organization.get();
            if (org.getMembers().stream().anyMatch(member -> member.getEmail().equals(subject))) return org;
            else throw new AccessForbiddenException("User is not a member of this organization");
        } else throw new OrganizationNotFoundException("An organization with the requested ID was not found");
    }

    public Set<Organization> getUserOrganizations(Claims authClaims) {
        AppUser appUser = this.userRepository.findByEmail(authClaims.getSubject());
        return appUser.getOrganizations();
    }

    public Organization updateOrganization(long organizationId, OrgRequest request, Claims authClaims)
            throws AccessForbiddenException, OrganizationNotFoundException, OrgRequestException {
        Organization organization = this.getOrganization(organizationId, authClaims);
        if (organization.getOwner().getEmail().equals(authClaims.getSubject())) {
            String newName = request.getName();
            Long newOwnerId = request.getOwner_id();

            if (newName != null) {
                if (newName.isBlank()) throw new OrgRequestException("Invalid organization name");
                else organization.setName(newName);
            }

            if (newOwnerId != null) {
                Optional<AppUser> newOwner = this.userRepository.findById(newOwnerId);
                if (newOwner.isPresent()) organization.setOwner(newOwner.get());
                else throw new OrgRequestException("Invalid owner id");
            }

            return this.organizationRepository.save(organization);
        } else throw new AccessForbiddenException("Organizations can only be updated by their owners");
    }

    public void deleteOrganization(long organizationId, Claims authClaims)
            throws AccessForbiddenException, OrganizationNotFoundException {
        Organization organization = this.getOrganization(organizationId, authClaims);
        if (organization.getOwner().getEmail().equals(authClaims.getSubject())) {
            this.organizationRepository.delete(organization);
        } else throw new AccessForbiddenException("Organizations can only be deleted by their owners");
    }
}
