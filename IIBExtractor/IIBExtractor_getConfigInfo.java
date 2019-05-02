import java.util.List;

import com.ibm.broker.config.proxy.BrokerProxy;
import com.ibm.broker.config.proxy.ConfigurableService;
import com.ibm.broker.config.proxy.SecurityIdentity;

import com.ibm.broker.javacompute.MbJavaComputeNode;

import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbJSON;
import com.ibm.broker.plugin.MbMessage;
import com.ibm.broker.plugin.MbMessageAssembly;
import com.ibm.broker.plugin.MbOutputTerminal;
import com.ibm.broker.plugin.MbUserException;


public class IIBExtractor_getConfigInfo extends MbJavaComputeNode {

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
		 	
		 	
		 	//Enumeration<ExecutionGroupProxy> executionGroupProxys = b.getExecutionGroups(null);
		 	//getProperties from ResourceManagers no data important
//		 	if (executionGroupProxys != null) {
//	        	while (executionGroupProxys.hasMoreElements()) {
//	        		ExecutionGroupProxy e = executionGroupProxys.nextElement();
//	        		MbElement outJson = jsonData.createElementAsLastChild(MbElement.TYPE_NAME,null,null);
//	        		
//	        		outJson.createElementAsLastChild(MbElement.TYPE_NAME,"IntegrationServer",e.getName());
//	        		
//	        		MbElement outJsonISConf = outJson.createElementAsLastChild(MbJSON.ARRAY, "ResourceManagers", null);
//	        		
//	        		Enumeration<ResourceManagerProxy> resourceManagerProxys = e.getResourceManagers(null);
//	        		if (resourceManagerProxys != null) {
//	        			int i=1;
//	    	        	while (resourceManagerProxys.hasMoreElements()) {
//	    	        		ResourceManagerProxy r = resourceManagerProxys.nextElement();
//	    	        		
//	    	        		outJsonISConf.createElementAsLastChild(MbElement.TYPE_NAME,r.getType()+i,r.getName());
//	    	        		
//	    	        		MbElement outJsonISConfProp = outJsonISConf.createElementAsLastChild(MbElement.TYPE_NAME, "Properties", null);
//	    	        		
//	    	        		Properties p = r.getProperties();
//	    	    		 	Enumeration<Object> keys = p.keys();
//	    	        		while (keys.hasMoreElements()) {
//	    	        		    String key = (String)keys.nextElement();
//	    	        		    String value = (String)p.get(key);
//	    	        		    outJsonISConfProp.createElementAsLastChild(MbElement.TYPE_NAME,key,value);
//	    	        		}
//	    	        		i++;
//	    	        	}
//	        		}
//	        	}
//		 	}

		 	//gets ODBCs FTPs SFTPs EMAIL SMTP
		 	List<SecurityIdentity> securityIdentitys = b.getSecurityIdentities();
		 	for (SecurityIdentity securityIdentity : securityIdentitys) {
		 		if (securityIdentity.getType().toString() == "odbc" ||
		 			securityIdentity.getType().toString() == "ftp"  ||
		 			securityIdentity.getType().toString() == "sftp"){
		 			
			 		MbElement outJsonappResList = jsonData.createElementAsLastChild(MbElement.TYPE_NAME, "Item", null);
			 		outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME, "BrokerName", brokerName);
			 		outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME, "Type", securityIdentity.getType().toString());
			 		outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME, "Name", securityIdentity.getName());
		 		}
			}
		 	
		 	//getAllSecurityProfiles
    		ConfigurableService[] securityProfiles = b.getConfigurableServices("SecurityProfiles");
    		for (ConfigurableService configurableService : securityProfiles) {
    			MbElement outJsonappResList = jsonData.createElementAsLastChild(MbElement.TYPE_NAME, "Item", null);
    			outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME, "BrokerName", brokerName);
    			outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"Type",configurableService.getType().toString());
    			outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"Name",configurableService.getName());
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
