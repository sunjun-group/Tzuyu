/*
 * Copyright (C) 2013 by SUTD (Singapore)
 * All rights reserved.
 *
 * 	Author: SUTD
 *  Version:  $Revision: 1 $
 */

package icsetlv;

import java.util.ResourceBundle;

import sav.common.core.utils.StringUtils;

/**
 * @author LLT
 *
 */
public class TestConfiguration {
	private static TestConfiguration config;
	private static final String junitCore = "org.junit.runner.JUnitCore";
	private String javaHome = System.getProperty("java.home");
	private String TRUNK;
	private String junitLib;
	public String tracerLibPath;
	public String javaSlicerPath;
	
	private TestConfiguration() {
		ResourceBundle res = ResourceBundle.getBundle("test_configuration");
		TRUNK = res.getString("trunk");
		junitLib = TRUNK + "/app/icsetlv/src/test/lib/*";
		tracerLibPath = TRUNK + "/etc/javaslicer/assembly/new/tracer.jar";
//		tracerLibPath = TRUNK + "/etc/javaslicer/assembly/new/libs/javaslicer-tracer-1.1.1-SNAPSHOT.jar";
//		javaSlicerPath = TRUNK + "/etc/javaslicer/assembly/new/libs/*";
	}
	
	public String getSourcepath(String module) {
		return StringUtils.join("", TRUNK, "/app/", module, "/src/test/java");
	}
	
	public String getTestTarget(String module) {
		return StringUtils.join("", TRUNK, "/app/", module, "/target/test-classes");
	}
	
	public String getTarget(String module) {
		return StringUtils.join("", TRUNK, "/app/", module, "/target/classes");
	}

	public static TestConfiguration getInstance() {
		if (config == null) {
			config = new TestConfiguration();
		}
		return config;
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

	public String getJavaBin() {
		return getJavahome() + "/bin";
	}
	
	public String getJunitLib() {
		return junitLib;
	}

	public static String getTrunk() {
		return getInstance().TRUNK;
	}
}
