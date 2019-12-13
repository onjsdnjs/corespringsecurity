package io.security.corespringsecurity.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourcesDto{

    private String resourceName;
    private String httpMethod;
    private int orderNum;
    private String resourceType;
    private String roleName;

}
