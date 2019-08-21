package com.pag.toolbox.object;

import com.pag.toolbox.util.Constants;

public class GridCell {

	private String name;
	private String value;
	private String dataType;
	
	public GridCell(String name,String value) {
		this.name = name;
		this.value = value;
		this.dataType = Constants.DATA_TYPE_STRING;
	}
	
	public String getValue() {
		return value;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDataType() {
		return dataType;
	}
	
	@Override
	public String toString() {
		return value;
	}
	
}
