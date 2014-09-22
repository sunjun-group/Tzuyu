/*
 * Copyright (C) 2013 by SUTD (Singapore)
 * All rights reserved.
 *
 * 	Author: SUTD
 *  Version:  $Revision: 1 $
 */

package faultLocaliation;

import faultLocaliation.sample.SampleProgramTest;
import faultLocaliation.sample.SamplePrograms;
import faultLocalization.dto.CoverageReport;
import faultLocalization.dto.LineCoverageInfo;
import icsetlv.AbstractTest;
import icsetlv.BugExpert;
import icsetlv.IcsetlvInput;
import icsetlv.common.dto.BreakPoint;
import icsetlv.common.dto.TcExecResult;
import icsetlv.common.exception.IcsetlvException;
import icsetlv.svm.LibSVM;
import icsetlv.variable.TestcasesExecutor;
import icsetlv.vm.VMConfiguration;

import java.util.ArrayList;
import java.util.List;

import javacocoWrapper.JavaCoCo;
import javaslicer.JavaSlicer;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.featureselection.ranking.RecursiveFeatureEliminationSVM;

import org.junit.Test;

import sav.common.core.utils.CollectionUtils;

/**
 * @author LLT
 * 
 */
public class ProgramTest extends AbstractTest {
	
	public ProgramTest() {
		module = FALTLOCALISATION;
	}

	@Test
	public void testLineCounter() throws Exception {
		String testingClassName1 = SamplePrograms.class.getName();

		ArrayList<String> testingClassNames = new ArrayList<String>();
		testingClassNames.add(testingClassName1);

		JavaCoCo javacoco = new JavaCoCo();
		CoverageReport result = javacoco.run(testingClassNames,
				SampleProgramTest.class);

		result.Tarantula();
	}
	
	@Test
	public void testIntegrateSlicing() throws Exception {
		List<BreakPoint> bkps = new ArrayList<BreakPoint>();
		String clazz = SampleProgramTest.class.getName();
//		bkps.add(new BreakPoint(clazz, "test1()V", 17));
		bkps.add(new BreakPoint(clazz, "test2()V", 26));
//		bkps.add(new BreakPoint(clazz, "test3()V", 35));
//		bkps.add(new BreakPoint(clazz, "test4()V", 44));
//		bkps.add(new BreakPoint(clazz, "test5()V", 53));
		JavaSlicer jSlicer = new JavaSlicer(config.tracerLibPath);
		VMConfiguration vmConfig = initVmConfig();
		vmConfig.addClasspath(config.getTarget(FALTLOCALISATION));
//		vmConfig.addClasspath(config.javaSlicerPath);
		jSlicer.setVmConfig(vmConfig);
		List<BreakPoint> result = jSlicer.run(bkps,
				CollectionUtils.listOf(SampleProgramTest.class.getName()));
		for (BreakPoint bkp : result) {
			if (CollectionUtils.existIn(bkp.getClassCanonicalName(), SamplePrograms.class.getName(),
					SampleProgramTest.class.getName())) {
				System.out.println(bkp.getId());
			}
		}
	}
	
	@Test
	public void testIntegrateSvm() throws Exception {
		String testingClassName1 = SamplePrograms.class.getName();
		ArrayList<String> testingClassNames = new ArrayList<String>();
		testingClassNames.add(testingClassName1);
		
		JavaCoCo javacoco = new JavaCoCo();
		CoverageReport result = javacoco.run(testingClassNames,
				SampleProgramTest.class);
		List<LineCoverageInfo> lineCoverageInfo = result.Tarantula();
		if (lineCoverageInfo.isEmpty()) {
			return;
		}
		float maxSpi = lineCoverageInfo.get(0).getSuspiciousness();
		List<LineCoverageInfo> maxSpiList = new ArrayList<LineCoverageInfo>();
		for (LineCoverageInfo info : lineCoverageInfo) {
			if (info.getSuspiciousness() == maxSpi) {
				maxSpiList.add(info);
			} else {
				break;
			}
		}
		List<BreakPoint> bkps = toBreakpoints(maxSpiList);
		IcsetlvInput input = initInput();
		runAnalysis(input, bkps);
	}
	
	private IcsetlvInput initInput() {
		IcsetlvInput input = new IcsetlvInput();
		VMConfiguration vmConfig = initVmConfig();
		input.setConfig(vmConfig);
		input.setPassTestcases(CollectionUtils.listOf("faultLocaliation.sample.SampleProgramTestPass"));
		input.setFailTestcases(CollectionUtils.listOf("faultLocaliation.sample.SampleProgramTestFail"));
		input.getConfig().addClasspath(config.getTestTarget(FALTLOCALISATION));
		return input;
	}
	
	private void runAnalysis(IcsetlvInput input, List<BreakPoint> bkps)
			throws IcsetlvException {
		TestcasesExecutor extractor = new TestcasesExecutor(input.getConfig(), 4);
		TcExecResult result = extractor.execute(input.getPassTestcases(), input.getFailTestcases(),
				bkps);
		System.out.println(result.toString(bkps));
		LibSVM svmrunner = new LibSVM();
		for (BreakPoint bkp : bkps) {
			System.out.println("line: " + bkp.getLineNo());
			Dataset tmpDS = buildDataSet(result, bkp);
			svmrunner.buildClassifier(tmpDS);
			System.out.println(svmrunner.getExplicitDivider().toString());
			System.out.println(svmrunner.modelAccuracy());
			/* Create a feature ranking algorithm */
			RecursiveFeatureEliminationSVM svmrfe = new RecursiveFeatureEliminationSVM(0.2);
			/* Apply the algorithm to the data set */
			svmrfe.build(tmpDS);
			/* Print out the rank of each attribute */
			for (int i = 0; i < svmrfe.noAttributes(); i++)
			    System.out.println(svmrfe.rank(i));
			System.out.println("--------------------------------------------");
		}
	}

	private Dataset buildDataSet(TcExecResult result, BreakPoint bkp) {
		Dataset data = BugExpert.buildDataset(result.getPassValues(bkp),
				result.getFailValues(bkp));
		return data;
	}

	private List<BreakPoint> toBreakpoints(List<LineCoverageInfo> maxSpiList) {
		List<BreakPoint> bkps = new ArrayList<BreakPoint>(maxSpiList.size());
//		for (LineCoverageInfo info : maxSpiList) {
//			String clazz = info.getClassName().replace("/", ".");
//			bkps.add(new BreakPoint(clazz, info.getLineIndex()));
//		}
		
		String clazz = "faultLocaliation.sample.SamplePrograms";
		bkps.add(new BreakPoint(clazz, 16));
		bkps.add(new BreakPoint(clazz, 20));
//		bkps.add(new BreakPoint(clazz, 13, new Variable("result"), new Variable("b")));
//		bkps.add(new BreakPoint(clazz, 18, new Variable("result"), new Variable("b")));
		return bkps;
	}
}
