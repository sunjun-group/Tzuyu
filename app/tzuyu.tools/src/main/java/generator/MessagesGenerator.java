/*
 * Copyright (C) 2013 by SUTD (Singapore)
 * All rights reserved.
 *
 * 	Author: SUTD
 *  Version:  $Revision: 1 $
 */

package generator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

/**
 * @author LLT
 *
 */
public class MessagesGenerator {

	private static final String START_GENERATED_PART_TOKEN = "//	Generated part";
	private static final String END_GENERATED_PART_TOKEN = "//	End generated part";
	private static String BASE = "D:/_1_Projects/Tzuyu/";
	private static String TRUNK = BASE + "workspace/trunk/";
	private static String MESSAGES_PROPERTIES_PATH = TRUNK
			+ "app/tzuyuEclipsePlugin/src/tzuyu/plugin/messages.properties";
	private static String GENERATED_MESSAGES_CLASS_PATH = TRUNK
			+ "app/tzuyuEclipsePlugin/src/tzuyu/plugin/commons/constants/Messages.java";
	

	public static void main(String[] args) {
		try {
			generateMessagesClass();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void generateMessagesClass() throws IOException {
		System.out.println("Start generating Messages.java from messages.properties");
		Properties props = loadProperties();
		String propsContent = buildPropsContent(props);
		File messageCl = new File(GENERATED_MESSAGES_CLASS_PATH);
		StringBuilder content = new StringBuilder();
		boolean inGeneratedZone = false;
		for (Object ln : FileUtils.readLines(messageCl)) {
			String line = (String) ln;
			if (!inGeneratedZone || line.contains(END_GENERATED_PART_TOKEN)) {
				content.append(line).append("\n");
			}
			if (line.contains(START_GENERATED_PART_TOKEN)) {
				inGeneratedZone = true;
				content.append(propsContent);
			} else if (line.contains(END_GENERATED_PART_TOKEN)) {
				inGeneratedZone = false;
			} 
		}
		// update class
		FileUtils.writeStringToFile(messageCl, content.toString());
		System.err.println("Success!!");
		System.err.println("\nWarning: Refresh tzuyu.plugin.commons.constants.Messages.java!");
	}

	private static String buildPropsContent(Properties props) {
		StringBuilder content = new StringBuilder();
		for (Object k : props.keySet()) {
			String key = (String) k;
			String value = props.getProperty(key);
			int argNo = -1;
			for (int i = 0; i < 10; i++) {
				if (value.indexOf("{" + i + "}") > -1) {
					argNo = i;
				}
			}
			
			content.append("\n")
					.append("\tpublic String ")
					.append(key.replace(".", "_"))
					.append("(");
			for (int i = 0; i <= argNo; i++) {
				content.append("Object arg" + i);
				if (i < argNo) {
					content.append(", ");
				}
			}
			content.append(") {")
					.append("\n\t\treturn getMessage(\"")
					.append(key).append("\"");
			for (int i = 0; i <= argNo; i++) {
				content.append(", ")
						.append("arg" + i);
			}
			content.append(");")
					.append("\n\t}\n");
					
		}
		return content.append("\n").toString();
	}

	private static Properties loadProperties() {
		Properties prop = new Properties();
		File file = new File(MESSAGES_PROPERTIES_PATH);
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(file));
			prop.load(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// do nothing
				}
			}
		}
		return prop;
	}

}
