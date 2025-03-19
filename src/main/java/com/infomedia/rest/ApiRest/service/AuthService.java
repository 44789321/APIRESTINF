package com.infomedia.rest.ApiRest.service;

import com.infomedia.rest.ApiRest.dto.*;
import com.infomedia.rest.ApiRest.model.User;
import com.infomedia.rest.ApiRest.model.Role;
import com.infomedia.rest.ApiRest.repository.RoleRepository;
import com.infomedia.rest.ApiRest.repository.UserRepository;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    private StrongPasswordEncryptor passwordEncryptor;

    public LoginResponse login(LoginRequest loginRequest) {
        // Buscar usuario por username
        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());

        // Verificar si el usuario existe
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Verificar si la contraseña es correcta
            if (passwordEncryptor.checkPassword(loginRequest.getPassword(), user.getPassword())) {
                Map<String, Object> data = new HashMap<>();
                data.put("username", user.getUsername());
                return new LoginResponse("Login exitoso", true, data);
            } else {
                return new LoginResponse("Usuario o contraseña incorrecta", false, null);
            }
        } else {
            return new LoginResponse("Usuario o constraseña incorrecta", false, null);
        }
    }

    public boolean selectRole(String username, Long roleId) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        Optional<Role> optionalRole = roleRepository.findById(roleId);

        if (optionalUser.isPresent() && optionalRole.isPresent()) {
            User user = optionalUser.get();
            user.setRoleId(roleId);
            userRepository.save(user);
            return true;
        }
        return false;
    }
}