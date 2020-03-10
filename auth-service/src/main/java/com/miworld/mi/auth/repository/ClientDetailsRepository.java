package com.miworld.mi.auth.repository;

import com.miworld.mi.auth.model.ClientSystemDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientDetailsRepository extends MongoRepository<ClientSystemDetails, Long> {

    public Optional<ClientSystemDetails> findByClientId(String clientId);
}
