package org.gabrielbarrilli.securitytelegram.relatorio.model;

import org.springframework.stereotype.Component;

@Component
public class QueryEstacionamentoModel {

    public String queryEstacionamentoCpf(String cpf) {
        StringBuilder query = new StringBuilder();

        query.append(
                        """
                                SELECT
                                    c.cpf AS cpf,
                                    c.nome AS nome,
                                
                                    ctv.cor AS cor,
                                    ctv.desconto AS desconto,
                                    ctv.marca AS marca,
                                    ctv.modelo AS modelo,
                                    ctv.placa AS placa,
                                    ctv.numero_recibo AS numero_recibo,
                                    ctv.valor AS valor,
                                    ctv.data_entrada AS data_entrada,
                                    ctv.data_saida AS data_saida,
                                    EXTRACT(EPOCH FROM AGE(ctv.data_saida, ctv.data_entrada)) / 3600 AS horas,
                                    (EXTRACT(EPOCH FROM AGE(ctv.data_saida, ctv.data_entrada)) % 3600) / 60 AS minutos,
                                
                                    v.codigo AS codigo
                                FROM clientes c
                                    LEFT JOIN public.clientes_tem_vagas ctv on c.id = ctv.id_cliente
                                    LEFT JOIN public.vagas v on ctv.id_vaga = v.id
                                WHERE c.cpf = '""").append(cpf)
                .append("' ORDER BY c.nome DESC;");

        return query.toString();
    }
}
