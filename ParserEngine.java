package com.pag.toolbox.main;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pag.toolbox.factory.ModuleFactory;
import com.pag.toolbox.object.Module;
import com.pag.toolbox.operation.BaseOperations;
import com.pag.toolbox.util.Constants;
import com.pag.toolbox.util.ParserUtil;


public class ParserEngine {
	
	private static Logger logger = Logger.getLogger(ParserEngine.class.getName());
	
	public static void main(String[] args) {
		
		long startTime = System.currentTimeMillis();
		logger.log( Level.INFO, "My ParserEngine starting here");
		try {
			String module = Constants.PS;
			Map<File,File> isamiInputVsOutputFiles = initIsamiInputVsOutputFiles(module);
			Module moduleData = ModuleFactory.getModule(module, isamiInputVsOutputFiles);
			BaseOperations baseOperations = new BaseOperations(moduleData);
			baseOperations.process();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Parsing failed ");
			logger.log(Level.SEVERE, e.getMessage());
			e.printStackTrace();
		}
		logger.log( Level.INFO, "My ParserEngine ends here");
		long totalTime = System.currentTimeMillis()- startTime;
		logger.log( Level.INFO, "Processing time = {0}Ms ",totalTime);
	}

	private static Map<File,File> initIsamiInputVsOutputFiles(String module) {
		String isamiInputLocation = null;
		String isamiOutputLocation = null;
		String fileNameFilter = null;
		String fileRelation = "input#output";
		switch (module) {
			case Constants.PS:
				isamiInputLocation="D:\\PAG-ToolBox\\DATA\\PS\\ISAMI\\INPUT";
				isamiOutputLocation="D:\\PAG-ToolBox\\DATA\\PS\\ISAMI\\OUTPUT";
				fileNameFilter = Constants.PS;
				break;
			case Constants.CORIN:
				isamiInputLocation="D:\\PAG-ToolBox\\DATA\\CORIN\\ISAMI\\INPUT";
				isamiOutputLocation="D:\\PAG-ToolBox\\DATA\\CORIN\\ISAMI\\OUTPUT";
				fileNameFilter = Constants.CORIN;
				break;
			case Constants.DFEM_OH:
				isamiInputLocation="D:\\PAG-ToolBox\\DATA\\DFEM_OH\\ISAMI\\INPUT";
				isamiOutputLocation="D:\\PAG-ToolBox\\DATA\\DFEM_OH\\ISAMI\\OUTPUT";
				fileNameFilter = Constants.DFEM_OH;
				break;
			default:
				break;
		}
		return init(isamiInputLocation,isamiOutputLocation,fileNameFilter,fileRelation);
	}

	private static Map<File, File> init(String isamiInputLocation,String isamiOutputLocation,String fileNameFilter,String fileRelation) {
		Map<File,File> isamiInputVsOutputFiles = new HashMap<>();
		List<File> isamiInputFiles = ParserUtil.getListOfFiles(isamiInputLocation,fileNameFilter);
		List<File> isamiOutputFiles = ParserUtil.getListOfFiles(isamiOutputLocation,fileNameFilter);
		isamiInputFiles.stream().parallel().forEach(isamiInputFile-> isamiInputVsOutputFiles.put(isamiInputFile, getOutputFile(isamiInputFile,isamiOutputFiles,fileRelation)));
		return isamiInputVsOutputFiles;
	}

	private static File getOutputFile(File isamiInputFile,List<File> isamiOutputFiles,String fileRelation) {
		String isamiInputFileName = ParserUtil.getFileNameWithoutExtension(isamiInputFile);
		String isamiOutputFileName = getOutPutFileName(isamiInputFileName,fileRelation);
		return getFile(isamiOutputFiles,isamiOutputFileName);
	}

	private static File getFile(List<File> isamiOutputFiles,String isamiOutputFileName) {
		for (File isamiOutputFile : isamiOutputFiles) {
			String tempIsamiOutputFileName = ParserUtil.getFileNameWithoutExtension(isamiOutputFile);
			if(isamiOutputFileName.equalsIgnoreCase(tempIsamiOutputFileName)){ 
				return isamiOutputFile;
			}
		}
		return null;
	}

	public static String getOutPutFileName(String isamiInputFileName,String fileRelation) {
		String[] split = fileRelation.split(Constants.HASH);
		return isamiInputFileName.replaceAll(split[0], split[1]);
	}

}
