import java.util.Enumeration;
import java.util.Properties;

import com.ibm.broker.config.proxy.BrokerProxy;
import com.ibm.broker.config.proxy.ConfigurableService;

import com.ibm.broker.javacompute.MbJavaComputeNode;

import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbJSON;
import com.ibm.broker.plugin.MbMessage;
import com.ibm.broker.plugin.MbMessageAssembly;
import com.ibm.broker.plugin.MbOutputTerminal;
import com.ibm.broker.plugin.MbUserException;


public class IIBExtractor_getConfigInfo2 extends MbJavaComputeNode {

	BrokerProxy b = null;
	
	public void evaluate(MbMessageAssembly inAssembly) throws MbException {
		MbOutputTerminal out = getOutputTerminal("out");
		//MbOutputTerminal alt = getOutputTerminal("alternate");

		MbMessage inMessage = inAssembly.getMessage();
		MbMessageAssembly outAssembly = null;
		try {
			// create new message as a copy of the input
			MbMessage outMessage = new MbMessage(inMessage);
			outAssembly = new MbMessageAssembly(inAssembly, outMessage);
			// ----------------------------------------------------------
			// Add user code below
			
			//getBrokerNodeName
			String brokerName = getBroker().getName();
		 	b = BrokerProxy.getLocalInstance(brokerName);
		
		 	MbElement jsonData = outMessage
		 			.getRootElement()
		 			.createElementAsLastChild(MbJSON.PARSER_NAME)
		 			.createElementAsLastChild(MbJSON.ARRAY,MbJSON.DATA_ELEMENT_NAME, null);
		 	
		 	ConfigurableService[] emailServer = b.getConfigurableServices("EmailServer");
    		for (ConfigurableService c : emailServer) {
    			MbElement outJsonappResList = jsonData.createElementAsLastChild(MbElement.TYPE_NAME, "Item", null);
    			outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME, "BrokerName", brokerName);
    			outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"Type",c.getType());
    			outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"Name",c.getName());
    			
    			Properties p = c.getProperties();
    		 	Enumeration<Object> keys = p.keys();
        		while (keys.hasMoreElements()) {
        		    String key = (String)keys.nextElement();
        		    String value = (String)p.get(key);
        		    outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,key,value);
        		}
            }
    		
    		ConfigurableService[] smtp = b.getConfigurableServices("SMTP");
    		for (ConfigurableService c : smtp) {
    			MbElement outJsonappResList = jsonData.createElementAsLastChild(MbElement.TYPE_NAME, "Item", null);
    			outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME, "BrokerName", brokerName);
    			outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"Type",c.getType());
    			outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"Name",c.getName());
    			
    			Properties p = c.getProperties();
    		 	Enumeration<Object> keys = p.keys();
        		while (keys.hasMoreElements()) {
        		    String key = (String)keys.nextElement();
        		    String value = (String)p.get(key);
        		    outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,key,value);
        		}
            }
		 	
			// End of user code
			// ----------------------------------------------------------
		} catch (MbException e) {
			// Re-throw to allow Broker handling of MbException
			throw e;
		} catch (RuntimeException e) {
			// Re-throw to allow Broker handling of RuntimeException
			throw e;
		} catch (Exception e) {
			// Consider replacing Exception with type(s) thrown by user code
			// Example handling ensures all exceptions are re-thrown to be handled in the flow
			throw new MbUserException(this, "evaluate()", "", "", e.toString(),
					null);
		}
		// The following should only be changed
		// if not propagating message to the 'out' terminal
		out.propagate(outAssembly);

	}

}
