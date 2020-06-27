package stockmarketui.simengine;

import java.util.HashMap;
import java.util.logging.Level;

import stockmarketui.simengine.Order.OrderType;
import stockmarketui.util.LogHandler;

public class TradingGateway {
	private HashMap<String, OrderBooks> orderBookMap;

	public TradingGateway(HashMap<String, OrderBooks> orderBookMap) {
		this.orderBookMap = orderBookMap;
	}

	public void addOrder(String input) {
		Order order = parseInput(input);
		addOrder(order);
	}

	public void addOrder(Order order) {
		if (order != null) {
			if (!orderBookMap.containsKey(order.getSymbol())) {
				orderBookMap.put(order.getSymbol(), new OrderBooks(order.getSymbol()));
			}
			orderBookMap.get(order.getSymbol()).addOrder(order);
			LogHandler.getInstance().log("Added order: " + order.getID() + " " + order.getSymbol() + " "
					+ order.getPriceMin() + "-" + order.getPriceMax() + " " + order.getQuantity());
		}
	}

	public void cancelOrder(String input) {
		Order order = parseInput(input);
		if (order != null) {
			orderBookMap.get(order.getSymbol()).removeOrder(order);
		}
	}

	private Order parseInput(String input) {
		if (!validInput(input)) {
			return null;
		}
		String[] splitStr = input.trim().split("\\s+"); //$NON-NLS-1$
		OrderType ordertype;
		if (splitStr[2].equals("B")) { //$NON-NLS-1$
			ordertype = OrderType.BUY;
		} else if (splitStr[2].equals("S")) { //$NON-NLS-1$
			ordertype = OrderType.SELL;
		} else {
			LogHandler.getInstance().log(Level.SEVERE, "Wrong type of the order?");
			return null;
		}
		Order parsed = new Order(ordertype, splitStr[1], Integer.valueOf(splitStr[3]), Integer.valueOf(splitStr[4]),
				Integer.valueOf(splitStr[5]));
		try {
			if (splitStr[6] != null) {
				parsed.setID(Integer.valueOf(splitStr[6]));
			}
		} catch (Exception e) {
			// try to get ID from user input
		}
		return parsed;
	}

	private boolean validInput(String input) {
		String[] splitStr = input.trim().split("\\s+");
		if (splitStr.length < 6) {
			LogHandler.getInstance().log(Level.SEVERE, "Missing parameter in input?");
			return false;
		}
		return true;
	}

}
