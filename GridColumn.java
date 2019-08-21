package com.pag.toolbox.object;


public class GridColumn {
	
	private String name;
	private boolean isHidden;
	
	
	public GridColumn(String name) {
		this.name = name;
		this.isHidden = false;
	}
	
	public GridColumn(String name,boolean isHidden) {
		this.name = name;
		this.isHidden = isHidden;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isHidden() {
		return isHidden;
	}
	
	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof GridColumn){
			GridColumn column = (GridColumn) obj;
			return this.name.equals(column.name);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
