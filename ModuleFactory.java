package com.pag.toolbox.factory;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pag.toolbox.object.GridRelation;
import com.pag.toolbox.object.Module;
import com.pag.toolbox.util.Constants;
import com.pag.toolbox.util.ParserUtil;



public class ModuleFactory {
	

	private ModuleFactory() {}
	
	public static Module getModule(String module, Map<File,File> isamiInputVsOutputFiles) {
		switch (module) {
			case Constants.PS:
				return createPlainStrengthModule(isamiInputVsOutputFiles);
			case Constants.CORIN:
				return createCorinModule(isamiInputVsOutputFiles);
			case Constants.DFEM_OH:
				return createDFEMModule(isamiInputVsOutputFiles);
			default:
				break;
		}
		return null;
	}

	private static Module createDFEMModule(Map<File, File> isamiInputVsOutputFiles) {
		Module dFEMModule = new Module();
		return dFEMModule;
	}

	private static Module createCorinModule(Map<File, File> isamiInputVsOutputFiles) {
		return null;
	}

	private static Module createPlainStrengthModule(Map<File, File> isamiInputVsOutputFiles) {
		Module plainStrength = new Module();
		plainStrength.setIsamiInputVsOutputFiles(isamiInputVsOutputFiles);
		plainStrength.setOutputfileName("PS_PAG-ToolBox_Output.csv");
		plainStrength.setOutputLocation("D:\\PAG-ToolBox\\DATA\\PS\\OUTPUT");
		List<String> isamiOutputColumns = ParserUtil.getColumnList(isamiInputVsOutputFiles.values(),Constants.PS_ISAMI_OUTPUT_COLUMN_LIST);
		plainStrength.setIsamiOutputColumns(isamiOutputColumns);
		plainStrength.setIsamiInputModulesVsColumns(initIsamiInputModulesVsColumnsForPlainStrength());
		plainStrength.setIsamiInputModuleVsRelations(initIsamiGridRelations());
		plainStrength.setIsamiOutputModuleVsRelations(new GridRelation(Constants.ISAMI_OUTPUT,Constants.ANALYSIS_ID));
		return plainStrength;
	}
	
	
	private static Map<String, List<String>> initIsamiInputModulesVsColumnsForPlainStrength() {
        Map<String, List<String>> map = new HashMap<>();
        map.put(Constants.ANALYSIS,Constants.ANALYSIS_COLUMN_LIST);
        map.put(Constants.TMATERIAL,Constants.TMATERIAL_COLUMN_LIST);
        map.put(Constants.TSTACKING,Constants.TSTACKING_COLUMN_LIST);
        map.put(Constants.GPANELLINK,Constants.GPANELLINK_COLUMN_LIST);
        map.put(Constants.STACKINGBYELEMENT,Constants.STACKINGBYELEMENT_COLUMN_LIST);
        map.put(Constants.METHOD,Constants.METHOD_COLUMN_LIST);
        map.put(Constants.CRITERION,Constants.CRITERION_COLUMN_LIST);
        map.put(Constants.CPSPANELLOAD,Constants.CPSPANELLOAD_COLUMN_LIST);
        return Collections.unmodifiableMap(map);
    }
	
	private static  List<GridRelation> initIsamiGridRelations() {
        return Arrays.asList(new GridRelation(Constants.ANALYSIS,Constants.ANALYSIS_ID),
        					 new GridRelation(Constants.STACKINGBYELEMENT,Constants.STACKING_BY_ELEMENT_ID),
							 new GridRelation(Constants.METHOD,Constants.METHOD_PARAM_ID),
							 new GridRelation(Constants.CRITERION,Constants.CRITERION_PARAM_ID));
							 //new GridRelation(Constants.CPSPANELLOAD,"PanelLoad_ID","PanelLink_ID"),
							 // new GridRelation(Constants.TSTACKING,"Stacking_ID#Element 1 Stacking"),
							 //new GridRelation(Constants.GPANELLINK,"PanelLink_ID"),
							 //new GridRelation(Constants.TMATERIAL,"Material_ID#Material 1"));
    }

}
