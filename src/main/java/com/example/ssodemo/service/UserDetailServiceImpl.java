package com.example.ssodemo.service;

import com.example.ssodemo.model.User;
import com.example.ssodemo.model.dto.SecurityUser;
import com.example.ssodemo.repo.UserRepository;
import com.sunteco.suncloud.lib.exception.StcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByEmailOrUsername(s, s).orElseThrow(() ->
                new StcException("user not found", "user not found!"));
        if (user.getPassword() == null) {
            throw new StcException("incorect username or password", "incorect username or password");
        }
//        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
        return new SecurityUser(user);
    }

}
