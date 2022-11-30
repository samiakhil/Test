package com.Saviynt.Reports;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.collections.Lists;
import org.testng.internal.Utils;
import org.testng.log4testng.Logger;
import org.testng.xml.XmlSuite;

//* Reporter that generates a single-page HTML report of the test results.

public class TestNGCustomReportListener extends Listeners implements IReporter {

	private static final Logger LOG = Logger.getLogger(TestNGCustomReportListener.class);

	protected PrintWriter writer;

	protected final List<SuiteResult> suiteResults = Lists.newArrayList();

	// Reusable buffer
	private final StringBuilder buffer = new StringBuilder();

	private String dReportTitle = "Saviynt Report";
	private String dReportFileName = "Saviynt-emailable-report.html";

	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		try {
			writer = createWriter(outputDirectory);
		} catch (IOException e) {
			LOG.error("Unable to create output file", e);
			return;
		}
		for (ISuite suite : suites) {
			suiteResults.add(new SuiteResult(suite));
		}

		writeDocumentStart();
		writeHead();
		writeBody();
		writeDocumentEnd();

		writer.close();
	}

	protected PrintWriter createWriter(String outdir) throws IOException {
		new File(outdir).mkdirs();
		return new PrintWriter(new BufferedWriter(new FileWriter(new File(outdir, dReportFileName))));
	}

	protected void writeReportTitle(String title) {
		writer.println("<center><h1>" + title + "</h1></center>");
	}

	protected void writeDocumentStart() {
		writer.println("<!DOCTYPE html PUBLIC -//W3C//DTD XHTML 1.1//EN/http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd>");
		writer.println("<html xmlns=http://www.w3.org/1999/xhtml>");
	}

	protected void writeHead() {
		writer.println("<head>");
		writer.println("<meta http-equiv=content-type content=text/html; charset=UTF-8/>");
		writer.println("<title>TestNG Report</title>");
		writeStylesheet();
		writer.println("</head>");
	}

	protected void writeStylesheet() {
		writer.println(
				"<link rel=stylesheet href=https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css>");
		writer.println("<script src=\"https://code.jquery.com/jquery-3.4.1.slim.min.js\" integrity=\"sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n\" crossorigin=\"anonymous\"></script>");
		writer.println("<script src=\"https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js\" integrity=\"sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo\" crossorigin=\"anonymous\"></script>");
		writer.println("<script src=\"https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js\" integrity=\"sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6\" crossorigin=\"anonymous\"></script>");
		writer.println("<style type=\"text/css\"> table {margin-bottom:10px;border-collapse:collapse;empty-cells:show}#summary {margin-top:30px}h1 <tr>{font-size:30px;}body {width:100%;}th,td {padding: 8px}th {vertical-align:bottom}td {vertical-align:top}table a {font-weight:bold;color:#0D1EB6;}.easy-overview {margin-left: auto; margin-right: auto;} .easy-test-overview tr:first-child {background-color:#FFA500 !important;}.stripe td {background-color:#E6EBF9}.num {text-align:right}.passedodd td {background-color: #3F3}.passedeven td {background-color: #0A0}.skippedodd td {background-color: #DDD}.skippedeven td {background-color: #CCC}.failedodd td,.attn {background-color: #F33}.failedeven td,.stripe .attn {background-color: #D00}.stacktrace {font-family:monospace}.totop {font-size:85%;text-align:center;border-bottom:2px solid #000}.invisible {display:none} .header-custom1 h1{color: white;text-transform: uppercase;font-size: 38px;}.easy-test-summary a {color: white;}.easy-test-overview a {color: #212529;}.easy-test-overview a:hover {	color: #212529;}.easy-test-summary tr  td{color:white;}.dtable{display:table;}.display_n{display:none;}#last_div > h2{display:none;}.easy-test-summary{display:none;} </style>");
		writer.println("<script> function showonly(browser){ var display_none_row = \"\"; if(browser == \"all\"){ display_none_row = \"table-row\"; jQuery( \"tr\" ).filter(\".total\").css( \"display\", display_none_row ); }else{ display_none_row = \"none\";} jQuery( \"tr\" ).filter(\".ie\").css( \"display\", display_none_row ); jQuery( \"tr\" ).filter(\".chrome\").css( \"display\", display_none_row ); jQuery( \"tr\" ).filter(\".total\").css( \"display\", display_none_row ); var class_browser = \".\"+browser; jQuery( \"tr\" ).filter(class_browser).css( \"display\", \"table-row\" ); $(\".easy-test-summary\").css(\"display\",\"none\"); $( \"tbody.display_n\" ).each(function() {$( this ).hide();});$( \"div.display_n\" ).each(function() {$( this ).hide();});} function open_div_show(div_id){$(\".easy-test-summary\").css(\"display\",\"block\"); $( \"tbody.display_n\" ).each(function() { $( this ).hide();  }); $( \"div.display_n\" ).each(function() { $( this ).hide();}); /*jQuery( \".div\" ).filter(\".display_n\").css( \"display\", \"none\" ); 	*/	$(div_id).css(\"display\",\"table-row-group\"); } function show_last_element(div_id){ $(\".easy-test-summary\").css(\"display\",\"block\"); $( \"div.display_n\" ).each(function() { $( this ).hide(); }); $(div_id).css(\"display\",\"block\"); } </script>");
	}

	protected void writeBody() {
		writer.println("<body>");
		writer.println("<div class=\"container-fluid\">");
		writeSuiteSummary();
		writeScenarioSummary();
		writeScenarioDetails();
		writer.println("</div>");
		writer.println("</body>");
	}

	protected void writeDocumentEnd() {
		writer.println("</html>");
	}

	protected void writeSuiteSummary() {
		NumberFormat integerFormat = NumberFormat.getIntegerInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");

		int totalTestsCount = 0;
		int totalPassedTests = 0;
		int totalSkippedTests = 0;
		int totalFailedTests = 0;
		long totalDuration = 0;

		writer.println("<div class=\"row\">");
		writer.println("<div class=\"easy-test-overview\">");
		writer.println("<table class=\"table-bordered easy-overview\">");
		writer.print("<tr><td colspan=\"9\" class=\"header-custom1\">");
		writer.print("<center>");
		writeReportTitle(dReportTitle);
		writer.print("</center><td>");
		writer.print("<td colspan=\"3\"> <center class=\"p-1\">");
		writer.print("<a onclick=\"showonly(\'all\')\"><svg version=\"1.1\" id=\"Layer_1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" x=\"0px\" y=\"0px\" width=\"35px\" height=\"35px\" viewBox=\"0 0 24 24\" enable-background=\"new 0 0 24 24\" xml:space=\"preserve\"><g id=\"icon\"><path d=\"M2.9,2h7.2c0.495,0,0.9,0.405,0.9,0.9v7.2c0,0.495,-0.405,0.9,-0.9,0.9H2.9c-0.495,0,-0.9,-0.405,-0.9,-0.9V2.9C2,2.405,2.405,2,2.9,2zM13,2.9v7.2c0,0.495,0.405,0.9,0.9,0.9h7.2c0.495,0,0.9,-0.405,0.9,-0.9V2.9c0,-0.495,-0.405,-0.9,-0.9,-0.9h-7.2C13.405,2,13,2.405,13,2.9zM2,13.9v7.2c0,0.495,0.405,0.9,0.9,0.9h7.2c0.495,0,0.9,-0.405,0.9,-0.9v-7.2c0,-0.495,-0.405,-0.9,-0.9,-0.9H2.9C2.405,13,2,13.405,2,13.9zM13,13.9v7.2c0,0.495,0.405,0.9,0.9,0.9h7.2c0.495,0,0.9,-0.405,0.9,-0.9v-7.2c0,-0.495,-0.405,-0.9,-0.9,-0.9h-7.2C13.405,13,13,13.405,13,13.9z\" fill=\"#5D9CEC\"/><path d=\"M11,2.9v7.2c0,0.495,-0.405,0.9,-0.9,0.9H5.9818l2.8364,-9H10.1C10.595,2,11,2.405,11,2.9zM21.1,2h-1.2818l-2.8364,9H21.1c0.495,0,0.9,-0.405,0.9,-0.9V2.9C22,2.405,21.595,2,21.1,2zM21.1,13h-1.2818l-2.8364,9H21.1c0.495,0,0.9,-0.405,0.9,-0.9v-7.2C22,13.405,21.595,13,21.1,13zM10.1,13H8.8182l-2.8364,9H10.1c0.495,0,0.9,-0.405,0.9,-0.9v-7.2C11,13.405,10.595,13,10.1,13z\" fill=\"#4B89DC\" opacity=\"0.8\"/></g></svg>	</a>");
		writer.print("<a onclick=\"showonly(\'chrome\')\"><svg version=\"1.1\" id=\"Layer_1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" x=\"0px\" y=\"0px\" width=\"35px\" height=\"35px\" viewBox=\"0 0 24 24\" enable-background=\"new 0 0 24 24\" xml:space=\"preserve\"><g id=\"icon\"><path d=\"M12,16.2308c1.3812,0,2.6097,-0.6654,3.3823,-1.6923h-1.69h1.69c0.5324,-0.7076,0.8485,-1.5868,0.8485,-2.5385c0,-2.3329,-1.8979,-4.2308,-4.2308,-4.2308v1.6923V7.7692c-2.3329,0,-4.2308,1.8979,-4.2308,4.2308c0,0.9517,0.3161,1.8309,0.8485,2.5385h1.69h-1.69C9.3903,15.5654,10.6188,16.2308,12,16.2308z\" fill=\"#4B89DC\"/> <path d=\"M6.9231,12c0,-2.7994,2.2775,-5.0769,5.0769,-5.0769h9.7575C19.9228,3.4041,16.2432,1,12,1c-3.8409,0,-7.2201,1.9698,-9.1876,4.953l4.3173,7.4778C6.9961,12.9767,6.9231,12.4968,6.9231,12z\" fill=\"#DB4453\"/><path d=\"M12,17.0769c-2.3026,0,-4.2508,-1.5414,-4.8703,-3.6461L2.8124,5.953C1.668,7.6884,1,9.7657,1,12c0,5.8592,4.5814,10.6468,10.3571,10.9798l4.3235,-7.4885C14.7549,16.4668,13.4477,17.0769,12,17.0769z\" fill=\"#36BC9B\"/><path d=\"M21.7575,6.9231H12c2.7994,0,5.0769,2.2775,5.0769,5.0769c0,1.3517,-0.532,2.5807,-1.3963,3.4914l-4.3235,7.4885C11.57,22.9921,11.7841,23,12,23c6.0751,0,11,-4.9249,11,-11C23,10.1681,22.5497,8.4423,21.7575,6.9231z\" fill=\"#FFCE55\"/><g><path d=\"M16.07,12c0,-1.1239,-0.4555,-2.1414,-1.1921,-2.8779l-5.7559,5.7559c0.7365,0.7365,1.754,1.1921,2.8779,1.1921C14.2478,16.07,16.07,14.2478,16.07,12z\" fill=\"#231F20\" opacity=\"0.1\"/><path d=\"M19.7782,4.2218L15.578,8.422c0.9157,0.9157,1.482,2.1807,1.482,3.578c0,2.7946,-2.2654,5.06,-5.06,5.06c-1.3973,0,-2.6623,-0.5664,-3.578,-1.482l-4.2002,4.2002C6.2124,21.7688,8.9624,23,12,23c6.0751,0,11,-4.9249,11,-11C23,8.9624,21.7688,6.2124,19.7782,4.2218z\" fill=\"#231F20\" opacity=\"0.1\"/></g></g></svg></a>");
		writer.print("<a onclick=\"showonly(\'ie\')\"><svg version=\"1.1\" id=\"Layer_1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" x=\"0px\" y=\"0px\" width=\"35px\" height=\"35px\" viewBox=\"0 0 24 24\" enable-background=\"new 0 0 24 24\" xml:space=\"preserve\"><g id=\"icon\"><circle cx=\"12\" cy=\"12\" r=\"11\" fill=\"#5D9CEC\"/><path d=\"M18.0008,12.9951H9.2449c0.0939,1.9215,1.0874,2.882,2.9812,2.882c0.6416,0,1.2206,-0.1613,1.7371,-0.4841c0.4849,-0.2785,0.7824,-0.6083,0.8919,-0.9899h2.9343c-0.8919,2.552,-2.7857,3.828,-5.6806,3.828c-1.8938,0,-3.4002,-0.5535,-4.5188,-1.6611c-1.119,-1.1073,-1.6784,-2.5482,-1.6784,-4.323c0,-1.7304,0.5751,-3.1642,1.7254,-4.301c1.1502,-1.1364,2.6409,-1.705,4.4719,-1.705c1.9718,0,3.4977,0.6896,4.5775,2.068C17.6874,9.5706,18.1259,11.1326,18.0008,12.9951zM9.2449,11.0151h5.4225c-0.3132,-1.6132,-1.1972,-2.4201,-2.6525,-2.4201c-0.939,0,-1.6591,0.2936,-2.1597,0.88C9.4796,9.9151,9.2759,10.4287,9.2449,11.0151z\" fill=\"#F6F7FB\"/><path d=\"M17.9142,6.3985c-0.0229,-0.0861,-0.0481,-0.1722,-0.0886,-0.2554c-0.018,-0.0422,-0.0367,-0.0843,-0.0624,-0.1232l-0.0364,-0.0596l-0.0185,-0.0297l-0.0093,-0.0148c-0.0096,-0.0126,0.0083,0.0072,-0.024,-0.032l-0.0673,-0.0849c-0.0129,-0.0151,-0.0188,-0.0262,-0.0374,-0.0446L17.5143,5.7c-0.0374,-0.0343,-0.0782,-0.0749,-0.1154,-0.1019l-0.1129,-0.0804c-0.1539,-0.0991,-0.3191,-0.1748,-0.4841,-0.2242c-0.3313,-0.1026,-0.6616,-0.1325,-0.9802,-0.1315c-0.6379,0.0118,-1.2387,0.1409,-1.8095,0.3169c-0.5719,0.1747,-1.1153,0.3982,-1.6381,0.6418c-0.5237,0.2425,-1.0229,0.5142,-1.5028,0.7977c-0.9602,0.5656,-1.8272,1.2125,-2.5895,1.8802c-0.7681,0.6754,-1.4441,1.3636,-1.9854,2.0758c-0.5423,0.7094,-0.9675,1.4147,-1.286,2.0664c-0.3158,0.6533,-0.5345,1.2486,-0.641,1.7582c-0.1062,0.5094,-0.0831,0.9347,0.0156,1.2025c0.0228,0.0673,0.0451,0.1259,0.0726,0.1732c0.0243,0.0486,0.0454,0.0891,0.0685,0.1181c0.0424,0.0602,0.0643,0.0912,0.0643,0.0912l0.0027,0.0031c0,0,0.0241,0.0268,0.0709,0.0786c0.021,0.0266,0.0563,0.0571,0.0995,0.0928c0.0414,0.0387,0.0937,0.0733,0.154,0.1103c0.2386,0.1553,0.6464,0.2724,1.1654,0.2839c0.4613,0.0098,1.0092,-0.05,1.6214,-0.1753c-0.0382,-0.0357,-0.0771,-0.0705,-0.1145,-0.1075c-0.0806,-0.0797,-0.1574,-0.1617,-0.2321,-0.2449c-0.466,0.183,-0.9012,0.2953,-1.2806,0.3271c-0.4898,0.0411,-0.8785,-0.0518,-1.0962,-0.189c-0.0554,-0.0332,-0.103,-0.0638,-0.1374,-0.0956c-0.0369,-0.0292,-0.0681,-0.0542,-0.0911,-0.0825c-0.0419,-0.0454,-0.0648,-0.0701,-0.0697,-0.0755c-0.0015,-0.0022,-0.0198,-0.0298,-0.0548,-0.0826c-0.0202,-0.0263,-0.038,-0.0633,-0.0586,-0.1075c-0.0238,-0.0427,-0.0422,-0.0962,-0.0613,-0.1575c-0.0819,-0.2432,-0.0814,-0.6414,0.0715,-1.1073c0.1532,-0.4667,0.4555,-0.9939,0.8811,-1.5334c0.4234,-0.5422,0.9679,-1.0988,1.5811,-1.6759c0.6141,-0.5751,1.2908,-1.1899,2.0381,-1.7914c0.7541,-0.6076,1.5669,-1.2132,2.4424,-1.7905c0.8774,-0.5682,1.8233,-1.1081,2.8299,-1.4938c0.2507,-0.0963,0.5052,-0.185,0.7609,-0.2554c0.2547,-0.0743,0.5122,-0.134,0.7664,-0.1668c0.2534,-0.0362,0.5053,-0.0457,0.7337,-0.0092c0.1154,0.0165,0.2202,0.0492,0.3167,0.0918l0.0699,0.0351c0.0236,0.0116,0.0342,0.0224,0.0535,0.0326l0.0271,0.0149c0.0092,0.0045,0.0251,0.0227,0.0373,0.0334l0.0744,0.071l0.0012,0.0016l0.0121,0.0149l0.0247,0.0296c0.0184,0.0186,0.0315,0.0412,0.0448,0.064c0.0307,0.0429,0.0522,0.0931,0.0745,0.144c0.04,0.1041,0.0696,0.22,0.0782,0.3444c0.0189,0.2479,-0.0154,0.5159,-0.0807,0.7792c-0.0913,0.3387,-0.2251,0.6714,-0.3875,0.9913c0.079,0.1163,0.1549,0.2341,0.2251,0.3547c0.2254,-0.382,0.4329,-0.7815,0.585,-1.2175c0.1016,-0.2851,0.1803,-0.5886,0.1915,-0.9173C17.9693,6.7423,17.9549,6.5705,17.9142,6.3985z\" fill=\"#FFCE55\"/><path d=\"M12,23c6.0751,0,11,-4.9249,11,-11c0,-3.0376,-1.2312,-5.7876,-3.2218,-7.7782L4.2218,19.7782C6.2124,21.7688,8.9624,23,12,23z\" fill=\"#231F20\" opacity=\"0.1\"/></g></svg></a>");
		writer.print("<a onclick=\"showonly(\'opera\')\"><svg version=\"1.1\" id=\"Layer_1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" x=\"0px\" y=\"0px\" width=\"35px\" height=\"35px\" viewBox=\"0 0 24 24\" enable-background=\"new 0 0 24 24\" xml:space=\"preserve\"><g id=\"icon\"><circle cx=\"12\" cy=\"12\" r=\"11\" fill=\"#DB4453\"/><path d=\"M12,5c-3.299,0,-5.9733,2.66,-5.9733,7S8.701,19,12,19s5.9733,-2.52,5.9733,-7S15.299,5,12,5zM12,18.3467c-1.5464,0,-2.8,-0.91,-2.8,-6.3933s1.2536,-6.3,2.8,-6.3s2.8,0.8167,2.8,6.3S13.5464,18.3467,12,18.3467z\" fill=\"#FFFFFF\"/><path d=\"M12,23c6.0751,0,11,-4.9249,11,-11c0,-3.0376,-1.2312,-5.7876,-3.2218,-7.7782L4.2218,19.7782C6.2124,21.7688,8.9624,23,12,23z\" fill=\"#231F20\" opacity=\"0.1\"/></g></svg></a>");
		writer.print("<a onclick=\"showonly(\'edge\')\"><svg version=\"1.1\" id=\"Layer_1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" x=\"0px\" y=\"0px\" width=\"35px\" height=\"35px\" viewBox=\"0 0 24 24\" enable-background=\"new 0 0 24 24\" xml:space=\"preserve\"><g id=\"icon\"><circle cx=\"12\" cy=\"12\" r=\"11\" fill=\"#5D9CEC\"/><path d=\"M9.034,13.261c0,0.316,0.05,0.604,0.153,0.863c0.109,0.255,0.251,0.483,0.431,0.682c0.181,0.199,0.392,0.37,0.641,0.513c0.24,0.144,0.501,0.263,0.78,0.355c0.277,0.094,0.567,0.164,0.861,0.209c0.299,0.045,0.588,0.068,0.883,0.068c0.37,0,0.715,-0.027,1.042,-0.082c0.327,-0.058,0.645,-0.133,0.959,-0.23s0.617,-0.212,0.92,-0.343c0.305,-0.131,0.615,-0.275,0.933,-0.435v2.785c-0.355,0.164,-0.704,0.302,-1.053,0.421c-0.349,0.115,-0.697,0.216,-1.053,0.296c-0.355,0.082,-0.715,0.144,-1.083,0.181c-0.368,0.037,-0.745,0.058,-1.138,0.058c-0.523,0,-1.029,-0.058,-1.515,-0.17c-0.486,-0.113,-0.941,-0.275,-1.369,-0.489c-0.427,-0.214,-0.817,-0.472,-1.172,-0.776c-0.357,-0.304,-0.66,-0.647,-0.915,-1.027c-0.255,-0.38,-0.451,-0.797,-0.593,-1.244c-0.137,-0.448,-0.207,-0.924,-0.207,-1.429c0,-0.54,0.078,-1.054,0.235,-1.544c0.159,-0.493,0.384,-0.941,0.68,-1.351c0.294,-0.411,0.654,-0.776,1.079,-1.095s0.902,-0.579,1.434,-0.78c-0.29,0.273,-0.516,0.596,-0.676,0.971c-0.163,0.374,-0.266,0.75,-0.312,1.125h4.964c0,-0.472,-0.05,-0.883,-0.153,-1.236c-0.102,-0.353,-0.266,-0.647,-0.495,-0.879c-0.227,-0.234,-0.519,-0.411,-0.878,-0.526c-0.36,-0.117,-0.793,-0.177,-1.301,-0.177c-0.599,0,-1.199,0.082,-1.798,0.253c-0.599,0.164,-1.168,0.4,-1.709,0.698c-0.54,0.302,-1.037,0.657,-1.491,1.064c-0.453,0.411,-0.835,0.852,-1.144,1.331c0.065,-0.554,0.196,-1.088,0.377,-1.596c0.181,-0.507,0.421,-0.98,0.708,-1.417c0.288,-0.431,0.625,-0.826,1.011,-1.179c0.386,-0.353,0.813,-0.657,1.286,-0.904c0.473,-0.246,0.974,-0.446,1.519,-0.579c0.545,-0.117,1.125,-0.187,1.735,-0.187c0.357,0,0.713,0.031,1.068,0.09c0.355,0.062,0.702,0.144,1.04,0.251c0.671,0.218,1.271,0.522,1.798,0.908c0.527,0.39,0.97,0.834,1.329,1.341c0.36,0.507,0.632,1.068,0.819,1.678s0.283,1.244,0.283,1.904v1.633H9.034z\" fill=\"#FFFFFF\"/><path d=\"M12,23c6.075,0,11,-4.925,11,-11c0,-3.038,-1.231,-5.788,-3.222,-7.778L4.222,19.778C6.212,21.769,8.962,23,12,23z\" fill=\"#24282D\" opacity=\"0.1\"/></g></svg></a>");
		writer.print("<a onclick=\"showonly(\'safari\')\"><svg version=\"1.1\" id=\"Layer_1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" x=\"0px\" y=\"0px\" width=\"35px\" height=\"35px\" viewBox=\"0 0 24 24\" enable-background=\"new 0 0 24 24\" xml:space=\"preserve\"><g id=\"icon\"><circle cx=\"12\" cy=\"12\" r=\"11\" fill=\"#CCD0D9\"/><path d=\"M12,21.3077c-5.1323,0,-9.3077,-4.1754,-9.3077,-9.3077S6.8677,2.6923,12,2.6923S21.3077,6.8677,21.3077,12S17.1323,21.3077,12,21.3077z\" fill=\"#4FC0E8\"/><path d=\"M16.1813,11.353c-0.2798,-1.8159,-1.7184,-3.2545,-3.5343,-3.5343L12,5.2308l-0.647,2.5879c-1.8159,0.2798,-3.2545,1.7184,-3.5343,3.5343L5.2308,12l2.5879,0.647c0.1193,0.7741,0.4497,1.4791,0.9296,2.0555l-2.7089,3.2901l3.2935,-2.7117c0.5698,0.4641,1.2619,0.7836,2.0202,0.9005l0.647,2.5879l0.647,-2.5879c1.8159,-0.2798,3.2545,-1.7184,3.5343,-3.5343l2.5879,-0.647L16.1813,11.353zM12,15.2154c-0.6939,0,-1.3367,-0.2217,-1.8626,-0.597l2.4648,-2.0295l-1.1592,-1.1592l-2.0344,2.4708c-0.3918,-0.5328,-0.6239,-1.19,-0.6239,-1.9006c0,-1.773,1.4424,-3.2154,3.2154,-3.2154S15.2154,10.227,15.2154,12S13.773,15.2154,12,15.2154z\" fill=\"#FFFFFF\"/><polygon points=\"11.4242,11.411 17.9871,6.0074 12.5835,12.5703\" fill=\"#ED5564\" stroke-linejoin=\"miter\"/><path d=\"M12,23c6.0751,0,11,-4.9249,11,-11c0,-3.0376,-1.2312,-5.7876,-3.2218,-7.7782L4.2218,19.7782C6.2124,21.7688,8.9624,23,12,23z\" fill=\"#231F20\" opacity=\"0.1\"/></g></svg></a>");
		writer.print("<a onclick=\"showonly(\'mozilla\')\"><svg version=\"1.1\" id=\"Layer_1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" x=\"0px\" y=\"0px\" width=\"35px\" height=\"35px\" viewBox=\"0 0 24 24\" enable-background=\"new 0 0 24 24\" xml:space=\"preserve\"><g id=\"icon\"><path d=\"M19.5263,4.2566C17.5633,2.246,14.8419,1,11.8333,1C6.9521,1,2.8261,4.2784,1.4715,8.7846H2l-0.1667,0.6769l-0.5951,0.2417C1.0832,10.4442,1,11.2121,1,12c0,1.5806,0.3308,3.082,0.922,4.4404l0.578,-0.2096l2.8333,3.0462L12.5,21.6462l6.6667,-3.3846l1.5,-2.8769l0.8333,-4.4L19.5263,4.2566z\" fill=\"#5D9CEC\"/><g><path d=\"M22.9672,8.7279l-0.2637,1.7183c0,0,-0.3771,-3.1813,-0.8393,-4.3706c-0.7084,-1.8224,-1.0235,-1.8078,-1.0255,-1.8051c0.4745,1.2242,0.3883,1.8819,0.3883,1.8819s-0.8348,-2.3087,-3.0391,-3.0576c-0.0136,-0.0101,-0.0269,-0.0206,-0.0406,-0.0306c-2.621,-0.869,-3.9566,-0.5542,-3.9465,-0.5419c0.0006,0.0007,0.0113,0.0032,0.0299,0.0071c-0.0062,0.0015,-0.0117,0.0029,-0.016,0.0041c0.0043,-0.0012,0.0098,-0.0026,0.016,-0.0041c0.2944,0.0618,2.719,0.5139,3.1711,1.1446c0,0,-1.1512,0,-2.2971,0.3352c-0.0518,0.015,4.2156,0.5413,5.0879,4.8711c0,0,-0.4678,-0.991,-1.0463,-1.1593c0.3804,1.1753,0.2828,3.4053,-0.0795,4.5136c-0.0466,0.1426,-0.0943,-0.6161,-0.8081,-0.9428c0.2287,1.6634,-0.0137,4.3014,-1.1501,5.0283c-0.0885,0.0566,0.7123,-2.6044,0.1611,-1.5756c-3.2915,5.1243,-7.2048,2.0732,-8.7921,0.9856c1.274,0.317,2.6272,0.0887,3.4107,-0.4551c0.7906,-0.5487,1.2583,-0.9498,1.6784,-0.8551c0.4198,0.0952,0.6993,-0.3325,0.3732,-0.7122c-0.3265,-0.3803,-1.1191,-0.9028,-2.1915,-0.618c-0.7563,0.2011,-1.6934,1.0508,-3.1239,0.1905c-1.2204,-0.7342,-1.2122,-1.3306,-1.2122,-1.7104c0,-0.3802,0.3329,-0.9577,0.9388,-0.863c0.5426,0.0851,0.2567,-0.2385,0.8407,0c0.159,0.0649,-0.016,-0.7755,-0.2423,-1.3137c0.4359,-0.9239,1.8503,-1.1983,1.9559,-1.2794c0.1912,-0.1468,0.086,-0.2202,0.1514,-0.4412c0.061,-0.206,0.1001,-0.7628,-1.4827,-0.6091c-0.7283,0.0708,-1.1916,-0.8875,-1.3434,-1.1203c0.0491,-0.2962,0.1282,-0.5668,0.2351,-0.8147c0.1086,-0.2259,0.2271,-0.4328,0.3454,-0.5895c0.0369,-0.049,0.0692,-0.0921,0.0984,-0.1319c0.2361,-0.2795,0.5257,-0.5184,0.8733,-0.717c0.1815,-0.104,-2.0288,-0.0065,-3.0231,1.3097c-0.267,0.0352,-0.6331,-0.0427,-1.0423,-0.0427c-0.513,0,-0.916,0.0569,-1.2891,0.152c-0.0598,0.0152,-0.1561,0.007,-0.274,-0.0217c-0.0539,-0.0567,-0.1252,-0.1283,-0.205,-0.2106c0.0037,-0.0042,0.0073,-0.0087,0.011,-0.0129c-0.3922,-0.4335,-0.8714,-1.0839,-0.9018,-1.8856c0,0,-1.3505,1.054,-1.1484,3.9271c-0.0479,0.4818,-0.1443,0.9215,-0.2471,1.301c-0.1209,0.3337,-0.227,0.6747,-0.316,1.0228c0.0025,-0.0028,0.0039,-0.0037,0.0062,-0.0061c-0.0336,0.1136,-0.0564,0.2054,-0.063,0.273c-0.004,0.0101,-0.0073,0.0184,-0.0116,0.029C1.0984,10.3209,1,11.1485,1,12c0,6.0241,4.7698,10.9143,10.6834,10.9961c0.238,-0.0057,0.4768,-0.0182,0.7162,-0.0387c0.71,-0.0469,1.4252,-0.1543,2.1388,-0.3295c0.3947,-0.0969,0.7696,-0.2113,1.1286,-0.339c2.2431,-0.8622,4.13,-2.4579,5.3774,-4.4999c0.6169,-1.0664,1.0428,-2.1941,1.3353,-3.2786c0.1849,-0.8041,0.286,-1.6409,0.2867,-2.502c0,0.0027,0.0001,0.0054,0.0001,0.0082c0,0.8641,-0.1013,1.7037,-0.2868,2.5104C23.2088,11.4539,22.9672,8.7279,22.9672,8.7279z\" fill=\"#F28268\"/></g><g><path d=\"M13.1919,14.4519c0.1328,-0.0448,0.256,-0.0589,0.3749,-0.0321c0.4198,0.0952,0.6993,-0.3325,0.3732,-0.7122c-0.1343,-0.1565,-0.3487,-0.3364,-0.628,-0.4749L13.1919,14.4519z\" fill=\"#F9AD95\"/></g><path d=\"M8.9734,9.3502c0.4594,-0.8885,1.8281,-1.1568,1.932,-1.2366c0.1912,-0.1468,0.086,-0.2202,0.1514,-0.4412c0.0211,-0.0712,0.0394,-0.1844,-0.0108,-0.2956l-2.8793,0.7309C8.646,8.4462,8.8591,8.8804,8.9734,9.3502z\" fill=\"#EFBBAB\"/><g><path d=\"M10.4074,7.0548l0.5624,0.9993c0.0985,-0.1114,0.031,-0.1926,0.0869,-0.3817C11.1041,7.5122,11.1379,7.1403,10.4074,7.0548z\" fill=\"#536880\"/></g><path d=\"M4.2395,19.8423c0.0125,0.0125,0.0248,0.0251,0.0374,0.0376c0.0411,0.0406,0.0828,0.0807,0.1246,0.1207c0.0182,0.0175,0.0363,0.035,0.0547,0.0524c0.0395,0.0373,0.0794,0.0741,0.1195,0.1108c0.021,0.0192,0.0419,0.0385,0.063,0.0575c0.0389,0.0351,0.0782,0.0699,0.1176,0.1044c0.0229,0.0201,0.0459,0.0401,0.0689,0.06c0.0387,0.0334,0.0777,0.0664,0.1168,0.0993c0.0242,0.0203,0.0486,0.0405,0.073,0.0605c0.0389,0.032,0.078,0.0639,0.1174,0.0954c0.0251,0.0201,0.0504,0.04,0.0757,0.0599c0.0394,0.0309,0.0788,0.0618,0.1186,0.0922c0.0257,0.0197,0.0518,0.039,0.0777,0.0584c0.04,0.03,0.08,0.06,0.1205,0.0895c0.0257,0.0187,0.0518,0.037,0.0777,0.0556c0.0413,0.0296,0.0826,0.0592,0.1244,0.0882c0.0238,0.0165,0.0479,0.0326,0.0719,0.0489c0.1501,0.1024,0.3029,0.201,0.4581,0.2959c0.0284,0.0174,0.0567,0.0351,0.0853,0.0522c0.039,0.0233,0.0785,0.046,0.1178,0.0688c0.0328,0.0191,0.0656,0.0384,0.0986,0.0571c0.0381,0.0216,0.0765,0.0426,0.1149,0.0637c0.0349,0.0192,0.0697,0.0385,0.1048,0.0573c0.0378,0.0203,0.0758,0.04,0.1138,0.0599c0.0363,0.0189,0.0725,0.0378,0.109,0.0563c0.0376,0.0191,0.0754,0.0378,0.1133,0.0564c0.0374,0.0184,0.0749,0.0367,0.1126,0.0547c0.0377,0.018,0.0755,0.0358,0.1134,0.0533c0.0384,0.0178,0.0769,0.0353,0.1156,0.0527c0.0376,0.0169,0.0753,0.0336,0.1131,0.0501c0.0397,0.0173,0.0796,0.0342,0.1195,0.051c0.0373,0.0157,0.0745,0.0314,0.112,0.0466c0.0414,0.0168,0.083,0.0332,0.1246,0.0495c0.0366,0.0144,0.0731,0.0289,0.1099,0.0428c0.0437,0.0166,0.0877,0.0326,0.1317,0.0487c0.0351,0.0128,0.0701,0.0259,0.1053,0.0383c0.0494,0.0174,0.0991,0.0341,0.1488,0.0508c0.0302,0.0102,0.0603,0.0208,0.0907,0.0307c0.0795,0.026,0.1593,0.0511,0.2396,0.0752c0.0177,0.0053,0.0355,0.0101,0.0532,0.0154c0.0638,0.0188,0.1277,0.0374,0.1919,0.0551c0.0312,0.0086,0.0626,0.0165,0.0938,0.0247c0.0511,0.0135,0.1022,0.0271,0.1536,0.0399c0.0347,0.0086,0.0696,0.0167,0.1044,0.025c0.0485,0.0116,0.0969,0.0231,0.1456,0.034c0.0363,0.0081,0.0727,0.0157,0.1091,0.0234c0.0476,0.0101,0.0953,0.0201,0.1431,0.0296c0.0372,0.0074,0.0746,0.0144,0.112,0.0214c0.0476,0.0089,0.0952,0.0176,0.143,0.0259c0.0375,0.0065,0.075,0.0127,0.1126,0.0188c0.0481,0.0078,0.0963,0.0152,0.1446,0.0224c0.0377,0.0056,0.0754,0.011,0.1132,0.0162c0.0489,0.0067,0.0979,0.0129,0.1469,0.0189c0.0373,0.0046,0.0746,0.0092,0.1121,0.0134c0.0503,0.0056,0.1008,0.0107,0.1513,0.0156c0.0366,0.0036,0.0731,0.0073,0.1097,0.0105c0.0527,0.0046,0.1055,0.0085,0.1584,0.0123c0.0347,0.0025,0.0693,0.0053,0.1041,0.0075c0.0586,0.0037,0.1174,0.0064,0.1762,0.0092c0.0293,0.0014,0.0585,0.0032,0.0878,0.0043c0.0883,0.0034,0.1767,0.0058,0.2655,0.0071c0.238,-0.0057,0.4768,-0.0182,0.7162,-0.0387c0.71,-0.0469,1.4252,-0.1543,2.1388,-0.3295c0.3947,-0.0969,0.7696,-0.2113,1.1286,-0.339c2.2431,-0.8622,4.13,-2.4579,5.3774,-4.4999c0.6169,-1.0664,1.0428,-2.1941,1.3353,-3.2786c0.0231,-0.1006,0.045,-0.2017,0.0654,-0.3033c0.0157,-0.0779,0.0299,-0.1564,0.044,-0.2349c0.0043,-0.0237,0.0092,-0.0473,0.0133,-0.0711c0.0167,-0.0964,0.0319,-0.1934,0.0462,-0.2906c0.0006,-0.0044,0.01,-0.0087,0.0155,-0.0131c-0.0466,0.3143,-0.1059,0.6243,-0.1761,0.9296c0.829,-3.0732,0.579,-5.7992,0.579,-5.7992l-0.2637,1.7183c0,0,-0.3771,-3.1813,-0.8393,-4.3706c-0.7084,-1.8224,-1.0235,-1.8078,-1.0255,-1.8051c0.4745,1.2242,0.3883,1.8819,0.3883,1.8819s-0.3955,-1.0931,-1.3604,-2.0189l-15.667,15.667C4.2132,19.8141,4.2258,19.8287,4.2395,19.8423z\" fill=\"#231F20\" opacity=\"0.1\"/></g></svg></a>");
		writer.print("</center></td></tr>");
		writer.print("<tr class=\"table-bordered table-head-custom1\">");
		writer.print("<th>Test ID</th>");
		writer.print("<th>Test Scenario Name</th>");
		writer.print("<th>Test Steps</th>");
		writer.print("<th>Passed</th>");
		writer.print("<th>Skipped</th>");
		writer.print("<th>Failed</th>");
		writer.print("<th>Included Groups</th>");
		writer.print("<th>Excluded Groups</th>");
		writer.print("<th>Browser</th>");
		writer.print("<th>Start Time</th>");
		writer.print("<th>End Time</th>");
		writer.print("<th>Total Time(hh:mm:ss)</th>");
		writer.println("</tr>");

		int testIndex = 0;
		for (SuiteResult suiteResult : suiteResults) {
			writer.print("<tr><th colspan=12>");
			writer.print("<center>" + Utils.escapeHtml(suiteResult.getSuiteName()) + "</center>");
			writer.println("</th></tr>");

			for (TestResult testResult : suiteResult.getTestResults()) {
				int testsCount = testResult.getTestCount();
				int passedTests = testResult.getPassedTestCount();
				int skippedTests = testResult.getSkippedTestCount();
				int failedTests = testResult.getFailedTestCount();

				Date startTime = testResult.getTestStartTime();
				Date endTime = testResult.getTestEndTime();
				long duration = testResult.getDuration();
				
				String DbrowserName = Utils.escapeHtml(testResult.getTestName());
				String[] Dbrowser = DbrowserName.split("\\s");
				String ClassNameD = "";
				writer.print("<tr");
				if ((testIndex % 2) == 1) {
					ClassNameD ="stripe";
				}
				writer.print(" class=\"" + Dbrowser[2] + " " + ClassNameD + "\">");

				buffer.setLength(0);
				String id = Utils.escapeHtml(testResult.getTestName());
				String[] testID = id.split("\\s");
				writeTableData(testID[1]);
				String testName = Utils.escapeHtml(testResult.getTestName());
				String[] test = testName.split("\\s");
				writeTableData(buffer.append("<a onclick=\"open_div_show(\'#t"+testIndex+"\')\" href=\"#t\"").append(testIndex).append(">").append(test[0]).append("</a>")
						.toString());

				writeTableData(integerFormat.format(testsCount), "num");
				writeTableData(integerFormat.format(passedTests), "num");
				writeTableData(integerFormat.format(skippedTests), (skippedTests > 0 ? "num attn" : "num"));
				writeTableData(integerFormat.format(failedTests), (failedTests > 0 ? "num attn" : "num"));

				writeTableData(testResult.getIncludedGroups());
				writeTableData(testResult.getExcludedGroups());
				String browserName = Utils.escapeHtml(testResult.getTestName());
				String[] browser = browserName.split("\\s");
				writeTableData(browser[2]);
				writeTableData(dateFormat.format(startTime), "num");
				writeTableData(dateFormat.format(endTime), "num");
				writeTableData(convertTimeToString(duration), "num");
				writer.println("</tr>");

				totalTestsCount += testsCount;
				totalPassedTests += passedTests;
				totalSkippedTests += skippedTests;
				totalFailedTests += failedTests;
				totalDuration += duration;
				testIndex++;
			}
		}

		// Print totals if there was more than one test
		if (testIndex > 1) {

			writer.print("<tr class=\"total\">");
			writer.print("<th colspan=1></th>");
			writer.print("<th>Total</th>");
			writeTableHeader(integerFormat.format(totalTestsCount), "num");
			writeTableHeader(integerFormat.format(totalPassedTests), "num");
			writeTableHeader(integerFormat.format(totalSkippedTests), (totalSkippedTests > 0 ? "num attn" : "num"));
			writeTableHeader(integerFormat.format(totalFailedTests), (totalFailedTests > 0 ? "num attn" : "num"));
			writer.print("<th colspan=5></th>");
			writeTableHeader(convertTimeToString(totalDuration), "num");
			writer.println("</tr>");
			
		}

		writer.println("</table>");
		writer.println("</div>");
		writer.println("</div>");
	}

	// * Writes a summary of all the test scenarios.

	protected void writeScenarioSummary() {
		writer.print("<div class=\"row\">");
		writer.print("<div class=\"easy-test-summary table-responsive\">");
		writer.print("<table class=\"table table-sm table-bordered\" id=\"summary\">");
		writer.print("<thead>");
		writer.print("<tr>");
		writer.print("<th>Class</th>");
		writer.print("<th>Method</th>");
		writer.print("<th>Short Exception</th>");
		writer.print("<th>Screenshot</th>");
		writer.print("<th>Start Time</th>");
		writer.print("<th>End Time</th>");
		writer.print("</tr>");
		writer.print("</thead>");

		int testIndex = 0;
		int scenarioIndex = 0;
		for (SuiteResult suiteResult : suiteResults) {
			writer.print("<tbody><tr><th colspan=6>");
			writer.print("<center>" + Utils.escapeHtml(suiteResult.getSuiteName()) + "</center>");
			writer.print("</th></tr></tbody>");

			for (TestResult testResult : suiteResult.getTestResults()) {
				writer.printf("<tbody class=\"display_n\" "+"id=t%d>", testIndex);

				String test = Utils.escapeHtml(testResult.getTestName());
				String[] testName = test.split("\\s");
				int startIndex = scenarioIndex;

				scenarioIndex += writeScenarioSummary(testName[0].toString() + " &#8212; failed (configuration methods)",
						testResult.getFailedConfigurationResults(), "failed", scenarioIndex);
				scenarioIndex += writeScenarioSummary(testName[0].toString() + " &#8212; failed", testResult.getFailedTestResults(),
						"failed", scenarioIndex);
				scenarioIndex += writeScenarioSummary(testName[0].toString() + " &#8212; skipped (configuration methods)",
						testResult.getSkippedConfigurationResults(), "skipped", scenarioIndex);
				scenarioIndex += writeScenarioSummary(testName[0].toString() + " &#8212; skipped", testResult.getSkippedTestResults(),
						"skipped", scenarioIndex);
				scenarioIndex += writeScenarioSummary(testName[0].toString() + " &#8212; passed", testResult.getPassedTestResults(),
						"passed", scenarioIndex);

				if (scenarioIndex == startIndex) {
					writer.print("<tr><th colspan=4 class=invisible/></tr>");
				}

				writer.println("</tbody>");

				testIndex++;
			}
		}

		writer.println("</table>");
		writer.println("</div>");
		writer.println("</div>");
	}

	// * Writes the scenario summary for the results of a given state for a single test.

	private int writeScenarioSummary(String description, List<ClassResult> classResults, String cssClassPrefix,
			int startingScenarioIndex) {
		int scenarioCount = 0;
		if (!classResults.isEmpty()) {
			writer.print("<tr><th colspan=6>");
			writer.print(description);
			writer.print("</th></tr>");

			int scenarioIndex = startingScenarioIndex;
			int classIndex = 0;
			for (ClassResult classResult : classResults) {
				String cssClass = cssClassPrefix + ((classIndex % 2) == 0 ? "even" : "odd");

				buffer.setLength(0);

				int scenariosPerClass = 0;
				int methodIndex = 0;
				for (MethodResult methodResult : classResult.getMethodResults()) {
					List<ITestResult> results = methodResult.getResults();
					int resultsCount = results.size();
					assert resultsCount > 0;

					ITestResult firstResult = results.iterator().next();
					String methodName = Utils.escapeHtml(firstResult.getMethod().getMethodName());
					long start = firstResult.getStartMillis();
					long end = firstResult.getEndMillis();

					String shortException = "";
					String failureScreenShot = "";
					Throwable exception = firstResult.getThrowable();
					boolean hasThrowable = exception != null;
					if (hasThrowable) {
						String str = Utils.shortStackTrace(exception, true);
						Scanner scanner = new Scanner(str);
						shortException = scanner.nextLine();
						scanner.close();
						List<String> msgs = Reporter.getOutput(firstResult);
						boolean hasReporterOutput = msgs.size() > 0;
						if (hasReporterOutput) {
							for (String info : msgs) {
								failureScreenShot = info + "<br/>";
							}
						}
					}

					DateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
					Calendar startTime = Calendar.getInstance();
					startTime.setTimeInMillis(start);

					Calendar endTime = Calendar.getInstance();
					endTime.setTimeInMillis(end);

					// The first method per class shares a row with the class
					// header
					if (methodIndex > 0) {
						buffer.append("<tr class=").append(cssClass).append(">");

					}

					// Write the timing information with the first scenario per
					// method
					buffer.append("<td><a onclick=\"show_last_element(\'#m"+scenarioIndex+"\')\" href=#m").append(scenarioIndex).append(">").append(methodName)
							.append("</a></td>").append("<td rowspan=").append(">").append(shortException)
							.append("</td>").append("<td rowspan=").append(">").append(failureScreenShot)
							.append("</td>").append("<td rowspan=").append(">")
							.append(formatter.format(startTime.getTime())).append("</td>").append("<td rowspan=")
							.append(">").append(formatter.format(endTime.getTime())).append("</td></tr>");
					scenarioIndex++;

					// Write the remaining scenarios for the method
					for (int i = 1; i < resultsCount; i++) {
						buffer.append("<tr class=").append(cssClass).append(">").append("<td><a href=#m")
								.append(scenarioIndex).append(">").append(methodName).append("</a></td></tr>");
						scenarioIndex++;
					}

					scenariosPerClass += resultsCount;
					methodIndex++;
								
				}

				// Write the test results for the class
				writer.print("<tr class=");
				writer.print(cssClass);
				writer.print(">");
				writer.print("<td rowspan=");
				writer.print(scenariosPerClass);
				writer.print(">");
				writer.print(Utils.escapeHtml(classResult.getClassName()));
				writer.print("</td>");
				writer.print(buffer);

				classIndex++;
			}
			scenarioCount = scenarioIndex - startingScenarioIndex;
		}

		return scenarioCount;
	}

	// * Writes the details for all test scenarios.

	protected void writeScenarioDetails() {
		int scenarioIndex = 0;
		writer.print("<div class=\"row dtable\" id=\"last_div\">");
		for (SuiteResult suiteResult : suiteResults) {
			for (TestResult testResult : suiteResult.getTestResults()) {
				writer.print("<h2>");
				String test = Utils.escapeHtml(testResult.getTestName());
				String[] testName = test.split("\\s");
				writer.print(testName[0]);
				writer.print("</h2>");
				

				scenarioIndex += writeScenarioDetails(testResult.getFailedConfigurationResults(), scenarioIndex);
				scenarioIndex += writeScenarioDetails(testResult.getFailedTestResults(), scenarioIndex);
				scenarioIndex += writeScenarioDetails(testResult.getSkippedConfigurationResults(), scenarioIndex);
				scenarioIndex += writeScenarioDetails(testResult.getSkippedTestResults(), scenarioIndex);
				scenarioIndex += writeScenarioDetails(testResult.getPassedTestResults(), scenarioIndex);
			}
		}
		writer.print("</div>");
	}

	// * Writes the scenario details for the results of a given state for a single test.

	private int writeScenarioDetails(List<ClassResult> classResults, int startingScenarioIndex) {
		int scenarioIndex = startingScenarioIndex;
		for (ClassResult classResult : classResults) {
			String className = classResult.getClassName();
			for (MethodResult methodResult : classResult.getMethodResults()) {
				List<ITestResult> results = methodResult.getResults();
				assert !results.isEmpty();

				String label = Utils
						.escapeHtml(className + "#" + results.iterator().next().getMethod().getMethodName());
				for (ITestResult result : results) {
					writeScenario(scenarioIndex, label, result);
					scenarioIndex++;
				}
			}
		}

		return scenarioIndex - startingScenarioIndex;
	}

	// * Writes the details for an individual test scenario.

	private void writeScenario(int scenarioIndex, String label, ITestResult result) {
		writer.print("<div class=\"display_n\" id=m"+scenarioIndex+">");
		writer.print("<h3 id=m");
		writer.print(scenarioIndex);
		writer.print(">");
		writer.print(label);
		writer.print("</h3>");

		writer.print("<table class=table-bordered result>");

		boolean hasRows = false;

		// Write test parameters (if any)
		Object[] parameters = result.getParameters();
		int parameterCount = (parameters == null ? 0 : parameters.length);
		if (parameterCount > 0) {
			writer.print("<tr class=param>");
			for (int i = 1; i <= parameterCount; i++) {
				writer.print("<th>Parameter #");
				writer.print(i);
				writer.print("</th>");
			}
			writer.print("</tr><tr class=param stripe>");
			for (Object parameter : parameters) {
				writer.print("<td>");
				writer.print(Utils.escapeHtml(Utils.toString(parameter)));
				writer.print("</td>");
			}
			writer.print("</tr>");
			hasRows = true;
		}

		// Write reporter messages (if any)
		List<String> reporterMessages = Reporter.getOutput(result);
		if (!reporterMessages.isEmpty()) {
			writer.print("<tr><th");
			if (parameterCount > 1) {
				writer.printf(" colspan=%d", parameterCount);
			}
			writer.print(">Messages</th></tr>");

			writer.print("<tr><td");
			if (parameterCount > 1) {
				writer.printf(" colspan=%d", parameterCount);
			}
			writer.print(">");
			writeReporterMessages(reporterMessages);
			writer.print("</td></tr>");
			hasRows = true;
		}

		// Write exception (if any)
		Throwable throwable = result.getThrowable();
		if (throwable != null) {
			writer.print("<tr><th");
			if (parameterCount > 1) {
				writer.printf(" colspan=%d", parameterCount);
			}
			writer.print(">");
			writer.print((result.getStatus() == ITestResult.SUCCESS ? "Expected Exception" : "Exception"));
			writer.print("</th></tr>");

			writer.print("<tr><td");
			if (parameterCount > 1) {
				writer.printf(" colspan=%d", parameterCount);
			}
			writer.print(">");
			writeStackTrace(throwable);
			writer.print("</td></tr>");
			hasRows = true;
		}

		if (!hasRows) {
			writer.print("<tr><th");
			if (parameterCount > 1) {
				writer.printf(" colspan=%d", parameterCount);
			}
			writer.print(" class=invisible/></tr>");
		}

		writer.print("</table>");
		
		writer.println("<p class=totop><a href=#summary>back to summary</a></p>");
		writer.print("</div>");
	}

	protected void writeReporterMessages(List<String> reporterMessages) {
		writer.print("<div class=messages>");
		Iterator<String> iterator = reporterMessages.iterator();
		assert iterator.hasNext();
		if (Reporter.getEscapeHtml()) {
			writer.print(Utils.escapeHtml(iterator.next()));
		} else {
			writer.print(iterator.next());
		}
		while (iterator.hasNext()) {
			writer.print("<br/>");
			if (Reporter.getEscapeHtml()) {
				writer.print(Utils.escapeHtml(iterator.next()));
			} else {
				writer.print(iterator.next());
			}
		}
		writer.print("</div>");
	}

	protected void writeStackTrace(Throwable throwable) {
		writer.print("<div class=stacktrace>");
		writer.print(Utils.shortStackTrace(throwable, true));
		writer.print("</div>");
	}

	// * Writes a TH element with the specified contents and CSS class names.
	// * @param html the HTML contents
	// * @param cssClasses the space-delimited CSS classes or null if there are
	// no classes to apply

	protected void writeTableHeader(String html, String cssClasses) {
		writeTag("th", html, cssClasses);
	}

	// * Writes a TD element with the specified contents.
	// * @param html the HTML contents

	protected void writeTableData(String html) {
		writeTableData(html, null);
	}

	// * Writes a TD element with the specified contents and CSS class names.
	// * @param html the HTML contents
	// * @param cssClasses the space-delimited CSS classes or null if there are
	// no classes to apply

	protected void writeTableData(String html, String cssClasses) {
		writeTag("td", html, cssClasses);
	}

	// * Writes an arbitrary HTML element with the specified contents and CSS
	// class names.
	// * @param tag the tag name
	// * @param html the HTML contents
	// * @param cssClasses the space-delimited CSS classes or null if there are no classes to apply

	protected void writeTag(String tag, String html, String cssClasses) {
		writer.print("<");
		writer.print(tag);
		if (cssClasses != null) {
			writer.print(" class=");
			writer.print(cssClasses);
			writer.print("");
		}
		writer.print(">");
		writer.print(html);
		writer.print("</");
		writer.print(tag);
		writer.print(">");
	}

	// *Groups {@link TestResult}s by suite.

	protected static class SuiteResult {
		private final String suiteName;
		private final List<TestResult> testResults = Lists.newArrayList();

		public SuiteResult(ISuite suite) {
			suiteName = suite.getName();
			for (ISuiteResult suiteResult : suite.getResults().values()) {
				testResults.add(new TestResult(suiteResult.getTestContext()));
			}
		}

		public String getSuiteName() {
			return suiteName;
		}

		// * @return the test results (possibly empty)

		public List<TestResult> getTestResults() {
			return testResults;
		}
	}
	// * Groups {@link ClassResult}s by test, type (configuration or test), and
	// status.

	protected static class TestResult {

		// * Orders test results by class name and then by method name (in
		// lexicographic order).

		protected static final Comparator<ITestResult> RESULT_COMPARATOR = new Comparator<ITestResult>() {

			public int compare(ITestResult o1, ITestResult o2) {
				int result = o1.getTestClass().getName().compareTo(o2.getTestClass().getName());
				if (result == 0) {
					result = o1.getMethod().getMethodName().compareTo(o2.getMethod().getMethodName());
				}
				return result;
			}
		};

		private final String testName;
		private final Date testStartTime;
		private final Date testEndTime;
		private final List<ClassResult> failedConfigurationResults;
		private final List<ClassResult> failedTestResults;
		private final List<ClassResult> skippedConfigurationResults;
		private final List<ClassResult> skippedTestResults;
		private final List<ClassResult> passedTestResults;
		private final int failedTestCount;
		private final int skippedTestCount;
		private final int passedTestCount;
		private final int testCount;
		private final long duration;
		private final String includedGroups;
		private final String excludedGroups;

		public TestResult(ITestContext context) {
			testName = context.getName();

			Set<ITestResult> failedConfigurations = context.getFailedConfigurations().getAllResults();
			Set<ITestResult> failedTests = context.getFailedTests().getAllResults();
			Set<ITestResult> skippedConfigurations = context.getSkippedConfigurations().getAllResults();
			Set<ITestResult> skippedTests = context.getSkippedTests().getAllResults();
			Set<ITestResult> passedTests = context.getPassedTests().getAllResults();

			failedConfigurationResults = groupResults(failedConfigurations);
			failedTestResults = groupResults(failedTests);
			skippedConfigurationResults = groupResults(skippedConfigurations);
			skippedTestResults = groupResults(skippedTests);
			passedTestResults = groupResults(passedTests);

			testStartTime = context.getStartDate();
			testEndTime = context.getEndDate();

			failedTestCount = failedTests.size();
			skippedTestCount = skippedTests.size();
			passedTestCount = passedTests.size();
			testCount = context.getAllTestMethods().length;

			duration = context.getEndDate().getTime() - context.getStartDate().getTime();

			includedGroups = formatGroups(context.getIncludedGroups());
			excludedGroups = formatGroups(context.getExcludedGroups());
		}

		// * Groups test results by method and then by class.

		protected List<ClassResult> groupResults(Set<ITestResult> results) {
			List<ClassResult> classResults = Lists.newArrayList();
			if (!results.isEmpty()) {
				List<MethodResult> resultsPerClass = Lists.newArrayList();
				List<ITestResult> resultsPerMethod = Lists.newArrayList();

				List<ITestResult> resultsList = Lists.newArrayList(results);
				Collections.sort(resultsList, RESULT_COMPARATOR);
				Iterator<ITestResult> resultsIterator = resultsList.iterator();
				assert resultsIterator.hasNext();

				ITestResult result = resultsIterator.next();
				resultsPerMethod.add(result);

				String previousClassName = result.getTestClass().getName();
				String previousMethodName = result.getMethod().getMethodName();
				while (resultsIterator.hasNext()) {
					result = resultsIterator.next();

					String className = result.getTestClass().getName();
					if (!previousClassName.equals(className)) {
						// Different class implies different method
						assert !resultsPerMethod.isEmpty();
						resultsPerClass.add(new MethodResult(resultsPerMethod));
						resultsPerMethod = Lists.newArrayList();

						assert !resultsPerClass.isEmpty();
						classResults.add(new ClassResult(previousClassName, resultsPerClass));
						resultsPerClass = Lists.newArrayList();

						previousClassName = className;
						previousMethodName = result.getMethod().getMethodName();
					} else {
						String methodName = result.getMethod().getMethodName();
						if (!previousMethodName.equals(methodName)) {
							assert !resultsPerMethod.isEmpty();
							resultsPerClass.add(new MethodResult(resultsPerMethod));
							resultsPerMethod = Lists.newArrayList();

							previousMethodName = methodName;
						}
					}
					resultsPerMethod.add(result);
				}
				assert !resultsPerMethod.isEmpty();
				resultsPerClass.add(new MethodResult(resultsPerMethod));
				assert !resultsPerClass.isEmpty();
				classResults.add(new ClassResult(previousClassName, resultsPerClass));
			}
			return classResults;
		}

		public String getTestName() {
			return testName;
		}

		public Date getTestStartTime() {
			return testStartTime;
		}

		public Date getTestEndTime() {
			return testEndTime;
		}

		// * @return the results for failed configurations (possibly empty)

		public List<ClassResult> getFailedConfigurationResults() {
			return failedConfigurationResults;
		}

		// * @return the results for failed tests (possibly empty)

		public List<ClassResult> getFailedTestResults() {
			return failedTestResults;
		}

		// * @return the results for skipped configurations (possibly empty)

		public List<ClassResult> getSkippedConfigurationResults() {
			return skippedConfigurationResults;
		}

		// * @return the results for skipped tests (possibly empty)

		public List<ClassResult> getSkippedTestResults() {
			return skippedTestResults;
		}

		// * @return the results for passed tests (possibly empty)

		public List<ClassResult> getPassedTestResults() {
			return passedTestResults;
		}

		public int getFailedTestCount() {
			return failedTestCount;
		}

		public int getSkippedTestCount() {
			return skippedTestCount;
		}

		public int getPassedTestCount() {
			return passedTestCount;
		}

		public long getDuration() {
			return duration;
		}

		public String getIncludedGroups() {
			return includedGroups;
		}

		public String getExcludedGroups() {
			return excludedGroups;
		}

		public int getTestCount() {
			return testCount;
		}

		// * Formats an array of groups for display.

		protected String formatGroups(String[] groups) {
			if (groups.length == 0) {
				return "";
			}

			StringBuilder builder = new StringBuilder();
			builder.append(groups[0]);
			for (int i = 1; i < groups.length; i++) {
				builder.append(", ").append(groups[i]);
			}
			return builder.toString();
		}
	}

	// * Groups {@link MethodResult}s by class.

	protected static class ClassResult {
		private final String className;
		private final List<MethodResult> methodResults;

		// * @param className-- the class name
		// * @param methodResults--the non-null, non-empty {@link MethodResult}
		// list

		public ClassResult(String className, List<MethodResult> methodResults) {
			this.className = className;
			this.methodResults = methodResults;
		}

		public String getClassName() {
			return className;
		}

		// * @return the non-null, non-empty {@link MethodResult} list

		public List<MethodResult> getMethodResults() {
			return methodResults;
		}
	}

	// * Groups test results by method.

	protected static class MethodResult {
		private final List<ITestResult> results;

		// * @param results-- the non-null, non-empty result list

		public MethodResult(List<ITestResult> results) {
			this.results = results;
		}

		// * @return the non-null, non-empty result list

		public List<ITestResult> getResults() {
			return results;
		}
	}

	/* Convert long type milliseconds to format hh:mm:ss */
	public String convertTimeToString(long miliSeconds) {
		int hrs = (int) TimeUnit.MILLISECONDS.toHours(miliSeconds) % 24;
		int min = (int) TimeUnit.MILLISECONDS.toMinutes(miliSeconds) % 60;
		int sec = (int) TimeUnit.MILLISECONDS.toSeconds(miliSeconds) % 60;
		return String.format("%02d:%02d:%02d", hrs, min, sec);
	}
}
