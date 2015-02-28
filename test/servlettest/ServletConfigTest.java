package servlettest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.upenn.cis.cis455.webserver.model.LauncherArgs;
import edu.upenn.cis.cis455.webserver.model.servlet.ServletConfigImpl;
import edu.upenn.cis.cis455.webserver.model.servlet.ServletContextImpl;
import edu.upenn.cis.cis455.webserver.util.Parser;

public class ServletConfigTest {
	
	private Parser parser;
	private ServletContextImpl context;
	private ServletConfigImpl config;
	private LauncherArgs launcher;
	private String servletName="Test";
	
	@Before
	public void setUp() throws Exception {
		launcher = new LauncherArgs();
		launcher.setPortNum(8090);
		launcher.setRootFolder(System.getProperty("user.home"));
		launcher.setWebXmlPath("E:\\Course Assignments\\Fall 2014\\Internet and Web Systems\\Assignments\\Hw1\\servlet-examples\\examples\\WebApplication2\\WEB-INF/web.xml");
		this.parser = new Parser();
		this.parser.parse(launcher.getWebXmlPath());
		this.context = new ServletContextImpl(parser,launcher.getRootFolder());
		this.config = new ServletConfigImpl(this.context, parser.getServletParams().get(this.servletName), this.servletName);
	}
	
	@Test
	public void testGetInitParameter() {
		this.config = new ServletConfigImpl(this.context, parser.getServletParams().get(this.servletName), this.servletName);
		assertEquals("Check the init parameters", "Val1", this.config.getInitParameter("Name1"));
	}

	@Test
	public void testGetServletContext() {
		this.config = new ServletConfigImpl(this.context, parser.getServletParams().get(this.servletName), this.servletName);
		assertEquals("Check servlet context", this.context, this.config.getServletContext());
	}

	@Test
	public void testGetServletName() {
		this.config = new ServletConfigImpl(this.context, parser.getServletParams().get(this.servletName), this.servletName);
		assertEquals("Check Servlet Name", this.servletName, this.config.getServletName());
	}

}
