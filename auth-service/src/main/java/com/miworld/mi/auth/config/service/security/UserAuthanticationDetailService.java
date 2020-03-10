package com.miworld.mi.auth.config.service.security;


import com.miworld.mi.auth.model.User;
import com.miworld.mi.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserAuthanticationDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("2222222222222222222222222222 UserAuthanticationDetailService->loadUserByUsername");

        User user = userRepository.findByUsername(username);

        if(user == null)
            throw new UnauthorizedUserException("Unauthorized");

        Collection<GrantedAuthority> grantedAuthorities = user.getRoles()
                .stream()
                .map(role ->  new SimpleGrantedAuthority(role.getCode()))
                .collect(Collectors.toList());
//        GrantedAuthority authority = new SimpleGrantedAuthority("USER");
//        grantedAuthorities.add(authority);

        System.out.println(new BCryptPasswordEncoder().encode("admin"));

        org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
                username, user.getPassword(), grantedAuthorities);
        return userDetails;

    }
}
