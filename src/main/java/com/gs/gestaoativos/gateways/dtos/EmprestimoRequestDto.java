package com.gs.gestaoativos.gateways.dtos;

import com.gs.gestaoativos.domains.Emprestimo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmprestimoRequestDto {

    private Integer idEmprestimo;

    @NotNull(message = "Data de empréstimo é obrigatória")
    private LocalDate dataEmprestimo;

    private LocalDate dataDevolucao;

    @NotBlank(message = "Status do empréstimo é obrigatório")
    @Size(max = 30, message = "Status deve ter no máximo 30 caracteres")
    private String statusEmprestimo;

    @NotNull(message = "Ativo é obrigatório")
    private Integer ativoIdAtivo;

    @NotNull(message = "Colaborador é obrigatório")
    private Long colaboradorIdColab;

    public Emprestimo toEntity() {
        return Emprestimo.builder()
                .idEmprestimo(this.idEmprestimo)
                .dataEmprestimo(this.dataEmprestimo)
                .dataDevolucao(this.dataDevolucao)
                .statusEmprestimo(this.statusEmprestimo)
                .build();
    }
}

