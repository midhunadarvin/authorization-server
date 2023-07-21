package com.chistadata.authorizationframework.controllers;

import com.chistadata.authorizationframework.dto.AddGroupDTO;
import com.chistadata.authorizationframework.dto.AddSubOperationDTO;
import com.chistadata.authorizationframework.dto.AddSubResourceDTO;
import com.chistadata.authorizationframework.models.ErrorResponse;
import com.chistadata.authorizationframework.models.IAuthorizationManager;
import com.chistadata.authorizationframework.models.Operation;
import com.chistadata.authorizationframework.models.Principal;
import com.chistadata.authorizationframework.models.PrincipalType;
import com.chistadata.authorizationframework.models.Resource;
import com.chistadata.authorizationframework.models.ResourceAccessPredicate;
import com.chistadata.authorizationframework.models.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("authorization-management")
public class AuthorizationManagementController {

    @Autowired
    @Qualifier("DatabaseAuthorizationManagerService")
    private IAuthorizationManager authorizationManagerService;

    @GetMapping(path = "/principals", produces = "application/json")
    public ResponseEntity<?> listPrincipals(@RequestParam(required = false) PrincipalType principalType) {
        try {
            List<Principal> principalList;
            if (principalType != null) {
                principalList = this.authorizationManagerService.getPrincipalList(principalType);
            } else {
                principalList = this.authorizationManagerService.getPrincipalList();
            }

            SuccessResponse<List<Principal>> response = new SuccessResponse<>("Fetched principal list successfully.", principalList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Failed to add principal. " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping(path = "/principals", produces = "application/json")
    public ResponseEntity<?> addPrincipal(@RequestBody Principal principal) {
        try {
            this.authorizationManagerService.addOrUpdate(principal);
            SuccessResponse<String> response = new SuccessResponse<>("Added principal successfully.", principal.toString());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Failed to add principal. " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping(path = "/principals/{principalId}", produces = "application/json")
    public ResponseEntity<?> removePrincipal(@PathVariable("principalId") String principalId) {
        try {
            this.authorizationManagerService.removePrincipal(principalId);
            SuccessResponse<String> response = new SuccessResponse<>("Removed principal successfully.", principalId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Failed to list principal. " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping(path = "/operations", produces = "application/json")
    public ResponseEntity<?> listOperations(@RequestParam(required = false) String parentOperationId, @RequestParam(required = false) String isFlat) {
        try {
            List<Operation> operationList;
            if (parentOperationId != null && !parentOperationId.isEmpty()) {
                Operation op = Operation.builder()
                        .id(parentOperationId)
                        .build();
                if (isFlat != null && isFlat.equals("true")) {
                    operationList = authorizationManagerService.getFlatSubOperationsList(op);
                } else {
                    operationList = authorizationManagerService.getSubOperationsList(op);
                }
            } else {
                operationList = authorizationManagerService.getOperationList();
            }

            SuccessResponse<List<Operation>> response = new SuccessResponse<>("Fetched operation list successfully.", operationList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Failed to list operations. " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping(path = "/operations", produces = "application/json")
    public ResponseEntity<?> addOperation(@RequestBody Operation operation) {
        try {
            this.authorizationManagerService.addOrUpdate(operation);
            SuccessResponse<String> response = new SuccessResponse<>("Added operation successfully.", operation.toString());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Failed to add operation. " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping(path = "/operations/{operationId}", produces = "application/json")
    public ResponseEntity<?> removeOperation(@PathVariable("operationId") String operationId) {
        try {
            this.authorizationManagerService.removeOperation(operationId);
            SuccessResponse<String> response = new SuccessResponse<>("Removed operation successfully.", operationId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Failed to remove operation. " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping(path = "/resources", produces = "application/json")
    public ResponseEntity<?> listResources(@RequestParam(required = false) String parentResourceId, @RequestParam(required = false) String isFlat) {
        try {
            List<Resource> resourceList;
            if (parentResourceId != null && !parentResourceId.isEmpty()) {
                Resource resource = Resource.builder()
                        .id(parentResourceId)
                        .build();
                if (isFlat != null && isFlat.equals("true")) {
                    resourceList = authorizationManagerService.getFlatSubResourcesList(resource);
                } else {
                    resourceList = authorizationManagerService.getSubResourcesList(resource);
                }
            } else {
                resourceList = authorizationManagerService.getResourcesList();
            }

            SuccessResponse<List<Resource>> response = new SuccessResponse<>("Fetched resource list successfully.", resourceList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Failed to list resources. " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping(path = "/resources", produces = "application/json")
    public ResponseEntity<?> addResource(@RequestBody Resource resource) {
        try {
            this.authorizationManagerService.addOrUpdate(resource);
            SuccessResponse<String> response = new SuccessResponse<>("Added resource successfully.", resource.toString());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Failed to add resource. " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping(path = "/resources/{resourceId}", produces = "application/json")
    public ResponseEntity<?> removeResource(@PathVariable("resourceId") String resourceId) {
        try {
            this.authorizationManagerService.removeResource(resourceId);
            SuccessResponse<String> response = new SuccessResponse<>("Removed resource successfully.", resourceId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Failed to remove resource. " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping(path = "/permissions", produces = "application/json")
    public ResponseEntity<?> addResourceAccessPredicate(@RequestBody ResourceAccessPredicate resourceAccessPredicate) {
        try {
            this.authorizationManagerService.addAccessPredicate(resourceAccessPredicate);
            SuccessResponse<String> response = new SuccessResponse<>("Added permission successfully.", resourceAccessPredicate.toString());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Failed to add permission. " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping(path = "/permissions", produces = "application/json")
    public ResponseEntity<?> removeResourceAccessPredicate(@RequestBody ResourceAccessPredicate resourceAccessPredicate) {
        try {
            this.authorizationManagerService.removeAccessPredicate(resourceAccessPredicate);
            SuccessResponse<String> response = new SuccessResponse<>("Removed access predicate successfully.", resourceAccessPredicate.toString());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Failed to remove resource. " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping(path = "/principals/group-members", produces = "application/json")
    public ResponseEntity<?> fetchGroupMembers(@RequestParam String principalId, @RequestParam(required = false) String isFlat) {
        try {
            List<Principal> membersList;
            /* TODO: Add check if it is a valid group */
            Principal group = Principal.builder()
                    .name(principalId)
                    .principalType(PrincipalType.Group)
                    .build();

            if (isFlat != null && isFlat.equals("true")) {
                membersList = authorizationManagerService.getFlatMembersList(group);
            } else {
                membersList = authorizationManagerService.getMembersList(group);
            }

            SuccessResponse<List<Principal>> response = new SuccessResponse<>("Fetched group members successfully.", membersList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Failed to fetch group members. " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping(path = "/principals/group-members", produces = "application/json")
    public ResponseEntity<?> addGroupMember(@RequestBody AddGroupDTO addGroupDto) {
        try {
            /* TODO: Add check if it is a valid group */
            Principal group = Principal.builder()
                    .name(addGroupDto.groupName())
                    .principalType(PrincipalType.Group)
                    .build();

            Principal member = Principal.builder()
                    .name(addGroupDto.memberName())
                    .build();

            this.authorizationManagerService.addGroupMember(group, member);
            SuccessResponse<String> response = new SuccessResponse<>("Added group member successfully.", addGroupDto.toString());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Failed to add group member. " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping(path = "/principals/group-members", produces = "application/json")
    public ResponseEntity<?> deleteGroupMember(@RequestBody AddGroupDTO addGroupDto) {
        try {
            /* TODO: Add check if it is a valid group */
            Principal group = Principal.builder()
                    .name(addGroupDto.groupName())
                    .principalType(PrincipalType.Group)
                    .build();

            Principal member = Principal.builder()
                    .name(addGroupDto.memberName())
                    .build();

            this.authorizationManagerService.removeGroupMember(group, member);
            SuccessResponse<String> response = new SuccessResponse<>("Removed group member successfully.", addGroupDto.toString());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Failed to remove group member. " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping(path = "/principals/sub-operation", produces = "application/json")
    public ResponseEntity<?> addSubOperation(@RequestBody AddSubOperationDTO addSubOperationDto) {
        try {
            /* TODO: Add check if it is a valid operation */
            Operation subOperation = Operation.builder()
                    .id(addSubOperationDto.subOperationName())
                    .build();

            Operation parentOperation = Operation.builder()
                    .id(addSubOperationDto.parentOperationName())
                    .build();

            this.authorizationManagerService.addSubOperation(parentOperation, subOperation);
            SuccessResponse<String> response = new SuccessResponse<>("Added sub operation successfully.", addSubOperationDto.toString());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Failed to add sub operation. " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping(path = "/principals/sub-operation", produces = "application/json")
    public ResponseEntity<?> removeSubOperation(@RequestBody AddSubOperationDTO addSubOperationDto) {
        try {
            /* TODO: Add check if it is a valid operation */
            Operation subOperation = Operation.builder()
                    .id(addSubOperationDto.subOperationName())
                    .build();

            Operation parentOperation = Operation.builder()
                    .id(addSubOperationDto.parentOperationName())
                    .build();

            this.authorizationManagerService.removeSubOperation(parentOperation, subOperation);
            SuccessResponse<String> response = new SuccessResponse<>("Removed sub operation successfully.", addSubOperationDto.toString());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Failed to remove sub operation. " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping(path = "/principals/sub-resource", produces = "application/json")
    public ResponseEntity<?> addSubResource(@RequestBody AddSubResourceDTO addSubOperationDto) {
        try {
            /* TODO: Add check if it is a valid resource */
            Resource subResource = Resource.builder()
                    .id(addSubOperationDto.subResourceName())
                    .build();

            Resource parentResource = Resource.builder()
                    .id(addSubOperationDto.parentResourceName())
                    .build();

            this.authorizationManagerService.addSubResource(parentResource, subResource);
            SuccessResponse<String> response = new SuccessResponse<>("Added sub resource successfully.", addSubOperationDto.toString());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Failed to add sub resource. " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping(path = "/principals/sub-resource", produces = "application/json")
    public ResponseEntity<?> removeSubResource(@RequestBody AddSubResourceDTO addSubOperationDto) {
        try {
            /* TODO: Add check if it is a valid resource */
            Resource subResource = Resource.builder()
                    .id(addSubOperationDto.subResourceName())
                    .build();

            Resource parentResource = Resource.builder()
                    .id(addSubOperationDto.parentResourceName())
                    .build();

            this.authorizationManagerService.removeSubResource(parentResource, subResource);
            SuccessResponse<String> response = new SuccessResponse<>("Added sub resource successfully.", addSubOperationDto.toString());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Failed to add sub resource. " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
