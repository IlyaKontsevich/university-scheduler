package com.scheduler.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.scheduler.auth.Credential;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserDetailsImpl implements UserDetails
{
    private Long id;

    private String username;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl( Long id, String username, String password,
        Collection<? extends GrantedAuthority> authorities )
    {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build( Credential credential )
    {
        List<GrantedAuthority> authorities = credential.getRole().getPermissions()
                                                       .stream()
                                                       .map( permission -> new SimpleGrantedAuthority( permission.getName() ) )
                                                       .collect( Collectors.toList() );

        return new UserDetailsImpl(
            credential.getId(),
            credential.getName(),
            credential.getPassword(),
            authorities );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
