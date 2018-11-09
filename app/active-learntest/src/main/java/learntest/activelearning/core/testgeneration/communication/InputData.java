package learntest.activelearning.core.testgeneration.communication;

import java.io.PrintWriter;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import learntest.activelearning.core.data.DpAttribute;
import learntest.activelearning.core.data.MethodInfo;
import learntest.activelearning.core.data.TestInputData;
import microbat.instrumentation.cfgcoverage.graph.Branch;
import sav.strategies.dto.execute.value.ExecValue;
import sav.strategies.dto.execute.value.ExecVar;
import sav.strategies.dto.execute.value.ExecVarType;

/**
 * @author LLT
 *
 */
public class InputData /* implements IInputData */ {
	private Logger log = LoggerFactory.getLogger(InputData.class);
	private RequestType requestType;
	private JSONObject obj = new JSONObject();
	private String logFilePath = "E://linyun//log.txt";

	// @Override
	public void writeData(PrintWriter pw) {
		if (obj == null) {
			return;
		}
		log.debug("write data: {}, {}", requestType, obj);
		pw.println(String.valueOf(requestType));
		pw.println(obj);

//		File file = new File(logFilePath);
//		try {
//			FileWriter fw = new FileWriter(file, true);
//			fw.write(String.valueOf(requestType));
//			fw.write("\r\n");
//			fw.write(String.valueOf(obj));
//			fw.write("\r\n");
//			fw.write("\r\n");
//
//			fw.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		// pw.flush();
	}

	public static InputData createStartMethodRequest(String methodId) {
		InputData inputData = new InputData();
		inputData.requestType = RequestType.$TRAINING;
		inputData.obj.put(JSLabels.METHOD_ID, methodId);
		return inputData;
	}

	public static InputData createTrainingRequest(MethodInfo targetMethod, Branch branch,
			List<TestInputData> positiveData, List<TestInputData> negativeData, int pointNumberLimit) {
		InputData inputData = new InputData();
		inputData.requestType = RequestType.$TRAINING;

		inputData.obj.put(JSLabels.METHOD_ID, targetMethod.getMethodId());
		inputData.obj.put(JSLabels.BRANCH_ID, branch.getBranchID());
		inputData.obj.put(JSLabels.POINT_NUMBER_LIMIT, pointNumberLimit);

		JSONArray positiveArray = transferToJsonArray(positiveData);
		inputData.obj.put(JSLabels.POSITIVE_DATA, positiveArray);

		JSONArray negativeArray = transferToJsonArray(negativeData);
		inputData.obj.put(JSLabels.NEGATIVE_DATA, negativeArray);

		return inputData;
	}

	public static InputData createBoundaryExplorationRequest(String methodID, Branch branch,
			List<TestInputData> testData) {
		InputData inputData = new InputData();
		inputData.requestType = RequestType.$BOUNDARY_EXPLORATION;
		inputData.obj.put(JSLabels.METHOD_ID, methodID);

		String branchID = branch == null ? "EMPTY" : branch.getBranchID();
		inputData.obj.put(JSLabels.BRANCH_ID, branchID);

		JSONArray jArray = transferToJsonArray(testData);
		inputData.obj.put(JSLabels.TEST_DATA, jArray);

		return inputData;
	}

	public static InputData createModelCheckRequest(Branch parentBranch, String methodID) {
		InputData inputData = new InputData();
		inputData.requestType = RequestType.$MODEL_CHECK;
		inputData.obj.put(JSLabels.BRANCH_ID, parentBranch.getBranchID());
		inputData.obj.put(JSLabels.METHOD_ID, methodID);

		return inputData;
	}

	public static InputData transferToLabelJSON(MethodInfo targetMethod, DataPoints points) {
		InputData inputData = new InputData();
		inputData.requestType = RequestType.$SEND_LABEL;
		JSONArray array = new JSONArray();
		// inputData.obj.put(JsLabels.METHOD_ID,
		// targetMethod.getMethodFullName());

		for (int i = 0; i < points.values.size(); i++) {
			JSONArray point = new JSONArray();
			DpAttribute[] attributes = points.attributes.get(i);
			for (int j = 0; j < points.varList.size(); j++) {
				ExecVar var = points.getVarList().get(j);
				JSONObject jsonObj = new JSONObject();
				jsonObj.put(JSLabels.NAME, var.getVarId());
				jsonObj.put(JSLabels.VALUE, points.values.get(i)[j]);
				jsonObj.put(JSLabels.TYPE, points.varList.get(j).getType());
				boolean label = points.getLabels().get(i);
				jsonObj.put(JSLabels.LABEL, label);

				DpAttribute attribute = attributes[j];
				jsonObj.put(JSLabels.IS_PADDING, attribute.isPadding());
				jsonObj.put(JSLabels.MODIFIABLE, attribute.isModifiable());
//				jsonObj.put(JSLabels.INFLUENTIAL_START, -1);
//				jsonObj.put(JSLabels.INFLUENTIAL_END, -1);

				point.put(jsonObj);
			}
			array.put(point);
		}

		inputData.obj.put(JSLabels.RESULT, array);

		return inputData;
	}

	public static InputData transferToMaskJSON(MethodInfo targetMethod, DataPoints points) {
		InputData inputData = new InputData();
		inputData.requestType = RequestType.$SEND_MASK_RESULT;
		JSONArray array = new JSONArray();
		// inputData.obj.put(JsLabels.METHOD_ID,
		// targetMethod.getMethodFullName());

		for (int i = 0; i < points.values.size(); i++) {
			JSONArray point = new JSONArray();
			DpAttribute[] attributes = points.attributes.get(i);
			for (int j = 0; j < points.varList.size(); j++) {
				ExecVar var = points.getVarList().get(j);
				JSONObject jsonObj = new JSONObject();
				jsonObj.put(JSLabels.NAME, var.getVarId());
				jsonObj.put(JSLabels.VALUE, points.values.get(i)[j]);
				jsonObj.put(JSLabels.TYPE, points.varList.get(j).getType());

				DpAttribute attribute = attributes[j];
				jsonObj.put(JSLabels.IS_PADDING, attribute.isPadding());
				jsonObj.put(JSLabels.MODIFIABLE, attribute.isModifiable());
//				jsonObj.put(JSLabels.INFLUENTIAL_START, -1);
//				jsonObj.put(JSLabels.INFLUENTIAL_END, -1);

				point.put(jsonObj);
			}
			array.put(point);
		}

		inputData.obj.put(JSLabels.RESULT, array);

		return inputData;
	}

	private static JSONArray transferToJsonArray(List<TestInputData> positiveData) {
		JSONArray arrayObj = new JSONArray();
		for (TestInputData testInput : positiveData) {
			DpAttribute[] attributes = testInput.getDataPoint();
			// BreakpointValue bpv = testInput.getInputValue();
			JSONArray positiveObj = new JSONArray();
			for (int i = 0; i < attributes.length; i++) {
				DpAttribute attribute = attributes[i];
				ExecValue value = attribute.getValue();
				JSONObject param = new JSONObject();

				param.put(JSLabels.TYPE, value.getType());
				param.put(JSLabels.NAME, value.getVarId());
				if (value.getType().equals(ExecVarType.REFERENCE) || value.getType().equals(ExecVarType.ARRAY)) {
					param.put(JSLabels.VALUE, 1);
				} else {
					param.put(JSLabels.VALUE, value.getStrVal());
				}
				param.put(JSLabels.IS_PADDING, attribute.isPadding());
				param.put(JSLabels.MODIFIABLE, attribute.isModifiable());

//				List<DpAttribute> dependentees = attribute.getPaddingDependentees();
//				if (dependentees == null || dependentees.isEmpty()) {
//					param.put(JSLabels.INFLUENTIAL_START, -1);
//					param.put(JSLabels.INFLUENTIAL_END, -1);
//				} else {
//					param.put(JSLabels.INFLUENTIAL_START, dependentees.get(0).getIdx());
//					param.put(JSLabels.INFLUENTIAL_END, dependentees.get(dependentees.size() - 1).getIdx());
//				}

				positiveObj.put(param);
			}
			arrayObj.put(positiveObj);
		}
		return arrayObj;
	}

	public static InputData createInputType(RequestType training) {
		InputData inputData = new InputData();
		inputData.requestType = RequestType.$TRAINING;
		return inputData;
	}

}
