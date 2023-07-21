package com.chistadata.authorizationframework.services;

import com.chistadata.authorizationframework.models.IAuthorizationManager;
import com.chistadata.authorizationframework.models.Operation;
import com.chistadata.authorizationframework.models.Principal;
import com.chistadata.authorizationframework.models.PrincipalType;
import com.chistadata.authorizationframework.models.Resource;
import com.chistadata.authorizationframework.models.ResourceAccessPredicate;
import com.chistadata.authorizationframework.utils.AfcasAccessor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

//@Service("commandLineAuthorizationManagerService")
public class CommandLineAuthorizationManagerService implements IAuthorizationManager {

    AfcasAccessor afcasAccessor;

    public CommandLineAuthorizationManagerService(AfcasAccessor afcasAccessor) {
        this.afcasAccessor = afcasAccessor;
    }

    @Override
    public void addOrUpdate(Principal principal) throws Exception {
        String result = this.afcasAccessor.executeCommand("add principal \"%s\" \"%s\" \"%s\" \"%s\" \"%s\" \"%s\"".formatted(
                principal.name(),
                principal.principalType(),
                principal.displayName(),
                principal.email(),
                principal.description(),
                principal.dataSource()
        ));

        if (!(result.contains("Added Principal") && result.contains("successfully"))) {
            throw new Exception(result);
        }
    }

    @Override
    public void removePrincipal(String id) throws Exception {
        String result = this.afcasAccessor.executeCommand("remove principal \"%s\"".formatted(
                id
        ));

        if (!(result.contains("Removed Principal") && result.contains("successfully"))) {
            throw new Exception(result);
        }
    }

    @Override
    public void addOrUpdate(Operation operation) throws Exception {
        String result = this.afcasAccessor.executeCommand("add operation \"%s\" \"%s\" \"%s\"".formatted(
                operation.id(),
                operation.name(),
                operation.description()
        ));

        if (!(result.contains("Added Operation") && result.contains("successfully"))) {
            throw new Exception(result);
        }
    }

    @Override
    public void removeOperation(String id) throws Exception {
        String result = this.afcasAccessor.executeCommand("remove operation \"%s\"".formatted(
                id
        ));

        if (!(result.contains("Removed Operation") && result.contains("successfully"))) {
            throw new Exception(result);
        }
    }

    @Override
    public void addOrUpdate(Resource resource) throws Exception {
        String result = this.afcasAccessor.executeCommand("add resource \"%s\" \"%s\"".formatted(
                resource.id(),
                resource.name()
        ));

        if (!(result.contains("Added Resource") && result.contains("successfully"))) {
            throw new Exception(result);
        }
    }

    @Override
    public void removeResource(String id) throws Exception {
        String result = this.afcasAccessor.executeCommand("remove resource \"%s\"".formatted(
                id
        ));

        if (!(result.contains("Removed Resource") && result.contains("successfully"))) {
            throw new Exception(result);
        }
    }

    @Override
    public void addAccessPredicate(ResourceAccessPredicate resourceAccessPredicate) throws Exception {
        String result = this.afcasAccessor.executeCommand("add permission \"%s\" \"%s\" \"%s\" \"%s\"".formatted(
                resourceAccessPredicate.principalId(),
                resourceAccessPredicate.operationId(),
                resourceAccessPredicate.resourceId(),
                resourceAccessPredicate.predicateType()
        ));

        if (!result.contains("Added permission successfully")) {
            throw new Exception(result);
        }
    }

    @Override
    public void removeAccessPredicate(ResourceAccessPredicate resourceAccessPredicate) throws Exception {
        String result = this.afcasAccessor.executeCommand("remove permission \"%s\" \"%s\" \"%s\" \"%s\"".formatted(
                resourceAccessPredicate.principalId(),
                resourceAccessPredicate.operationId(),
                resourceAccessPredicate.resourceId(),
                resourceAccessPredicate.predicateType()
        ));

        if (!(result.contains("Removed permission") && result.contains("successfully"))) {
            throw new Exception(result);
        }
    }

    @Override
    public void addGroupMember(Principal group, Principal member) throws Exception {

    }

    @Override
    public void removeGroupMember(Principal group, Principal member) throws Exception {

    }

    @Override
    public void addSubOperation(Operation parent, Operation subOperation) throws Exception {

    }

    @Override
    public void removeSubOperation(Operation parent, Operation subOperation) throws Exception {

    }

    @Override
    public void addSubResource(Resource resource, Resource subResource) throws Exception {

    }

    @Override
    public void removeSubResource(Resource resource, Resource subResource) throws Exception {

    }

    @Override
    public List<Principal> getPrincipalList() throws SQLException {
        return null;
    }

    @Override
    public List<Principal> getPrincipalList(PrincipalType type) throws SQLException {
        return null;
    }

    @Override
    public List<Principal> getMembersList(Principal pr) throws SQLException {
        return null;
    }

    @Override
    public List<Principal> getFlatMembersList(Principal pr) throws SQLException {
        return null;
    }

    @Override
    public List<Operation> getOperationList() throws SQLException {
        return null;
    }

    @Override
    public List<Operation> getSubOperationsList(Operation op) throws SQLException {
        return null;
    }

    @Override
    public List<Operation> getFlatSubOperationsList(Operation op) throws SQLException {
        return null;
    }

    @Override
    public List<Resource> getResourcesList() throws SQLException {
        return null;
    }

    @Override
    public List<Resource> getSubResourcesList(Resource resource) throws SQLException {
        return null;
    }

    @Override
    public List<Resource> getFlatSubResourcesList(Resource resource) throws SQLException {
        return null;
    }
}
