package br.chronos.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@NoArgsConstructor
@Table(name = "calendario_local_2")
public class Local {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "bl_permitir_chamada_usina", nullable = false)
    private Boolean permitirChamadaUsina = false;

    @NotNull
    @Column(name = "bl_permitir_carregamento", nullable = false)
    private Boolean permitirCarregamento = true;

    @NotNull(message = "Descrição não pode ser nula")
    @Column(name = "tx_descricao", nullable = false)
    private String descricao;

    @NotNull(message = "Tempo de operação não pode ser nulo")
    @Column(name = "nr_tempo_operacao", nullable = false)
    private Integer tempoOperacao;

    @NotNull(message = "Cota de operação não pode ser nula")
    @Column(name = "nr_cota_operacao", nullable = false)
    private Integer cotaOperacao;
}