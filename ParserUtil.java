package com.pag.toolbox.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Row;

public class ParserUtil {
	
	private static Logger logger = Logger.getLogger(ParserUtil.class.getName());
	
	private ParserUtil() {}

	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Object obj) {
		if (obj == null) {
			return true;
		}

		if (obj instanceof CharSequence) {
			return ((CharSequence) obj).length() == 0;
		}
		if (obj instanceof Collection) {
			return ((Collection) obj).isEmpty();
		}
		if (obj instanceof Map) {
			return ((Map) obj).isEmpty();
		}
		return false;
	}

	public static List<File> getListOfFiles(String directoryName) {
		File directory = new File(directoryName);
		List<File> resultList = new ArrayList<>();
		File[] fList = directory.listFiles();
		resultList.addAll(Arrays.asList(fList));
		for (File file : fList) {
			if (file.isDirectory()) {
				resultList.addAll(getListOfFiles(file.getAbsolutePath()));
			}
		}
		return resultList;
	}
	
	public static List<File> getListOfFiles(String directoryName,String fileNameFilter) {
		List<File> listOfFilteredFiles = new ArrayList<>();
		List<File> listOfFiles = getListOfFiles(directoryName);
		for (File file : listOfFiles) {
			String completeFileName = file.getName().toUpperCase();
			if (file.isFile() && completeFileName.contains(fileNameFilter.toUpperCase())) {
				listOfFilteredFiles.add(file);
			}
		}
		return listOfFilteredFiles;
	}
	
	public static String getFileExtension(File file) {
	    String name = file.getName();
	    return getFileExtension(name);
	}
	
	public static String getFileExtension(String fileName) {
	    int lastIndexOf = fileName.lastIndexOf(Constants.DOT);
	    if (lastIndexOf == -1) {
	        return Constants.BLANK; // empty extension
	    }
	    return fileName.substring(lastIndexOf);
	}
	
	public static boolean isEmpty(String input){
		return input==null || input.isEmpty();
	}
	
	public static List<String> stringToList(String commaSepratedString,String separator){
		if(!isEmpty(commaSepratedString) && !isEmpty(separator)){
			String[] split = commaSepratedString.split(separator,Constants.STRING_SPLIT_LIMIT);
			return new ArrayList<>(Arrays.asList(split));
		}
		return Collections.emptyList();
	}
	
	public static String listTostring(List<String> inputList,String separator){
		if(!isEmpty(inputList)){
			return String.join(separator, inputList);
		}
		return Constants.BLANK;
	}
	
	public static String listTostring(List<String> inputList){
		return listTostring(inputList,Constants.CSV_SEPRATOR_FOR_SYSTEM);
	}
	
	public static List<String> stringToList(String commaSepratedString){
		return stringToList(commaSepratedString,Constants.CSV_SEPRATOR_FOR_SYSTEM);
	}
	
	public static List<String> rowToList(Row row,int minimumCoulmnCount){
		List<String> list  = new ArrayList<>();
		for (int cn = 0; cn < minimumCoulmnCount; cn++) {
			String cellStr = row.getCell(cn,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
			cellStr = removeJunk(cellStr);
			list.add(cellStr);
		}
		return list;
	}

	public static boolean isBlankRow(String inputString){
		return isEmpty(inputString)|| Pattern.matches(Constants.REGX_CONTAINS_ONLY_COMMA_OR_COLON, inputString);
	}
	
	public static List<String> mergeListsWithoutDuplicates(List<String> list1, List<String> list2) {
		
		if(isEmpty(list1) && isEmpty(list2)) return Collections.emptyList();
		else if(isEmpty(list1)) return list2;
		else if(isEmpty(list2)) return list1;
		
		List<String> list1Copy = new ArrayList<>(list1);
		List<String> list2Copy = new ArrayList<>(list2);
		list2Copy.removeAll(list1Copy);
		list1Copy.addAll(list2Copy);
		return list1Copy;
	}
	
	public static int partitionOffsetBeg(long sizePerRow ,int partitionIndex) {
		return (int)Math.floor((double)(partitionIndex * Constants.MAX_OUTPUT_FILE_SIZE)/sizePerRow)+Math.min(1, partitionIndex);
	}
	
	public static int partitionOffsetEnd(int length, long sizePerRow ,int partitionIndex) {
		return Math.min((int)(((partitionIndex+1)* Constants.MAX_OUTPUT_FILE_SIZE)/sizePerRow)+1,length);
	}
	
	public static int getNumberOfPartitions(int totalFilelength){
		return (int) Math.ceil((double)totalFilelength / Constants.MAX_OUTPUT_FILE_SIZE);
	}
	
	public static long getSizePerRow(int totalFileSize,int numberOfrows){
		return  totalFileSize / numberOfrows;
	}

	public static List<String> getColumnList(Collection<File> files,List<String> columns) {
		List<String> finalColumnList =  new ArrayList<>();
		if (!ParserUtil.isEmpty(files)) {
			for (File file : files) {
				if (isValidInputFile(file)) {
					try (Scanner inputStream = new Scanner(file)){
						List<String> currentColumnList = findColumns(inputStream,columns);
						finalColumnList = ParserUtil.mergeListsWithoutDuplicates(finalColumnList, currentColumnList);
					} catch (FileNotFoundException e) {
						logger.log( Level.SEVERE, "No file found");
					}
				}
			}
		}
		return finalColumnList;
	}
	
	public static boolean isValidInputFile(File file) {
		return file!=null && !ParserUtil.isEmpty(ParserUtil.getFileExtension(file));
	}
	
	public static List<String> findColumns(Scanner inputStream,List<String> columns) {
		while (inputStream.hasNext()) {
			String columnsStr = inputStream.nextLine();
			columnsStr = columnsStr.replaceAll(Constants.REGX_NEWLINE, Constants.SPACE);
			List<String> columnList = ParserUtil.stringToList(columnsStr);
			List<String> tempColumnList = new ArrayList<>(columnList);
			tempColumnList.retainAll(columns);
			if (!ParserUtil.isEmpty(tempColumnList)) {
				return columnList;
			}
		}
		logger.log( Level.SEVERE, "No file headers found");
		return Collections.emptyList();
	}
	
	public static List<String> findColumns(Iterator<Row> iterator,List<String> columns) {
		while (iterator.hasNext()) {
			Row row = iterator.next();
			List<String> columnList = ParserUtil.rowToList(row,row.getLastCellNum());
			List<String> tempColumnList = new ArrayList<>(columnList);
			tempColumnList.retainAll(columns);
			if (!ParserUtil.isEmpty(tempColumnList)) {
				return columnList;
			}
		}
		logger.log( Level.SEVERE, "No file headers found");
		return Collections.emptyList();
	}
	
	public static String getFileNameWithoutExtension(File file) {
		return file.getName().replaceAll(Constants.REGX_EXT, Constants.BLANK);
	}

	public static String removeJunk(String data) {
		return data.replaceAll(Constants.REGX_NEWLINE, Constants.SPACE).replaceAll(Constants.CSV_SEPRATOR_FOR_SYSTEM, Constants.NOT_CSV_SEPRATOR_FOR_SYSTEM);
	}

	
}
