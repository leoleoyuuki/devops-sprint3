package br.com.fiap.challenge3.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "TendenciasGastos")
public class TendenciasGastos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 1900, message = "O ano deve ser maior ou igual a 1900")
    @Max(value = 2025, message = "O ano deve ser menor ou igual a 2025")
    @NotNull(message = "O ano é obrigatório")
    private Integer ano;

    @PositiveOrZero(message = "O gasto em marketing deve ser zero ou positivo")
    @NotNull(message = "O gasto em marketing é obrigatório")
    private Double gastoMarketing;

    @PositiveOrZero(message = "O gasto em automação deve ser zero ou positivo")
    @NotNull(message = "O gasto em automação é obrigatório")
    private Double gastoAutomacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa")
    private Empresas empresa;
}


