package com.gs.gestaoativos.gateways.dtos;

import com.gs.gestaoativos.domains.Categoria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaResponseDto {
    private Integer idCateg;
    private String nomeCateg;
    private String descCateg;

    public static CategoriaResponseDto fromEntity(Categoria categoria) {
        return new CategoriaResponseDto(
                categoria.getIdCateg(),
                categoria.getNomeCateg(),
                categoria.getDescCateg()
        );
    }
}

