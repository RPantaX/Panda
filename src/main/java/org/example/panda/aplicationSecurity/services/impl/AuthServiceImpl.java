package org.example.panda.aplicationSecurity.services.impl;

import lombok.AllArgsConstructor;
import org.example.panda.aplicationSecurity.persistence.entities.ERole;
import org.example.panda.aplicationSecurity.persistence.entities.Role;
import org.example.panda.aplicationSecurity.persistence.entities.User;
import org.example.panda.aplicationSecurity.persistence.repositories.RoleRepository;
import org.example.panda.aplicationSecurity.persistence.repositories.UserRepository;
import org.example.panda.aplicationSecurity.services.IAuthService;
import org.example.panda.aplicationSecurity.services.IJWTUtilityService;
import org.example.panda.aplicationSecurity.services.models.dtos.LoginDto;
import org.example.panda.aplicationSecurity.services.models.dtos.SecurityResponseDto;
import org.example.panda.aplicationSecurity.services.models.dtos.UserDto;
import org.example.panda.trabajador.dtos.TrabajadorDto;
import org.example.panda.trabajador.entities.Trabajador;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements IAuthService {
    private final ModelMapper modelMapper;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final IJWTUtilityService jwtUtilityService;

    @Override
    public HashMap<String, String> login(LoginDto loginDTO) throws Exception {
        try {
            HashMap<String, String> jwt = new HashMap<>();
            Optional<User> user = userRepository.findByUsername(loginDTO.getUsername());
            if (user.isEmpty()) {
                jwt.put("error", "User not registered");
                return jwt;
            }
            // verificar la contraseña
            if (verifyPassword(loginDTO.getPassword(), user.get().getPassword())) {
                jwt.put("jwt", jwtUtilityService.generateJWT(user.get().getId()));
                jwt.put("username",loginDTO.getUsername());
                jwt.put("idUser", String.valueOf(user.get().getId()));
            } else{
                jwt.put("error","Authentication failed");
            }
            return jwt;
        } catch (Exception e) {
            throw new Exception(e.toString());
        }
    }

    @Override
    public SecurityResponseDto register(UserDto userDto) throws Exception {
        try {
            SecurityResponseDto responseDTO = new SecurityResponseDto();
            List<User> getAllUsers = userRepository.findAll();
            for (User repearFields : getAllUsers) {
                if (Objects.equals(repearFields.getUsername(), userDto.getUsername())) {
                    throw new IllegalArgumentException("Ya existe un usuario con el mismo nombre!");
                }
            }
            Set<Role> roles =getRoles(dtoToEntity(userDto));
            userDto.setRoles(roles);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);//encriptamos la contraseña
            userDto.setPassword(encoder.encode(userDto.getPassword())); //y se la asignamos al usuario
            boolean isAdmin= userDto.getRoles().stream().anyMatch(r -> ERole.ADMIN.equals(r.getRole()));
            User userSave=dtoToEntity(userDto);
            userSave.setAdmin(isAdmin);
            userSave.setEstado("activo");
            userRepository.save(userSave);
            responseDTO.setMessage("Usuario creado exitosamente!");
            return responseDTO;
        } catch (Exception e) {
            throw new Exception(e.toString());
        }
    }

    private boolean verifyPassword(String enteredPassword, String storedPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(enteredPassword, storedPassword); // si hace match true, si no false
    }

    // convertir de DTO a Entidad
    private User dtoToEntity(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }
    private Set<Role> getRoles(User user){
        Optional<Role> ou = roleRepository.findByRole(ERole.USER);
        Set<Role> roles = new HashSet<>();
        if (ou.isPresent()) {
            roles.add(ou.orElseThrow());
        }
        if(user.isAdmin()){
            Optional<Role> oa = roleRepository.findByRole(ERole.ADMIN);
            if (oa.isPresent()) {
                roles.add(oa.orElseThrow());
            }
        }
        return roles;
    }
}
