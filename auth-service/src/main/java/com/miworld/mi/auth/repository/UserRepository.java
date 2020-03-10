package com.miworld.mi.auth.repository;

import com.miworld.mi.auth.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, Long> {
    public User findByUsername(String userName);
    public List<User> findByLastName(String lastName);

    @Query(" {'roles.code' : { $in: ?0 }  }")
    List<User> findByApprover(List<String> role);
}
