/*
 * Copyright (C) 2013 by SUTD (Singapore)
 * All rights reserved.
 *
 * 	Author: SUTD
 *  Version:  $Revision: 1 $
 */

package javaslicer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import faultLocaliation.sample.SampleProgramTest;

/**
 * @author LLT
 *
 */
public class TestcasesRunner {
	
	/**
	 * args: essential data to execute the testcases.
	 * arg[0]: all simpleNames of test classes.
	 */
	public static void main(String[] args) {
//		new SampleProgramTest().test1();
		List<Class<?>> testClasses = extractTestClasses(args[0]);
		/* for each test method found in test class, 
		 * execute it, and ignore exeption.
		 */
		for (Class<?> testClass : testClasses) {
			List<Method> testMethods = new ArrayList<Method>();
			for (Method method : testClass.getMethods()) {
				if (method.getAnnotation(Test.class) != null) {
					testMethods.add(method);
				}
			}
			// run test method
			if (!testMethods.isEmpty()) {
				try {
					Object instance = testClass.getConstructor().newInstance();
					for (Method method : testMethods) {
						method.invoke(instance);
					}
				} catch (Exception e) {
					// ignore all classes cannot run as expected
				}
			}
		}
		
	}

	private static List<Class<?>> extractTestClasses(String tests) {
		String[] clazzNames = tests.split(";");
		List<Class<?>> result = new ArrayList<Class<?>>(clazzNames.length);
		for (String clazzName : clazzNames) {
			try {
				result.add(Class.forName(clazzName));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static String toArgs(List<String> tests) {
//		StringBuilder sb = new StringBuilder();
//		sb.append(b)
		String result = null;
		for (String test : tests) {
			if (result == null) {
				result = test;
			} else {
				result = result + ";" + test;
			}
		}
		return result;
	}
}