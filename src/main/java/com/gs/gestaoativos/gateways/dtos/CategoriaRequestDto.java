package com.gs.gestaoativos.gateways.dtos;

import com.gs.gestaoativos.domains.Categoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaRequestDto {

    private Integer idCateg;

    @NotBlank(message = "Nome da categoria é obrigatório")
    @Size(max = 50, message = "Nome da categoria deve ter no máximo 50 caracteres")
    private String nomeCateg;

    @Size(max = 200, message = "Descrição deve ter no máximo 200 caracteres")
    private String descCateg;

    public Categoria toEntity() {
        return Categoria.builder()
                .idCateg(this.idCateg)
                .nomeCateg(this.nomeCateg)
                .descCateg(this.descCateg)
                .build();
    }
}

