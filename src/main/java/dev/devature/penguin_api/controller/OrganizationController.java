package dev.devature.penguin_api.controller;

import dev.devature.penguin_api.entity.Organization;
import dev.devature.penguin_api.exception.AccessForbiddenException;
import dev.devature.penguin_api.exception.OrgRequestException;
import dev.devature.penguin_api.exception.OrganizationNotFoundException;
import dev.devature.penguin_api.model.OrgRequest;
import dev.devature.penguin_api.service.OrganizationService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping
    public ResponseEntity<?> createOrganization(@RequestAttribute("authClaims") Claims authClaims,
                                                @RequestBody OrgRequest orgRequest) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(this.organizationService.createOrganization(orgRequest, authClaims));
        } catch (OrgRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrganization(@RequestAttribute("authClaims") Claims authClaims,
                                                        @PathVariable Long id) {
        try {
            Organization organization = this.organizationService.getOrganization(id, authClaims);
            return ResponseEntity.ok().body(organization);
        } catch (OrganizationNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (AccessForbiddenException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Set<Organization>> getUserOrganizations(@RequestAttribute("authClaims") Claims authClaims) {
        return ResponseEntity.ok(this.organizationService.getUserOrganizations(authClaims));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchOrganization(@RequestAttribute("authClaims") Claims authClaims,
                                                          @RequestBody OrgRequest orgRequest,
                                                          @PathVariable Long id) {
        try {
            return ResponseEntity.ok(this.organizationService.updateOrganization(id, orgRequest, authClaims));
        } catch (OrganizationNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (AccessForbiddenException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (OrgRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrganization(@RequestAttribute("authClaims") Claims authClaims,
                                                @PathVariable Long id) {
        try {
            this.organizationService.deleteOrganization(id, authClaims);
            return ResponseEntity.ok().build();
        } catch (OrganizationNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (AccessForbiddenException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
