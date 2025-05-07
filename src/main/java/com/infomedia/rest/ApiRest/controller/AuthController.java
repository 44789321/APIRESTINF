package com.infomedia.rest.ApiRest.controller;


import com.infomedia.rest.ApiRest.dto.AuthResponseDTO;
import com.infomedia.rest.ApiRest.dto.LoginDTO;
import com.infomedia.rest.ApiRest.model.People;
import com.infomedia.rest.ApiRest.model.ProjectRole;
import com.infomedia.rest.ApiRest.model.Role;
import com.infomedia.rest.ApiRest.model.User;
import com.infomedia.rest.ApiRest.repository.*;
import com.infomedia.rest.ApiRest.service.AuthService;
import com.infomedia.rest.ApiRest.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    /*--------------------------Inyección de Repositorios--------------------*/
    private final AuthService authService;
    private final JwtService jwtService;
    @Autowired
    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRoleRepository projectRoleRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PeopleRepository peopleRepository;

    /*------------------------Métodos GET en el sistema----------------------------------*/
    @GetMapping("/roles")
    public ResponseEntity<?> getRolesForAuthenticatedUser(@RequestHeader("Authorization") String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            String username = jwtService.extractUsername(token);
            Optional<User> userOpt = userRepository.findByUsername(username);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(404).body("Usuario no encontrado");
            }
            User user = userOpt.get();
            Long filterId = user.getFilterId();
            List<ProjectRole> userProjectRoles = projectRoleRepository.findByUserFilter(filterId);
            if (userProjectRoles.isEmpty()) {
                return ResponseEntity.ok(Collections.emptyList());
            }
            // Obtener los IDs de roles únicos
            Set<Long> roleIds = new HashSet<>();
            for (ProjectRole pr : userProjectRoles) {
                roleIds.add(pr.getRoleName()); // Obtener el ID del rol
            }
            // Crear una lista de objetos con roleId y roleName
            List<Map<String, Object>> rolesData = new ArrayList<>();
            for (Long id : roleIds) {
                roleRepository.findById(id).ifPresent(role -> {
                    Map<String, Object> roleData = new HashMap<>();
                    roleData.put("roleId", role.getId());      // Asignar roleId
                    roleData.put("roleName", role.getName());  // Asignar roleName
                    rolesData.add(roleData);  // Agregar a la lista
                });
            }
            // Retornar la respuesta en formato JSON
            Map<String, Object> response = new HashMap<>();
            response.put("data", rolesData);   // Datos de roles
            response.put("success", true);     // Indicar éxito
            response.put("message", "Roles obtenidos correctamente"); // Mensaje de éxito
            return ResponseEntity.ok(response);  // Retornar la respuesta
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener los roles: " + e.getMessage());
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<AuthResponseDTO> validateToken(@RequestHeader("Authorization") String token) {
        try {
            // Eliminar el prefijo "Bearer " si está presente
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            String username = jwtService.extractUsername(token);
            boolean isValid = jwtService.validateToken(token, username);
            if (isValid) {
                return ResponseEntity.ok(new AuthResponseDTO("Token válido", true));
            } else {
                return ResponseEntity.status(401).body(new AuthResponseDTO("Token inválido o expirado", false));
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body(new AuthResponseDTO("Token inválido", false));
        }
    }

    /*-----------------------------Métodos POST--------------------------------------*/

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        User authenticatedUser = authService.authenticate(loginDTO.getUsername(), loginDTO.getPassword());
        if (authenticatedUser != null) {
            String token = jwtService.generateToken(authenticatedUser);
            AuthResponseDTO response = new AuthResponseDTO(
                    "Login exitoso",
                    true,
                    authenticatedUser.getUserId(),
                    authenticatedUser.getRoleId(),
                    token
            );
            return ResponseEntity.ok(response);
        } else {
            AuthResponseDTO response = new AuthResponseDTO("Credenciales inválidas", false);
            return ResponseEntity.status(401).body(response);
        }
    }

    /*---------------------Gets de prueba para funcionamiento-------------------------------*/

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/roles-disponibles")
    public ResponseEntity<List<Role>>getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/people")
    public ResponseEntity<List<People>>getAllPeople(){
        List<People> people = peopleRepository.findAll();
        return ResponseEntity.ok(people);
    }
}