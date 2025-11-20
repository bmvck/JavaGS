package com.gs.gestaoativos.gateways.dtos;

import com.gs.gestaoativos.domains.Manutencao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManutencaoResponseDto {
    private Integer idManutencao;
    private String tipoManutencao;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private BigDecimal custo;
    private String descricao;
    private Integer ativoIdAtivo;
    private String ativoMarca;
    private String ativoModelo;

    public static ManutencaoResponseDto fromEntity(Manutencao manutencao) {
        return new ManutencaoResponseDto(
                manutencao.getIdManutencao(),
                manutencao.getTipoManutencao(),
                manutencao.getDataInicio(),
                manutencao.getDataFim(),
                manutencao.getCusto(),
                manutencao.getDescricao(),
                manutencao.getAtivo() != null ? manutencao.getAtivo().getIdAtivo() : null,
                manutencao.getAtivo() != null ? manutencao.getAtivo().getMarca() : null,
                manutencao.getAtivo() != null ? manutencao.getAtivo().getModelo() : null
        );
    }
}

