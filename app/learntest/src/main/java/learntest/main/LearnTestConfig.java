package learntest.main;

public class LearnTestConfig {
	
	public static final String MODULE = "learntest";
	
	private static String pkgbase = "testdata.test.benchmark.inttostring.";

	public static String typeName = "IntToString";
	public static String methodName = "transform";
	public static String filePath = "D:/git/Ziyuan/app/learntest/src/test/java/testdata/benchmark/inttostring/" + typeName + ".java";
	public static String className = "testdata.benchmark.inttostring." + typeName;
	
	public static String pkg = pkgbase + typeName.toLowerCase();
	public static String testPath = pkg + "." + typeName + "1";
	
	public static String resPkg = "testdata.result." + typeName.toLowerCase();
	
}
