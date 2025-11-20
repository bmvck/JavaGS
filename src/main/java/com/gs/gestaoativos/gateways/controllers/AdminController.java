package com.gs.gestaoativos.gateways.controllers;

import com.gs.gestaoativos.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Endpoints administrativos (temporários)")
public class AdminController {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioService usuarioService;

    @PostMapping("/gerar-hash")
    @Operation(summary = "Gerar hash BCrypt", description = "Gera um hash BCrypt para uma senha (endpoint temporário)")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> gerarHash(@RequestBody Map<String, String> request) {
        String senha = request.get("senha");
        if (senha == null || senha.isEmpty()) {
            throw new RuntimeException("Senha é obrigatória");
        }
        String hash = passwordEncoder.encode(senha);
        return Map.of("senha", senha, "hash", hash);
    }

    @PostMapping("/definir-senha-colaborador/{id}")
    @Operation(summary = "Definir senha do colaborador", description = "Define a senha de um colaborador (endpoint temporário)")
    @ResponseStatus(HttpStatus.OK)
    public void definirSenhaColaborador(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String senha = request.get("senha");
        if (senha == null || senha.isEmpty()) {
            throw new RuntimeException("Senha é obrigatória");
        }
        usuarioService.definirSenha(id, senha);
    }
}

