//package com.miworld.mi.auth.service;
//
//
//import com.miworld.mi.auth.model.ClientSystemDetails;
//import com.miworld.mi.auth.repository.ClientDetailsRepository;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import static com.miworld.mi.auth.model.Constents.*;
//
//import javax.annotation.PostConstruct;
//import java.util.*;
//
//@Service
//public class ClientService {
//
//    private final Logger log = LoggerFactory.getLogger(this.getClass());
//    public static final String SEQ_NAME = "client_seq";
//
//    @Autowired
//    ClientDetailsRepository clientDetailsRepository;
//
//    @Autowired
//    SequenceService sequenceService;
//
//    @Lazy
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//    public void save(ClientSystemDetails clientSystemDetails) {
//        log.info("Saving Client Details {}", clientSystemDetails);
//        ClientSystemDetails client = getClientId(clientSystemDetails.getClientId());
//        if(clientSystemDetails.getId() == null) {
//            if(client != null) throw new RuntimeException("Client Already Exist !");
//            clientSystemDetails.setId(sequenceService.getNextSequenceId(SEQ_NAME));
//        }
//        clientDetailsRepository.save(clientSystemDetails);
//    }
//
//    public ClientSystemDetails getClientById(Long id) {
//        Optional<ClientSystemDetails> user = clientDetailsRepository.findById(id);
//        return user.orElse(null);
//    }
//
//    public ClientSystemDetails getClientId(String clientId) {
//        return clientDetailsRepository.findByClientId(clientId).orElse(null);
//    }
//
//    public void delete(Long id){
//        clientDetailsRepository.deleteById(id);
//    }
//
//
//    @PostConstruct
//    public void init(){
//        ClientSystemDetails  clientSystemDetails = this.getClientId("web-client");
//        if(clientSystemDetails == null){
//            clientSystemDetails = ClientSystemDetails.builder()
//                    .resourceIds(new HashSet<>(Arrays.asList("resource-server-rest-api","user-service-api","lessonplan-service-api")))
//                    .scope(new HashSet<>(Arrays.asList(SCOPE_READ, SCOPE_WRITE, SERVER)))
//                    .authorizedGrantTypes(new HashSet<>(Arrays.asList(GRANT_TYPE_PASSWORD, AUTHORIZATION_CODE, REFRESH_TOKEN, IMPLICIT, CLIENT_CREDENTIALS)))
//                    .authorities(Collections.emptyList())
//                    .clientId("web-client")
//                    .clientSecret(passwordEncoder.encode("web-secret"))
//                    .accessTokenValiditySeconds(3600)
//                    .refreshTokenValiditySeconds(3600)
//                    .build();
//            this.save(clientSystemDetails);
//            log.info("LOADED clientSystemDetails {} ", clientSystemDetails);
//        }else {
//
//            log.info("SKIP To Add default client clientSystemDetails");
//        }
//    }
//}
