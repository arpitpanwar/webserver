package servlettest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;

import edu.upenn.cis.cis455.webserver.model.LauncherArgs;
import edu.upenn.cis.cis455.webserver.model.servlet.ServletContextImpl;
import edu.upenn.cis.cis455.webserver.util.Constants;
import edu.upenn.cis.cis455.webserver.util.Parser;

public class ServletContextTest {
	
	private Parser parser;
	private ServletContextImpl context;
	private LauncherArgs launcher;

	@Before
	public void setUp() throws Exception {
		launcher = new LauncherArgs();
		launcher.setPortNum(8090);
		launcher.setRootFolder(System.getProperty("user.home"));
		launcher.setWebXmlPath("E:\\Course Assignments\\Fall 2014\\Internet and Web Systems\\Assignments\\Hw1\\servlet-examples\\examples\\WebApplication2\\WEB-INF/web.xml");
		this.parser = new Parser();
		this.parser.parse(launcher.getWebXmlPath());
		this.context = new ServletContextImpl(parser,launcher.getRootFolder());
	}

	@Test
	public void testGetAttribute() {
		this.context = new ServletContextImpl(parser,launcher.getRootFolder());
		this.context.setAttribute("Test", "Test Val");
		assertEquals("Check If correct attribute is fetched", "Test Val", this.context.getAttribute("Test"));
	}

	@Test
	public void testGetAttributeNames() {
		this.context = new ServletContextImpl(parser,launcher.getRootFolder());
		this.context.setAttribute("Test", "Test Val");
		List<String> names = new ArrayList<>();
		names.add("Test");
		assertEquals("Check if correct names are fetched",Collections.enumeration(names).nextElement(),this.context.getAttributeNames().nextElement());
	}

	@Test
	public void testGetContext() {
		this.context = new ServletContextImpl(parser,launcher.getRootFolder());
		assertEquals("Check if correct context is retrieved",this.context,this.context.getContext("/"));
	}

	@Test
	public void testGetInitParameter() {
		this.context = new ServletContextImpl(parser,launcher.getRootFolder());
		assertEquals("Check if init param is correct", null, this.context.getInitParameter("Test"));
	}

	@Test
	public void testGetInitParameterNames() {
		this.context = new ServletContextImpl(parser,launcher.getRootFolder());
		assertEquals("Check if init param is correct", false, this.context.getInitParameterNames().hasMoreElements());
	}

	@Test
	public void testGetMajorVersion() {
		
		this.context = new ServletContextImpl(parser,launcher.getRootFolder());
		assertEquals("Check if init param is correct", 2, this.context.getMajorVersion());
	}


	@Test
	public void testGetMinorVersion() {
		this.context = new ServletContextImpl(parser,launcher.getRootFolder());
		assertEquals("Check if init param is correct", 4, this.context.getMinorVersion());
	}

	@Test
	public void testGetServerInfo() {
		this.context = new ServletContextImpl(parser,launcher.getRootFolder());
		assertEquals("Testing Server info", Constants.SERVER_INFO, this.context.getServerInfo());
	}

	@Test
	public void testGetServlet() throws ServletException {
		this.context = new ServletContextImpl(parser,launcher.getRootFolder());
		assertEquals("Testing Get Servlet", null, this.context.getServlet(""));
	}

	@Test
	public void testGetServletContextName() {
		this.context = new ServletContextImpl(parser,launcher.getRootFolder());
		assertEquals("Testing Get Servlet Context Name", Constants.DEFAULT_CONTEXT, this.context.getServletContextName());
	}

	

}
