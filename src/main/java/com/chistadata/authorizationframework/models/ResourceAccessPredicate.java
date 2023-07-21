package com.chistadata.authorizationframework.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Builder
public record ResourceAccessPredicate(
        String principalId,
        String operationId,
        String resourceId,
        ResourceAccessPredicateType predicateType

) implements Serializable { }