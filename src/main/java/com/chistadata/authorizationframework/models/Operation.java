package com.chistadata.authorizationframework.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Builder
public record Operation(
        String id,
        String name,
        String description

) implements Serializable { }
