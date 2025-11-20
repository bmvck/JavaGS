package com.gs.gestaoativos.gateways.dtos;

import com.gs.gestaoativos.domains.Historico;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoResponseDto {
    private Integer idHistorico;
    private LocalDate dataMovimentacao;
    private String tipoMovimentacao;
    private String descricaoMoviment;
    private Integer ativoIdAtivo;
    private String ativoMarca;
    private Long colaboradorIdColab;
    private String colaboradorNome;

    public static HistoricoResponseDto fromEntity(Historico historico) {
        return new HistoricoResponseDto(
                historico.getIdHistorico(),
                historico.getDataMovimentacao(),
                historico.getTipoMovimentacao(),
                historico.getDescricaoMoviment(),
                historico.getAtivo() != null ? historico.getAtivo().getIdAtivo() : null,
                historico.getAtivo() != null ? historico.getAtivo().getMarca() : null,
                historico.getColaborador() != null ? historico.getColaborador().getIdColab() : null,
                historico.getColaborador() != null ? historico.getColaborador().getNomeColab() : null
        );
    }
}

