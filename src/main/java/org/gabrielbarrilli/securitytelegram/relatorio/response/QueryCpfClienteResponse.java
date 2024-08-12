package org.gabrielbarrilli.securitytelegram.relatorio.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

public record QueryCpfClienteResponse(
        String cpf,
        String nome,
        String cor,
        BigDecimal desconto,
        String marca,
        String modelo,
        String placa,
        String numero_recibo,
        BigDecimal valor,
        String codigo,
        Timestamp data_entrada,
        Timestamp data_saida,
        BigDecimal hours,
        BigDecimal minutes
) {
}
