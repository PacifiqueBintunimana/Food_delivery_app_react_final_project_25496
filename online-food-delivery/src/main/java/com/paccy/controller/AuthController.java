package com.paccy.controller;

import com.paccy.config.JwtProvider;
import com.paccy.model.Cart;
import com.paccy.model.USER_ROLE;
import com.paccy.model.User;
import com.paccy.repository.CartRepository;
import com.paccy.repository.UserRepository;
import com.paccy.request.LoginRequest;
import com.paccy.response.AuthResponse;
import com.paccy.service.CustomerUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private CustomerUserDetailsService customerUserDetailsService;

    @Autowired
    private CartRepository cartRepository;
@PostMapping("/signup")
    public ResponseEntity<AuthResponse> CreateUserHandler(@RequestBody User user) throws Exception {
/*
        ResponseDTO<JWTResponseDTO> response = new ResponseDTO<>();

        User user = userService.registerUser(userDTO);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);

        JWTResponseDTO jwtResponseDTO = new JWTResponseDTO();
        jwtResponseDTO.setJwt(jwt);
        jwtResponseDTO.setEmail(user.getEmail());
        jwtResponseDTO.setRole(user.getRole());
        jwtResponseDTO.setUserName(user.getFullName());

        response.setPayload(jwtResponseDTO);
        response.setMessage("Registered Successfully");
        response.setHttpStatus(HttpStatus.CREATED);
        response.setCode("201");

        return new ResponseEntity<>(response, response.getHttpStatus());
    }*/


        User isEmailExist =userRepository.findByEmail(user.getEmail());
        if(isEmailExist != null) {
            throw new Exception("email is already used");
        }
        User createUser= new User();
        createUser.setEmail(user.getEmail());
        createUser.setFullName(user.getFullName());
        createUser.setRole(user.getRole());
        createUser.setPassword(passwordEncoder.encode(user.getPassword()));
        User saveuser=userRepository.save(createUser);

        Cart cart =new Cart();
        cart.setCustomer(saveuser);
        cartRepository.save(cart);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);

        AuthResponse authResponseDTO = new AuthResponse();
        authResponseDTO.setJwt(jwt);
        authResponseDTO.setMessage("Registered Successfully");
        authResponseDTO.setRole(user.getRole());

        return new ResponseEntity<>(authResponseDTO,HttpStatus.CREATED);

    }
   @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest req){
String username=req.getEmail();
String password= req.getPassword();
        Authentication authentication = authenticate(username, password);
        Collection<? extends GrantedAuthority>authorities=authentication.getAuthorities();
        String role = authorities.isEmpty()?null:authorities.iterator().next().getAuthority();

        String jwt = jwtProvider.generateToken(authentication);

        AuthResponse authResponseDTO = new AuthResponse();
        authResponseDTO.setJwt(jwt);
        authResponseDTO.setMessage("Login Successfully");
        authResponseDTO.setRole(USER_ROLE.valueOf(role));


        return new ResponseEntity<>(authResponseDTO,HttpStatus.OK);


    }

    private Authentication authenticate(String username, String password) {
    UserDetails userDetails= customerUserDetailsService.loadUserByUsername(username);
    if(userDetails==null){
       throw new BadCredentialsException("invalid username....");
    }
    if(!passwordEncoder.matches(password,userDetails.getPassword())){
        throw new BadCredentialsException("invalid password....");
    }
    return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }
}
