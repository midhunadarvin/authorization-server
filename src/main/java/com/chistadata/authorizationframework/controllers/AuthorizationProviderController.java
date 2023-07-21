package com.chistadata.authorizationframework.controllers;

import com.chistadata.authorizationframework.dto.IsAuthorizedDTO;
import com.chistadata.authorizationframework.dto.IsMemberOfDTO;
import com.chistadata.authorizationframework.models.ErrorResponse;
import com.chistadata.authorizationframework.models.IAuthorizationProvider;
import com.chistadata.authorizationframework.models.Operation;
import com.chistadata.authorizationframework.models.Principal;
import com.chistadata.authorizationframework.models.Resource;
import com.chistadata.authorizationframework.models.ResourceAccessPredicate;
import com.chistadata.authorizationframework.models.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("authorization-provider")
public class AuthorizationProviderController {

    @Autowired
    @Qualifier("DatabaseAuthorizationProviderService")
    private IAuthorizationProvider authorizationProviderService;

    @GetMapping(path = "/is-authorized", produces = "application/json")
    public ResponseEntity<?> isAuthorized(@RequestParam String principalId, @RequestParam String operationId, @RequestParam String resourceId) {
        try {
            Boolean isAuthorized = authorizationProviderService.isAuthorized(principalId, operationId, resourceId);
            SuccessResponse<Boolean> response = new SuccessResponse<>("Checked IsAuthorized successfully.", isAuthorized);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Failed to check IsAuthorized. " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping(path = "/is-member-of", produces = "application/json")
    public ResponseEntity<?> isMemberOf(@RequestParam String groupId, @RequestParam String memberId) {
        try {
            Boolean isMemberOf = authorizationProviderService.isMemberOf(groupId, memberId);
            SuccessResponse<Boolean> response = new SuccessResponse<>("Checked IsMemberOf successfully.", isMemberOf);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Failed to check IsMemberOf. " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping(path = "/is-sub-operation", produces = "application/json")
    public ResponseEntity<?> isSubOperation(@RequestParam String operationId, @RequestParam String subOperationId) {
        try {
            Boolean isMemberOf = authorizationProviderService.isSubOperation(operationId, subOperationId);
            SuccessResponse<Boolean> response = new SuccessResponse<>("Checked IsSubOperation successfully.", isMemberOf);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Failed to check IsSubOperation. " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping(path = "/is-sub-resource", produces = "application/json")
    public ResponseEntity<?> isSubResource(@RequestParam String resourceId, @RequestParam String subResourceId) {
        try {
            Boolean isMemberOf = authorizationProviderService.isSubResource(resourceId, subResourceId);
            SuccessResponse<Boolean> response = new SuccessResponse<>("Checked IsSubResource successfully.", isMemberOf);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Failed to check IsSubResource. " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping(path = "/authorization-digest", produces = "application/json")
    public ResponseEntity<?> fetchAuthorizedOperations(@RequestParam String principalId) {
        try {
            List<ResourceAccessPredicate> resourceAccessPredicateList = authorizationProviderService.getAuthorizationDigest(principalId);
            SuccessResponse<List<ResourceAccessPredicate>> response = new SuccessResponse<>("Fetched authorization digest successfully.", resourceAccessPredicateList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Failed to fetch authorization digest. " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping(path = "/authorized-operations", produces = "application/json")
    public ResponseEntity<?> fetchAuthorizedOperations(@RequestParam String principalId, @RequestParam String resourceId) {
        try {
            List<Operation> operationList = authorizationProviderService.getAuthorizedOperations(principalId, resourceId);
            SuccessResponse<List<Operation>> response = new SuccessResponse<>("Fetched Authorized Operations successfully.", operationList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Failed to fetch authorized operations. " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping(path = "/authorized-resources", produces = "application/json")
    public ResponseEntity<?> fetchAuthorizedResources(@RequestParam String principalId, @RequestParam String operationId) {
        try {
            List<Resource> resourceList = authorizationProviderService.getAuthorizedResources(principalId, operationId);
            SuccessResponse<List<Resource>> response = new SuccessResponse<>("Fetched Authorized Resources successfully.", resourceList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Failed to fetch authorized resources. " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}