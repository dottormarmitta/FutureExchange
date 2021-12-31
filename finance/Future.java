package finance;

import calendar.MonthDates;
import graph.DirectedEdge;

/**
 * This class represent a financial future. It is represented as a {@link DirectedEdge}
 * connecting
 * <br>
 * "InceptionDate &#8594; DeliveryDate"
 * 
 * @author Guglielmo Del Sarto
 */
public class Future implements DirectedEdge {
	
	// Never to be changed
	private final int to;
	private final int from;
	
	private double[] bid;
	private int bidPointer;  // Indicating the current top Bid price
	private double[] ask;
	private int askPointer;  // Indicating the current top Ask price
	private long[] bidLot;
	private long[] askLot;

	/**
	 * This constructor builds a Future starting from specific parameters
	 * 
	 * @param bid a descending-sorted array of bids
	 * @param bidQuantity an array of bid lots
	 * @param ask an ascending-sorted array of asks
	 * @param askQuantity an array of ask lots
	 * @param inceptionDate the inception date
	 * @param deliveryDate the delivery date
	 */
	public Future(double[] bid, long[] bidQuantity, double[] ask, long[] askQuantity, 
			MonthDates inceptionDate, MonthDates deliveryDate) {
		
		this.bid = bid;
		this.bidPointer = 0;
		this.bidLot = bidQuantity;
		
		this.ask = ask;
		this.askPointer = 0;
		this.askLot = askQuantity;
		
		this.to = deliveryDate.ordinal(); // The int representation of the date
		this.from = inceptionDate.ordinal(); // The int representation of the date
	}
	

	@Override
	public double bid() {
		if (from < to) {
			return bidPointer >= bid.length ? Double.NEGATIVE_INFINITY : bid[bidPointer];
		}
		return askPointer >= ask.length ? Double.NEGATIVE_INFINITY : -ask[askPointer];
	}
	
	@Override
	public double ask() {
		if (from < to) {
			return askPointer >= ask.length ? Double.POSITIVE_INFINITY : ask[askPointer];
		} 
		return bidPointer >= bid.length ? Double.POSITIVE_INFINITY : -bid[bidPointer];
	}

	@Override
	public int from() {
		return from;
	}

	@Override
	public int to() {
		return to;
	}

	@Override
	public void executeBid() {
		if (from < to) {
			if(bidPointer < bidLot.length && --bidLot[bidPointer] == 0) {
				/*
				 * If I am here, the current bid lot is empty, I should look to
				 * the next one (hence, I move my pointer)
				 */
				bidPointer++; 
			}
		} else {
			if(askPointer < askLot.length && --askLot[askPointer] == 0) {
				/*
				 * If I am here, the current ask lot is empty, I should look to
				 * the next one (hence, I move my pointer)
				 */
				askPointer++;
			}
		}
	}

	@Override
	public void executeAsk() {
		if (from < to) {
			if(askPointer < askLot.length && --askLot[askPointer] == 0) {
				/*
				 * If I am here, the current ask lot is empty, I should look to
				 * the next one (hence, I move my pointer)
				 */
				askPointer++;
			}
		} else {
			if(bidPointer < bidLot.length && --bidLot[bidPointer] == 0) {
				/*
				 * If I am here, the current bid lot is empty, I should look to
				 * the next one (hence, I move my pointer)
				 */
				bidPointer++;
			}
		}
	}

}
