package com.gs.gestaoativos.gateways.dtos;

import com.gs.gestaoativos.domains.Colaborador;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColaboradorResponseDto {
    private Long idColab;
    private String nomeColab;
    private String cpfColab;
    private String emailColab;
    private String telColab;
    private String statusColab;
    private String funcaoColab;
    private String areaColab;
    private Long responsavel;
    private String responsavelNome;
    private String empresa;
    private String ramalInterno;
    private String role;

    public static ColaboradorResponseDto fromEntity(Colaborador colaborador) {
        return new ColaboradorResponseDto(
                colaborador.getIdColab(),
                colaborador.getNomeColab(),
                colaborador.getCpfColab(),
                colaborador.getEmailColab(),
                colaborador.getTelColab(),
                colaborador.getStatusColab(),
                colaborador.getFuncaoColab(),
                colaborador.getAreaColab(),
                colaborador.getResponsavel() != null ? colaborador.getResponsavel().getIdColab() : null,
                colaborador.getResponsavel() != null ? colaborador.getResponsavel().getNomeColab() : null,
                colaborador.getEmpresa(),
                colaborador.getRamalInterno(),
                colaborador.getRole()
        );
    }
}

