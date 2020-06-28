package stockmarketui.simengine;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;

import stockmarketui.simengine.Order.OrderType;
import stockmarketui.util.LogHandler;

public class TradingGateway {
	private HashMap<String, OrderBooks> orderBookMap;

	public TradingGateway(HashMap<String, OrderBooks> orderBookMap) {
		this.orderBookMap = orderBookMap;
	}

	private OrderBooks getOrderBooks(String symbol) {
		if (!orderBookMap.containsKey(symbol)) {
			orderBookMap.put(symbol, new OrderBooks(symbol));
		}
		return orderBookMap.get(symbol);
	}

	private boolean validateConsoleInput(String[] splitStr) {
		if (splitStr.length == 6 || splitStr.length == 7) {
			return true;
		}
		return false;
	}

	private boolean validateUiInput(String[] splitStr) {
		if (splitStr.length == 5) {
			return true;
		}
		return false;
	}

	private Order parseInput(String input) throws IOException {
		if (input == null || input.isEmpty()) {
			throw new IOException("Empty input");
		}
		String[] splitStr = input.trim().split("\\s+");
		if (splitStr == null) {
			throw new IOException("Bad input");
		}
		Order parsed;
		if (validateConsoleInput(splitStr)) {
			parsed = parseConsoleStyle(splitStr);
		} else if (validateUiInput(splitStr)) {
			parsed = parseUiStyle(splitStr);
		} else {
			throw new IOException("Missing parameter in input?");
		}
		if (parsed == null) {
			throw new IOException("Something gone horribly wrong. Stay safe, stay legal!");
		}
		return parsed;
	}

	private Order parseConsoleStyle(String[] splitStr) throws IOException {
		OrderType ordertype;
		String ordertypeString = splitStr[2];
		if (ordertypeString.equals("B") || ordertypeString.equals("b")
				|| ordertypeString.equals(OrderType.BUY.toString())) {
			ordertype = OrderType.BUY;
		} else if (ordertypeString.equals("S") || ordertypeString.equals("s")
				|| ordertypeString.equals(OrderType.SELL.toString())) {
			ordertype = OrderType.SELL;
		} else {
			throw new IOException("Wrong type of the order?");
		}
		String symbol = splitStr[1];
		int priceMin = Integer.parseInt(splitStr[3]);
		int priceMax = Integer.parseInt(splitStr[4]);
		int quantity = Integer.parseInt(splitStr[5]);
		Order parsed = new Order(ordertype, symbol, priceMin, priceMax, quantity);
		try {
			if (splitStr[6] != null) {
				parsed.setID(Integer.parseInt(splitStr[6]));
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// try to get ID from user input for cancel action
			// it's ok if no any
		}
		return parsed;
	}

	private Order parseUiStyle(String[] splitStr) throws IOException {
		// [ID, SELL, symbol, 1-2 (price range), quantity]
		int id = Integer.parseInt(splitStr[0]);
		OrderType ordertype;
		String ordertypeString = splitStr[1];
		if (ordertypeString.equals("B") || ordertypeString.equals("b")
				|| ordertypeString.equals(OrderType.BUY.toString())) {
			ordertype = OrderType.BUY;
		} else if (ordertypeString.equals("S") || ordertypeString.equals("s")
				|| ordertypeString.equals(OrderType.SELL.toString())) {
			ordertype = OrderType.SELL;
		} else {
			throw new IOException("Wrong type of the order?");
		}
		String symbol = splitStr[2];
		String[] splitPrice = splitStr[3].trim().split("-");
		int priceMin = Integer.parseInt(splitPrice[0]);
		int priceMax = Integer.parseInt(splitPrice[1]);
		int quantity = Integer.parseInt(splitStr[4]);
		Order parsed = new Order(ordertype, symbol, priceMin, priceMax, quantity);
		parsed.setID(id);
		return parsed;
	}

	public void addOrder(String input) {
		try {
			Order order = parseInput(input);
			getOrderBooks(order.getSymbol()).addOrder(order);
			LogHandler.getInstance().log("Added order: " + order.getID() + " " + order.getSymbol() + " "
					+ order.getPriceMin() + "-" + order.getPriceMax() + " " + order.getQuantity());
		} catch (ArrayIndexOutOfBoundsException e) {
			LogHandler.getInstance().log(Level.SEVERE, "Missing parameter in input?");
		} catch (NumberFormatException e) {
			LogHandler.getInstance().log(Level.SEVERE, "Please check if all parameters correct");
		} catch (Exception e) {
			LogHandler.getInstance().log(Level.SEVERE, e.getMessage());
		}
	}

	public void cancelOrder(String input) {
		try {
			Order order = parseInput(input);
			getOrderBooks(order.getSymbol()).removeOrder(order);
		} catch (ArrayIndexOutOfBoundsException e) {
			LogHandler.getInstance().log(Level.SEVERE, "Missing parameter in input?");
		} catch (NumberFormatException e) {
			LogHandler.getInstance().log(Level.SEVERE, "Please check if all parameters correct");
		} catch (Exception e) {
			LogHandler.getInstance().log(Level.SEVERE, e.getMessage());
		}
	}

}
