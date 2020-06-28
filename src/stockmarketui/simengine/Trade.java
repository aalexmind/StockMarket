package stockmarketui.simengine;

import java.time.Instant;

public class Trade {
	private final int ID;
	private Order orderSell;
	private Order orderBuy;
	private Instant matchTime;
	private int pricefinal;
	private String symbol;

	public Trade(int id, Order orderSell, Order orderBuy, Instant matchTime, int pricefinal) {
		this.ID = id;
		setOrderBuy(orderBuy);
		setOrderSell(orderSell);
		setMatchTime(matchTime);
		setPricefinal(pricefinal);
		setSymbol(orderSell.getSymbol());
	}

	public String print() {
		return getID() + " " + getOrderSell().print() + " " + getOrderBuy().print() + " " + " final price: " + getPricefinal();
	}

	private int getID() {
		return ID;
	}

	public Order getOrderSell() {
		return orderSell;
	}

	private void setOrderSell(Order orderSell) {
		this.orderSell = orderSell;
	}

	public Order getOrderBuy() {
		return orderBuy;
	}

	private void setOrderBuy(Order orderBuy) {
		this.orderBuy = orderBuy;
	}

	public Instant getMatchTime() {
		return matchTime;
	}

	private void setMatchTime(Instant matchTime) {
		this.matchTime = matchTime;
	}

	public int getPricefinal() {
		return pricefinal;
	}

	private void setPricefinal(int pricefinal) {
		this.pricefinal = pricefinal;
	}

	public String getSymbol() {
		return symbol;
	}

	private void setSymbol(String symbol) {
		this.symbol = symbol;
	}
}
