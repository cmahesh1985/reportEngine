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
	
}
