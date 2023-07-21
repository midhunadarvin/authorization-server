package com.chistadata.authorizationframework.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record IsMemberOfDTO(
        String groupId,
        String memberId
) implements Serializable { };
