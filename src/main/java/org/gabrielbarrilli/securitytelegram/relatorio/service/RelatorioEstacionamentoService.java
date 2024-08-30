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

        List<EstacionamentoRelatorio> estacionamentoRelatorio = estacionamentoRelatorioConvert(estacionamento);

        List<EstacionamentoRelatorio> estacionamentoRelatorioList = new ArrayList<>();

        estacionamentoRelatorioList.addAll(estacionamentoRelatorio);

        estacionamentoRelatorio = criaRelatorio(estacionamento);

        final Map<String, Object> PARAMETROS = new HashMap<>();
        getParametros(PARAMETROS, parking, estacionamentoRelatorio);

        printPDF(MODELO_RELATORIO_ESTACIONAMENTO, estacionamentoRelatorioList, fileName, PARAMETROS, response);
    }

    private List<EstacionamentoRelatorio> estacionamentoRelatorioConvert(List<QueryCpfClienteResponse> estacionamento) {

        List<EstacionamentoRelatorio> estacionamentoRelatorioList = new ArrayList<>();

        estacionamento.forEach(
                estacionamentoRelatorio -> {
                    estacionamentoRelatorioList.add(
                            new EstacionamentoRelatorio(
                                    estacionamentoRelatorio.cpf(),
                                    estacionamentoRelatorio.nome(),
                                    estacionamentoRelatorio.cor(),
                                    estacionamentoRelatorio.desconto(),
                                    estacionamentoRelatorio.marca(),
                                    estacionamentoRelatorio.modelo(),
                                    estacionamentoRelatorio.placa(),
                                    estacionamentoRelatorio.numero_recibo(),
                                    estacionamentoRelatorio.valor(),
                                    estacionamentoRelatorio.codigo(),
                                    estacionamentoRelatorio.data_entrada(),
                                    estacionamentoRelatorio.data_saida(),
                                    estacionamentoRelatorio.hours().longValue(),
                                    estacionamentoRelatorio.minutes().longValue()
                            )
                    );
                }
        );

        return estacionamentoRelatorioList;
    }

    private static List<EstacionamentoRelatorio> criaRelatorio(List<QueryCpfClienteResponse> clienteResponseList) {

        List<EstacionamentoRelatorio> estacionamentoRelatorioList = new ArrayList<>();
        clienteResponseList.forEach(
                clienteResponse -> {
                    EstacionamentoRelatorio estacionamentoRelatorio1 = new EstacionamentoRelatorio();

                    estacionamentoRelatorio1.setCpf(clienteResponse.cpf());
                    estacionamentoRelatorio1.setNome(clienteResponse.nome());
                    estacionamentoRelatorio1.setCor(clienteResponse.cor());
                    estacionamentoRelatorio1.setDesconto(clienteResponse.desconto());
                    estacionamentoRelatorio1.setMarca(clienteResponse.marca());
                    estacionamentoRelatorio1.setModelo(clienteResponse.modelo());
                    estacionamentoRelatorio1.setPlaca(clienteResponse.placa());
                    estacionamentoRelatorio1.setNumero_recibo(clienteResponse.numero_recibo());
                    estacionamentoRelatorio1.setValor(clienteResponse.valor());
                    estacionamentoRelatorio1.setCodigo(clienteResponse.codigo());
                    estacionamentoRelatorio1.setData_entrada(clienteResponse.data_entrada());
                    estacionamentoRelatorio1.setData_saida(clienteResponse.data_saida());
                    estacionamentoRelatorio1.setHours(clienteResponse.hours().longValue());
                    estacionamentoRelatorio1.setMinutes(clienteResponse.minutes().longValue());

                    estacionamentoRelatorioList.add(
                            estacionamentoRelatorio1
                    );
                }
        );

        return estacionamentoRelatorioList;
    }

    private void getParametros(Map<String, Object> parametros, BufferedImage parking, List<EstacionamentoRelatorio> estacionamentoRelatorioList) {

        parametros.put("parking", parking);
        parametros.put("cpf", estacionamentoRelatorioList.get(0).getCpf());
        parametros.put("nome", estacionamentoRelatorioList.get(0).getNome());
        parametros.put("cor", estacionamentoRelatorioList.get(0).getCor());
        estacionamentoRelatorioList.forEach(
                estacionamentoRelatorio -> {
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
        );

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












