package com.gs.gestaoativos.gateways.dtos;

import com.gs.gestaoativos.domains.DispositivoIoT;
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
public class DispositivoIoTRequestDto {

    private Integer idDisp;

    @NotBlank(message = "Identificador de hardware é obrigatório")
    @Size(max = 50, message = "Identificador deve ter no máximo 50 caracteres")
    private String identificadorHw;

    @Size(max = 100, message = "Descrição deve ter no máximo 100 caracteres")
    private String descricao;

    private LocalDate dataCadastro;

    @Size(max = 20, message = "Status deve ter no máximo 20 caracteres")
    private String statusDisp = "ONLINE";

    @NotNull(message = "Ativo é obrigatório")
    private Integer ativoIdAtivo;

    public DispositivoIoT toEntity() {
        return DispositivoIoT.builder()
                .idDisp(this.idDisp)
                .identificadorHw(this.identificadorHw)
                .descricao(this.descricao)
                .dataCadastro(this.dataCadastro != null ? this.dataCadastro : LocalDate.now())
                .statusDisp(this.statusDisp != null ? this.statusDisp : "ONLINE")
                .build();
    }
}

