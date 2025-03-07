package com.infomedia.rest.ApiRest.controller;

import com.infomedia.rest.ApiRest.dto.*;
import com.infomedia.rest.ApiRest.model.User;
import com.infomedia.rest.ApiRest.model.Role;
import com.infomedia.rest.ApiRest.repository.UserRepository;
import com.infomedia.rest.ApiRest.repository.RoleRepository;
import com.infomedia.rest.ApiRest.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthService authService;

    /*------------------------Methods Get----------------------------------*/
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles(){
        List<Role> roles = roleRepository.findAll();
        return ResponseEntity.ok(roles);
    }

    /*------------------------Methods Post--------------------------------*/
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/select-role")
    public ResponseEntity<String> selectRole(@RequestBody SelectRoleRequest request) {
        boolean updated = authService.selectRole(request.getUsername(), request.getRoleId());
        if (updated) {
            return ResponseEntity.ok("Rol seleccionado correctamente.");
        } else {
            return ResponseEntity.badRequest().body("Rol o usuario no encontrado, intente de nuevo.");
        }
    }


}