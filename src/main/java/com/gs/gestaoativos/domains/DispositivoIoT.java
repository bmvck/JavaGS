package com.gs.gestaoativos.domains;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "dispositivo_iot")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idDisp")
public class DispositivoIoT {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_dispositivo_iot")
    @SequenceGenerator(name = "seq_dispositivo_iot", sequenceName = "seq_dispositivo_iot", allocationSize = 1)
    @Column(name = "id_disp")
    private Integer idDisp;

    @NotBlank(message = "Identificador de hardware é obrigatório")
    @Size(max = 50, message = "Identificador deve ter no máximo 50 caracteres")
    @Column(name = "identificador_hw", nullable = false, unique = true, length = 50)
    private String identificadorHw;

    @Size(max = 100, message = "Descrição deve ter no máximo 100 caracteres")
    @Column(length = 100)
    private String descricao;

    @Column(name = "data_cadastro", nullable = false)
    @Builder.Default
    private LocalDate dataCadastro = LocalDate.now();

    @Size(max = 20, message = "Status deve ter no máximo 20 caracteres")
    @Column(name = "status_disp", length = 20)
    @Builder.Default
    private String statusDisp = "ONLINE";

    @NotNull(message = "Ativo é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ativo_id_ativo", nullable = false)
    private Ativo ativo;
}

