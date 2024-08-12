package org.gabrielbarrilli.securitytelegram.relatorio.mapper;

import org.gabrielbarrilli.securitytelegram.relatorio.response.QueryCpfClienteResponse;
import org.springframework.jdbc.core.RowMapper;

public class QueryEstacionamentoMapper {

    public static RowMapper<QueryCpfClienteResponse> rowMapperCpfCliente =
            ((rs, rowNum) -> new QueryCpfClienteResponse(
                    rs.getString("cpf"),
                    rs.getString("nome"),
                    rs.getString("cor"),
                    rs.getBigDecimal("desconto"),
                    rs.getString("marca"),
                    rs.getString("modelo"),
                    rs.getString("placa"),
                    rs.getString("numero_recibo"),
                    rs.getBigDecimal("valor"),
                    rs.getString("codigo"),
                    rs.getTimestamp("data_entrada"),
                    rs.getTimestamp("data_saida"),
                    rs.getBigDecimal("horas"),
                    rs.getBigDecimal("minutos")
            ));
}
