package com.chistadata.authorizationframework.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record AddGroupDTO(
        String groupName,
        String memberName
) implements Serializable { }
