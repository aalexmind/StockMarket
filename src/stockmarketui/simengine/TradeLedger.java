package stockmarketui.simengine;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import stockmarketui.util.LogHandler;

public class TradeLedger {
	private List<Trade> tradeHistory;
	private boolean greedy = false;

	public TradeLedger() {
		tradeHistory = new ArrayList<Trade>();
	}

	public TradeLedger(boolean greedy) {
		this();
		setGreedy(greedy);
	}

	public void addTrade(Order orderSell, Order orderBuy) {
		int pricefinal = decideFinalPrice(orderSell, orderBuy);
		tradeHistory.add(new Trade(tradeHistory.size(), orderSell, orderBuy, Instant.now(), pricefinal));
	}

	private int decideFinalPrice(Order orderSell, Order orderBuy) {
		if (greedy) {
			return orderBuy.getPriceMax();
		}
		return orderSell.getPriceMin();
	}

	public void printTrades() {
		tradeHistory.forEach(t -> LogHandler.getInstance().log("Trade between: " + t.getOrderSell().getID() + " " + t.getOrderBuy().getID()));
	}

	public boolean isGreedy() {
		return greedy;
	}

	public void setGreedy(boolean greedy) {
		this.greedy = greedy;
	}

	public List<Trade> getTradeHistory() {
		return tradeHistory;
	}

}
