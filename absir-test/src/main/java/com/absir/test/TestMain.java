/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-6-10 上午10:10:52
 */
package com.absir.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.absir.appserv.system.helper.HelperClient;
import com.absir.core.kernel.KernelCollection;
import com.trilead.ssh2.Connection;
import com.trilead.ssh2.SCPClient;

/**
 * @author absir
 * 
 */
public class TestMain {

	public static void main(String... args) throws IOException {
		Connection conn = new Connection("42.62.40.112");
		conn.connect();
		boolean isAuthenticated = conn.authenticateWithPassword("root", "88923100Uc");
		if (isAuthenticated == false) {
			throw new IOException("Authentication failed.");
		}

		File file = new File("d:\\fight\\xls\\");
		List<String> xlsFile = new ArrayList<String>();
		for (File xls : file.listFiles()) {
			xlsFile.add(xls.toString());
		}

		SCPClient client = new SCPClient(conn);
		client.put(KernelCollection.toArray(xlsFile, String.class), "/server/tomcat7/webapps/appserv-static/WEB-INF/classes/xls/");
		conn.close();

		System.out.println(HelperClient.openConnection("http://42.62.40.112:8080/achieve-dtyx/asset/modifier/1", null, null));
	}
}
