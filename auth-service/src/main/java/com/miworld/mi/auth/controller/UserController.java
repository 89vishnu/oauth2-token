//package com.miworld.mi.auth.controller;
//
//import com.miworld.mi.auth.model.User;
//import com.miworld.mi.auth.service.UserService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping(value= "/users", produces="application/json;charset=UTF-8")
//public class UserController {
//    private final Logger log = LoggerFactory.getLogger(this.getClass());
//
//    @Autowired
//    UserService userService;
//
//    //@PreAuthorize("hasRole('ADMIN')")
//    @PostMapping
//    public ResponseEntity create(@RequestBody User user) {
//        log.debug("Creating user.{}", user);
//        userService.saveUser(user);
//        return ResponseEntity.ok().build();
//    }
//
//    //@PreAuthorize("hasRole('ADMIN')")
//    @PutMapping
//    public ResponseEntity update(@RequestBody User user) {
//        log.debug("Updating user.");
//        userService.saveUser(user);
//        return ResponseEntity.ok().build();
//    }
//
//    //@PreAuthorize("hasRole('USER')")
//    @ResponseBody
//    @GetMapping(value = "/{username}")
//    public ResponseEntity<User> get(@PathVariable(name = "username") String username) {
//        log.debug("Getting User By id: {}", username);
//        return ResponseEntity.ok().body(userService.getUser(username));
//    }
//
//    @DeleteMapping(value = "/{id}")
//    public ResponseEntity delete(@PathVariable(name = "id") Long id) {
//        log.debug("Deleting User By id: {}", id);
//        userService.deleteUser(id);
//        return ResponseEntity.ok().build();
//    }
//
//    @ResponseBody
//    @GetMapping(value = "/approvers")
//    public ResponseEntity<List<User>> getApprovers() {
//        return ResponseEntity.ok().body(userService.getApprovers());
//    }
//
//}
