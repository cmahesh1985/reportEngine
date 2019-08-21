package com.pag.toolbox.object;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.pag.toolbox.operation.BaseOperations;
import com.pag.toolbox.util.Constants;
import com.pag.toolbox.util.ParserUtil;

public class Grid {
	
	private final Logger logger = Logger.getLogger(BaseOperations.class.getName());
	private List<String> columns;
	private List<GridRow> rowDatas;
	private GridRelation gridRelation;
	
	public Grid(List<String> columns,List<GridRow> rowData) {
		this.columns = columns;
		this.rowDatas = rowData;
	}
	
	public Grid(List<String> columns,List<GridRow> rowData,GridRelation gridRelation) {
		this.columns = columns;
		this.rowDatas = rowData;
		this.gridRelation = gridRelation;
	}

	public List<String> getColumns() {
		return columns;
	}

	public List<GridRow> getRowDataList() {
		return rowDatas;
	}
	
	public GridRelation getGridRelation() {
		return gridRelation;
	}
	
	public void setGridRelation(GridRelation gridRelation) {
		this.gridRelation = gridRelation;
	}
	
	public void restructure() {
		this.rowDatas.stream().forEach(rowData -> rowData.restructure(this.columns));
	}
	
	public void mergeHorizontal(Grid tobeMerged){
		if(tobeMerged!=null && !ParserUtil.isEmpty(tobeMerged.rowDatas)){
			this.columns.addAll(tobeMerged.columns);
			this.rowDatas.stream().parallel().forEach(rowData -> rowData.mergeHorizontal(tobeMerged));
		}
	}
	
	public void mergeVertical(Grid tobeMerged){
		if(tobeMerged!=null && !ParserUtil.isEmpty(tobeMerged.rowDatas)){
			if(!this.columns.equals(tobeMerged.columns)){
				this.columns = tobeMerged.columns;
				this.restructure();
			}
			rowDatas.addAll(tobeMerged.rowDatas);
		}
	}
	
	
	public List<List<GridRow>> split(List<GridRow> toSplit) {
		try {
			int numberOfRows = toSplit.size();
			int totalFilelength = this.toString().getBytes("UTF-8").length;
			int numberOfPartitions = ParserUtil.getNumberOfPartitions(totalFilelength);
			long sizePerRow = ParserUtil.getSizePerRow(totalFilelength, numberOfRows);
			return IntStream
					.range(0, numberOfPartitions).parallel().boxed()
					.map(i -> toSplit.subList(
							ParserUtil.partitionOffsetBeg(sizePerRow, i),
							ParserUtil.partitionOffsetEnd(numberOfRows, sizePerRow, i)))
					.collect(Collectors.toList());

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error during partioning of current file");
			logger.log(Level.SEVERE, e.getMessage());
		}
		return Collections.emptyList();
	}
	

	public void write(String fileName) {
		List<GridRow> toSplit = new ArrayList<>(rowDatas);
		List<List<GridRow>> splits = split(toSplit);
		this.rowDatas.clear();
		int numberOfsplits = splits.size();
		for (int count = 0; count < numberOfsplits; count++) {
			List<GridRow> split = splits.get(count);
			this.rowDatas.addAll(split);
			if (count < numberOfsplits - 1) {
				writeToFile(fileName);
				this.rowDatas.clear();
			}
		}
	}
	
	public void writeToFile(String fileName) {
		String replacement = Constants.UNDERSCORE.concat(Integer.toString(Module.incrementOutputFileCounter())).concat(Constants.DOT);
		fileName = fileName.replace(Constants.DOT,replacement);
		File file = new File(fileName);
		try (PrintWriter writer = new PrintWriter(file);) {
			writer.write(toString());
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"Error while writing file" + e.getMessage());
		}
	}

	@Override
	public String toString() {
		StringBuilder finalOutput = new StringBuilder();
		finalOutput.append(this.getColumns().stream().map(Object::toString).collect(Collectors.joining(Constants.CSV_SEPRATOR_FOR_SYSTEM)));
		finalOutput.append(Constants.NEW_LINE);
		finalOutput.append(this.getRowDataList().stream().map(Object::toString)
				.collect(Collectors.joining(Constants.NEW_LINE)));
		return finalOutput.toString();
	}


}
