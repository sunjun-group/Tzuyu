/*
 * Copyright (C) 2013 by SUTD (Singapore)
 * All rights reserved.
 *
 * 	Author: SUTD
 *  Version:  $Revision: 1 $
 */

package icsetlv;

/**
 * @author LLT
 *
 */
public class TestConfiguration {
	private static TestConfiguration config;
	private static final String junitCore = "org.junit.runner.JUnitCore";
//	private static final String TRUNK = "F:\\project\\Tzuyu\\";
//	private String javaHome = "C:\\Program Files\\Java\\jdk1.7.0_45";
//	private static final String TRUNK = "/home/lylytran/projects/Tzuyu/workspace/trunk";
//	private String javaHome = "/home/lylytran/projects/Tzuyu/tools/java-7-openjdk-amd64";
	public static final String TRUNK = "D:/_1_Projects/Tzuyu/workspace/trunk";
	private String javaHome = "D:/_1_Projects/Tzuyu/tools/jdk1.6.0_26-64b";
	private String sourcePath = TRUNK + "/app/icsetlv/src/test/java";
	private String binPath = TRUNK + "/app/icsetlv/target/test-classes";
	private String junitLib = TRUNK + "/app/icsetlv/src/test/lib/*";
	
	private TestConfiguration() {
		
	}

	public static TestConfiguration getInstance() {
		if (config == null) {
			config = new TestConfiguration();
		}
		return config;
	}

	public String getSourcepath() {
		return sourcePath;
	}

	public String getJavahome() {
		return javaHome;
	}
	
	public String getJreFolder() {
		return getJavahome() + "/jre";
	}

	public String getJunitcore() {
		return junitCore;
	}

	public String getAppBinpath() {
		return binPath;
	}
	
	public String getJavaBin() {
		return getJavahome() + "/bin";
	}
	
	public String getJunitLib() {
		return junitLib;
	}
}
