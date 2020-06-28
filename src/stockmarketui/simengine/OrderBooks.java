package stockmarketui.simengine;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import stockmarketui.util.LogHandler;

public class OrderBooks {
	private String symbol;
	private List<Order> orders;
	private int lastId = 0;

	public OrderBooks(String symbol) {
		this.symbol = symbol;
		orders = new LinkedList<>();
	}

	public void addOrder(Order order) {
		order.setID(lastId++);
		orders.add(order);
		sortOrders();
	}

	public void removeOrder(Order order) {
		removeOrder(order.getID());
	}

	public void removeOrder(int ID) {
		if (!orders.removeIf(val -> val.getID() == ID)) {
			LogHandler.getInstance().log("No order found with this ID and symbol");
		}
	}

	private void sortOrders() {
		Collections.sort(orders);
	}

	public void cleanup() {
		orders.removeIf(v -> v.canBeRemoved());
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void printOrders() {
		orders.forEach(val -> LogHandler.getInstance().log(val.print()));
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

}
