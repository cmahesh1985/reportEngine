package com.pag.toolbox.operation;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.pag.toolbox.object.Grid;
import com.pag.toolbox.object.GridRelation;
import com.pag.toolbox.object.GridRow;
import com.pag.toolbox.object.Module;
import com.pag.toolbox.util.Constants;
import com.pag.toolbox.util.ParserUtil;

public class BaseOperations {
	
	private final Logger logger = Logger.getLogger(BaseOperations.class.getName());
	private final Module moduleInfo;
	private Grid finalOutput;
	
	public BaseOperations(Module moduleInfo) {
		this.moduleInfo = moduleInfo;
	}
	
	public Module getModuleData() {
		return moduleInfo;
	}
	
	public void process(){
		Map<File, File> inputVsOutputFiles = moduleInfo.getIsamiInputVsOutputFiles();
		isamiInputVsOutputFiles.entrySet().stream().forEach(e -> {
			Grid tempOutput = parse(e);
			if(finalOutput==null){
				finalOutput = tempOutput;
			}else{
				finalOutput.mergeVertical(tempOutput);
				finalOutput.write(moduleInfo.getOutputFileName());
			}
		});
		
		//Write remaining data if any to file 
		if(finalOutput!=null && !ParserUtil.isEmpty(finalOutput.getRowDataList())){
			finalOutput.writeToFile(moduleInfo.getOutputFileName());
		}
	}
	
	private Grid parse(Entry<File, File> e) {
		File isamiInputFile = e.getKey();
		File isamiOutputFile = e.getValue();
		Grid isamiOutput = parseIsamiOutput(isamiOutputFile);
		if(isamiOutput!=null){
			return parseIsamiInput(isamiOutput,isamiInputFile);
		}
		return null;
	}

	private Grid parseIsamiOutput(File isamiOutputFile) {
		
		if(isamiOutputFile==null){
			return null;
		}
		
		String fileExtension = ParserUtil.getFileExtension(isamiOutputFile);
		List<String> isamiOutputColumns = moduleInfo.getIsamiOutputColumns();
		if(Constants.CSV.equalsIgnoreCase(fileExtension)){
			return parseCsv(isamiOutputFile, isamiOutputColumns,moduleInfo.getIsamiOutputModuleVsRelations());
		}else if(Constants.XLS.equalsIgnoreCase(fileExtension)){
			try(Workbook workbook = WorkbookFactory.create(isamiOutputFile)) {
				Sheet excelWSheet =  workbook.getSheetAt(0);
				return parseXls(excelWSheet, isamiOutputColumns,moduleInfo.getIsamiOutputModuleVsRelations());
			}catch (Exception e) {
				logger.log(Level.SEVERE, "Parsing failed for -> " + isamiOutputFile.getName());
				logger.log(Level.SEVERE, e.getMessage());
			}
		}
		
		return null;
	}
	
	private Grid parseIsamiInput(Grid isamiOutput,File isamiInputFile) {
		List<GridRelation> isamiInputModuleVsRelations = moduleInfo.getIsamiInputModuleVsRelations();
		for (GridRelation gridRelation : isamiInputModuleVsRelations) {
			Grid isamiInput = parseModule(isamiInputFile, gridRelation);
			isamiOutput.mergeHorizontal(isamiInput);
		}
		return isamiOutput;

	}

	private Grid parseModule(File isamiInputFile,GridRelation gridRelation){
		String module = gridRelation.getName();
		Map<String, List<String>> isamiInputModulesVsColumns = moduleInfo.getIsamiInputModulesVsColumns();
		List<String> columns =  isamiInputModulesVsColumns.get(module);
		try(Workbook workbook = WorkbookFactory.create(isamiInputFile)) {
			Sheet excelWSheet =  workbook.getSheet(module);
			switch (module) {
			case Constants.ANALYSIS: 
			case Constants.TMATERIAL:
			case Constants.STACKINGBYELEMENT:
			case Constants.METHOD:
			case Constants.CRITERION:
				return parseXls(excelWSheet,columns,gridRelation);
			case Constants.CPSPANELLOAD:
				return parseCpspanelload(excelWSheet,columns,gridRelation);
			case Constants.TSTACKING:
				return parseTstacking(excelWSheet,columns,gridRelation);
			case Constants.GPANELLINK:
				return parseGpanellink(excelWSheet,columns,gridRelation);
			default:
				return null;
		    }
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Parsing failed for -> " + isamiInputFile.getName());
			logger.log(Level.SEVERE, e.getMessage());
		}
		return null;
	}

	private Grid parseGpanellink(Sheet excelWSheet, List<String> columns,GridRelation gridRelation) {
		Grid gridData = new Grid(columns, new ArrayList<GridRow>(),gridRelation);
		return gridData;
	}

	private Grid parseTstacking(Sheet excelWSheet, List<String> columns,GridRelation gridRelation) {
		Grid gridData = new Grid(columns, new ArrayList<GridRow>(),gridRelation);
		return gridData;
	}

	private Grid parseCpspanelload(Sheet excelWSheet, List<String> columns,GridRelation gridRelation) {
		Grid gridData = new Grid(columns, new ArrayList<GridRow>(),gridRelation);
		return gridData;
	}

	private Grid parseXls(Sheet excelWSheet, List<String> columns,GridRelation gridRelation) {
		Iterator<Row> itr = excelWSheet.iterator();
		List<String> columnList = ParserUtil.findColumns(itr,columns);
		Grid gridData = null;
		if (!ParserUtil.isEmpty(columnList)) {
			gridData = new Grid(columnList, new ArrayList<GridRow>(),gridRelation);
			while (itr.hasNext()) {
				List<String> cellList = ParserUtil.rowToList(itr.next(),columnList.size());
				if (!ParserUtil.isBlankRow(ParserUtil.listTostring(cellList))) {
					GridRow rowData = new GridRow(new ArrayList<>());
					rowData.setCells(columnList, cellList);
					rowData.setkey(gridRelation);
					gridData.getRowDataList().add(rowData);
				}
			}
		}
		return gridData;
	}
	
	private Grid parseCsv(File isamiOutputFile,List<String> isamiOutputColumns, GridRelation gridRelation) {
		Grid gridData = null;
		try (Scanner inputStream = new Scanner(isamiOutputFile)){
			logger.log(Level.INFO, "Parsing starts for -> " + isamiOutputFile.getName());
			List<String> columnList = ParserUtil.findColumns(inputStream,isamiOutputColumns);
			if (!ParserUtil.isEmpty(columnList)) {
				gridData = new Grid(columnList, new ArrayList<GridRow>(),gridRelation);
				while (inputStream.hasNext()) {
					String row = inputStream.nextLine();
					if (!ParserUtil.isBlankRow(row)) {
						List<String> cellList = ParserUtil.stringToList(row);
						GridRow rowData = new GridRow(new ArrayList<>());
						rowData.setCells(columnList, cellList);
						rowData.setkey(gridRelation);
						gridData.getRowDataList().add(rowData);
					}
				}
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Parsing failed for -> " + isamiOutputFile.getName());
			logger.log(Level.SEVERE, e.getMessage());
		}
		logger.log(Level.INFO, "Parsing ends for -> " + isamiOutputFile.getName());
		return gridData;
	}

}
