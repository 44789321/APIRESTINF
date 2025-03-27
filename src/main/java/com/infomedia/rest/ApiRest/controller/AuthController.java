package com.infomedia.rest.ApiRest.controller;

import com.infomedia.rest.ApiRest.dto.*;
import com.infomedia.rest.ApiRest.model.*;
import com.infomedia.rest.ApiRest.repository.*;
import com.infomedia.rest.ApiRest.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    /*--------------------------Inyección de Repositorios--------------------*/
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthService authService;
    private final ProjectRoleRepository projectRoleRepository;
    private final PeopleRepository peopleRepository;
    private final ProyectoRepository proyectoRepository;
    private final ClienteRepository clienteRepository;

    /*------------------------Métodos GET en el sistema----------------------------------*/

    @GetMapping("/user-filter")
    public ResponseEntity<?> getUserFilter(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(404).body("Sin sesion activa");
        }
        String username = (String) session.getAttribute("user");
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Long filterId = user.getFilterId();

            return ResponseEntity.ok(filterId);
        }
        return ResponseEntity.status(404).body("User not found");
    }

    @GetMapping("/roles")
    public ResponseEntity<?> getAvailableRoles(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(404).body("Sin sesión activa");
        }
        String username = (String) session.getAttribute("user");
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Long filterId = user.getFilterId();
            List<ProjectRole> projectRoles = projectRoleRepository.findByUserFilter(filterId);
            if (projectRoles.isEmpty()) {
                return ResponseEntity.status(404).body("No roles disponibles para este usuario");
            }
            Set<Map<String, Object>> uniqueRoles = new HashSet<>();
            for (ProjectRole projectRole : projectRoles) {
                Map<String, Object> roleInfo = new HashMap<>();
                Long roleId = projectRole.getRoleName();
                String roleName = authService.getRoleName(roleId);
                roleInfo.put("roleId", roleId);  // Agregar el ID del rol
                roleInfo.put("roleName", roleName);  // Agregar el nombre del rol
                uniqueRoles.add(roleInfo);
            }
            return ResponseEntity.ok(new ArrayList<>(uniqueRoles));  // Devolver lista con ID y nombre del rol
        }

        return ResponseEntity.status(404).body("Usuario no encontrado");
    }

    @GetMapping("/nombre")
    public ResponseEntity<?> getNombre(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Sin sesión activa"));
        }

        String username = (String) session.getAttribute("user");
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Long filterId = user.getFilterId();

            Optional<People> peopleOptional = peopleRepository.findByPeopleId(filterId);
            if (peopleOptional.isPresent()) {
                return ResponseEntity.ok(Map.of("nombre", peopleOptional.get().getUserName()));
            }
            return ResponseEntity.status(404).body(Map.of("error", "No se encontró coincidencia en INF_PERSONAS"));
        }
        return ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado"));
    }

    @GetMapping("/proyecto")
    public ResponseEntity<?> getProyectos(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(404).body("Sin sesión activa");
        }
        String username = (String) session.getAttribute("user");
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }
        User user = userOptional.get();
        Long filterId = user.getFilterId();
        List<ProjectRole> projectRoles = projectRoleRepository.findByUserFilter(filterId);
        if (projectRoles.isEmpty()) {
            return ResponseEntity.status(404).body("No hay proyectos asociados a este usuario");
        }
        List<String> projectNames = projectRoles.stream()
                .map(ProjectRole::getProjectName)
                .distinct()
                .toList();
        List<Proyecto> proyectos = proyectoRepository.findByProjectIdIn(projectNames);
        if (proyectos.isEmpty()) {
            return ResponseEntity.status(404).body("No se encontraron proyectos con esos IDs");
        }
        List<String> projectNamesResult = proyectos.stream()
                .map(Proyecto::getProjectName)
                .distinct()
                .toList();
        return ResponseEntity.ok(projectNamesResult);
    }

    /*-----------------------------Métodos POST--------------------------------------*/

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            return ResponseEntity.status(400).body(new LoginResponse("Ya existe una sesión activa", false, null));
        }
        LoginResponse response = authService.login(request);
        if (response.isSuccess()) {
            session = httpServletRequest.getSession(true);
            session.setAttribute("user", request.getUsername());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(response);
        }
    }

    @PostMapping("/select-role")
    public ResponseEntity<?> selectRole(@RequestBody Map<String, Object> payload, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(404).body("Sin sesión activa");
        }
        if (!payload.containsKey("roleId") || payload.get("roleId") == null) {
            return ResponseEntity.status(400).body("El parámetro 'roleId' es obligatorio");
        }
        Long selectedRoleId = null;
        try {
            selectedRoleId = Long.parseLong(payload.get("roleId").toString());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("El 'roleId' debe ser un número válido");
        }
        String username = (String) session.getAttribute("user");
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Long filterId = user.getFilterId();
            List<ProjectRole> projectRoles = projectRoleRepository.findByUserFilter(filterId);
            Set<Long> availableRoleIds = new HashSet<>();
            for (ProjectRole projectRole : projectRoles) {
                availableRoleIds.add(projectRole.getRoleName());
            }
            if (!availableRoleIds.contains(selectedRoleId)) {
                return ResponseEntity.status(400).body("Rol seleccionado no disponible para este usuario");
            }
            session.setAttribute("selectedRole", selectedRoleId.toString());
            return ResponseEntity.ok("Rol seleccionado correctamente");
        }
        return ResponseEntity.status(404).body("Usuario no encontrado");
    }



    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logout successful");
    }


    /*---------------------Gets de prueba para funcionamiento-------------------------------*/

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/roles-disponibles")
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/people")
    public ResponseEntity<List<People>>getAllPeople(){
        List<People> people = peopleRepository.findAll();
        return ResponseEntity.ok(people);
    }

    @GetMapping("/user-active")
    public ResponseEntity<User> getUserActive(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.notFound().build();
        }
        String username = (String) session.getAttribute("user");
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/get-selected-role")
    public ResponseEntity<?> getSelectedRole(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("selectedRole") == null) {
            return ResponseEntity.status(404).body("No se ha seleccionado un rol");
        }
        String selectedRole = (String) session.getAttribute("selectedRole");
        return ResponseEntity.ok("Rol seleccionado: " + selectedRole);
    }
}