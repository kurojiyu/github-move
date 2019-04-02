import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Enumeration;

import com.ibm.broker.config.proxy.ApplicationProxy;
import com.ibm.broker.config.proxy.BrokerProxy;
import com.ibm.broker.config.proxy.ConfigManagerProxyPropertyNotInitializedException;
import com.ibm.broker.config.proxy.ConfigurableService;
import com.ibm.broker.config.proxy.ExecutionGroupProxy;
import com.ibm.broker.config.proxy.MessageFlowProxy;
import com.ibm.broker.config.proxy.RestApiProxy;
import com.ibm.broker.javacompute.MbJavaComputeNode;
import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbJSON;
import com.ibm.broker.plugin.MbMessage;
import com.ibm.broker.plugin.MbMessageAssembly;
import com.ibm.broker.plugin.MbOutputTerminal;
import com.ibm.broker.plugin.MbUserException;


public class Mf_test_JavaCompute extends MbJavaComputeNode {

   // private static final String DEFAULT_BROKER_NAME = "IIB.DEVL01.ITESM";
    
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
			//MbBroker iib;
			String brokerName = getBroker().getName();
		 	b = BrokerProxy.getLocalInstance(brokerName);
		 	
            
		 	// ---------- get integration servers ----------
		 	//MbElement outRoot 			= outMessage.getRootElement();
		 	
		 	MbElement jsonData =    
		 		   outMessage.getRootElement().createElementAsLastChild(MbJSON.PARSER_NAME).createElementAsLastChild
            (MbJSON.ARRAY,MbJSON.DATA_ELEMENT_NAME, null);
	 		//MbElement outJsonRoot 		= outRoot.createElementAsLastChild("JSON");
	 		//MbElement outJsonData 		= outJsonRoot.createElementAsLastChild(MbJSON.ARRAY, "Data", null);
	 		//MbElement outJsonBrokerInfo = outJsonData.createElementAsLastChild(MbJSON.ARRAY, "BrokerInformation", null);
	 		//outJsonBrokerInfo.createElementAsFirstChild(MbElement.TYPE_NAME,"NameBroker", brokerName);
//	 		String configService = b.getConfigurableServiceProperty("ODBC");
//	 		for (String s : configService) {
//	 			outJsonBrokerInfo.createElementAsFirstChild(MbElement.TYPE_NAME,"Name",s);
//            }
//	 		
		 	//MbElement outJsonisList 	= outJsonBrokerInfo.createElementAsFirstChild(MbJSON.ARRAY, "IntegrationServers",null);
//		 	
		 	//MbElement outJsonresourceList 	= outJsonisList.createElementAsFirstChild(MbJSON.ARRAY, "Resources",null);
		 	
		 	try {
		 		
		 		SimpleDateFormat utcFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		 		
		        Enumeration<ExecutionGroupProxy> executionGroupProxy;
		        Enumeration<ApplicationProxy> applicationProxy;
		        Enumeration<RestApiProxy> restApiProxy;	
		        
		        //getExecutionGroups = Integration Servers
		        executionGroupProxy = b.getExecutionGroups(null);		        
                
		        if (executionGroupProxy != null) {
		        	while (executionGroupProxy.hasMoreElements()) {
		        		ExecutionGroupProxy e = executionGroupProxy.nextElement();
		        		
		        		// Create Integration Servers
		        		//MbElement outJsonappList = outJsonisList.createElementAsFirstChild(MbJSON.ARRAY, e.getName(), e.getName());
		        		//outJsonappList.createElementAsFirstChild(MbElement.TYPE_NAME, "IntegrationServerName", e.getName());
		        		
		        		//Get Applications
		        		//Get Rest APIs
		        		applicationProxy = e.getApplications(null);
		                restApiProxy = e.getRestApis(null);
		                
		                if (applicationProxy != null){
		                	while (applicationProxy.hasMoreElements()) {
		                		ApplicationProxy a = applicationProxy.nextElement();
		                		
		                		//MbElement outJsonappResList = outJsonappList.createElementAsFirstChild(MbJSON.ARRAY,a.getName(),null);
		                		//MbElement outJsonappResList = outJsonData.createElementAsLastChild(MbJSON.ARRAY,a.getName(),null);
		                		MbElement outJsonappResList = jsonData.createElementAsLastChild(MbElement.TYPE_NAME, "Item", null);
		                		outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"BrokerName", brokerName);
		                		outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"IntegrationName", e.getName());
		                		outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"ApplicationName", a.getName());
				            	outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"type", a.getType().toString());
				            	outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"deployTime", utcFormat.format(a.getDeployTime()));
				            	
				            	Enumeration<MessageFlowProxy> messageFlowProxy = a.getMessageFlows(null);				         
				            	if (messageFlowProxy != null) {
				            		MessageFlowProxy m = messageFlowProxy.nextElement();
				            		outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"additionalInstances",m.getAdditionalInstances());
						        }
		                	}
		                }
		                if (restApiProxy != null){
		                	while (restApiProxy.hasMoreElements()) {
		                		RestApiProxy r = restApiProxy.nextElement();
		                		
		                		//MbElement outJsonappResList = outJsonappList.createElementAsFirstChild(MbJSON.ARRAY,r.getName(),null);
		                		//MbElement outJsonappResList = outJsonData.createElementAsLastChild(MbJSON.ARRAY,r.getName(),null);
		                		MbElement outJsonappResList = jsonData.createElementAsLastChild(MbElement.TYPE_NAME, "Item", null);
		                		outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"BrokerName", brokerName);
		                		outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"IntegrationName", e.getName());
		                		outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"ApplicationName", r.getName());
				            	outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"type", r.getType().toString());
				            	outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"deployTime", utcFormat.format(r.getDeployTime()));
				            	
				            	Enumeration<MessageFlowProxy> messageFlowProxy = r.getMessageFlows(null);				         
				            	if (messageFlowProxy != null) {
				            		MessageFlowProxy m = messageFlowProxy.nextElement();
				            		outJsonappResList.createElementAsLastChild(MbElement.TYPE_NAME,"additionalInstances",m.getAdditionalInstances());
						        }
		                	}
		                }
//		                MbElement outJsonresourceItem = outJsonappList.createElementAsFirstChild(MbJSON.ARRAY,"ODBC",null);
//		                ResourceManagerProxy rmp = e.getResourceManagerByName("ODBC");
//		                String[] runtimeProperties =  rmp.getRuntimePropertyNames();
//		                for (String s : runtimeProperties) {
//				        	outJsonresourceItem.createElementAsFirstChild(MbElement.TYPE_NAME,"Name",s);
//		                }
		        	}
		        }
//		        MbElement outJsonconfigList = outJsonBrokerInfo.createElementAsFirstChild(MbJSON.ARRAY, "ConfigurableServices",null);
//		        MbElement outJsonsecurityList = outJsonconfigList.createElementAsFirstChild(MbJSON.ARRAY, "SecurityProfiles", null);
//		        ConfigurableService[] securityProfiles = b.getConfigurableServices("SecurityProfiles");
//		        
//		        for (ConfigurableService configurableService : securityProfiles) {
//		        	outJsonsecurityList.createElementAsFirstChild(MbJSON.ARRAY,"Name",configurableService.getName());
//                }
		        
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
	
	public void getExecutionGroups(BrokerProxy b, StringBuffer key, StringBuffer value){
		try {
	        Enumeration<ExecutionGroupProxy> e;			
			e = b.getExecutionGroups(null);			
	        if (e == null) {
	            value.append(""+e);
	        } else {
	            int count = 0;
	            while (e.hasMoreElements()) {
	                count++;
	                key.append("\n    ["+count+"]");
	                value.append("\n");
	                ExecutionGroupProxy c = e.nextElement();
	                value.append(c);
	            }
	        }
	        //p.setProperty(""+key, ""+value);
		} catch (ConfigManagerProxyPropertyNotInitializedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}


}
