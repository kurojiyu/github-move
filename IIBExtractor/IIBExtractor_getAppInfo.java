import java.text.SimpleDateFormat;

import java.util.Enumeration;
import java.util.Properties;

import com.ibm.broker.javacompute.MbJavaComputeNode;

import com.ibm.broker.config.proxy.ApplicationProxy;
import com.ibm.broker.config.proxy.BrokerProxy;
import com.ibm.broker.config.proxy.ConfigManagerProxyPropertyNotInitializedException;
import com.ibm.broker.config.proxy.ExecutionGroupProxy;
import com.ibm.broker.config.proxy.MessageFlowProxy;
import com.ibm.broker.config.proxy.RestApiProxy;

import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbJSON;
import com.ibm.broker.plugin.MbMessage;
import com.ibm.broker.plugin.MbMessageAssembly;
import com.ibm.broker.plugin.MbOutputTerminal;
import com.ibm.broker.plugin.MbUserException;


public class IIBExtractor_getAppInfo extends MbJavaComputeNode {
    
    BrokerProxy b = null;
    
	public void evaluate(MbMessageAssembly inAssembly) throws MbException {
		MbOutputTerminal out = getOutputTerminal("out");
		//MbOutputTerminal alt = getOutputTerminal("alternate");

		MbMessage inMessage = inAssembly.getMessage();
		MbMessageAssembly outAssembly = null;
		try {
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
		 	
		 	try {
		 		SimpleDateFormat utcFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
	
		 		//declare Enums for IS, APPs, RestAPIs
		        Enumeration<ExecutionGroupProxy> executionGroupProxys;
		        Enumeration<ApplicationProxy> applicationProxys;
		        Enumeration<RestApiProxy> restApiProxys;	
		        
		        //getExecutionGroups = Integration Servers
		        executionGroupProxys = b.getExecutionGroups(null);		        
                
		        if (executionGroupProxys != null) {
		        	while (executionGroupProxys.hasMoreElements()) {
		        		ExecutionGroupProxy e = executionGroupProxys.nextElement();
		        		
		        		//Get Applications
		        		applicationProxys = e.getApplications(null);
		        		//Get Rest APIs
		                restApiProxys = e.getRestApis(null);
		                
		                if (applicationProxys != null){
		                	while (applicationProxys.hasMoreElements()) {
		                		ApplicationProxy a = applicationProxys.nextElement();
		                		
		                		MbElement outJsonappResList = jsonData.createElementAsLastChild(MbElement.TYPE_NAME, "Item", null);
		                		outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"BrokerName", brokerName);
		                		outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"IntegrationName", e.getName());
		                		outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"ApplicationName", a.getName());
				            	outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"Type", a.getType().toString());
				            	outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"DeployTime", utcFormat.format(a.getDeployTime()));
				            	
				            	Enumeration<MessageFlowProxy> messageFlowProxy = a.getMessageFlows(null);				         
				            	if (messageFlowProxy != null) {
				            		MessageFlowProxy m = messageFlowProxy.nextElement();
				            		outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"AdditionalInstances",m.getAdditionalInstances());
				            		
				            		//MbElement outJsonISConfProp = outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME, "Properties", null);
				            		outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"SecurityProfileName","");
			    	        		Properties p = m.getAdvancedProperties();
			    	    		 	Enumeration<Object> keys = p.keys();
			    	        		while (keys.hasMoreElements()) {
			    	        		    String key = (String)keys.nextElement();
			    	        		    String value = (String)p.get(key);
			    	        		    if (key.equals("securityProfileName")){
			    	        		    	outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"SecurityProfileName",value);
			    	        		    }
			    	        		}
						        }
		                	}
		                }
		                if (restApiProxys != null){
		                	while (restApiProxys.hasMoreElements()) {
		                		RestApiProxy r = restApiProxys.nextElement();
		                		
		                		MbElement outJsonappResList = jsonData.createElementAsLastChild(MbElement.TYPE_NAME, "Item", null);
		                		outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"BrokerName", brokerName);
		                		outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"IntegrationName", e.getName());
		                		outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"ApplicationName", r.getName());
				            	outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"Type", r.getType().toString());
				            	outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"DeployTime", utcFormat.format(r.getDeployTime()));
				            	
				            	Enumeration<MessageFlowProxy> messageFlowProxy = r.getMessageFlows(null);				         
				            	if (messageFlowProxy != null) {
				            		MessageFlowProxy m = messageFlowProxy.nextElement();
				            		outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"AdditionalInstances",m.getAdditionalInstances());
				            		
				            		//MbElement outJsonISConfProp = outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME, "Properties", null);
				            		outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"SecurityProfileName","");
				            		Properties p = m.getAdvancedProperties();
			    	    		 	Enumeration<Object> keys = p.keys();
			    	        		while (keys.hasMoreElements()) {
			    	        		    String key = (String)keys.nextElement();
			    	        		    String value = (String)p.get(key);
			    	        		    if (key.equals("securityProfileName")){
			    	        		    	outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"SecurityProfileName",value);
			    	        		    }
			    	        		}
						        }
		                	}
		                }
		        	}
		        }
		        
			} catch (ConfigManagerProxyPropertyNotInitializedException e1) {
				e1.printStackTrace();
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
