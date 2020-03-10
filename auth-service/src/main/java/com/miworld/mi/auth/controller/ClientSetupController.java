//package com.miworld.mi.auth.controller;
//
//import com.miworld.mi.auth.model.ClientSystemDetails;
//import com.miworld.mi.auth.model.User;
//import com.miworld.mi.auth.service.ClientService;
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
//@RequestMapping(value= "/clients", produces="application/json;charset=UTF-8")
//public class ClientSetupController {
//    private final Logger log = LoggerFactory.getLogger(this.getClass());
//
//    @Autowired
//    ClientService clientService;
//
//    //@PreAuthorize("hasRole('ADMIN')")
//    @PostMapping
//    public ResponseEntity create(@RequestBody ClientSystemDetails clientSystemDetails) {
//        log.debug("Creating ClientSystemDetails.{}", clientSystemDetails);
//        clientService.save(clientSystemDetails);
//        return ResponseEntity.ok().build();
//    }
//
//    //@PreAuthorize("hasRole('ADMIN')")
//    @PutMapping
//    public ResponseEntity update(@RequestBody ClientSystemDetails clientSystemDetails) {
//        log.debug("Updating ClientSystemDetails.");
//        clientService.save(clientSystemDetails);
//        return ResponseEntity.ok().build();
//    }
//
//    //@PreAuthorize("hasRole('USER')")
//    @ResponseBody
//    @GetMapping(value = "/{clientId}")
//    public ResponseEntity<ClientSystemDetails> get(@PathVariable(name = "clientId") String clientId) {
//        log.debug("Getting Client By id: {}", clientId);
//        return ResponseEntity.ok().body(clientService.getClientId(clientId));
//    }
//
//    @DeleteMapping(value = "/{id}")
//    public ResponseEntity delete(@PathVariable(name = "id") Long id) {
//        log.debug("Deleting User By id: {}", id);
//        clientService.delete(id);
//        return ResponseEntity.ok().build();
//    }
//
//}
