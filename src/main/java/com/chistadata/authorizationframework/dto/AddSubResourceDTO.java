package com.chistadata.authorizationframework.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record AddSubResourceDTO(
        String parentResourceName,
        String subResourceName
) implements Serializable { }