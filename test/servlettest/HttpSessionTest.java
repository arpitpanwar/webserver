package servlettest;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import edu.upenn.cis.cis455.webserver.model.LauncherArgs;
import edu.upenn.cis.cis455.webserver.model.servlet.HttpSessionImpl;
import edu.upenn.cis.cis455.webserver.model.servlet.ServletContextImpl;
import edu.upenn.cis.cis455.webserver.util.Constants;
import edu.upenn.cis.cis455.webserver.util.Parser;

public class HttpSessionTest {
	
	private Parser parser;
	private ServletContextImpl context;
	private LauncherArgs launcher;
	private HttpSessionImpl session;

	@Before
	public void setUp() throws Exception {
		launcher = new LauncherArgs();
		launcher.setPortNum(8090);
		launcher.setRootFolder(System.getProperty("user.home"));
		launcher.setWebXmlPath("E:\\Course Assignments\\Fall 2014\\Internet and Web Systems\\Assignments\\Hw1\\servlet-examples\\examples\\WebApplication2\\WEB-INF/web.xml");
		this.parser = new Parser();
		this.parser.parse(launcher.getWebXmlPath());
		this.context = new ServletContextImpl(parser,launcher.getRootFolder());
		this.session = new HttpSessionImpl(this.context);
	}

	

	@Test
	public void testGetCreationTime() {
		Date dt = new Date();
		this.session = new HttpSessionImpl(context);
		assertEquals("Check creation time", dt.getTime(), this.session.getCreationTime());
	}

	
	@Test
	public void testGetMaxInactiveInterval() {
		this.session = new HttpSessionImpl(context);
		assertEquals("Check max inactive interval", 30, this.session.getMaxInactiveInterval());

	}
	

}
