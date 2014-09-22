/*
 * Copyright (C) 2013 by SUTD (Singapore)
 * All rights reserved.
 *
 * 	Author: SUTD
 *  Version:  $Revision: 1 $
 */

package icsetlv;

import icsetlv.common.dto.BreakPoint;
import icsetlv.slicer.SlicerInput;
import icsetlv.vm.VMConfiguration;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;

import sav.common.core.utils.StringUtils;

/**
 * @author LLT
 * 
 */
public class AbstractTest {
	protected static TestConfiguration config = TestConfiguration.getInstance();
	
	public void print(Object... objs) {
		System.out.println(StringUtils.spaceJoin(objs));
	}

	public void printBkps(List<BreakPoint> breakpoints) {
		for (BreakPoint bkp : breakpoints) {
			printBreakpoint(bkp);
		}
	}

	public void printBreakpoint(BreakPoint bkp) {
		System.out.println(bkp);
	}

	protected VMConfiguration initVmConfig() {
		VMConfiguration vmConfig = new VMConfiguration();
		vmConfig.setJavaHome(config.getJavahome());
		vmConfig.setDebug(true);
		vmConfig.setPort(findFreePort());
		vmConfig.setLaunchClass(config.getJunitcore());
		vmConfig.addClasspath(config.getJavaBin());
		vmConfig.addClasspath(config.getAppBinpath());
		vmConfig.addClasspath(config.getJunitLib());
		return vmConfig;
	}
	
	public static int findFreePort() {
		ServerSocket socket= null;
		try {
			socket= new ServerSocket(0);
			return socket.getLocalPort();
		} catch (IOException e) { 
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
		return -1;		
	}	
	
	protected SlicerInput initSlicerInput() {
		SlicerInput input = new SlicerInput();
		input.setAppBinFolder(config.getAppBinpath());
		input.setJre(config.getJavahome());
		return input;
	}
}
