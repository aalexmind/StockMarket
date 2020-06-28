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
	private final Instant timeAdded;

	private boolean canBeRemoved;

	public Order() {
		timeAdded = Instant.now();
		priceMin = -1;
		setCanBeRemoved(false);
	}

	public Order(OrderType type, String symbol, int priceMin, int priceMax, int quantity) {
		this();
		setOrderType(type);
		setSymbol(symbol);
		setPrices(priceMin, priceMax);
		setQuantity(quantity);
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

	private void setPrices(int priceMin, int priceMax) {
		if (priceMax >= priceMin) {
			setPriceMin(priceMin);
			setPriceMax(priceMax);
		} else {
			setPriceMin(priceMax);
			setPriceMax(priceMin);
		}
	}

	public String print() {
		return getID() + " " + getOrderType() + " " + getSymbol() + " " + getPriceRange() + " " + getQuantity();
	}

	private Instant getCreatedOn() {
		return timeAdded;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public Integer getPriceMax() {
		return priceMax;
	}

	public void setPriceMax(int priceMax) {
		this.priceMax = priceMax;
	}

	public Integer getPriceMin() {
		return priceMin;
	}

	public void setPriceMin(int priceMin) {
		this.priceMin = priceMin;
	}

	public String getPriceRange() {
		return getPriceMin() + "-" + getPriceMax();
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public boolean canBeRemoved() {
		return canBeRemoved;
	}

	public void setCanBeRemoved(boolean remove) {
		this.canBeRemoved = remove;
	}

}
