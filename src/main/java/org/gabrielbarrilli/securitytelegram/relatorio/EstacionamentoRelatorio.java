package org.gabrielbarrilli.securitytelegram.relatorio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EstacionamentoRelatorio {

    private String cpf;

    private String nome;

    private String cor;

    private BigDecimal desconto;

    private String marca;

    private String modelo;

    private String placa;

    private String numero_recibo;

    private BigDecimal valor;

    private String codigo;

    private Timestamp data_entrada;

    private Timestamp data_saida;

    private Long hours;

    private Long minutes;
}
