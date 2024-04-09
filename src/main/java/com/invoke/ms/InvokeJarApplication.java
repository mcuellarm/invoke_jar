package com.invoke.ms;

import java.util.HashMap;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.cysce.connector.controller.CysConnector;
import com.cysce.connector.model.MQConfig;
import com.cysce.connector.model.MsgField;
import com.ibm.mq.MQException;

@SpringBootApplication
public class InvokeJarApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvokeJarApplication.class, args);

		//Declaración e inicialización de los parametros de conexión 
		//(para testing sólo se tendrá en cuenta el parámetro TestingMode)
		MQConfig mqconf = new MQConfig();
		
		mqconf.setQueueManagerName("QM1");
		mqconf.setChannelName("DEV.ADMIN.SVRCONN");
		mqconf.setHostName("localhost");
		mqconf.setPort(1414);
		mqconf.setWaitInterval(60000);
		mqconf.setUser("admin");
		mqconf.setPass("passw0rd");
		mqconf.setTestingMode(true);
		
		
		try {
			//Obtener instancia del conector
			CysConnector mqc = CysConnector.getInstance(mqconf);
			
			//parámetros requeridos para enviar invocar el sendAndReceive
			String valpa = "V01;V02;V03"; 
			String reqFormatName = "BGMCSA";
			HashMap<String, Object> requestMsg = testReqMsg();
			HashMap<String, Object> headerMsg = testHeaderMsg();
			HashMap<Integer, MsgField> headerFields = testHeaderFields();
			HashMap<Integer, MsgField> requestFields = testReqFields();
			HashMap<Integer, MsgField> responseFields = testResFields();
			HashMap<Integer, MsgField> errorFields = testErrorFields();
			HashMap<Integer, MsgField> noticeFields = testNoticeFields();
			HashMap<Integer, MsgField> journalFields = testJournalFields();
			
			
			HashMap<String, HashMap<Integer, MsgField>> messageFields = new HashMap<>();
			
			messageFields.put("HEADER", headerFields);
			messageFields.put("BGMCSA", requestFields);
			messageFields.put("ERROR", errorFields);
			messageFields.put("AVISO", noticeFields);
			messageFields.put("JOURNAL", journalFields);
			messageFields.put("BGMCSA1", responseFields);
			
			//Invocar el sendAndReceive y obtener la respuesta 
			HashMap<String, List<HashMap<String, Object>>> res = mqc.sendAndReceive(valpa, reqFormatName, messageFields, headerMsg, requestMsg);
			
			//Visualización de la respuesta
			System.out.print("Response:\n");
		    res.forEach((k,v) -> {System.out.println("ITEM: " + k); v.forEach(item-> {item.forEach((key,val) ->{ System.out.println("Key: " + key + " Value: " + val);});});});
		    
		} catch (MQException e) {
			e.printStackTrace();
		}
		
	}
	
	public static HashMap<String, Object> testReqMsg(){
		HashMap<String, Object> msg = new HashMap<>();
		 msg.put("CODIGO-CTA-CLIENTE", "01020501850009269321");
		 msg.put("DIVISA", "VES");

		 		
		return msg;
		
	}
	
	
	public static HashMap<Integer, MsgField> testReqFields(){
		HashMap<Integer, MsgField> msg = new HashMap<>();
		MsgField field = new MsgField("CCC", "CODIGO-CTA-CLIENTE", "A", 1, (float) 20.00, "0");
		msg.put(1, field);
		
		field = new MsgField("DIVISA", "DIVISA", "A", 2, (float) 3.00, "VES");
		msg.put(2, field);
				
		
		return msg;
		
	}
	
	public static HashMap<String, Object> testHeaderMsg(){
		HashMap<String, Object> msg = new HashMap<>();
		 msg.put("USER", "BDVN005");
		 msg.put("TERMINAL", "0001");
		 msg.put("TIMEOUT", 10000);
		 msg.put("EXECUTION_TYPE", "D");
		 msg.put("SIZE", 0021);
		 msg.put("TRANSACTION", "BCSACSAB");
		 msg.put("APPLICATION", "01");
		 msg.put("SOURCE", "checkaccountbalances-7d8bf9c8cf-r5x46000");
		 msg.put("LOG_LEVEL", "1");
		 		
		return msg;
		
	}
	
	public static HashMap<Integer, MsgField> testHeaderFields(){
		HashMap<Integer, MsgField> msg = new HashMap<>();
		MsgField field = new MsgField("user", "USER", "A", 1, (float) 8.00, " ");
		msg.put(1, field);
		
		field = new MsgField("terminal", "TERMINAL", "A", 2, (float) 4.00, " ");
		msg.put(2, field);
				
		field = new MsgField("timeout", "TIMEOUT", "S", 3, (float) 5.00, 100);
		msg.put(3, field);
		
		field = new MsgField("exType", "EXECUTION_TYPE", "A", 4, (float) 1.00, "D");
		msg.put(4, field);
		
		field = new MsgField("size", "SIZE", "S", 5, (float) 4.00, 21);
		msg.put(5, field);
		
		field = new MsgField("transaccion", "TRANSACTION", "A", 6, (float) 8.00, " ");
		msg.put(6, field);
		
		field = new MsgField("app", "APPLICATION", "A", 7, (float) 2.00, " ");
		msg.put(7, field);
		
		field = new MsgField("source", "SOURCE", "A", 8, (float) 40.00, " ");
		msg.put(8, field);
		
		field = new MsgField("log", "LOG_LEVEL", "A", 9, (float) 1.00, "1");
		msg.put(9, field);
		
		return msg;
		
	}
	
	public static HashMap<Integer, MsgField> testResFields(){
		HashMap<Integer, MsgField> msg = new HashMap<>();
				
		MsgField field = new MsgField("PCCC", "CUENTA-PPAL", "A", 1, (float) 20.00, "1010101");
		msg.put(1, field);
		
		field = new MsgField("PSRETTO", "PPAL-SDO-RET-TOT", "N", 2, (float) 15.2, "0");
		msg.put(2, field);
		
		field = new MsgField("PSSDOFI", "PPAL-SDO-FINAL", "N", 3, (float) 15.2, "0");
		msg.put(3, field);
		
		return msg;
	}
	
	public static HashMap<Integer, MsgField> testErrorFields(){
		HashMap<Integer, MsgField> msg = new HashMap<>();
				
		MsgField field = new MsgField("codigo", "ERROR_CODE", "A", 1, (float) 11.00, " ");
		msg.put(1, field);
		
		field = new MsgField("descripcion", "DESCRIPTION", "A", 2, (float) -1.00, " ");
		msg.put(2, field);
				
		return msg;
	}
	
	public static HashMap<Integer, MsgField> testNoticeFields(){
		HashMap<Integer, MsgField> msg = new HashMap<>();
				
		MsgField field = new MsgField("aviso", "NOTICE", "A", 1, (float) 11.00, " ");
		msg.put(1, field);
		
		field = new MsgField("descripcion", "DESCRIPTION", "A", 2, (float) -1.00, " ");
		msg.put(2, field);
				
		return msg;
	}
	
	public static HashMap<Integer, MsgField> testJournalFields(){
		HashMap<Integer, MsgField> msg = new HashMap<>();
				
		MsgField field = new MsgField("JOURNAL", "JOURNAL", "A", 1, (float) 11.00, " ");
		msg.put(1, field);
		
		field = new MsgField("CENTRO", "CENTRO", "A", 2, (float) 4.00, " ");
		msg.put(2, field);
		
		field = new MsgField("NETNAME", "NETNAME", "A", 3, (float) 8.00, " ");
		msg.put(3, field);
		
		field = new MsgField("APLICAC", "APLICAC", "A", 4, (float) 2.00, " ");
		msg.put(4, field);
		
		field = new MsgField("SECUENC", "SECUENC", "A", 5, (float) 3.00, " ");
		msg.put(5, field);
		
		field = new MsgField("DIVISA", "DIVISA", "A", 6, (float) 3.00, " ");
		msg.put(6, field);
		
		field = new MsgField("IMPORTE", "IMPORTE", "A", 7, (float) 17.00, " ");
		msg.put(7, field);
		
		field = new MsgField("IND-DEB-HAB", "IND-DEB-HAB", "A", 8, (float) 1.00, " ");
		msg.put(8, field);
		
		field = new MsgField("IND-CAJ-COMP", "IND-CAJ-COMP", "A", 9, (float) 1.00, " ");
		msg.put(9, field);
		
		field = new MsgField("IND-TOTA", "IND-TOTA", "A", 10, (float) 1.00, " ");
		msg.put(10, field);
		
		field = new MsgField("PRODUCTO", "PRODUCTO", "A", 11, (float) 20.00, " ");
		msg.put(11, field);
		
		field = new MsgField("REFERENC", "REFERENC", "A", 12, (float) 20.00, " ");
		msg.put(12, field);
		
		field = new MsgField("MAS-INFORM", "MAS-INFORM", "A", 13, (float) 20.00, " ");
		msg.put(13, field);
		
		field = new MsgField("FECHA-OPER", "FECHA-OPER", "A", 14, (float) 10.00, " ");
		msg.put(14, field);
		
		field = new MsgField("FECHA-CONT", "FECHA-CONT", "A", 15, (float) 10.00, " ");
		msg.put(15, field);
		
		field = new MsgField("CODTRAN", "CODTRAN", "A", 16, (float) 4.00, " ");
		msg.put(16, field);
		
		field = new MsgField("NUMTASK", "NUMTASK", "A", 17, (float) 7.00, " ");
		msg.put(17, field);
				
		return msg;
	}

}
