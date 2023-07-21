package com.chistadata.authorizationframework.models;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
public record Principal(
        String name,
        String email,
        String displayName,
        String description,
        String dataSource,
        PrincipalType principalType
) implements Serializable { }

