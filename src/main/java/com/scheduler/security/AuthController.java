package com.scheduler.security;

import com.scheduler.auth.CredentialRepository;
import com.scheduler.auth.RoleRepository;
import com.scheduler.security.jwt.utils.JwtUtils;
import com.scheduler.security.jwt.vo.JwtResponse;
import com.scheduler.security.vo.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping( "/api/auth" )
public class AuthController
{
    private final AuthenticationManager authenticationManager;
    private final CredentialRepository credentialRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    @PostMapping( "/signin" )
    public ResponseEntity<?> authenticateUser( @Valid @RequestBody LoginRequest loginRequest )
    {

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken( loginRequest.getUsername(), loginRequest.getPassword() ) );

        SecurityContextHolder.getContext().setAuthentication( authentication );
        String jwt = jwtUtils.generateJwtToken( authentication );

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                                        .map( GrantedAuthority::getAuthority )
                                        .collect( Collectors.toList() );

        return ResponseEntity.ok( new JwtResponse( jwt,
            userDetails.getId(),
            userDetails.getUsername(),
            roles ) );
    }
}
