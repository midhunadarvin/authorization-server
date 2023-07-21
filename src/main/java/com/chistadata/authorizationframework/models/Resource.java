package com.chistadata.authorizationframework.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Builder
public record Resource(
        String id,
        String name

) implements Serializable { }
