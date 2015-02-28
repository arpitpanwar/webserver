package edu.upenn.cis.cis455.webserver.util;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletInputStream;
/**
 * Servlet input stream implementation
 * @author cis455
 *
 */
public class ServletInputStreamImpl extends ServletInputStream {

	private InputStream stream;
	
	public  ServletInputStreamImpl(InputStream stream) {
			this.stream = stream;
	}
	
	@Override
	public int read() throws IOException {
		
		return stream.read();
	}

}
