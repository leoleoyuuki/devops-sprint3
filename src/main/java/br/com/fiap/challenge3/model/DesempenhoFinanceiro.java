package br.com.fiap.challenge3.model;

import jakarta.persistence.*;
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
@Table(name = "DesempenhoFinanceiro")
public class DesempenhoFinanceiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PositiveOrZero(message = "A receita deve ser zero ou positivo")
    @NotNull(message = "A receita é obrigatória")
    private Double receita;

    @PositiveOrZero(message = "O lucro deve ser zero ou positivo")
    @NotNull(message = "O lucro é obrigatório")
    private Double lucro;

    @DecimalMin(value = "0.0", inclusive = true, message = "O crescimento não pode ser menor que 0%")
    @DecimalMax(value = "100.0", inclusive = true, message = "O crescimento não pode ser maior que 100%")
    @NotNull(message = "O crescimento é obrigatório")
    private Double crescimento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa")
    private Empresas empresa;
}
