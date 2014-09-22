/*
 * Copyright (C) 2013 by SUTD (Singapore)
 * All rights reserved.
 *
 * 	Author: SUTD
 *  Version:  $Revision: 1 $
 */

package icsetlv.vm;

import icsetlv.common.Constants;
import icsetlv.common.exception.IcsetlvException;
import icsetlv.common.utils.CollectionBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import sav.common.core.utils.StringUtils;
import sun.misc.VM;

/**
 * @author LLT
 * 
 */
public class VMRunner {
	protected static final String cpToken = "-cp";
	/*
	 * from jdk 1.5, we can use new JVM option: -agentlib 
	 * Benefits of using the new -agentlib args is, it doesn't contain any whitespace, so
	 * you don't need to worry if you need to quote it or not. But if you do
	 * want to use the old flags, be careful about when to quote the value and
	 * when to not quote.
	 */
	protected static final String debugToken = "-agentlib:jdwp=transport=dt_socket,suspend=y,address=%s";
	protected static final String enableAssertionToken = "-ea";
	
	public static Process startJVM(VMConfiguration config) throws IcsetlvException {
		VMRunner vmRunner = new VMRunner();
		return vmRunner.start(config);
	}
	
	public Process start(VMConfiguration config) throws IcsetlvException {
		if (config.getPort() == -1) {
			throw new IcsetlvException("Cannot find free port to start jvm!");
		}
		
		CollectionBuilder<String, Collection<String>> builder = CollectionBuilder
						.init(new ArrayList<String>())
						.add(buildJavaExecArg(config));
		buildVmOption(builder, config);
//		List<String> commands = 
		buildProgramArgs(config, builder);
		return startVm((List<String>)builder.getResult());
	}

	protected void buildProgramArgs(VMConfiguration config,
			CollectionBuilder<String, Collection<String>> builder) {
		builder.add(cpToken)
				.add(toClasspathStr(config.getClasspaths()))
				.add(config.getLaunchClass());
//		List<String> commands = builder.getResult();
		for (String arg : config.getProgramArgs()) {
//			commands.add(arg);
			builder.add(arg);
		}
//		return commands;
//		return builder;
	}
	
	protected void buildVmOption(CollectionBuilder<String, ?> builder, VMConfiguration config) {
		builder.addIf(String.format(debugToken, config.getPort()), config.isDebug())
				.addIf(enableAssertionToken, config.isEnableAssertion());
	}

	protected Process startVm(List<String> commands)
			throws IcsetlvException {
		ProcessBuilder processBuilder = new ProcessBuilder(commands);
		processBuilder.redirectErrorStream(true);
		Process process = null;
		try {
			process = processBuilder.start();
		} catch (IOException e) {
			IcsetlvException.rethrow(e, "cannot start jvm process");
		}
		return process;
	}
	
	private String toClasspathStr(List<String> classpaths) {
		return StringUtils.join(classpaths, File.pathSeparator);
	}

	private String buildJavaExecArg(VMConfiguration config) {
		return StringUtils.join(Constants.FILE_SEPARATOR, config.getJavaHome(), "bin", "java");
	}
}
