package com.gs.gestaoativos.gateways.controllers;

import com.gs.gestaoativos.domains.Emprestimo;
import com.gs.gestaoativos.gateways.dtos.EmprestimoRequestDto;
import com.gs.gestaoativos.gateways.dtos.EmprestimoResponseDto;
import com.gs.gestaoativos.services.EmprestimoService;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/emprestimos")
@RequiredArgsConstructor
@Tag(name = "Empréstimos", description = "Endpoints para gerenciamento de empréstimos de ativos")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    @PostMapping
    @Operation(summary = "Criar empréstimo", description = "Cria um novo empréstimo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Empréstimo criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public EmprestimoResponseDto criar(@Valid @RequestBody EmprestimoRequestDto dto) {
        Emprestimo emprestimo = emprestimoService.criar(dto.toEntity(), dto.getAtivoIdAtivo(), dto.getColaboradorIdColab());
        return EmprestimoResponseDto.fromEntity(emprestimo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar empréstimo", description = "Atualiza um empréstimo existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empréstimo atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado")
    })
    public EmprestimoResponseDto atualizar(@PathVariable Integer id, @Valid @RequestBody EmprestimoRequestDto dto) {
        Emprestimo emprestimo = emprestimoService.atualizar(id, dto.toEntity(), dto.getAtivoIdAtivo(), dto.getColaboradorIdColab());
        return EmprestimoResponseDto.fromEntity(emprestimo);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar empréstimo por ID", description = "Retorna um empréstimo específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empréstimo encontrado"),
            @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado")
    })
    public EmprestimoResponseDto buscarPorId(@PathVariable Integer id) {
        Emprestimo emprestimo = emprestimoService.buscarPorId(id);
        return EmprestimoResponseDto.fromEntity(emprestimo);
    }

    @GetMapping
    @Operation(summary = "Listar empréstimos", description = "Retorna uma lista paginada de empréstimos com filtros opcionais. Campos válidos para ordenação: dataEmprestimo, dataDevolucao, statusEmprestimo")
    public ResponseEntity<Page<EmprestimoResponseDto>> listar(
            @ParameterObject @PageableDefault(size = 10, sort = "dataEmprestimo", direction = Sort.Direction.DESC) Pageable pageable,
            @Parameter(description = "Filtrar por status") @RequestParam(required = false) String status,
            @Parameter(description = "Filtrar por ID do ativo") @RequestParam(required = false) Integer ativoId,
            @Parameter(description = "Filtrar por ID do colaborador") @RequestParam(required = false) Long colaboradorId,
            @Parameter(description = "Filtrar por data de início") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @Parameter(description = "Filtrar por data de fim") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        
        if (status != null || ativoId != null || colaboradorId != null || dataInicio != null || dataFim != null) {
            List<Emprestimo> emprestimos = emprestimoService.buscarPorFiltros(status, ativoId, colaboradorId, dataInicio, dataFim);
            List<EmprestimoResponseDto> dtos = emprestimos.stream()
                    .map(EmprestimoResponseDto::fromEntity)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new org.springframework.data.domain.PageImpl<>(dtos, pageable, emprestimos.size()));
        }
        
        Page<Emprestimo> emprestimos = emprestimoService.listar(pageable);
        Page<EmprestimoResponseDto> dtos = emprestimos.map(EmprestimoResponseDto::fromEntity);
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar empréstimo", description = "Remove um empréstimo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Empréstimo deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        emprestimoService.deletar(id);
    }
}

