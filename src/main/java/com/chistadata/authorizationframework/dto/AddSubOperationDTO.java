package com.chistadata.authorizationframework.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record AddSubOperationDTO(
        String parentOperationName,
        String subOperationName
) implements Serializable { }