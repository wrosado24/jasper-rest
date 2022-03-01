package com.nivutech.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;

import com.nivutech.entity.Persona;
import com.nivutech.service.PersonaService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@WebServlet(name = "Persona", urlPatterns = { "/personaSrv" })
public class PersonaServlet extends HttpServlet {

	@Autowired
	private PersonaService personaService;

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, JRException {
		response.setContentType("application/json;charset=UTF-8");

		if (request.getParameter("accion") != null) {
			String accion = request.getParameter("accion");
			switch (accion) {
			case "exportarPDF":
				this.exportarPDF(request, response);
				break;
			case "exportarXLS":
				this.exportarXLS(request, response);
			}
		} else {
			this.printError("No se indico la operacion a realizar", response);
		}
	}

	private void exportarPDF(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletOutputStream out = response.getOutputStream();
		try {
			
			//InputStream jasperRep = this.getServletConfig().getServletContext()
					//.getResourceAsStream("application.properties");

			System.out.println("Ruta jasper: " + this.getServletConfig().getServletContext().getResourcePaths("/src/main/resources/application.properties"));

			List<Persona> lista = personaService.list();

			File file = ResourceUtils.getFile("classpath:reporte_not_parameter.jasper");
			JasperReport report = (JasperReport) JRLoader.loadObject(file);
			JRBeanArrayDataSource ds = new JRBeanArrayDataSource(lista.toArray());

			response.setContentType("application/pdf");
			response.addHeader("Content-disposition", "inline; filename=reporte001.pdf");
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, null, ds);
			JasperExportManager.exportReportToPdfStream(jasperPrint, out);
			
			out.flush();
			out.close();
		} catch (Exception e) {
			response.setContentType("text/plain");
			out.print("ocurrió un error al intentar generar el reporte:" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void exportarXLS(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, JRException {
		String sourceFileName = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "reporte_not_parameter.jasper").getAbsolutePath();
		List<Persona> lista = personaService.list();
		JRBeanArrayDataSource ds = new JRBeanArrayDataSource(lista.toArray());
		JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFileName, null, ds);
		JRXlsxExporter exporter = new JRXlsxExporter();
        SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
        reportConfigXLS.setSheetNames(new String[] { "sheet1" });
        exporter.setConfiguration(reportConfigXLS);
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
        response.setHeader("Content-Disposition", "attachment;filename=jasperReport.xlsx");
        response.setContentType("application/octet-stream");
        exporter.exportReport();
	}

	private void printError(String msjError, HttpServletResponse response) throws IOException {
		response.getWriter().print("{\"msj\": \"" + msjError + "\"}");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			processRequest(request, response);
		} catch (ServletException | IOException | JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			processRequest(request, response);
		} catch (ServletException | IOException | JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
