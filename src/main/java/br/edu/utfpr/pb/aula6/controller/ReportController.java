package br.edu.utfpr.pb.aula6.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.edu.utfpr.pb.aula6.report.SerieReport;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

@Controller
@RequestMapping("report")
public class ReportController {

	@Autowired
	private SerieReport serieReport;
	
	@GetMapping("serie/visualizar")
	public void serieVisualizarReport(HttpServletResponse response) throws JRException, SQLException, IOException {
		JasperPrint jasperPrint = serieReport.generateReport(
									" .: Relatório de Séries :. ", 
									"classpath:/report/rel-series.jrxml", 
									new BigDecimal(7));
		visualiar(jasperPrint, response);
	}

	private void visualiar(JasperPrint jasperPrint, 
			HttpServletResponse response) throws IOException, JRException {
		response.setContentType("application/pdf");
		OutputStream out = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint, out);
	}
	
	@GetMapping("serie/download")
	public void SerieDownloadReport(HttpServletResponse response) throws JRException, SQLException, IOException {
		JasperPrint jasperPrint = serieReport.generateReport(
				" .: Relatório de Séries :. ", 
				"classpath:/report/rel-series.jrxml", 
				new BigDecimal(70));
		download(jasperPrint, response);
	}

	private void download(JasperPrint jasperPrint, 
			HttpServletResponse response) 
					throws IOException, JRException {
		
		response.setContentType("application/x-download");
		response.setHeader("Content-Disposition", 
	String.format("attachment;filename=\"RelatorioSerie.pdf\"") );
		OutputStream out = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(
				jasperPrint, out);
		
	}
	
}
