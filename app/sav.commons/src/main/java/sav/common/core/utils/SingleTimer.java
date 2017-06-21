/*
 * Copyright (C) 2013 by SUTD (Singapore)
 * All rights reserved.
 *
 * 	Author: SUTD
 *  Version:  $Revision: 1 $
 */

package sav.common.core.utils;

/**
 * @author LLT
 *
 */
public class SingleTimer {
	private long start;
	private String taskName;

	public SingleTimer(String taskName, long start) {
		this.taskName = taskName;
		this.start = start;
	}

	public static SingleTimer start(String taskName) {
		SingleTimer timer = new SingleTimer(taskName, currentTime());
		return timer;
	}

	private static long currentTime() {
		return System.currentTimeMillis();
	}

	public long getExecutionTime() {
		return currentTime() - start;
	}

	public void logResults(org.slf4j.Logger log) {
		if (!log.isDebugEnabled()) {
			return;
		}
		log.debug(taskName + ": " + TextFormatUtils.printTimeString(getExecutionTime()));
	}
	
	public void logResults(org.slf4j.Logger log, long maxRt) {
		if (getExecutionTime() > maxRt) {
			logResults(log);
		}
	}
}
