package com.infomedia.rest.ApiRest.service;

import com.infomedia.rest.ApiRest.dto.*;
import com.infomedia.rest.ApiRest.model.Role;
import com.infomedia.rest.ApiRest.model.User;
import com.infomedia.rest.ApiRest.repository.ProjectRoleRepository;
import com.infomedia.rest.ApiRest.repository.RoleRepository;
import com.infomedia.rest.ApiRest.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StrongPasswordEncryptor passwordEncryptor;
    @Autowired
    private ProjectRoleRepository projectRoleRepository;
    @Autowired
    private RoleRepository roleRepository;


    private Map<Long, String> roleCache = new HashMap<>();

    @PostConstruct
    public void loadRolesIntoCache() {
        List<Role> roles = roleRepository.findAll();
        for (Role role : roles) {
            roleCache.put(role.getId(), role.getName());
        }
    }

    public String getRoleName(Long roleId) {
        return roleCache.getOrDefault(roleId, "Desconocido");
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncryptor.checkPassword(loginRequest.getPassword(), user.getPassword())) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        user.getUserId(),
                        null,
                        Collections.emptyList()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);

                Map<String, Object> data = new HashMap<>();
                data.put("username", user.getUsername());
                data.put("userId", user.getUserId());

                return new LoginResponse("Successful", true, data);
            } else {
                return new LoginResponse("Usuario o contraseña invalidos", false, null);
            }
        } else {
            return new LoginResponse("Usuario o contraseña invalidos", false, null);
        }
    }

    @Value("${auth.service.url}")
    private String userFilterEndpoint;

    public Long getUserFilter() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(userFilterEndpoint, Long.class);
    }
}