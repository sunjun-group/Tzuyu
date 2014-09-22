/*
 * Copyright (C) 2013 by SUTD (Singapore)
 * All rights reserved.
 *
 * 	Author: SUTD
 *  Version:  $Revision: 1 $
 */

package faultLocalization.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import faultLocalization.dto.LineCoverageInfo.LineCoverageInfoComparator;

/**
 * @author khanh
 *
 */
/**
 * @author khanh
 *
 */
public class CoverageReport {
	private HashMap<String, ClassCoverageInAllTestcases> mapClassLineToTestCasesCover = new HashMap<String, ClassCoverageInAllTestcases>();
	private HashMap<Integer, TestcaseCoverageInfo> passedTestcaseCoverageInfo = new HashMap<Integer, TestcaseCoverageInfo>();
	private HashMap<Integer, TestcaseCoverageInfo> failedTestcaseCoverageInfo = new HashMap<Integer, TestcaseCoverageInfo>();
	
	private List<String> testingClassNames;
	
	public CoverageReport(List<String> testingClassNames){
		this.testingClassNames = testingClassNames;
	}
	
	public void addInfo(int testcaseIndex, String className, int lineIndex, boolean isPassed, boolean isCovered){
		//update mapClassLineToTestCasesCover
		if (isCovered) {
			ClassCoverageInAllTestcases classCoverage;
			if (mapClassLineToTestCasesCover.containsKey(className)) {
				classCoverage = mapClassLineToTestCasesCover.get(className);
			} else {
				classCoverage = new ClassCoverageInAllTestcases(className);
				mapClassLineToTestCasesCover.put(className, classCoverage);
			}

			classCoverage.addInfo(lineIndex, testcaseIndex, isPassed);
		}
		
		//update passedTestcaseCoverageInfo, failedTestcaseCoverageInfo
		HashMap<Integer, TestcaseCoverageInfo> allTestcasesCoverageInfo = (isPassed)? passedTestcaseCoverageInfo: failedTestcaseCoverageInfo;
		TestcaseCoverageInfo testcaseCoverage;
		if(allTestcasesCoverageInfo.containsKey(testcaseIndex)){
			testcaseCoverage = allTestcasesCoverageInfo.get(testcaseIndex);
		}else{
			testcaseCoverage = new TestcaseCoverageInfo(testcaseIndex);
			allTestcasesCoverageInfo.put(testcaseIndex, testcaseCoverage);
		}
		testcaseCoverage.addInfo(className, lineIndex, isCovered);
	}
	
	public List<LineCoverageInfo> Tarantula(){
		List<LineCoverageInfo> linesCoverageInfo = new ArrayList<LineCoverageInfo>();
		
		for(ClassCoverageInAllTestcases classCoverage: mapClassLineToTestCasesCover.values()){
			linesCoverageInfo.addAll(classCoverage.getLineCoverageInfo());
		}
		
		for(LineCoverageInfo lineCoverageInfo: linesCoverageInfo){
			lineCoverageInfo.computeSuspiciousness(passedTestcaseCoverageInfo.size(), failedTestcaseCoverageInfo.size());
		}
		
		Collections.sort(linesCoverageInfo, new LineCoverageInfoComparator());
		
		for(LineCoverageInfo lineCoverageInfo: linesCoverageInfo){
			System.out.println(lineCoverageInfo.toString());
		}
		return linesCoverageInfo;
	}
	
	
	/**
	 * @param failedTestcaseIndex
	 * @return return the passed testcase with the spectrum which is nearest compared with the one of the failed testcase
	 */
	public int getNearestPassedTestcase(int failedTestcaseIndex)
	{
		HashMap<Integer, Integer> mapPassedTest2Difference = new HashMap<Integer, Integer>();
		//init value
		for(Integer passedTest: passedTestcaseCoverageInfo.keySet())
		{
			mapPassedTest2Difference.put(passedTest, 0);
		}
		
		for(Entry<String, ClassCoverageInAllTestcases> entry: mapClassLineToTestCasesCover.entrySet()){
			String tempClassName = entry.getKey().replace('/', '.');
			if(this.testingClassNames.contains(tempClassName))
			{
				ClassCoverageInAllTestcases classCoverage = entry.getValue();
				classCoverage.updateDifference(mapPassedTest2Difference, failedTestcaseIndex);
			}
		}
		
		int minDifference = Integer.MAX_VALUE;
		int nearestPassedIndex = -1;
		
		for(Entry<Integer, Integer> entry : mapPassedTest2Difference.entrySet()){
			Integer difference = entry.getValue();
			
			if(difference > 0 &&  difference < minDifference){
				minDifference = entry.getKey();
				nearestPassedIndex = entry.getKey();
			}
		}
		
		System.out.println("nearest " + nearestPassedIndex);
		return nearestPassedIndex;
	}
	
	public void Test(){
		for(Integer failTest: failedTestcaseCoverageInfo.keySet()){
			getNearestPassedTestcase(failTest);
		}
	}
}
