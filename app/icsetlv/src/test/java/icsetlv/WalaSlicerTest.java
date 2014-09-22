/*
 * Copyright (C) 2013 by SUTD (Singapore)
 * All rights reserved.
 *
 * 	Author: SUTD
 *  Version:  $Revision: 1 $
 */

package icsetlv;

import icsetlv.common.dto.BreakPoint;
import icsetlv.common.exception.IcsetlvException;
import icsetlv.slicer.SlicerInput;
import icsetlv.slicer.WalaSlicer;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * @author LLT
 *
 */
public class WalaSlicerTest extends AbstractTest {
	private TestData type = TestData.FIND_MAX;
	
	@Test
	public void testSlice() throws IcsetlvException {
		SlicerInput input = new SlicerInput();
		input.setAppBinFolder(config.getTestTarget(ICSETLV));
		input.setJre(config.getJavahome());
		// entry points
		List<String[]> classEntryPoints = makeEntryPoints();
		input.setClassEntryPoints(classEntryPoints);
		// breakpoints
		List<BreakPoint> breakpoints = new ArrayList<BreakPoint>();
		addBreakpoints(breakpoints);
		WalaSlicer slicer = new WalaSlicer(input);
		List<BreakPoint> slicingResult = slicer.slice(breakpoints);
		printBkps(slicingResult);
	}

	private void addBreakpoints(List<BreakPoint> breakpoints) {
		switch (type) {
		case BOUNDED_STACK:
			breakpoints.add(new BreakPoint("testdata.boundedStack.BoundedStack", "push(Ljava/lang/Integer;)Z", 37));
//			breakpoints.add(new BreakPoint("testdata.boundedStack.BoundedStack", "pop()Ljava/lang/Integer;", 43));
//			breakpoints.add(new BreakPoint("testdata.SamplePrograms", "Max(Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Integer;)Ljava/lang/Integer;", 21));
			break;
		case TEST_DEVIDER:
			breakpoints.add(new BreakPoint("testdata.testDevider.TestDevider", "stop", 9));
			breakpoints.add(new BreakPoint("testdata.testDevider.TestDevider", "testLong", 23));
			breakpoints.add(new BreakPoint("testdata.testDevider.TestDevider", "testInt", 31));
			breakpoints.add(new BreakPoint("testdata.testDevider.TestDevider", "testNegInt", 39));
			breakpoints.add(new BreakPoint("testdata.testDevider.TestDevider", "test2Params", 47));
			break;
		default:
			breakpoints.add(new BreakPoint("testdata.slice.FindMax", "findMax", 11));
		}
	}

	private List<String[]> makeEntryPoints() {
		List<String[]> classEntryPoints = new ArrayList<String[]>();
		switch (type) {
		case BOUNDED_STACK:
			classEntryPoints.add(make("Ltestdata/boundedStack/BoundedStack", "push(Ljava/lang/Integer;)Z"));
//			classEntryPoints
//					.add(make(
//							"Ltestdata/SamplePrograms",
//							"Max(Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Integer;)Ljava/lang/Integer;"));
//			classEntryPoints.add(make("Ltestdata/boundedStack/tzuyu/fail/BoundedStack7", "test1()V"));
//			classEntryPoints.add(make("Ltestdata/boundedStack/tzuyu/fail/BoundedStack7", "test2()V"));
//			classEntryPoints.add(make("Ltestdata/boundedStack/tzuyu/fail/BoundedStack7", "test3()V"));
//			classEntryPoints.add(make("Ltestdata/boundedStack/tzuyu/fail/BoundedStack7", "test4()V"));
//			classEntryPoints.add(make("Ltestdata/boundedStack/tzuyu/fail/BoundedStack7", "test5()V"));
//			classEntryPoints.add(make("Ltestdata/boundedStack/tzuyu/fail/BoundedStack7", "test6()V"));
//			classEntryPoints.add(make("Ltestdata/boundedStack/tzuyu/fail/BoundedStack7", "test7()V"));
			break;
		case TEST_DEVIDER:
			classEntryPoints.add(make("Ltestdata/testDevider/tzuyu/fail/TestDevider3", "test1"));
			 classEntryPoints.add(make("Ltestdata/testDevider/tzuyu/fail/TestDevider3", "test2"));
			 classEntryPoints.add(make("Ltestdata/testDevider/tzuyu/fail/TestDevider3", "test3"));
			 classEntryPoints.add(make("Ltestdata/testDevider/tzuyu/fail/TestDevider3", "test4"));
			 classEntryPoints.add(make("Ltestdata/testDevider/tzuyu/fail/TestDevider3", "test5"));
			 classEntryPoints.add(make("Ltestdata/testDevider/tzuyu/fail/TestDevider3", "test6"));
			 classEntryPoints.add(make("Ltestdata/testDevider/tzuyu/fail/TestDevider3", "test7"));
			 classEntryPoints.add(make("Ltestdata/testDevider/tzuyu/fail/TestDevider3", "test8"));
			 classEntryPoints.add(make("Ltestdata/testDevider/tzuyu/fail/TestDevider3", "test9"));
			 classEntryPoints.add(make("Ltestdata/testDevider/tzuyu/fail/TestDevider3", "test10"));
			 classEntryPoints.add(make("Ltestdata/testDevider/tzuyu/fail/TestDevider4", "test1"));
			 classEntryPoints.add(make("Ltestdata/testDevider/tzuyu/fail/TestDevider4", "test2"));
			 classEntryPoints.add(make("Ltestdata/testDevider/tzuyu/fail/TestDevider4", "test3"));
			 classEntryPoints.add(make("Ltestdata/testDevider/tzuyu/fail/TestDevider4", "test4"));
			 classEntryPoints.add(make("Ltestdata/testDevider/tzuyu/fail/TestDevider4", "test5"));
			 classEntryPoints.add(make("Ltestdata/testDevider/tzuyu/fail/TestDevider4", "test6"));
			 classEntryPoints.add(make("Ltestdata/testDevider/tzuyu/fail/TestDevider4", "test7"));
			 classEntryPoints.add(make("Ltestdata/testDevider/tzuyu/fail/TestDevider4", "test8"));
			 classEntryPoints.add(make("Ltestdata/testDevider/tzuyu/fail/TestDevider4", "test9"));
			 classEntryPoints.add(make("Ltestdata/testDevider/tzuyu/fail/TestDevider4", "test10"));
			 classEntryPoints.add(make("Ltestdata/testDevider/tzuyu/fail/TestDevider5", "test1"));
			 classEntryPoints.add(make("Ltestdata/testDevider/tzuyu/fail/TestDevider5", "test2"));
			 classEntryPoints.add(make("Ltestdata/testDevider/tzuyu/fail/TestDevider5", "test3"));
			 classEntryPoints.add(make("Ltestdata/testDevider/tzuyu/fail/TestDevider5", "test4"));
			 classEntryPoints.add(make("Ltestdata/testDevider/tzuyu/fail/TestDevider5", "test5"));
			 classEntryPoints.add(make("Ltestdata/testDevider/tzuyu/fail/TestDevider5", "test6"));
			 classEntryPoints.add(make("Ltestdata/testDevider/tzuyu/fail/TestDevider5", "test7"));
			 classEntryPoints.add(make("Ltestdata/testDevider/tzuyu/fail/TestDevider5", "test8"));
			 classEntryPoints.add(make("Ltestdata/testDevider/tzuyu/fail/TestDevider5", "test9"));
			break;
		default:
			classEntryPoints.add(make("Ltestdata/slice/FindMaxCallerFailTest1", "test2"));
			classEntryPoints.add(make("Ltestdata/slice/FindMaxCallerPassTest1", "test1"));
		}
		return classEntryPoints;
	}
	
	private String[] make(String clazz, String method) {
		return new String[]{clazz, method};
	}

	private enum TestData{
		BOUNDED_STACK,
		TEST_DEVIDER,
		FIND_MAX
	}
}
