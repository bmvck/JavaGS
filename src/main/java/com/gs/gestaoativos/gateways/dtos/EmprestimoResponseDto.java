package com.gs.gestaoativos.gateways.dtos;

import com.gs.gestaoativos.domains.Emprestimo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmprestimoResponseDto {
    private Integer idEmprestimo;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao;
    private String statusEmprestimo;
    private Integer ativoIdAtivo;
    private String ativoMarca;
    private String ativoModelo;
    private Long colaboradorIdColab;
    private String colaboradorNome;

    public static EmprestimoResponseDto fromEntity(Emprestimo emprestimo) {
        return new EmprestimoResponseDto(
                emprestimo.getIdEmprestimo(),
                emprestimo.getDataEmprestimo(),
                emprestimo.getDataDevolucao(),
                emprestimo.getStatusEmprestimo(),
                emprestimo.getAtivo() != null ? emprestimo.getAtivo().getIdAtivo() : null,
                emprestimo.getAtivo() != null ? emprestimo.getAtivo().getMarca() : null,
                emprestimo.getAtivo() != null ? emprestimo.getAtivo().getModelo() : null,
                emprestimo.getColaborador() != null ? emprestimo.getColaborador().getIdColab() : null,
                emprestimo.getColaborador() != null ? emprestimo.getColaborador().getNomeColab() : null
        );
    }
}

