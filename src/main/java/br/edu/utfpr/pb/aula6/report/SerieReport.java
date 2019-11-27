package br.edu.utfpr.pb.aula6.report;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@Transactional
@Repository
public class SerieReport {
	
	@Autowired
	@Qualifier("jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	public JasperPrint generateReport(String titulo,
							String caminho,
							BigDecimal nota) throws JRException, SQLException, IOException {
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		String path = resourceLoader.getResource(caminho).getURI().getPath();
		
		JasperReport jasperReport = JasperCompileManager.compileReport(path);
		
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("TITULO", titulo);
		parameters.put("NOTA", nota);
		
		JasperPrint print = JasperFillManager.fillReport(jasperReport,
				parameters, conn);
		
		return print;
	}

}



