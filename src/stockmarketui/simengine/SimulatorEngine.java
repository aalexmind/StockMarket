package stockmarketui.simengine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Level;

import stockmarketui.util.LogHandler;

public class SimulatorEngine {
	private static SimulatorEngine instance;

	private HashMap<String, OrderBooks> orderBookMap;
	private MatchingEngine matchingEngine;
	private TradeLedger tradeLedger;
	private TradingGateway tradingGateway;

	private SimulatorEngine() {
		orderBookMap = new HashMap<>();
		tradeLedger = new TradeLedger();
		matchingEngine = new MatchingEngine(tradeLedger, orderBookMap);
		tradingGateway = new TradingGateway(orderBookMap);
		matchingEngine.startScheduleTask();
	}

	public static SimulatorEngine getInstance() {
		if (instance == null) {
			instance = new SimulatorEngine();
		}
		return instance;
	}

	public void init(String[] args) {
		if (args == null) {
			return;
		}
		if (args.length == 0) {
			LogHandler.getInstance().log("No input argument found"); //$NON-NLS-1$
		}
		boolean greedy = false;
		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				if (args[i].equals("-l")) { //$NON-NLS-1$
					try {
						LogHandler.setLogPath(args[i + 1]);
					} catch (Exception e) {
						LogHandler.getInstance().log("Wrong or missing path to log file"); //$NON-NLS-1$
					}
				} else if (args[i].equals("-g")) { //$NON-NLS-1$
					greedy = true;
				}
			}
		}

		tradeLedger.setGreedy(greedy);
		processOrders();
	}

	public void fillOrderBookMap(HashMap<String, OrderBooks> orderBookMap2, String symbol) {
		orderBookMap2.put(symbol, new OrderBooks(symbol));
	}

	private void processOrders() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String s;
		try {
			do {
				s = br.readLine();
				parseInput(s);
			} while (!s.equals("exit")); //$NON-NLS-1$
		} catch (IOException e) {
			LogHandler.getInstance().log(e.getMessage());
		}

	}

	private void parseInput(String input) {
		try {
			String type = input.trim().split("\\s+")[0]; //$NON-NLS-1$
			switch (type) {
			case "add": //$NON-NLS-1$
				tradingGateway.addOrder(input);
				break;
			case "cancel": //$NON-NLS-1$
				tradingGateway.cancelOrder(input);
				break;
			case "trades": //$NON-NLS-1$
				tradeLedger.printTrades();
				break;
			case "orders": //$NON-NLS-1$
				orderBookMap.forEach((key, value) -> value.printOrders());
				break;
			case "help": //$NON-NLS-1$
				LogHandler.getInstance().log("Syntax: command [symbol S/B min_price max_price quantity]");
				LogHandler.getInstance().log("commands: add, cancel, trades, orders, help");
				LogHandler.getInstance().log("Example commands: add gog S 30 50 10");
				break;
			case "exit": //$NON-NLS-1$
				matchingEngine.shutdown();
				return;

			default:
				LogHandler.getInstance().log(Level.SEVERE, "Unknown command");
				break;
			}
		} catch (Exception e) {
			LogHandler.getInstance().log(Level.SEVERE, e.getMessage());
		}
	}

	public TradingGateway getTradingGateway() {
		return tradingGateway;
	}

	public TradeLedger getTradeLedger() {
		return tradeLedger;
	}

	public HashMap<String, OrderBooks> getOrderBookMap() {
		return orderBookMap;
	}

}
