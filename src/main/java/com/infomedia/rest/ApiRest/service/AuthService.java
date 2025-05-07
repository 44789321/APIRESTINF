package com.infomedia.rest.ApiRest.service;

import com.infomedia.rest.ApiRest.model.Role;
import com.infomedia.rest.ApiRest.model.User;
import com.infomedia.rest.ApiRest.repository.RoleRepository;
import com.infomedia.rest.ApiRest.repository.UserRepository;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final StrongPasswordEncryptor passwordEncryptor;

    @Autowired
    public AuthService(UserRepository userRepository, StrongPasswordEncryptor passwordEncryptor) {
        this.userRepository = userRepository;
        this.passwordEncryptor = passwordEncryptor;
    }

    public User authenticate(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncryptor.checkPassword(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }

    public User registerUser(User user) {
        String encryptedPassword = passwordEncryptor.encryptPassword(user.getPassword());
        user.setPassword(encryptedPassword);
        return userRepository.save(user);
    }

    @Autowired
    private RoleRepository roleRepository;

    public String getRoleName(Long roleId) {
        Optional<Role> roleOptional = roleRepository.findById(roleId);

        if (roleOptional.isPresent()) {
            return roleOptional.get().getName();
        }

        return "Desconocido";
    }
}