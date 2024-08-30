package org.gabrielbarrilli.securitytelegram.relatorio.service;

import org.gabrielbarrilli.securitytelegram.relatorio.mapper.QueryEstacionamentoMapper;
import org.gabrielbarrilli.securitytelegram.relatorio.model.QueryEstacionamentoModel;
import org.gabrielbarrilli.securitytelegram.relatorio.response.QueryCpfClienteResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueryEstacionamentoService {

    private final QueryEstacionamentoModel queryEstacionamentoModel;
    private final JdbcTemplate jdbcTemplate;

    public QueryEstacionamentoService(QueryEstacionamentoModel queryEstacionamentoModel, JdbcTemplate jdbcTemplate) {
        this.queryEstacionamentoModel = queryEstacionamentoModel;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<QueryCpfClienteResponse> buscarCpfClienteRelatorio(String cpf) {
        StringBuilder query = new StringBuilder();

        query.append(queryEstacionamentoModel.queryEstacionamentoCpf(cpf));

        return jdbcTemplate.query(query.toString(), QueryEstacionamentoMapper.rowMapperCpfCliente);
    }
}
