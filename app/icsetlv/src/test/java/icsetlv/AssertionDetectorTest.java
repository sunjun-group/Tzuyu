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
import icsetlv.variable.AssertionDetector;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author LLT
 * 
 */
@RunWith(Parameterized.class)
public class AssertionDetectorTest extends AbstractTest {
	@Parameter
	public Map<String, List<String>> assertionsClazzes;

	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] { { singleEleMap(TestConfiguration
				.getInstance().getSourcepath()
				+ "testdata/boundedStack/BoundedStack.java") } });
	}

	private static <T, V>Map<T, V> singleEleMap(T key) {
		Map<T, V> result = new HashMap<T, V>();
		result.put(key, null);
		return result;
	}

	@Test
	public void testScan() throws IcsetlvException {
		List<BreakPoint> breakpoints = AssertionDetector.scan(assertionsClazzes);
		printBkps(breakpoints);
		Assert.assertEquals(breakpoints.size(), 2);
	}
}
