package learntest.activelearning.core.handler;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gentest.core.data.Sequence;
import learntest.activelearning.core.coverage.CoverageAgentRunner;
import learntest.activelearning.core.settings.LearntestSettings;
import learntest.core.commons.TimeController;
import learntest.core.commons.data.classinfo.MethodInfo;
import microbat.instrumentation.cfgcoverage.CoverageAgentParams;
import microbat.instrumentation.cfgcoverage.CoverageOutput;
import microbat.instrumentation.cfgcoverage.InstrumentationUtils;
import microbat.instrumentation.cfgcoverage.CoverageAgentParams.CoverageCollectionType;
import sav.common.core.SavException;
import sav.common.core.SavRtException;
import sav.common.core.utils.SingleTimer;
import sav.strategies.dto.AppJavaClassPath;

/**
 * @author LLT
 *
 */
public class CoverageCounter {
	private String agentJarPath;
	private String savJunitRunnerJarPath;
	private int cdgLayer;
	private Logger log = LoggerFactory.getLogger(CoverageCounter.class);
	private long methodExecTimeout;
	private boolean collectConditionVariation;
	private TimeController timeController = TimeController.getInstance();
	private boolean runCoverageAsMethodInvoke = false;
	private boolean usingSocket = false;
	private CoverageAgentRunner coverageAgent;

	public CoverageCounter(LearntestSettings settings, boolean collectConditionVariation, AppJavaClassPath appClasspath) {
		this.agentJarPath = settings.getResources().getMicrobatInstrumentationJarPath();
		this.cdgLayer = settings.getCfgExtensionLayer();
		this.methodExecTimeout = settings.getMethodExecTimeout() + 60l; // allow to run a little longer due to coverage collection process.
		this.savJunitRunnerJarPath = settings.getResources().getSavJunitRunnerJarPath();
		this.collectConditionVariation = collectConditionVariation;
		this.runCoverageAsMethodInvoke = settings.isRunCoverageAsMethodInvoke();
		usingSocket = settings.isCoverageRunSocket();
		coverageAgent = new CoverageAgentRunner(agentJarPath, savJunitRunnerJarPath, appClasspath,
				runCoverageAsMethodInvoke);
	}

	public CoverageOutput runCoverage(MethodInfo targetMethod, List<String> junitMethods, AppJavaClassPath appClasspath,
			int inputValueExtractLevel, CoverageCollectionType cvgType, List<Sequence> sequences) throws SavException, SavRtException {
		log.debug("calculate coverage..");
		SingleTimer timer = SingleTimer.start("cfg-coverage");
		/* build agent params */
		CoverageAgentParams agentParams = new CoverageAgentParams();
		agentParams.setCdgLayer(cdgLayer);
		agentParams.setClassPaths(appClasspath.getClasspaths());
		agentParams.setTargetMethodLoc(
				InstrumentationUtils.getClassLocation(targetMethod.getClassName(), targetMethod.getMethodSignature()));
		agentParams.setInclusiveMethodIds(new ArrayList<String>());
		agentParams.setWorkingDirectory(appClasspath.getWorkingDirectory());
		agentParams.setVarLayer(inputValueExtractLevel);
		agentParams.setCollectConditionVariation(collectConditionVariation);
		agentParams.setCoverageType(cvgType);
		CoverageOutput coverageOutput = null;
		if (usingSocket) {
			coverageOutput = coverageAgent.runWithSocket(agentParams, methodExecTimeout, junitMethods, sequences);
		} else {
			coverageOutput = coverageAgent.run(agentParams, methodExecTimeout, junitMethods);
		}
		timer.captureExecutionTime();
		timer.logResults(log);
		timeController.logCoverageRunningTime(targetMethod, junitMethods, timer.getExecutionTime());
		return coverageOutput;
	}
	
	public void setUsingSocket(boolean usingSocket) {
		this.usingSocket = usingSocket;
	}
	
	public void stop() {
		if (usingSocket) {
			if (coverageAgent != null) {
				coverageAgent.reset();
			}
		}
	}
}
