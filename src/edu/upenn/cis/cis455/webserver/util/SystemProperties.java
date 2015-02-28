package edu.upenn.cis.cis455.webserver.util;

import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * Loading the properties file
 * @author cis455
 *
 */

public class SystemProperties {
	
	static final  Logger LOG = Logger.getLogger(SystemProperties.class);
   	private Properties prop;
    private String propFile;
    private static SystemProperties sysProp = null;
    
    private SystemProperties(){
      try{ 
    	propFile = Constants.PROP_FILE;  
        prop = new Properties();
        prop.load(SystemProperties.class.getResourceAsStream(propFile));
      }catch(Exception e){
        LOG.debug("Cannot Load Properties file "+e.getMessage());
      }
    }
    
    public static SystemProperties load(){
    	
    	if(sysProp == null)
    		sysProp = new SystemProperties();
    	
    	return sysProp;
    		
    }
    
    public String getProperty(String property){
        
        String value = prop.getProperty(property);
       
        return value;
        
    }
    
    
    public String getProperty(String property,String def){
        
        String value = prop.getProperty(property,def);
       
        return value;
        
    }
    
    
    public synchronized void setProperty(String property, String value){
        prop.setProperty(property, value);
    }
    
}