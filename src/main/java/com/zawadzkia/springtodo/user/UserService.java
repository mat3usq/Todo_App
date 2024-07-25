package com.zawadzkia.springtodo.user;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.zawadzkia.springtodo.auth.Authority;
import com.zawadzkia.springtodo.auth.AuthorityRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    

    public UserModel existingUser(String username) {
        Optional<UserModel> userModel = userRepository.findByUsername(username);
        return userModel.orElse(null);
    }

    public void saveUser(UserModel user) {
        user.setPassword("{noop}" + user.getPassword());
        user.setEnabled(true);
        Authority authority = new Authority();
        authority.setAuthority("ROLE_USER");
        authority.setUser(user);
        userRepository.save(user);
        authorityRepository.save(authority);
    }
}
