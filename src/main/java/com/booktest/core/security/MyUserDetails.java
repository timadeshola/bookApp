package com.booktest.core.security;

import com.booktest.core.exceptions.CustomException;
import com.booktest.jpa.entity.User;
import com.booktest.jpa.repos.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;

@Service
@Slf4j
public class MyUserDetails implements UserDetailsService {

    private UserRepository userRepository;
    private AuthorityDetails authorityDetails;

    @Autowired
    public MyUserDetails(UserRepository userRepository, AuthorityDetails authorityDetails) {
        this.userRepository = userRepository;
        this.authorityDetails = authorityDetails;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DisabledException {

        try {
            Optional<User> userOptional = userRepository.findUserByUsername(username);

            log.info("user details : {}", userOptional);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getStatus().equals(true)) {
                    user.setLastLoginDate(new Timestamp(System.currentTimeMillis()));
                    userRepository.saveAndFlush(user);

                    return org.springframework.security.core.userdetails.User//
                            .withUsername(username)//
                            .password(user.getPassword())//
                            .authorities(authorityDetails.getAuthorities(user))//
                            .accountExpired(false)//
                            .accountLocked(false)//
                            .credentialsExpired(false)//
                            .disabled(false)//
                            .build();
                }
            }

            } catch(Exception e){
                throw new CustomException("user not found", HttpStatus.NOT_FOUND);
            }
            return null;
        }



    }
