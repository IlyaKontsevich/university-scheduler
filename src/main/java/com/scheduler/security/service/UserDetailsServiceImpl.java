package com.scheduler.security.service;

import com.scheduler.auth.Credential;
import com.scheduler.auth.CredentialRepository;
import com.scheduler.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService
{
    private final CredentialRepository credentialRepository;

    @Override
    public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException
    {
        Credential credential = credentialRepository.findByName( username )
                                                    .orElseThrow( () -> new UsernameNotFoundException("User Not Found with username: " + username) );
        return UserDetailsImpl.build( credential );
    }
}
