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
		boolean greedy = false;
		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				if (args[i].equals("-g")) {
					greedy = true;
				}
			}
		}

		tradeLedger.setGreedy(greedy);
		processOrders();
	}

	private void processOrders() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder s = new StringBuilder();
		try {
			do {
				s.setLength(0);
				s.append(br.readLine());
				parseInput(s.toString());
			} while (!s.toString().equals("exit"));
		} catch (IOException e) {
			LogHandler.getInstance().log(e.getMessage());
		}

	}

	private void parseInput(String input) {
		try {
			String type = input.trim().split("\\s+")[0];
			switch (type) {
			case "add":
				tradingGateway.addOrder(input);
				break;
			case "cancel":
				tradingGateway.cancelOrder(input);
				break;
			case "trades":
				tradeLedger.printTrades();
				break;
			case "orders":
				orderBookMap.forEach((key, value) -> value.printOrders());
				break;
			case "help":
				LogHandler.getInstance().log("Syntax: command [symbol S/B min_price max_price quantity]");
				LogHandler.getInstance().log("commands: add, cancel, trades, orders, help");
				LogHandler.getInstance().log("Example commands: add gog S 30 50 10");
				break;
			case "exit":
				shutdown();
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

	public void addOrder(String input) {
		tradingGateway.addOrder(input);
	}

	public void cancelOrder(String input) {
		tradingGateway.cancelOrder(input);
	}

	public TradeLedger getTradeLedger() {
		return tradeLedger;
	}

	public HashMap<String, OrderBooks> getOrderBookMap() {
		return orderBookMap;
	}

	public void shutdown() {
		matchingEngine.shutdown();
	}

}
