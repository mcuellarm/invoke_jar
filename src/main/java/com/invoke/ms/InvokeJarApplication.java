package com.invoke.ms;

import java.util.HashMap;

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
			String transaction = ""; 
			HashMap<String, Object> requestMsg = testReqMsg();
			HashMap<Integer, MsgField> requestFields = testReqFieldsMsg();;
			HashMap<Integer, MsgField> responseFields = testResFieldsMsg();;
			
			//Invocar el sendAndReceive y obtener la respuesta 
			HashMap<String, Object> res = mqc.sendAndReceive(transaction, requestMsg, requestFields, responseFields);
			
			//Visualización de la respuesta
			System.out.print("Response:\n");
		    res.forEach((k,v) -> System.out.println("Key: " + k + ": Value: " + v ));
		    
		} catch (MQException e) {
			e.printStackTrace();
		}
		
	}
	
	public static HashMap<String, Object> testReqMsg(){
		HashMap<String, Object> msg = new HashMap<>();
		 msg.put("CONDENTI", "1001");
		 msg.put("CUENTA", "10101010");
		 msg.put("DIVISA", "USD");
		 msg.put("CODPROD", "A3");
		 msg.put("DISPONI", 999.99);
		 		
		return msg;
		
	}
	
	public static HashMap<Integer, MsgField> testReqFieldsMsg(){
		HashMap<Integer, MsgField> msg = new HashMap<>();
		MsgField field = new MsgField("codigoEntidad", "CONDENTI", "A", 1, (float) 10.00, "10001");
		msg.put(1, field);
		
		field = new MsgField("cuenta", "CUENTA", "A", 2, (float) 15.00, "1010101010");
		msg.put(2, field);
		
		field = new MsgField("divisa", "DIVISA", "A", 4, (float) 3.00, "USD");
		msg.put(4, field);
		
		field = new MsgField("codigoProducto", "CODPROD", "A", 3, (float) 2.00, "A2");
		msg.put(3, field);
		
		field = new MsgField("saldoDisponible", "DISPONI", "S", 5, (float) 15.2, 1.447252525);
		msg.put(5, field);
		
		return msg;
		
	}
	
	public static HashMap<Integer, MsgField> testResFieldsMsg(){
		HashMap<Integer, MsgField> msg = new HashMap<>();
		MsgField field = new MsgField("codigoEntidad", "CONDENTI", "A", 1, (float) 10.00, "10001");
		msg.put(1, field);
		
		field = new MsgField("cuenta", "CUENTA", "A", 2, (float) 15.00, "1010101010");
		msg.put(2, field);
		
		field = new MsgField("divisa", "DIVISA", "A", 4, (float) 3.00, "USD");
		msg.put(4, field);
		
		field = new MsgField("codigoProducto", "CODPROD", "A", 3, (float) 2.00, "A2");
		msg.put(3, field);
		
		field = new MsgField("saldoDisponible", "DISPONI", "S", 5, (float) 15.2, 1.447252525);
		msg.put(5, field);
		
		return msg;
	}

}
