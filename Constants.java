package com.pag.toolbox.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Constants {
	
	private Constants() {}
	public static final String CSV_SEPRATOR_FOR_SYSTEM = ";";
	public static final String NOT_CSV_SEPRATOR_FOR_SYSTEM = ",";
	public static final String NEW_LINE = "\n";
	public static final String BLANK = "";
	public static final String SPACE = " ";
	public static final String UNDERSCORE = "_";
	public static final String FILE_SEPRATOR = "\\";
	public static final String DATA_TYPE_STRING = "String";
	public static final int STRING_SPLIT_LIMIT = -1;
	public static final String EMPTY_CELL = "-Nan-";
	public static final String REGX_COLONS_AT_END = ";+$";
	public static final String REGX_CONTAINS_ONLY_COMMA_OR_COLON = "^[;,]+$";
	public static final String REGX_NEWLINE = "[\\r\\n]+";
	public static final String REGX_EXT = "(?<=.)\\.[^.]+$";
	public static final String DOT = ".";
	public static final long MAX_OUTPUT_FILE_SIZE = 5000000;//5mb//5000000
	public static final String HASH = "#";
	
	//---CORIN specific constant
	public static final String CSV = ".csv";
	public static final String XLS = ".xls";
	public static final String CORIN = "CORIN";
	
	//---PlainStrength specific constant
	public static final String PS = "PS";
	public static final List<String> PS_ISAMI_OUTPUT_COLUMN_LIST = Collections.unmodifiableList(Arrays.asList("Analysis_ID","PanelLoad_ID"));
	public static final List<String> ANALYSIS_COLUMN_LIST = Collections.unmodifiableList(Arrays.asList("Analysis_ID","StackingByElement_ID","PanelLoad_ID","MethodParam_ID","CriterionParam_ID"));
	public static final List<String> TMATERIAL_COLUMN_LIST = Collections.unmodifiableList(Arrays.asList("Material_ID","Material Name","Specification","Library"));
	public static final List<String> TSTACKING_COLUMN_LIST = Collections.unmodifiableList(Arrays.asList("Stacking_ID","Orientation","Number of plies","Material 1","Material 2"));
	public static final List<String> GPANELLINK_COLUMN_LIST = Collections.unmodifiableList(Arrays.asList("PanelLink_ID","Element ID","NodeA_ID","NodeA_X","NodeA_Y","NodeA_Z","NodeB_ID","NodeB_X","NodeB_Y","NodeB_Z","NodeC_ID","NodeC_X","NodeC_Y","NodeC_Z","NodeD_ID","NodeD_X","NodeD_Y","	NodeD_Z"));
	public static final List<String> STACKINGBYELEMENT_COLUMN_LIST = Collections.unmodifiableList(Arrays.asList("StackingByElement_ID","PanelLink_ID","Element 1 Stacking","Element 2 Stacking","Element 3 Stacking","Element 4 Stacking","Element 5 Stacking","Element 6 Stacking","Element 7 Stacking","Element 8 Stacking","Element 9 Stacking","Element 10 Stacking","Element 11 Stacking","Element 12 Stacking","Draping Angle (deg)","Draping Angle Std Dev (deg)"));
	public static final List<String> METHOD_COLUMN_LIST = Collections.unmodifiableList(Arrays.asList("MethodParam_ID","Calculation method","Conditionning","Output level"));
	public static final List<String> CRITERION_COLUMN_LIST = Collections.unmodifiableList(Arrays.asList("CriterionParam_ID","Criterion"));
	public static final List<String> CPSPANELLOAD_COLUMN_LIST = Collections.unmodifiableList(Arrays.asList("PanelLoad_ID","PanelLink_ID","Load Case Selection","LC ID","Conditioning Type","Operating Temperature","Element1_Nxx(N/mm)","Element1_Nyy(N/mm)","Element1_Nxy(N/mm)","Element1_Mxx(N)","Element1_Myy(N)","Element1_Mxy(N)"));
	public static final String ANALYSIS = "Analysis";
	public static final String TMATERIAL =  "TMaterial";
	public static final String TSTACKING =  "TStacking";
	public static final String GPANELLINK = "GPanelLink";  
	public static final String STACKINGBYELEMENT = "StackingByElement"; 
	public static final String METHOD = "Method"; 
	public static final String CRITERION =  "Criterion";
	public static final String CPSPANELLOAD ="CPSPanelLoad";
	public static final String ISAMI_OUTPUT = "ISAMI_OUTPUT";
	public static final String DFEM_OH = "DFEM_OH";
	public static final String CRITERION_PARAM_ID = "CriterionParam_ID";
	public static final String METHOD_PARAM_ID = "MethodParam_ID";
	public static final String STACKING_BY_ELEMENT_ID = "StackingByElement_ID";
	public static final String ANALYSIS_ID = "Analysis_ID";
	
}
