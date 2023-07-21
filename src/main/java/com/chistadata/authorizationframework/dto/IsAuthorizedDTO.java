package com.chistadata.authorizationframework.dto;

import lombok.Builder;
import java.io.Serializable;

@Builder
public record IsAuthorizedDTO(
        String principalId,
        String operationId,
        String resourceId
) implements Serializable { };
