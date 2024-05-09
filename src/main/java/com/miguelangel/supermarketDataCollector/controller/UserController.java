package com.miguelangel.supermarketDataCollector.controller;

import com.miguelangel.supermarketDataCollector.configuration.security.TokenGenerator;
import com.miguelangel.supermarketDataCollector.dto.LoginDTO;
import com.miguelangel.supermarketDataCollector.dto.RegisterDTO;
import com.miguelangel.supermarketDataCollector.dto.UserDTO;
import com.miguelangel.supermarketDataCollector.entity.Product;
import com.miguelangel.supermarketDataCollector.entity.Role;
import com.miguelangel.supermarketDataCollector.entity.UserEntity;
import com.miguelangel.supermarketDataCollector.services.IProductService;
import com.miguelangel.supermarketDataCollector.services.IRoleService;
import com.miguelangel.supermarketDataCollector.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final IUserService userService;
    private final IRoleService roleService;
    private final AuthenticationManager authenticationManager;
    private final TokenGenerator tokenGenerator;

    @Autowired
    public UserController(IUserService userService,  TokenGenerator tokenGenerator, AuthenticationManager authenticationManager, IRoleService roleService) {
        this.userService = userService;
        this.tokenGenerator = tokenGenerator;
        this.authenticationManager = authenticationManager;
        this.roleService = roleService;
    }

    @PostMapping("/register")
    private ResponseEntity<UserDTO> registerUser(@RequestBody RegisterDTO registerDTO) {
        Optional<UserEntity> oldUser = userService.findByEmail(registerDTO.getEmail());
        if (oldUser.isPresent()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }

        Role role = roleService.findByName("USER").get();
        UserEntity userSave = new UserEntity();
        userSave.setName(registerDTO.getName());
        userSave.setLastName(registerDTO.getLastName());
        userSave.setEmail(registerDTO.getEmail());
        userSave.setPassword(registerDTO.getPassword());
        userSave.setPhone(registerDTO.getPhone());
        userSave.setRoles(Collections.singleton(role));

        try {
            UserEntity user = userService.save(userSave);
            UserDTO registeredUser = new UserDTO(user);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody LoginDTO loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenGenerator.generateToken(authentication);

        UserEntity user = userService.findByEmail(loginDto.getEmail()).get();
        user.setLastConnection(LocalDate.now());
        userService.save(user);

        UserDTO userDTO = new UserDTO(user);
        userDTO.setToken(token);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO user) {
        UserDTO savedUserDTO = null;
        try {
            if (user.getId() < 1 || user.getName().isEmpty() || user.getLastName().isEmpty() || user.getEmail().isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            Optional<UserEntity> oldUser = userService.findById(user.getId());
            if (oldUser.isEmpty()) {
                return new ResponseEntity<>(savedUserDTO, HttpStatus.NOT_FOUND);
            }

            UserEntity userUpdate = oldUser.get();

            if (!Objects.equals(user.getEmail(), userUpdate.getEmail())) {
                Optional<UserEntity> emailUser = userService.findByEmail(user.getEmail());
                if (emailUser.isPresent()) {
                    return new ResponseEntity<>(savedUserDTO, HttpStatus.NOT_ACCEPTABLE);
                }
            }

            userUpdate.setEmail(user.getEmail());
            userUpdate.setName(user.getName());
            userUpdate.setLastName(user.getLastName());
            userUpdate.setPhone(user.getPhone());
            UserEntity savedUser = userService.save(userUpdate);
            savedUserDTO = new UserDTO(savedUser);
            return new ResponseEntity<>(savedUserDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(savedUserDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
