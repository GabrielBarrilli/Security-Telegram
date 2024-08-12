package org.gabrielbarrilli.securitytelegram.relatorio.service;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.gabrielbarrilli.securitytelegram.relatorio.EstacionamentoRelatorio;
import org.gabrielbarrilli.securitytelegram.relatorio.ReportImageUtil;
import org.gabrielbarrilli.securitytelegram.relatorio.response.QueryCpfClienteResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

@Service
public class RelatorioEstacionamentoService {

    private final ReportImageUtil reportImageUtil;
    private final QueryEstacionamentoService queryEstacionamentoService;
    private static final Map<String, Object> PARAMETROS = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(RelatorioEstacionamentoService.class);

    public RelatorioEstacionamentoService(ReportImageUtil reportImageUtil, QueryEstacionamentoService queryEstacionamentoService) {
        this.reportImageUtil = reportImageUtil;
        this.queryEstacionamentoService = queryEstacionamentoService;
    }

    public void estacionamentoRelatorio(String cpf, HttpServletResponse response) throws Exception {

        final String MODELO_RELATORIO_ESTACIONAMENTO = "/reports/estacionamentos.jrxml";

        String fileName = "estacionamentos";

        BufferedImage parking = reportImageUtil.getBufferedImage("reports/images/parking.png");

        var estacionamento = queryEstacionamentoService.buscarCpfClienteRelatorio(cpf);

        EstacionamentoRelatorio estacionamentoRelatorio = estacionamentoRelatorioConvert(estacionamento);

        List<EstacionamentoRelatorio> estacionamentoRelatorioList = new ArrayList<>();

        estacionamentoRelatorioList.add(estacionamentoRelatorio);

        estacionamentoRelatorio = criaRelatorio(estacionamento);

        final Map<String, Object> PARAMETROS = new HashMap<>();
        getParametros(PARAMETROS, parking, estacionamentoRelatorio);

        printPDF(MODELO_RELATORIO_ESTACIONAMENTO, estacionamentoRelatorioList, fileName, PARAMETROS, response);
    }

    private EstacionamentoRelatorio estacionamentoRelatorioConvert(QueryCpfClienteResponse estacionamento) {

        return new EstacionamentoRelatorio(
                estacionamento.cpf(),
                estacionamento.nome(),
                estacionamento.cor(),
                estacionamento.desconto(),
                estacionamento.marca(),
                estacionamento.modelo(),
                estacionamento.placa(),
                estacionamento.numero_recibo(),
                estacionamento.valor(),
                estacionamento.codigo(),
                estacionamento.data_entrada(),
                estacionamento.data_saida(),
                estacionamento.hours().longValue(),
                estacionamento.minutes().longValue()
        );
    }

    private static EstacionamentoRelatorio criaRelatorio(QueryCpfClienteResponse clienteResponse) {

        EstacionamentoRelatorio estacionamentoRelatorio = new EstacionamentoRelatorio();
        estacionamentoRelatorio.setCpf(clienteResponse.cpf());
        estacionamentoRelatorio.setNome(clienteResponse.nome());
        estacionamentoRelatorio.setCor(clienteResponse.cor());
        estacionamentoRelatorio.setDesconto(clienteResponse.desconto());
        estacionamentoRelatorio.setMarca(clienteResponse.marca());
        estacionamentoRelatorio.setModelo(clienteResponse.modelo());
        estacionamentoRelatorio.setPlaca(clienteResponse.placa());
        estacionamentoRelatorio.setNumero_recibo(clienteResponse.numero_recibo());
        estacionamentoRelatorio.setValor(clienteResponse.valor());
        estacionamentoRelatorio.setCodigo(clienteResponse.codigo());
        estacionamentoRelatorio.setData_entrada(clienteResponse.data_entrada());
        estacionamentoRelatorio.setData_saida(clienteResponse.data_saida());
        estacionamentoRelatorio.setHours(clienteResponse.hours().longValue());
        estacionamentoRelatorio.setMinutes(clienteResponse.minutes().longValue());

        return estacionamentoRelatorio;
    }

    private void getParametros(Map<String, Object> parametros, BufferedImage parking, EstacionamentoRelatorio estacionamentoRelatorio) {

        parametros.put("parking", parking);
        parametros.put("cpf", estacionamentoRelatorio.getCpf());
        parametros.put("nome", estacionamentoRelatorio.getNome());
        parametros.put("cor", estacionamentoRelatorio.getCor());
        parametros.put("desconto", estacionamentoRelatorio.getDesconto());
        parametros.put("marca", estacionamentoRelatorio.getMarca());
        parametros.put("modelo", estacionamentoRelatorio.getModelo());
        parametros.put("placa", estacionamentoRelatorio.getPlaca());
        parametros.put("numero_recibo", estacionamentoRelatorio.getNumero_recibo());
        parametros.put("valor", estacionamentoRelatorio.getValor());
        parametros.put("codigo", estacionamentoRelatorio.getCodigo());
        parametros.put("data_entrada", estacionamentoRelatorio.getData_entrada());
        parametros.put("data_saida", estacionamentoRelatorio.getData_saida());
        parametros.put("hours", estacionamentoRelatorio.getHours());
        parametros.put("minutes", estacionamentoRelatorio.getMinutes());
    }

    private void printPDF(String jasperPath, List<?> dataSource, String fileName, Map<String, Object> parametros, HttpServletResponse response) throws Exception {
        InputStream relativeWebPath = this.getClass().getResourceAsStream(jasperPath);
        logger.info("relativeWebPath: {}", relativeWebPath);
        logger.info("jasperPath: {}", jasperPath);
        OutputStream outputStream = response.getOutputStream();
        response.setContentType("application/pdf");
        response.setHeader("Content-disposition", "inline; filename=" + fileName);

        var report = JasperCompileManager.compileReport(relativeWebPath);

        JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(dataSource, false);
        JasperPrint print = JasperFillManager.fillReport(report, parametros, source);
        JasperExportManager.exportReportToPdfStream(print, outputStream);
        outputStream.flush();
        outputStream.close();
    }
}












