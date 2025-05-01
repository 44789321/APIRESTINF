package com.infomedia.rest.ApiRest.controller;

import com.infomedia.rest.ApiRest.dto.*;
import com.infomedia.rest.ApiRest.model.*;
import com.infomedia.rest.ApiRest.repository.*;
import com.infomedia.rest.ApiRest.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {


    /*--------------------------Inyección de Repositorios--------------------*/
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthService authService;
    private final ProjectRoleRepository projectRoleRepository;
    private final PeopleRepository peopleRepository;
    private final ProyectoRepository proyectoRepository;
    private final ClienteRepository clienteRepository;

    @Autowired
    public AuthController(UserRepository userRepository,
                          RoleRepository roleRepository,
                          AuthService authService,
                          ProjectRoleRepository projectRoleRepository,
                          PeopleRepository peopleRepository,
                          ProyectoRepository proyectoRepository,
                          ClienteRepository clienteRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authService = authService;
        this.projectRoleRepository = projectRoleRepository;
        this.peopleRepository = peopleRepository;
        this.proyectoRepository = proyectoRepository;
        this.clienteRepository = clienteRepository;
    }
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
            return ResponseEntity.status(404).body(Map.of(
                    "message", "Sin sesión activa",
                    "success", false,
                    "data", null
            ));
        }
        String username = (String) session.getAttribute("user");
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Long filterId = user.getFilterId();
            List<ProjectRole> projectRoles = projectRoleRepository.findByUserFilter(filterId);
            if (projectRoles.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of(
                        "message", "No roles disponibles para este usuario",
                        "success", false,
                        "data", null
                ));
            }
            Set<Map<String, Object>> uniqueRoles = new HashSet<>();
            for (ProjectRole projectRole : projectRoles) {
                Map<String, Object> roleInfo = new HashMap<>();
                Long roleId = projectRole.getRoleName();
                String roleName = authService.getRoleName(roleId);
                roleInfo.put("roleId", roleId);
                roleInfo.put("roleName", roleName);
                uniqueRoles.add(roleInfo);
            }
            return ResponseEntity.ok(Map.of(
                    "message", "Roles obtenidos correctamente",
                    "success", true,
                    "data", new ArrayList<>(uniqueRoles)
            ));
        }

        return ResponseEntity.status(404).body(Map.of(
                "message", "Usuario no encontrado",
                "success", false,
                "data", null
        ));
    }

    @GetMapping("/nombre")
    public ResponseEntity<?> getNombre(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(404).body(Map.of(
                    "message", "Sin sesión activa",
                    "success", false,
                    "data", null
            ));
        }

        String username = (String) session.getAttribute("user");
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Long filterId = user.getFilterId();

            Optional<People> peopleOptional = peopleRepository.findByPeopleId(filterId);
            if (peopleOptional.isPresent()) {
                return ResponseEntity.ok(Map.of(
                        "message", "Nombre obtenido correctamente",
                        "success", true,
                        "data", Map.of("nombre", peopleOptional.get().getUserName())
                ));
            }
            return ResponseEntity.status(404).body(Map.of(
                    "message", "No se encontró coincidencia en INF_PERSONAS",
                    "success", false,
                    "data", null
            ));
        }
        return ResponseEntity.status(404).body(Map.of(
                "message", "Usuario no encontrado",
                "success", false,
                "data", null
        ));
    }

    @GetMapping("/proyecto")
    public ResponseEntity<?> getProyectos(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(404).body(Map.of(
                    "message", "Sin sesión activa",
                    "success", false,
                    "data", null
            ));
        }
        String username = (String) session.getAttribute("user");
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of(
                    "message", "Usuario no encontrado",
                    "success", false,
                    "data", null
            ));
        }
        User user = userOptional.get();
        Long filterId = user.getFilterId();
        List<ProjectRole> projectRoles = projectRoleRepository.findByUserFilter(filterId);
        if (projectRoles.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of(
                    "message", "No hay proyectos asociados a este usuario",
                    "success", false,
                    "data", null
            ));
        }
        List<String> projectNames = projectRoles.stream()
                .map(ProjectRole::getProjectName)
                .distinct()
                .toList();
        List<Proyecto> proyectos = proyectoRepository.findByProjectIdIn(projectNames);
        if (proyectos.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of(
                    "message", "No se encontraron proyectos con esos IDs",
                    "success", false,
                    "data", null
            ));
        }
        List<String> projectNamesResult = proyectos.stream()
                .map(Proyecto::getProjectName)
                .distinct()
                .toList();
        return ResponseEntity.ok(Map.of(
                "message", "Proyectos obtenidos correctamente",
                "success", true,
                "data", projectNamesResult
        ));
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
    public ResponseEntity<LoginResponse> selectRole(@RequestBody SelectRoleRequest request, HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(404).body(new LoginResponse("Sin sesión activa", false, null));
        }

        String username = (String) session.getAttribute("user");
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(404).body(new LoginResponse("Usuario no encontrado", false, null));
        }

        User user = userOptional.get();
        Long filterId = user.getFilterId();
        List<ProjectRole> projectRoles = projectRoleRepository.findByUserFilter(filterId);

        Set<Long> availableRoleIds = projectRoles.stream()
                .map(ProjectRole::getRoleName)
                .collect(Collectors.toSet());

        Long selectedRoleId = request.getRoleId();
        if (!availableRoleIds.contains(selectedRoleId)) {
            return ResponseEntity.status(400).body(new LoginResponse("Rol seleccionado no disponible para este usuario", false, null));
        }

        session.setAttribute("selectedRole", selectedRoleId.toString());
        return ResponseEntity.ok(new LoginResponse("Rol seleccionado correctamente", true, null));
    }


    @PostMapping("/logout")
    public ResponseEntity<LoginResponse> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(400).body(
                    new LoginResponse("No hay sesión activa para cerrar", false, null)
            );
        }

        session.invalidate();

        return ResponseEntity.ok(
                new LoginResponse("Sesión cerrada correctamente", true, null)
        );
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