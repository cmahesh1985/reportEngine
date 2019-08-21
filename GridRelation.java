package com.pag.toolbox.object;

import java.util.Arrays;
import java.util.List;

import com.pag.toolbox.util.Constants;

public class GridRelation {
	
	private String name;
	private List<String> relations;
	
	public GridRelation(String name,String... relation) {
		this.name = name;
		this.relations = Arrays.asList(relation);
	}
	
	public String getName() {
		return name;
	}
	
	public String calculateKey(List<GridCell> cells,boolean isChekcingInDest) {
		StringBuilder key = new StringBuilder();
		for (String relation : relations) {
			String toCheck = relation;
			String partOfKey = relation;
			if(relation.contains(Constants.HASH)){
				//Format is : sourceFileColumnName#destFileColumnName
				String[] split = relation.split(Constants.HASH);
				toCheck = isChekcingInDest ? split[1]:split[0];
				partOfKey = split[0];
			}
			for (GridCell cell : cells) {
				if(cell.getName().equals(toCheck)){
					key = key.append(partOfKey).append(cell.getValue());
					break;
				}
			}
		}
		return key.toString();
	}

}
