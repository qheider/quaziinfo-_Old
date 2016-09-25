package org.witchcraft.action.test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.query.JRJpaQueryExecuterFactory;

import org.testng.annotations.BeforeClass;
import org.witchcraft.seam.action.BaseAction;
import com.oreon.kg.domain.users.User;



public class BaseReportsTest extends BaseTest<User> {

	protected JasperReport jasperReport;
	protected JasperPrint jasperPrint;

	@BeforeClass
	public void init() {
		super.init();
	}

	// @Test
	public void runReportTest(String reportName) throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(JRJpaQueryExecuterFactory.PARAMETER_JPA_ENTITY_MANAGER, em);

		InputStream reportStreamXML = this.getClass().getResourceAsStream(
				"/reports/" + reportName + ".jrxml");

		jasperReport = JasperCompileManager.compileReport(reportStreamXML);
		jasperPrint = JasperFillManager.fillReport(jasperReport, parameters);
		JasperExportManager.exportReportToPdfFile(jasperPrint,
				"src/main/resources/reports/" + reportName + ".pdf");

	}

	

	@Override
	public BaseAction<User> getAction() {
		// TODO Auto-generated method stub
		return null;
	}

}
