package com.chistadata.authorizationframework.services;

import com.chistadata.authorizationframework.models.EdgeSource;
import com.chistadata.authorizationframework.models.IAuthorizationManager;
import com.chistadata.authorizationframework.models.Principal;
import com.chistadata.authorizationframework.models.Operation;
import com.chistadata.authorizationframework.models.PrincipalType;
import com.chistadata.authorizationframework.models.Resource;
import com.chistadata.authorizationframework.models.ResourceAccessPredicate;
import com.chistadata.authorizationframework.utils.CachedRowSetPrinter;
import com.chistadata.authorizationframework.utils.DatabaseHelper;
import org.springframework.stereotype.Service;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service("DatabaseAuthorizationManagerService")
public class DatabaseAuthorizationManagerService implements IAuthorizationManager {

    @Override
    public void addOrUpdate(Principal pr) throws Exception {
        Object[] parameterValues = {
                pr.name(),
                pr.principalType().ordinal(),
                pr.displayName(),
                pr.email(),
                pr.description(),
                pr.dataSource()
        };
        DatabaseHelper.executeStoredProcedure("call \"AddOrUpdatePrincipal\"(?,?,?,?,?,?)", parameterValues);
    }

    @Override
    public void removePrincipal(String id) throws Exception {
        Object[] parameterValues = {
                id
        };
        DatabaseHelper.executeStoredProcedure("call \"RemovePrincipal\"(?)", parameterValues);
    }

    @Override
    public void addOrUpdate(Operation op) throws Exception {
        Object[] parameterValues = {
                op.id(),
                op.name(),
                op.description()
        };
        DatabaseHelper.executeStoredProcedure("call \"AddOrUpdateOperation\"(?,?,?)", parameterValues);
    }

    @Override
    public void removeOperation(String id) throws Exception {
        Object[] parameterValues = {
                id
        };
        DatabaseHelper.executeStoredProcedure("call \"RemoveOperation\"(?)", parameterValues);
    }


    @Override
    public void addOrUpdate(Resource resource) throws Exception {
        Object[] parameterValues = {
                resource.id(),
                resource.name()
        };
        DatabaseHelper.executeStoredProcedure("call \"AddOrUpdateResource\"(?,?)", parameterValues);
    }

    @Override
    public void removeResource(String id) throws Exception {
        Object[] parameterValues = {
                id
        };
        DatabaseHelper.executeStoredProcedure("call \"RemoveResource\"(?)", parameterValues);
    }

    @Override
    public void addAccessPredicate(ResourceAccessPredicate resourceAccessPredicate) throws Exception {
        Object[] parameterValues = {
                resourceAccessPredicate.principalId(),
                resourceAccessPredicate.operationId(),
                resourceAccessPredicate.resourceId(),
                resourceAccessPredicate.predicateType().ordinal()
        };
        DatabaseHelper.executeStoredProcedure("call \"AddAccessPredicate\"(?,?,?,?)", parameterValues);
    }

    @Override
    public void removeAccessPredicate(ResourceAccessPredicate resourceAccessPredicate) throws Exception {
        Object[] parameterValues = {
                resourceAccessPredicate.principalId(),
                resourceAccessPredicate.operationId(),
                resourceAccessPredicate.resourceId(),
                resourceAccessPredicate.predicateType().ordinal()
        };
        DatabaseHelper.executeStoredProcedure("call \"RemoveAccessPredicate\"(?,?,?,?)", parameterValues);
    }

    @Override
    public void addGroupMember(Principal group, Principal member) throws Exception {
        if (group.principalType() != PrincipalType.Group) {
            throw new IllegalArgumentException("Only groups may have members");
        }
        Object[] parameterValues = {
                member.name(),
                group.name(),
                EdgeSource.Principal.toString()
        };
        DatabaseHelper.executeStoredProcedure("call \"AddEdgeWithSpaceSavings\"(?,?,?)", parameterValues);
    }

    @Override
    public void removeGroupMember(Principal group, Principal member) throws Exception {
        Object[] parameterValues = {
                member.name(),
                group.name(),
                EdgeSource.Principal.toString()
        };
        DatabaseHelper.executeStoredProcedure("call \"RemoveEdgeWithSpaceSavings\"(?,?,?)", parameterValues);
    }

    @Override
    public void addSubOperation(Operation parent, Operation subOperation) throws Exception {
        Object[] parameterValues = {
                subOperation.id(),
                parent.id(),
                EdgeSource.Operation.toString()
        };
        DatabaseHelper.executeStoredProcedure("call \"AddEdgeWithSpaceSavings\"(?,?,?)", parameterValues);
    }

    @Override
    public void removeSubOperation(Operation parent, Operation subOperation) throws Exception {
        Object[] parameterValues = {
                subOperation.id(),
                parent.id(),
                EdgeSource.Operation.toString()
        };
        DatabaseHelper.executeStoredProcedure("call \"RemoveEdgeWithSpaceSavings\"(?,?,?)", parameterValues);
    }

    @Override
    public void addSubResource(Resource resource, Resource subResource) throws Exception {
        Object[] parameterValues = {
                subResource.id(),
                resource.id(),
                EdgeSource.Resource.toString()
        };
        DatabaseHelper.executeStoredProcedure("call \"AddEdgeWithSpaceSavings\"(?,?,?)", parameterValues);
    }

    @Override
    public void removeSubResource(Resource resource, Resource subResource) throws Exception {
        Object[] parameterValues = {
                subResource.id(),
                resource.id(),
                EdgeSource.Resource.toString()
        };
        DatabaseHelper.executeStoredProcedure("call \"RemoveEdgeWithSpaceSavings\"(?,?,?)", parameterValues);
    }

    @Override
    public List<Principal> getPrincipalList() throws SQLException {
        CachedRowSet result = DatabaseHelper.executeQuery("SELECT * FROM \"GetPrincipalList\"()");
        return buildPrincipalList(result);
    }

    @Override
    public List<Principal> getPrincipalList(PrincipalType type) throws SQLException {
        Object[] parameterValues = {
                type.ordinal()
        };
        CachedRowSet result = DatabaseHelper.executeQuery("SELECT * FROM \"GetPrincipalList\"(?)", parameterValues);
        return buildPrincipalList(result);
    }

    @Override
    public List<Principal> getMembersList(Principal pr) throws SQLException {
        Object[] parameterValues = {
                pr.name(),
                0
        };
        CachedRowSet result = DatabaseHelper.executeQuery("SELECT * FROM \"GetMembersList\"(?,?)", parameterValues);
        return buildPrincipalList(result);
    }

    @Override
    public List<Principal> getFlatMembersList(Principal pr) throws SQLException {
        Object[] parameterValues = {
                pr.name(),
                1
        };
        CachedRowSet result = DatabaseHelper.executeQuery("SELECT * FROM \"GetMembersList\"(?,?)", parameterValues);
        return buildPrincipalList(result);
    }

    @Override
    public List<Operation> getOperationList() throws SQLException {
        CachedRowSet result = DatabaseHelper.executeQuery("SELECT * FROM \"GetOperationList\"()");
        return buildOperationList(result);
    }

    @Override
    public List<Operation> getSubOperationsList(Operation op) throws SQLException {
        Object[] parameterValues = {
                op.id(),
                0
        };
        CachedRowSet result = DatabaseHelper.executeQuery("SELECT * FROM \"GetSubOperationsList\"(?,?)", parameterValues);
        return buildOperationList(result);
    }

    @Override
    public List<Operation> getFlatSubOperationsList(Operation op) throws SQLException {
        Object[] parameterValues = {
                op.id(),
                1
        };
        CachedRowSet result = DatabaseHelper.executeQuery("SELECT * FROM \"GetSubOperationsList\"(?,?)", parameterValues);
        return buildOperationList(result);
    }

    @Override
    public List<Resource> getResourcesList() throws SQLException {
        CachedRowSet result = DatabaseHelper.executeQuery("SELECT * FROM \"GetResourcesList\"()");
        return buildResourceList(result);
    }

    @Override
    public List<Resource> getSubResourcesList(Resource resource) throws SQLException {
        Object[] parameterValues = {
                resource.id(),
                0
        };
        CachedRowSet result = DatabaseHelper.executeQuery("SELECT * FROM \"GetSubResourcesList\"(?,?)", parameterValues);
        return buildResourceList(result);
    }

    @Override
    public List<Resource> getFlatSubResourcesList(Resource resource) throws SQLException {
        Object[] parameterValues = {
                resource.id(),
                1
        };
        CachedRowSet result = DatabaseHelper.executeQuery("SELECT * FROM \"GetSubResourcesList\"(?,?)", parameterValues);
        return buildResourceList(result);
    }

    private List<Principal> buildPrincipalList(CachedRowSet cachedRowSet) throws SQLException {
        cachedRowSet.beforeFirst();
        List<Principal> result = new ArrayList<>();

        if (!cachedRowSet.next()) {
            return result;
        }

        do {
            Principal pr = constructPrincipal(cachedRowSet);
            result.add(pr);
        } while (cachedRowSet.next());

        return result;
    }

    private Principal constructPrincipal(CachedRowSet cachedRowSet) throws SQLException {
        return Principal.builder()
                .name(cachedRowSet.getString(1))
                .displayName(cachedRowSet.getString(2))
                .principalType(PrincipalType.values()[cachedRowSet.getInt(3)])
                .email(cachedRowSet.getString(4))
                .build();
    }


    private List<Operation> buildOperationList(CachedRowSet cachedRowSet) throws SQLException {
        cachedRowSet.beforeFirst();
        List<Operation> result = new ArrayList<>();

        if (!cachedRowSet.next()) {
            return result;
        }

        do {
            Operation op = constructOperation(cachedRowSet);
            result.add(op);
        } while (cachedRowSet.next());

        return result;
    }

    private List<Resource> buildResourceList(CachedRowSet cachedRowSet) throws SQLException {
        cachedRowSet.beforeFirst();
        List<Resource> result = new ArrayList<>();

        if (!cachedRowSet.next()) {
            return result;
        }

        do {
            Resource resource = constructResource(cachedRowSet);
            result.add(resource);
        } while (cachedRowSet.next());

        return result;
    }

    private Operation constructOperation(CachedRowSet cachedRowSet) throws SQLException {
        return Operation.builder()
                .id(cachedRowSet.getString(1))
                .name(cachedRowSet.getString(2))
                .description(cachedRowSet.getString(3))
                .build();
    }

    private Resource constructResource(CachedRowSet cachedRowSet) throws SQLException {
        return Resource.builder()
                .id(cachedRowSet.getString(1))
                .name(cachedRowSet.getString(2))
                .build();
    }
}

