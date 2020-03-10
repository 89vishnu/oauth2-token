package com.miworld.mi.auth.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientSystemDetails {

    @Id
    @NotNull
    private Long id;

    @NotNull
    @Indexed(unique = true)
    private String clientId;
    private String clientSecret;
    private Set<String> resourceIds = new HashSet<>();
    private boolean secretRequired;
    private boolean scoped;
    private Set<String> scope = new HashSet<>();
    private Set<String> authorizedGrantTypes = new HashSet<>();
    private Set<String> registeredRedirectUri = new HashSet<>();
    private Collection<String> authorities = new HashSet<>();
    private Integer accessTokenValiditySeconds;
    private  Integer refreshTokenValiditySeconds;
    private boolean autoApprove;
   // private Map<String, Object> additionalInformation = new HashMap<>();
}
