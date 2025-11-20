package com.gs.gestaoativos.gateways.dtos;

import com.gs.gestaoativos.domains.Colaborador;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColaboradorRequestDto {

    private Long idColab;

    @NotBlank(message = "Nome do colaborador é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nomeColab;

    @Size(max = 11, message = "CPF deve ter no máximo 11 caracteres")
    private String cpfColab;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    @Size(max = 80, message = "Email deve ter no máximo 80 caracteres")
    private String emailColab;

    @Size(max = 16, message = "Telefone deve ter no máximo 16 caracteres")
    private String telColab;

    @Size(max = 3, message = "Status deve ter no máximo 3 caracteres")
    private String statusColab = "ATV";

    @Size(max = 30, message = "Função deve ter no máximo 30 caracteres")
    private String funcaoColab;

    @Size(max = 50, message = "Área deve ter no máximo 50 caracteres")
    private String areaColab;

    private Long responsavel;

    @Size(max = 100, message = "Empresa deve ter no máximo 100 caracteres")
    private String empresa;

    @Size(max = 4, message = "Ramal interno deve ter no máximo 4 caracteres")
    private String ramalInterno;

    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    private String senha;

    @Size(max = 20, message = "Role deve ter no máximo 20 caracteres")
    private String role;

    public Colaborador toEntity() {
        return Colaborador.builder()
                .idColab(this.idColab)
                .nomeColab(this.nomeColab)
                .cpfColab(this.cpfColab)
                .emailColab(this.emailColab)
                .telColab(this.telColab)
                .statusColab(this.statusColab)
                .funcaoColab(this.funcaoColab)
                .areaColab(this.areaColab)
                .empresa(this.empresa)
                .ramalInterno(this.ramalInterno)
                .senha(this.senha)
                .role(this.role != null ? this.role : "USER")
                .build();
    }
}

