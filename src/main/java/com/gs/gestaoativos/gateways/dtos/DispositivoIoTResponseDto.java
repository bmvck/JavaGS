package com.gs.gestaoativos.gateways.dtos;

import com.gs.gestaoativos.domains.DispositivoIoT;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DispositivoIoTResponseDto {
    private Integer idDisp;
    private String identificadorHw;
    private String descricao;
    private LocalDate dataCadastro;
    private String statusDisp;
    private Integer ativoIdAtivo;
    private String ativoMarca;
    private String ativoModelo;

    public static DispositivoIoTResponseDto fromEntity(DispositivoIoT dispositivo) {
        return new DispositivoIoTResponseDto(
                dispositivo.getIdDisp(),
                dispositivo.getIdentificadorHw(),
                dispositivo.getDescricao(),
                dispositivo.getDataCadastro(),
                dispositivo.getStatusDisp(),
                dispositivo.getAtivo() != null ? dispositivo.getAtivo().getIdAtivo() : null,
                dispositivo.getAtivo() != null ? dispositivo.getAtivo().getMarca() : null,
                dispositivo.getAtivo() != null ? dispositivo.getAtivo().getModelo() : null
        );
    }
}

