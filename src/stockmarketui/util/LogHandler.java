package stockmarketui.util;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogHandler {

	public static Logger logger;
	static FileHandler fh;
	private final static String LOGFILENAME = "StockMarketSimulator.log";
	private static String LOGPATH = null;

	long prevTotal = 0;
	long prevFree = Runtime.getRuntime().freeMemory();

	private LogHandler() {
		System.setProperty("ava.util.logging.SimpleFormatter.format",
				"[%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS] %5$s%6$s%n");
		logger = Logger.getLogger(LogHandler.class.getName());
		setLevel(Level.ALL);
		setLogger(LOGPATH);
	}

	private static class LogHandlerHelper {
		private static final LogHandler INSTANCE = new LogHandler();
	}

	public static LogHandler getInstance() {
		return LogHandlerHelper.INSTANCE;
	}

	public static void setLogPath(String filePath) {
		LOGPATH = filePath;
		getInstance();
	}

	private void setLogger(String filePath) {
		try {
			String message = null;
			String absFilePath = FileOperations.makePathAbsolute(filePath);
			if (!FileOperations.isValid(filePath)) {
				message = "Path to log is not valid. Using default.";
				absFilePath = FileOperations.makePathAbsolute(LOGFILENAME);
			}
			File file = FileOperations.getFile(absFilePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			fh = new FileHandler(absFilePath);
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
			if (message != null) {
				log(message);
			}
		} catch (SecurityException | IOException e) {
			log(e.getMessage());
		}
	}

	public void setLevel(Level level) {
		logger.setLevel(level);
		Arrays.stream(logger.getHandlers()).forEach(handler -> handler.setLevel(level));
	}

	public void logError(String message) {
		if (message != null) {
			printMem();
			logger.severe(message);
		}
	}

	public void log(String message) {
		if (message != null) {
			printMem();
			logger.info(message);
		}
	}

	public void log(Level level, String message) {
		if (message != null) {
			if (level != null) {
				printMem();
				logger.log(level, message);
			} else {
				log(message);
			}
		}
	}

	public void log(Level level, String sourceClass, String sourceMethod, String message) {
		if (message != null) {
			if (level != null & sourceClass != null && sourceMethod != null) {
				printMem();
				logger.finest("sourceClass: " + sourceClass + "sourceMethod" + sourceMethod);
				logger.logp(level, sourceClass, sourceMethod, message);
			} else {
				log(level, message);
			}
		}
	}

	public void log(Level level, String sourceClass, String sourceMethod, String message, Object thrown) {
		if (message != null) {
			if (level != null & sourceClass != null && sourceMethod != null) {
				printMem();
				logger.finest("sourceClass: " + sourceClass + "sourceMethod" + sourceMethod);
				logger.logp(level, sourceClass, sourceMethod, message, thrown);
			} else {
				log(level, message);
			}
		}
	}

	private void printMem() {
//		Runtime rt = Runtime.getRuntime();
//		long total = rt.totalMemory();
//		long free = rt.freeMemory();
//		if (total != prevTotal || free != prevFree) {
//			long used = total - free;
//			long prevUsed = (prevTotal - prevFree);
//			logger.finest("Total: " + total + ", Used: " + used + ", DeltaUsed: " + (used - prevUsed) + ", Free: "
//					+ free + ", DeltaFree: " + (free - prevFree));
//			prevTotal = total;
//			prevFree = free;
//		}
	}

}