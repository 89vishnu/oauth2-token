package com.miworld.mi.auth.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Role {
    private String code;
    private String description;
}
