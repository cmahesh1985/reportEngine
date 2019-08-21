package com.pag.toolbox.object;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.pag.toolbox.util.Constants;


public class Module {
	
	private List<String> isamiOutputColumns;
	private List<GridRelation> isamiInputModuleVsRelations;
	private GridRelation isamiOutputModuleVsRelations;
	private Map<String,List<String>> isamiInputModulesVsColumns;
	private Map<File,File> isamiInputVsOutputFiles;
	private String outputLocation;
	private String outputfileName;
	private static int outputFileCounter;

	public List<String> getIsamiOutputColumns() {
		return isamiOutputColumns;
	}
	
	public List<GridRelation> getIsamiInputModuleVsRelations() {
		return isamiInputModuleVsRelations;
	}
	public Map<String,List<String>> getIsamiInputModulesVsColumns() {
		return isamiInputModulesVsColumns;
	}
	public Map<File, File> getIsamiInputVsOutputFiles() {
		return isamiInputVsOutputFiles;
	}
	
	public void setIsamiOutputColumns(List<String> isamiOutputColumns) {
		this.isamiOutputColumns = isamiOutputColumns;
	}

	public void setIsamiInputModuleVsRelations(List<GridRelation> isamiInputModuleVsRelations) {
		this.isamiInputModuleVsRelations = isamiInputModuleVsRelations;
	}
	public void setIsamiInputModulesVsColumns(
			Map<String, List<String>> isamiInputModulesVsColumns) {
		this.isamiInputModulesVsColumns = isamiInputModulesVsColumns;
	}
	public void setIsamiInputVsOutputFiles(Map<File, File> isamiInputVsOutputFiles) {
		this.isamiInputVsOutputFiles = isamiInputVsOutputFiles;
	}

	public void setOutputLocation(String outputLocation) {
		this.outputLocation = outputLocation;
	}
	public String getOutputFileName() {
		StringBuilder fileName = new StringBuilder(outputLocation);
		return fileName.append(Constants.FILE_SEPRATOR).append(outputfileName).toString();
	}
	
	public void setOutputfileName(String outputfileName) {
		this.outputfileName = outputfileName;
	}
	public static int incrementOutputFileCounter() {
		return outputFileCounter++;
	}
	
	public GridRelation getIsamiOutputModuleVsRelations() {
		return isamiOutputModuleVsRelations;
	}
	
	public void setIsamiOutputModuleVsRelations(GridRelation isamiOutputModuleVsRelations) {
		this.isamiOutputModuleVsRelations = isamiOutputModuleVsRelations;
	}
}
