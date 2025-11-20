package com.gs.gestaoativos.gateways.controllers;

import com.gs.gestaoativos.domains.Colaborador;
import com.gs.gestaoativos.gateways.dtos.ColaboradorRequestDto;
import com.gs.gestaoativos.gateways.dtos.ColaboradorResponseDto;
import com.gs.gestaoativos.services.ColaboradorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/colaboradores")
@RequiredArgsConstructor
@Tag(name = "Colaboradores", description = "Endpoints para gerenciamento de colaboradores")
public class ColaboradorController {

    private final ColaboradorService colaboradorService;

    @PostMapping
    @Operation(summary = "Criar colaborador", description = "Cria um novo colaborador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Colaborador criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ColaboradorResponseDto criar(@Valid @RequestBody ColaboradorRequestDto dto) {
        Colaborador colaborador = colaboradorService.criar(dto.toEntity(), dto.getResponsavel());
        return ColaboradorResponseDto.fromEntity(colaborador);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar colaborador", description = "Atualiza um colaborador existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Colaborador atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Colaborador não encontrado")
    })
    public ColaboradorResponseDto atualizar(@PathVariable Long id, @Valid @RequestBody ColaboradorRequestDto dto) {
        Colaborador colaborador = colaboradorService.atualizar(id, dto.toEntity(), dto.getResponsavel());
        return ColaboradorResponseDto.fromEntity(colaborador);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar colaborador por ID", description = "Retorna um colaborador específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Colaborador encontrado"),
            @ApiResponse(responseCode = "404", description = "Colaborador não encontrado")
    })
    public ColaboradorResponseDto buscarPorId(@PathVariable Long id) {
        Colaborador colaborador = colaboradorService.buscarPorId(id);
        return ColaboradorResponseDto.fromEntity(colaborador);
    }

    @GetMapping
    @Operation(summary = "Listar colaboradores", description = "Retorna uma lista paginada de colaboradores com filtros opcionais. Campos válidos para ordenação: nomeColab, emailColab, statusColab, areaColab, funcaoColab")
    public ResponseEntity<Page<ColaboradorResponseDto>> listar(
            @ParameterObject @PageableDefault(size = 10, sort = "nomeColab", direction = Sort.Direction.ASC) Pageable pageable,
            @Parameter(description = "Filtrar por nome") @RequestParam(required = false) String nome,
            @Parameter(description = "Filtrar por email") @RequestParam(required = false) String email,
            @Parameter(description = "Filtrar por status") @RequestParam(required = false) String status,
            @Parameter(description = "Filtrar por área") @RequestParam(required = false) String area) {
        
        if (nome != null || email != null || status != null || area != null) {
            List<Colaborador> colaboradores = colaboradorService.buscarPorFiltros(nome, email, status, area);
            List<ColaboradorResponseDto> dtos = colaboradores.stream()
                    .map(ColaboradorResponseDto::fromEntity)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new org.springframework.data.domain.PageImpl<>(dtos, pageable, colaboradores.size()));
        }
        
        Page<Colaborador> colaboradores = colaboradorService.listar(pageable);
        Page<ColaboradorResponseDto> dtos = colaboradores.map(ColaboradorResponseDto::fromEntity);
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/{id}/definir-senha")
    @Operation(summary = "Definir senha do colaborador", description = "Define ou atualiza a senha de um colaborador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Senha definida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Colaborador não encontrado")
    })
    @ResponseStatus(HttpStatus.OK)
    public void definirSenha(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String senha = request.get("senha");
        if (senha == null || senha.isEmpty()) {
            throw new RuntimeException("Senha é obrigatória");
        }
        colaboradorService.definirSenha(id, senha);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar colaborador", description = "Remove um colaborador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Colaborador deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Colaborador não encontrado")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        colaboradorService.deletar(id);
    }
}
