package com.pag.toolbox.object;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.pag.toolbox.util.Constants;
import com.pag.toolbox.util.ParserUtil;

public class GridRow {
	
	private String key;
	private List<GridCell> cells;
	
	
	public GridRow(List<GridCell> cells) {
		this.cells = cells;
	}
	
	public void setCells(List<String> columns,List<String> cells) {
		if(columns.size() == cells.size()){
			for(int i = 0;i< columns.size() ;i++){
				String name = columns.get(i);
				String value = cells.get(i);
				this.cells.add(new GridCell(name, value));
			}
		}
	}
	
	public void setkey(GridRelation relation) {
		if(ParserUtil.isEmpty(key)){
			key = calculateKey(relation);
		}
	}

	public String calculateKey(GridRelation relation) {
		return relation.calculateKey(cells,false);
	}
	
	@Override
	public String toString(){
		return this.cells.stream().map(Object::toString)
				.collect(Collectors.joining(Constants.CSV_SEPRATOR_FOR_SYSTEM));
	}
	
	public void restructure(List<String> columns) {
		List<GridCell> tempCells = new ArrayList<>();
		for (String column : columns){
			boolean found = false;
			for (GridCell cellData : this.cells) {
				if(column.equals(cellData.getName())){
					tempCells.add(cellData);
					found = true;
					break;
				}
			}
			if(!found){
				tempCells.add(new GridCell(column,Constants.EMPTY_CELL));
			}
		}
		this.cells = tempCells;
	}

	public void mergeHorizontal(Grid source) {
		this.key = source.getGridRelation().calculateKey(cells,true);
		source.getRowDataList().stream().parallel().forEach(rowData -> {
			if (rowData.key.equalsIgnoreCase(this.key)) {
				this.cells.addAll(rowData.cells);
			}
		});
	}
	
}
