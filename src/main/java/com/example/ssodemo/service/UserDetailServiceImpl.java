package com.example.ssodemo.service;

import com.example.ssodemo.model.User;
import com.example.ssodemo.repo.UserRepository;
import com.sunteco.suncloud.lib.exception.StcException;
import com.sunteco.suncloud.lib.model.UserInfo;
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
        UserInfo result = new UserInfo(user.getUsername(), user.getPassword());
        result.setFullName(user.getFullName());
        result.setId(user.getId());
        result.setEmail(user.getEmail());
        result.setFirstName(user.getFistName());
        result.setLastName(user.getLastName());
        result.setStatus(user.getStatus());
        result.setPhoneNumber(user.getPhoneNumber());
        result.setTenantId(user.getTenantId());
        result.setAccountType(user.getAccountType());
        result.setHifaVerifying(user.getHifaVerifying() != null ? user.getHifaVerifying() : 0);

        return result;
    }

}
