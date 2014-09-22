/*
 * Copyright (C) 2013 by SUTD (Singapore)
 * All rights reserved.
 *
 * 	Author: SUTD
 *  Version:  $Revision: 1 $
 */

package javaslicer;

import java.util.Collection;

import icsetlv.common.exception.IcsetlvException;
import icsetlv.common.utils.CollectionBuilder;
import icsetlv.vm.VMConfiguration;
import icsetlv.vm.VMRunner;

/**
 * @author LLT
 *
 */
public class JavaSlicerVmRunner extends VMRunner {
	private static final String javaAgent = "-javaagent:";
	private String tracerJarPath;
	private String traceFilePath;
	
	public JavaSlicerVmRunner(String tracerJarPath) {
		this.tracerJarPath = tracerJarPath;
	}
	
	public Process start(VMConfiguration config, String traceFilePath)
			throws IcsetlvException {
		this.traceFilePath = traceFilePath;
		return super.start(config);
	}

	@Override
	protected void buildVmOption(CollectionBuilder<String, ?> builder,
			VMConfiguration config) {
		builder.add(String.format("-javaagent:%s=tracefile:%s", tracerJarPath,
				traceFilePath));
	}
	
	@Override
	protected void buildProgramArgs(VMConfiguration config,
			CollectionBuilder<String, Collection<String>> builder) {
		super.buildProgramArgs(config, builder);
		builder.add("-s small pmd");
	}
	
	public void setTracerJarPath(String tracerJarPath) {
		this.tracerJarPath = tracerJarPath;
	}

}
