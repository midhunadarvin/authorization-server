package com.chistadata.authorizationframework.services;

import com.chistadata.authorizationframework.models.EdgeSource;
import com.chistadata.authorizationframework.models.IAuthorizationProvider;
import com.chistadata.authorizationframework.models.Operation;
import com.chistadata.authorizationframework.models.Resource;
import com.chistadata.authorizationframework.models.ResourceAccessPredicate;
import com.chistadata.authorizationframework.models.ResourceAccessPredicateType;
import com.chistadata.authorizationframework.utils.DatabaseHelper;
import org.springframework.stereotype.Service;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service("DatabaseAuthorizationProviderService")
public class DatabaseAuthorizationProviderService implements IAuthorizationProvider {
    @Override
    public boolean isAuthorized(String principalId, String operationId, String resourceId) throws SQLException {
        if (principalId == null || principalId.isEmpty()) {
            throw new IllegalArgumentException("principalId");
        }

        if (operationId == null || operationId.isEmpty()) {
            throw new IllegalArgumentException("operationId");
        }

        if (resourceId == null) {
            throw new IllegalArgumentException("resourceId");
        }

        Object[] parameterValues = {
                principalId,
                operationId,
                resourceId
        };

        return (boolean) DatabaseHelper.executeScalar("SELECT * FROM \"IsAuthorized\"(?, ?, ?)", parameterValues);
    }

    @Override
    public boolean isMemberOf(String groupId, String memberId) throws SQLException {
        if (groupId == null || groupId.isEmpty()) {
            throw new IllegalArgumentException("groupId");
        }

        if (memberId == null || memberId.isEmpty()) {
            throw new IllegalArgumentException("memberId");
        }

        Object[] parameterValues = {
                memberId,
                groupId,
                EdgeSource.Principal.toString()
        };

        return (boolean) DatabaseHelper.executeScalar("SELECT * FROM \"EdgeExists\"(?, ?, ?)", parameterValues);
    }

    @Override
    public boolean isSubOperation(String operationId, String subOperationId) throws SQLException {
        if (operationId == null || operationId.isEmpty()) {
            throw new IllegalArgumentException("operationId");
        }

        if (subOperationId == null || subOperationId.isEmpty()) {
            throw new IllegalArgumentException("subOperationId");
        }

        Object[] parameterValues = {
                subOperationId,
                operationId,
                EdgeSource.Operation.toString()
        };

        return (boolean) DatabaseHelper.executeScalar("SELECT * FROM \"EdgeExists\"(?, ?, ?)", parameterValues);
    }

    @Override
    public boolean isSubResource(String resourceId, String subResourceId) throws SQLException {
        if (resourceId == null || resourceId.isEmpty()) {
            throw new IllegalArgumentException("resource");
        }

        if (subResourceId == null || subResourceId.isEmpty()) {
            throw new IllegalArgumentException("subResource");
        }

        Object[] parameterValues = {
                subResourceId,
                resourceId,
                EdgeSource.Resource.toString()
        };

        return (boolean) DatabaseHelper.executeScalar("SELECT * FROM \"EdgeExists\"(?, ?, ?)", parameterValues);
    }

    @Override
    public List<ResourceAccessPredicate> getAuthorizationDigest(String principalId) throws SQLException {
        if (principalId == null || principalId.isEmpty()) {
            throw new IllegalArgumentException("principalId");
        }

        Object[] parameterValues = {
                principalId
        };

        CachedRowSet result = DatabaseHelper.executeQuery("SELECT * FROM \"GetAuthorizationDigest\"(?)", parameterValues);
        List<ResourceAccessPredicate> resourceAccessPredicateList = new ArrayList<>();
        if (result.next()) {
            do {
                String operationName = result.getString(1);
                String resourceName = result.getString(2);
                int accessPredicateType = result.getInt(3);

                ResourceAccessPredicate accessPredicate = ResourceAccessPredicate.builder()
                        .principalId(principalId)
                        .operationId(operationName)
                        .resourceId(resourceName)
                        .predicateType(ResourceAccessPredicateType.values()[accessPredicateType])
                        .build();
                resourceAccessPredicateList.add(accessPredicate);
            } while (result.next());
        }
        return resourceAccessPredicateList;
    }

    @Override
    public List<Operation> getAuthorizedOperations(String principalId, String resourceId) throws SQLException {
        if (principalId == null || principalId.isEmpty()) {
            throw new IllegalArgumentException("principalId");
        }

        if (resourceId == null || resourceId.isEmpty()) {
            throw new IllegalArgumentException("resourceId");
        }

        Object[] parameterValues = {
                principalId,
                resourceId
        };

        CachedRowSet result = DatabaseHelper.executeQuery("SELECT * FROM \"GetAuthorizedOperations\"(?,?)", parameterValues);
        List<Operation> operationList = new ArrayList<>();
        if (result.next()) {
            do {
                Operation operation = Operation.builder()
                        .id(result.getString(1))
                        .name(result.getString(1))
                        .build();
                operationList.add(operation);
            } while (result.next());
        }
        return operationList;
    }

    @Override
    public List<Resource> getAuthorizedResources(String principalId, String operationId) throws SQLException {
        if (principalId == null || principalId.isEmpty()) {
            throw new IllegalArgumentException("principalId");
        }

        if (operationId == null || operationId.isEmpty()) {
            throw new IllegalArgumentException("operationId");
        }

        Object[] parameterValues = {
                principalId,
                operationId
        };

        CachedRowSet result = DatabaseHelper.executeQuery("SELECT * FROM \"GetAuthorizedResources\"(?,?)", parameterValues);
        List<Resource> resourceList = new ArrayList<>();
        if (result.next()) {
            do {
                String resourceName = result.getString(1);

                Resource resource = Resource.builder()
                        .id(resourceName)
                        .name(resourceName)
                        .build();
                resourceList.add(resource);
            } while (result.next());
        }
        return resourceList;
    }
}
