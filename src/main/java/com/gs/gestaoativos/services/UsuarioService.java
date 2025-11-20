package com.gs.gestaoativos.services;

import com.gs.gestaoativos.domains.Colaborador;
import com.gs.gestaoativos.gateways.repositories.ColaboradorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final ColaboradorRepository colaboradorRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Usa o email como username
        Colaborador colaborador = colaboradorRepository.findByEmailColab(username)
                .orElseThrow(() -> new UsernameNotFoundException("Colaborador não encontrado com email: " + username));

        if (!"ATV".equals(colaborador.getStatusColab())) {
            throw new UsernameNotFoundException("Colaborador inativo: " + username);
        }

        if (colaborador.getSenha() == null || colaborador.getSenha().isEmpty()) {
            throw new UsernameNotFoundException("Colaborador sem senha cadastrada: " + username);
        }

        // Usa o campo role, ou "USER" como padrão
        String role = colaborador.getRole() != null && !colaborador.getRole().isEmpty() 
                ? colaborador.getRole() 
                : "USER";

        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + role)
        );

        return User.builder()
                .username(colaborador.getEmailColab())
                .password(colaborador.getSenha())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!"ATV".equals(colaborador.getStatusColab()))
                .build();
    }

    public void definirSenha(Long colaboradorId, String senha) {
        Colaborador colaborador = colaboradorRepository.findByIdNative(colaboradorId)
                .orElseThrow(() -> new RuntimeException("Colaborador não encontrado com ID: " + colaboradorId));
        
        colaborador.setSenha(passwordEncoder.encode(senha));
        colaboradorRepository.save(colaborador);
    }
}

