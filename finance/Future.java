package finance;

import graph.DirectedEdge;

/**
 * This class represent a financial future. It is represented as a {@link: DirectedEdge}
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
	private int bidPointer;
	private double[] ask;
	private int askPointer;
	private long[] bidLot;
	private long[] askLot;

	/**
	 * This constructor builds a Future starting from specific parameters
	 * 
	 * @param bid a descending-sorted array of bids
	 * @param bidQuantity an array of bid lots
	 * @param ask an ascending-sorted array of asks
	 * @param askQuantity an array of ask lots
	 * @param inceptionDateID the ID of the vertex
	 * @param deliveryDateID the ID of the vertex
	 */
	public Future(double[] bid, long[] bidQuantity, double[] ask, long[] askQuantity, 
			int inceptionDateID, int deliveryDateID) {
		
		this.bid = bid;
		this.bidPointer = 0;
		this.bidLot = bidQuantity;
		
		this.ask = ask;
		this.askPointer = 0;
		this.askLot = askQuantity;
		
		this.to = deliveryDateID;
		this.from = inceptionDateID;
	}
	

	@Override
	public double bid() {
		if (from < to) {
			if(bidPointer >= bid.length) {
				return Double.NEGATIVE_INFINITY;
			} else {
				return bid[bidPointer];
			}
		} else {
			if(askPointer >= ask.length) {
				return Double.NEGATIVE_INFINITY;
			} else {
				return -ask[askPointer];
			}
		}
	}
	
	@Override
	public double ask() {
		if (from < to) {
			if(askPointer >= ask.length) {
				return Double.POSITIVE_INFINITY;
			} else {
				return ask[askPointer];
			}
		} else {
			if(bidPointer >= bid.length) {
				return Double.POSITIVE_INFINITY;
			} else {
				return -bid[bidPointer];
			}
		}
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
				bidPointer++;
			}
		} else {
			if(askPointer < askLot.length && --askLot[askPointer] == 0) {
				askPointer++;
			}
		}
	}

	@Override
	public void executeAsk() {
		if (from < to) {
			if(askPointer < askLot.length && --askLot[askPointer] == 0) {
				askPointer++;
			}
		} else {
			if(bidPointer < bidLot.length && --bidLot[bidPointer] == 0) {
				bidPointer++;
			}
		}
	}

}
