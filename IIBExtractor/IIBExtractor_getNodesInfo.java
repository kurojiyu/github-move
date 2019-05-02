import java.util.Enumeration;
import java.util.Properties;

import com.ibm.broker.javacompute.MbJavaComputeNode;

import com.ibm.broker.config.proxy.ApplicationProxy;
import com.ibm.broker.config.proxy.BrokerProxy;
import com.ibm.broker.config.proxy.ConfigManagerProxyPropertyNotInitializedException;
import com.ibm.broker.config.proxy.ExecutionGroupProxy;
import com.ibm.broker.config.proxy.MessageFlowProxy;
import com.ibm.broker.config.proxy.MessageFlowProxy.Node;
import com.ibm.broker.config.proxy.RestApiProxy;
import com.ibm.broker.config.proxy.SubFlowProxy;

import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbJSON;
import com.ibm.broker.plugin.MbMessage;
import com.ibm.broker.plugin.MbMessageAssembly;
import com.ibm.broker.plugin.MbOutputTerminal;
import com.ibm.broker.plugin.MbUserException;


public class IIBExtractor_getNodesInfo extends MbJavaComputeNode {

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
		                		
				            	Enumeration<MessageFlowProxy> messageFlowProxy = a.getMessageFlows(null);				         
				            	if (messageFlowProxy != null) {
				            		while (messageFlowProxy.hasMoreElements()){
				            			MessageFlowProxy m = messageFlowProxy.nextElement();
				            			
				            			Enumeration<Node> nodes = m.getNodes();
				            			if(nodes != null){
				            				while (nodes.hasMoreElements()) {
				            					Node n = nodes.nextElement();
						                		if(n.getType() != "SubFlowNode" && n.getType() != "ComIbmLabelNode" && n.getType() != "ComIbmRouteToLabelNode"){
						                    		Properties p = n.getProperties();
						                    		Enumeration<Object> keys = p.keys();
						                    		while (keys.hasMoreElements()) {
						                    		    String key = (String)keys.nextElement();
						                    		    String value = (String)p.get(key);
						                    		    
						                    		    MbElement outJsonappResList = jsonData.createElementAsLastChild(MbElement.TYPE_NAME, "Item", null);
								                		outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"BrokerName", brokerName);
								                		outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"ApplicationName", a.getName());
						                    		    outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"Node", n.getType());
						                    		    outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"Property",key);
						                    		    outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"Value",value);
						                    		}
						                		}
						            		}
				            			}
				            		} 
						        }
				            	
				            	Enumeration<SubFlowProxy> subFlowProxy = a.getSubFlows(null);
				            	if (subFlowProxy != null) {
				            		while (subFlowProxy.hasMoreElements()) {
					            		SubFlowProxy s = subFlowProxy.nextElement();
					            		
					            		Enumeration<Node> nodes = s.getNodes();
					            		if(nodes != null){
						            		while (nodes.hasMoreElements()) {
						                		Node n = nodes.nextElement();
						                		if(n.getType() != "SubFlowNode" && n.getType() != "ComIbmLabelNode" && n.getType() != "ComIbmRouteToLabelNode"){
						                			
						                    		Properties p = n.getProperties();
						                    		Enumeration<Object> keys = p.keys();
						                    		while (keys.hasMoreElements()) {
						                    		    String key = (String)keys.nextElement();
						                    		    String value = (String)p.get(key);
						                    		    
						                    		    MbElement outJsonappResList = jsonData.createElementAsLastChild(MbElement.TYPE_NAME, "Item", null);
								                		outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"BrokerName", brokerName);
								                		outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"ApplicationName", a.getName());
						                    		    outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"Node", n.getType());
						                    		    outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"Property",key);
						                    		    outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"Value",value);
						                    		}
						                		}
						            		}
					            		}
				            		}
				            	}
		                	}
		                }
		                if (restApiProxys != null){
		                	while (restApiProxys.hasMoreElements()) {
		                		RestApiProxy r = restApiProxys.nextElement();
		                		
				            	Enumeration<MessageFlowProxy> messageFlowProxy = r.getMessageFlows(null);				         
				            	if (messageFlowProxy != null) {
				            		while (messageFlowProxy.hasMoreElements()){
				            			MessageFlowProxy m = messageFlowProxy.nextElement();
				            			
				            			Enumeration<Node> nodes = m.getNodes();
				            			if(nodes != null){
						            		while (nodes.hasMoreElements()) {
						                		Node n = nodes.nextElement();
						                		if(n.getType() != "SubFlowNode" && n.getType() != "ComIbmLabelNode" && n.getType() != "ComIbmRouteToLabelNode"){
						                			
						                    		Properties p = n.getProperties();
						                    		Enumeration<Object> keys = p.keys();
						                    		while (keys.hasMoreElements()) {
						                    		    String key = (String)keys.nextElement();
						                    		    String value = (String)p.get(key);
						                    		    

								                		MbElement outJsonappResList = jsonData.createElementAsLastChild(MbElement.TYPE_NAME, "Item", null);
								                		outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"BrokerName", brokerName);
								                		outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"IntegrationName", e.getName());
								                		outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"ApplicationName", r.getName());
						                    		    outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"Node",n.getType());
						                    		    outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"Property",key);
						                    		    outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"Value",value);
						                    		}
						                		}
						            		}
				            			}
				            		}
						        }
				            	Enumeration<SubFlowProxy> subFlowProxy = r.getSubFlows(null);
				            	if (subFlowProxy != null) {
				            		while (subFlowProxy.hasMoreElements()) {
					            		SubFlowProxy s = subFlowProxy.nextElement();
					            		
					            		Enumeration<Node> nodes = s.getNodes();
					            		if(nodes != null){
						            		while (nodes.hasMoreElements()) {
						                		Node n = nodes.nextElement();
						                		if(n.getType() != "SubFlowNode" && n.getType() != "ComIbmLabelNode" && n.getType() != "ComIbmRouteToLabelNode"){
						                			
						                    		Properties p = n.getProperties();
						                    		Enumeration<Object> keys = p.keys();
						                    		while (keys.hasMoreElements()) {
						                    		    String key = (String)keys.nextElement();
						                    		    String value = (String)p.get(key);
						                    		    

								                		MbElement outJsonappResList = jsonData.createElementAsLastChild(MbElement.TYPE_NAME, "Item", null);
								                		outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"BrokerName", brokerName);
								                		outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"IntegrationName", e.getName());
								                		outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"ApplicationName", r.getName());
						                    		    outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"Node", n.getType());
						                    		    outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"Property",key);
						                    		    outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"Value",value);
						                    		}
						                		}
						            		}
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
