package stockmarketui.simengine;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import stockmarketui.simengine.Order.OrderType;
import stockmarketui.util.LogHandler;

public class MatchingEngine {
	private TradeLedger tradeLedger;
	HashMap<String, OrderBooks> orderBookMap;
	private final ScheduledExecutorService scheduledService = Executors.newScheduledThreadPool(1);

	public MatchingEngine(TradeLedger tradeLedger, HashMap<String, OrderBooks> orderBookMap) {
		this.tradeLedger = tradeLedger;
		this.orderBookMap = orderBookMap;
	}

	public void balanceOrders() {
		orderBookMap.forEach((symbol, orderBook) -> {
			List<Order> buyOrders = orderBook.getOrders().stream().filter(v -> v.getOrderType().equals(OrderType.BUY))
					.collect(Collectors.toList());
			List<Order> sellOrders = orderBook.getOrders().stream().filter(v -> v.getOrderType().equals(OrderType.SELL))
					.collect(Collectors.toList());
			for (Order orderSell : sellOrders) {
				if (!orderSell.canBeRemoved()) {
					for (Order orderBuy : buyOrders) {
						if (!orderBuy.canBeRemoved() && !orderSell.canBeRemoved()) {
							processMatch(orderBuy, orderSell);
						}
					}
				}
			}
			orderBook.cleanup();
		});
	}

	private void processMatch(Order orderBuy, Order orderSell) {
		if (orderSell.getPriceMin() <= orderBuy.getPriceMax()) {
			tradeLedger.addTrade(orderSell, orderBuy);
			LogHandler.getInstance().log("Trade happened: " + orderSell.getID() + " and " + orderBuy.getID());
			if (orderSell.getQuantity() == orderBuy.getQuantity()) {
				orderSell.setCanBeRemoved(true);
				orderBuy.setCanBeRemoved(true);
			} else if (orderSell.getQuantity() > orderBuy.getQuantity()) {
				orderSell.setQuantity(orderSell.getQuantity() - orderBuy.getQuantity());
				orderBuy.setCanBeRemoved(true);
			} else if (orderSell.getQuantity() < orderBuy.getQuantity()) {
				orderBuy.setQuantity(orderBuy.getQuantity() - orderSell.getQuantity());
				orderSell.setCanBeRemoved(true);
			}
		}

	}

	public void startScheduleTask() {
		scheduledService.scheduleAtFixedRate(new Runnable() {
			public void run() {
				try {
					balanceOrders();
				} catch (Exception e) {
					LogHandler.getInstance().log(e.getMessage());
				}
			}
		}, 0, 1L, TimeUnit.SECONDS);
	}

	public void shutdown() {
		if (scheduledService != null) {
			scheduledService.shutdown();
		}
	}
}
