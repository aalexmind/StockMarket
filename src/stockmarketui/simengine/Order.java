package stockmarketui.simengine;

import java.time.Instant;

public class Order implements Comparable<Order> {
	public enum OrderType {
		BUY, SELL;
	}

	private OrderType orderType;
	private int ID;
	private String symbol;
	private int priceMin;
	private int priceMax;
	private int quantity;
	private Instant timeAdded;

	private boolean canBeRemoved;

	public Order() {
		timeAdded = Instant.now();
		priceMin = -1;
		setCanBeRemoved(false);
	}

	public Order(OrderType type, String symbol, Integer priceMin, Integer priceMax, Integer quantity) {
		this();
		this.setOrderType(type);
		this.symbol = symbol;
		this.priceMin = priceMin;
		this.setPriceMax(priceMax);
		this.setQuantity(quantity);
	}

	@Override
	public int compareTo(Order o) {
		if (getPriceMin() >= 0 && o.getPriceMin() >= 0) {
			int sComp = getPriceMin().compareTo(o.getPriceMin());
			if (sComp != 0) {
				return sComp;
			}
		}
		if (getCreatedOn() != null && o.getCreatedOn() != null) {
			return getCreatedOn().compareTo(o.getCreatedOn());
		}
		return 0;
	}

	public String print() {
		String order = this.getID() + " " + this.getOrderType() + " " + this.getSymbol() + " " + this.getPriceMin()
				+ "-" + this.getPriceMax() + " " + this.getQuantity();
		return order;
	}

	private Instant getCreatedOn() {
		return timeAdded;
	}

	public int getID() {
		return ID;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public int getPriceMax() {
		return priceMax;
	}

	public Integer getPriceMin() {
		return priceMin;
	}

	public int getQuantity() {
		return quantity;
	}

	public String getSymbol() {
		return symbol;
	}

	public boolean canBeRemoved() {
		return canBeRemoved;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public void setPriceMax(int priceMax) {
		this.priceMax = priceMax;
	}

	public void setPriceMin(int priceMin) {
		this.priceMin = priceMin;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setCanBeRemoved(boolean remove) {
		this.canBeRemoved = remove;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

}
