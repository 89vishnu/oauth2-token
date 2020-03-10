//package com.miworld.mi.auth.service;
//
//
//import com.miworld.mi.auth.model.Role;
//import com.miworld.mi.auth.model.User;
//import com.miworld.mi.auth.repository.UserRepository;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.PostConstruct;
//import java.util.*;
//
//@Service
//public class UserService {
//
//    private final Logger log = LoggerFactory.getLogger(this.getClass());
//    public static final String SEQ_NAME = "users_seq";
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    SequenceService sequenceService;
//
//    @Lazy
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//    public void saveUser(User user) {
//        log.info("Saving user {}", user);
//        User eUser = getUser(user.getUsername());
//        if(user.getId() == null) {
//            if(eUser != null) throw new RuntimeException("User Already Exist !");
//            user.setId(sequenceService.getNextSequenceId(SEQ_NAME));
//            user.setCreatedOn(new Date());
//            user.setPassword(passwordEncoder.encode(user.getPassword()));
//        }else {
//            user.setPassword(eUser.getPassword());
//        }
//
//        userRepository.save(user);
//    }
//
//    public User getUserById(Long id) {
//        Optional<User> user = userRepository.findById(id);
//        return user.orElse(null);
//    }
//
//    public User getUser(String username) {
//        return userRepository.findByUsername(username);
//    }
//
//    public void deleteUser(Long id){
//        userRepository.deleteById(id);
//    }
//
//    public List<User> getApprovers(){
//        return userRepository.findByApprover(Arrays.asList("HM","HOD"));
//    }
//
//    @PostConstruct
//    public void init(){
//        User user = this.getUser("admin");
//        if(user == null){
//            user = new User();
//            user.setFirstName("Admin");
//            user.setLastName("Admin");
//            user.setUsername("admin");
//            user.setPassword("admin");
//            user.setMi(true);
//         //   user.setSchool("");
//            user.setTitle("Head Teacher");
//            user.setRoles(new ArrayList<>());
//            user.getRoles().add(Role.builder().code("HM").description("Head Teacher").build());
//            log.info("Loading USER {} ", user);
//            this.saveUser(user);
//            log.info("LOADED USER {} ", user);
//        }else {
//
//            log.info("SKIP LOADING USER");
//        }
//    }
//}
