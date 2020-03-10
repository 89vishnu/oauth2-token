package com.miworld.mi.auth.config.service.security;

import com.miworld.mi.auth.model.ClientSystemDetails;
import com.miworld.mi.auth.repository.ClientDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/*this is important*/

@Component
public class ClientAuthanticationDetailsService implements ClientDetailsService {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientDetailsRepository clientDetailsRepository;


    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {

        System.out.println("333333333333333333333333333333 ClientAuthanticationDetailsService->loadClientByClientId");

        //ClientSystemDetails.builder()
        //                .resourceIds(new HashSet<>(Arrays.asList("resource-server-rest-api","user-service-api","lessonplan-service-api")))
        //                .scope(new HashSet<>(Arrays.asList(SCOPE_READ, SCOPE_WRITE, SERVER)))
        //                .authorizedGrantTypes(new HashSet<>(Arrays.asList(GRANT_TYPE_PASSWORD, AUTHORIZATION_CODE, REFRESH_TOKEN, IMPLICIT, CLIENT_CREDENTIALS)))
        //                .authorities(Collections.emptyList())
        //                .clientId("web-client")
        //                .clientSecret(passwordEncoder.encode("web-secret"))
        //                .accessTokenValiditySeconds(3600)
        //                .refreshTokenValiditySeconds(3600)
        //                .build()

        ClientSystemDetails client = clientDetailsRepository.findByClientId(clientId).orElse(null);

        if(client == null){
            throw new ClientRegistrationException("Client Does not exist");
        }

        String resourceIds = client.getResourceIds().stream().collect(Collectors.joining(","));
        String scopes = client.getScope().stream().collect(Collectors.joining(","));
        String grantTypes = client.getAuthorizedGrantTypes().stream().collect(Collectors.joining(","));
        String authorities = client.getAuthorities().stream().collect(Collectors.joining(","));

        BaseClientDetails base = new BaseClientDetails(client.getClientId(), resourceIds, scopes, grantTypes, authorities);
        base.setClientSecret(client.getClientSecret());
        base.setAccessTokenValiditySeconds(client.getAccessTokenValiditySeconds());
        base.setRefreshTokenValiditySeconds(client.getRefreshTokenValiditySeconds());
        //base.setAdditionalInformation(client.getAdditionalInformation());
        base.setAutoApproveScopes(client.getScope());
        return base;
    }
}
