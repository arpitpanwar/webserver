package servlettest;

import static org.junit.Assert.*;

import java.net.Socket;

import org.junit.Before;
import org.junit.Test;

import edu.upenn.cis.cis455.webserver.model.LauncherArgs;
import edu.upenn.cis.cis455.webserver.model.Request;
import edu.upenn.cis.cis455.webserver.model.servlet.ServletContextImpl;
import edu.upenn.cis.cis455.webserver.util.Parser;

public class HttpServletRequestTest {

	private Request request;
	private Parser parser;
	private Socket socket;
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
		socket = new Socket();
		request = new Request(socket);
		request.setRequestPath("/index.html");
		request.setQueryString("var1=val1&var2=val2");
	}

	
	@Test
	public void testGetQueryString() {
	
		assertEquals("Test Query String","var1=val1&var2=val2", this.request.getQueryString());
	}


}
