/*
 * Copyright (C) 2013 by SUTD (Singapore)
 * All rights reserved.
 *
 * 	Author: SUTD
 *  Version:  $Revision: 1 $
 */

package javaslicer;

import icsetlv.common.dto.BreakPoint;
import icsetlv.common.exception.IcsetlvException;
import icsetlv.vm.VMConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import sav.common.core.utils.CollectionUtils;

import de.unisb.cs.st.javaslicer.common.classRepresentation.Instruction;
import de.unisb.cs.st.javaslicer.slicing.SliceInstructionsCollector;
import de.unisb.cs.st.javaslicer.slicing.Slicer;
import de.unisb.cs.st.javaslicer.slicing.SlicingCriterion;
import de.unisb.cs.st.javaslicer.traceResult.PrintUniqueUntracedMethods;
import de.unisb.cs.st.javaslicer.traceResult.ThreadId;
import de.unisb.cs.st.javaslicer.traceResult.TraceResult;

/**
 * @author LLT
 * 
 */
public class JavaSlicer {
	private JavaSlicerVmRunner vmRunner;
	private VMConfiguration vmConfig;
	
	
	public JavaSlicer(String tracerJarPath) {
		this.vmRunner = new JavaSlicerVmRunner(tracerJarPath);
	}

	/**
	 * @param vmConfig:
	 * requires: javaHome, classpaths
	 */
	public List<BreakPoint> run(List<BreakPoint> bkps, List<String> testClasses)
			throws IcsetlvException, IOException, InterruptedException {
		File tempFile = File.createTempFile(getTraceFileName(), "trace");
		String tempFileName = tempFile.getAbsolutePath();
		/* run program and create trace file */
		vmConfig.setLaunchClass(TestcasesRunner.class.getCanonicalName());
		vmConfig.addProgramArgs(TestcasesRunner.toArgs(testClasses));
		Process process = vmRunner.start(vmConfig, tempFileName);
		while(true) {
			try {
				process.exitValue();
				FileUtils.copyFile(tempFile,
								new File("/home/lylytran/projects/Tzuyu/workspace/REF-CODE/javaslicer/test.trace"));
				break;
			} catch (IllegalThreadStateException ex) {
				// means: not yet terminated
				Thread.currentThread();
				Thread.sleep(1000);
			}
		}
		
		/* do slicing */
		return slice(tempFileName, bkps);
	}
	
	public List<BreakPoint> slice(String traceFilePath, List<BreakPoint> bkps) throws InterruptedException {
		File traceFile = new File(traceFilePath);
		TraceResult trace;
		try {
			trace = TraceResult.readFrom(traceFile);
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<BreakPoint>();
		}

		List<SlicingCriterion> criteria = new ArrayList<SlicingCriterion>(bkps.size());
		for (BreakPoint bkp : bkps) {
			SlicingCriterion criterion = SlicingCriterion.parse(
					buildSlicingCriterionStr(bkp), trace.getReadClasses());
			criteria.add(criterion);
		}

		List<ThreadId> threads = trace.getThreads();
		if (threads.size() == 0) {
			//TODO
			System.err
					.println("The trace file contains no tracing information.");
			System.exit(-1);
		}

		ThreadId tracing = null;
		for (ThreadId t : threads) {
			if ("main".equals(t.getThreadName())
					&& (tracing == null || t.getJavaThreadId() < tracing
							.getJavaThreadId()))
				tracing = t;
		}

		if (tracing == null) {
			System.err.println("Couldn't find the main thread.");
			return new ArrayList<BreakPoint>();
		}

		long startTime = System.nanoTime();
		Slicer slicer = new Slicer(trace);
		
		SliceInstructionsCollector collector = new SliceInstructionsCollector();
		slicer.addSliceVisitor(collector);
		slicer.addUntracedCallVisitor(new PrintUniqueUntracedMethods());
		slicer.process(tracing, criteria, false);
		Set<Instruction> slice = collector.getDynamicSlice();
		long endTime = System.nanoTime();

		Instruction[] sliceArray = slice.toArray(new Instruction[slice.size()]);
		Arrays.sort(sliceArray);
		List<BreakPoint> result = new ArrayList<BreakPoint>();
		for (Instruction inst : sliceArray) {
			BreakPoint bkp = new BreakPoint(inst.getMethod().getReadClass().getName(), 
					inst.getMethod().getName(), inst.getLineNumber());
			CollectionUtils.addIfNotNullNotExist(result, bkp);
		}

		System.out.format((Locale) null, "Computation took %.2f seconds.%n",
				1e-9 * (endTime - startTime));
		return result;
	}

	private String buildSlicingCriterionStr(BreakPoint bkp) {
		return String.format("%s.%s:%s:*", bkp.getClassCanonicalName(),
				bkp.getMethodName(), bkp.getLineNo());
	}

	private String getTraceFileName() {
		return "test";
	}

	public void setVmConfig(VMConfiguration vmConfig) {
		this.vmConfig = vmConfig;
	}
	
}
