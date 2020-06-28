package stockmarketui.util;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogHandler {

	public static Logger logger;
	private LogHandler() {
		System.setProperty("java.util.logging.SimpleFormatter.format",
				"[%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS] %5$s%6$s%n");
		logger = Logger.getLogger(LogHandler.class.getName());
		setLogger(Level.ALL);
	}

	private static class LogHandlerHelper {
		private static final LogHandler INSTANCE = new LogHandler();
	}

	public static LogHandler getInstance() {
		return LogHandlerHelper.INSTANCE;
	}

	public void setLogger(Level level) {
		try {
			Arrays.stream(logger.getHandlers()).forEach(handler -> handler.setLevel(level));
		} catch (SecurityException e) {
			log(e.getMessage());
		}
		log("hello");
	}

	public void logError(String message) {
		if (message != null) {
			logger.severe(message);
		}
	}

	public void log(String message) {
		if (message != null) {
			logger.info(message);
		}
	}

	public void log(Level level, String message) {
		if (message != null) {
			if (level != null) {
				logger.log(level, message);
			} else {
				log(message);
			}
		}
	}

	public void log(Level level, String sourceClass, String sourceMethod, String message) {
		if (message != null) {
			if (level != null & sourceClass != null && sourceMethod != null) {
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
				logger.finest("sourceClass: " + sourceClass + "sourceMethod" + sourceMethod);
				logger.logp(level, sourceClass, sourceMethod, message, thrown);
			} else {
				log(level, message);
			}
		}
	}

}