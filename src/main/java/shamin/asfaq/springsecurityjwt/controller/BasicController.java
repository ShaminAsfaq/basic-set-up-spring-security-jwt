package shamin.asfaq.springsecurityjwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import shamin.asfaq.springsecurityjwt.model.AuthenticationRequest;
import shamin.asfaq.springsecurityjwt.model.AuthenticationResponse;
import shamin.asfaq.springsecurityjwt.util.JwtUtil;

import java.util.HashMap;
import java.util.Map;

@RestController
public class BasicController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Qualifier("myUserDetailsService")
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/hello")
    public ResponseEntity<Map<String,String>> hello() {
        Map<String, String> toBeReturned = new HashMap<>();
        toBeReturned.put("Response", "Hello World !");
        return ResponseEntity.ok(toBeReturned);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException exception) {
            throw new Exception("Incorrect username or password", exception);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(token));
    }
}
