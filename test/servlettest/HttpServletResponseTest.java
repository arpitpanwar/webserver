package servlettest;

import static org.junit.Assert.*;

import java.net.Socket;

import org.junit.Before;
import org.junit.Test;

import edu.upenn.cis.cis455.webserver.model.LauncherArgs;
import edu.upenn.cis.cis455.webserver.model.Request;
import edu.upenn.cis.cis455.webserver.model.Response;
import edu.upenn.cis.cis455.webserver.model.servlet.HttpServletRequestImpl;
import edu.upenn.cis.cis455.webserver.model.servlet.HttpServletResponseImpl;
import edu.upenn.cis.cis455.webserver.model.servlet.ServletConfigImpl;
import edu.upenn.cis.cis455.webserver.model.servlet.ServletContextImpl;
import edu.upenn.cis.cis455.webserver.util.Constants;
import edu.upenn.cis.cis455.webserver.util.Parser;

public class HttpServletResponseTest {

	private HttpServletResponseImpl response;
	private Socket socket;
	private Request request;
	private LauncherArgs launcher;
	private Parser parser;
	private ServletContextImpl context;
	
	@Before
	public void setUp() throws Exception {
		
		socket = new Socket();
		request = new Request(socket);
		request.setRequestPath("/index.html");
		request.setQueryString("var1=val1&var2=val2");
		
		launcher = new LauncherArgs();
		launcher.setPortNum(8090);
		launcher.setRootFolder(System.getProperty("user.home"));
		launcher.setWebXmlPath("E:\\Course Assignments\\Fall 2014\\Internet and Web Systems\\Assignments\\Hw1\\servlet-examples\\examples\\WebApplication2\\WEB-INF/web.xml");
		this.parser = new Parser();
		this.parser.parse(launcher.getWebXmlPath());
		this.context = new ServletContextImpl(parser,launcher.getRootFolder());
		Response res = new Response();
		
		response = new HttpServletResponseImpl(res);
		
	}

	
	@Test
	public void testGetBufferSize() {
		assertEquals("Testing Buffer Size", Constants.DEFAULT_OUTPUT_BUFFER_SIZE, this.response.getBufferSize());
	}

	
	@Test
	public void testIsCommitted() {
		assertEquals("Is Committed", false, this.response.isCommitted());
	}

}