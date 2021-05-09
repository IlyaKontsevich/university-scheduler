package com.scheduler.security.jwt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class JwtResponse
{
    private String jwt;
    private Long credentialId;
    private String username;
    private List<String> authorities;
}
