package org.gabrielbarrilli.securitytelegram.relatorio;

import org.springframework.stereotype.Service;

@Service
public class EstacionamentoService {

    private final ReportImageUtil reportImageUtil;

    public EstacionamentoService(ReportImageUtil reportImageUtil) {
        this.reportImageUtil = reportImageUtil;
    }
}
