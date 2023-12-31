package com.chistadata.authorizationframework.models;

import java.sql.SQLException;
import java.util.List;

public interface IAuthorizationManager {
    // CRUD for principals
    void addOrUpdate(Principal pr) throws Exception;
    void removePrincipal(String id) throws Exception;

    // CRUD for operations
    void addOrUpdate(Operation pr) throws Exception;
    void removeOperation(String id) throws Exception;

    // CRUD for resources
    void addOrUpdate(Resource resource) throws Exception;
    void removeResource(String id) throws Exception;

    // These two methods are for maintaining the ACL
    void addAccessPredicate(ResourceAccessPredicate resourceAccessPredicate) throws Exception;
    void removeAccessPredicate(ResourceAccessPredicate resourceAccessPredicate) throws Exception;

    // These are to maintain the hierarchy of principal, operation, and resources
    void addGroupMember(Principal group, Principal member) throws Exception;
    void removeGroupMember(Principal group, Principal member) throws Exception;
    void addSubOperation(Operation parent, Operation subOperation) throws Exception;
    void removeSubOperation(Operation parent, Operation subOperation) throws Exception;
    void addSubResource(Resource resource, Resource subResource) throws Exception;
    void removeSubResource(Resource resource, Resource subResource) throws Exception;

    // These are for listing purposes
    List<Principal> getPrincipalList() throws SQLException;
    List<Principal> getPrincipalList(PrincipalType type) throws SQLException;
    List<Principal> getMembersList(Principal pr) throws SQLException;
    List<Principal> getFlatMembersList(Principal pr) throws SQLException;

    List<Operation> getOperationList() throws SQLException;
    List<Operation> getSubOperationsList(Operation op) throws SQLException;
    List<Operation> getFlatSubOperationsList(Operation op) throws SQLException;

    List<Resource> getResourcesList() throws SQLException;
    List<Resource> getSubResourcesList(Resource resource) throws SQLException;
    List<Resource> getFlatSubResourcesList(Resource resource) throws SQLException;
}
