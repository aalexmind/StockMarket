package stockmarketui.simengine;

import java.time.Instant;

public class Trade {
	private Order orderSell;
	private Order orderBuy;
	private Instant matchTime;
	private int pricefinal;

	public Trade(Order orderSell, Order orderBuy, Instant matchTime, int pricefinal) {
		this.setOrderBuy(orderBuy);
		this.setOrderSell(orderSell);
		this.setMatchTime(matchTime);
		this.setPricefinal(pricefinal);
	}

	public String print() {
		String trade = this.orderSell.print() + " " + this.orderBuy.print() + " " + " final price: "
				+ this.getPricefinal();
		return trade;
	}

	public Order getOrderSell() {
		return orderSell;
	}

	public void setOrderSell(Order orderSell) {
		this.orderSell = orderSell;
	}

	public Order getOrderBuy() {
		return orderBuy;
	}

	public void setOrderBuy(Order orderBuy) {
		this.orderBuy = orderBuy;
	}

	public Instant getMatchTime() {
		return matchTime;
	}

	public void setMatchTime(Instant matchTime) {
		this.matchTime = matchTime;
	}

	public int getPricefinal() {
		return pricefinal;
	}

	private void setPricefinal(int pricefinal) {
		this.pricefinal = pricefinal;
	}
}
