package graph;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import calendar.MonthDates;

/**
 * This class has an implementation of the Bellman-Ford algorithm for finding top bid and top ask
 * in a financial exchange engine.
 * <br>
 * The algorithm hereby presented takes inspiration from the book "Algorithms"
 * by R. Sedgewick and K. Wayne
 * 
 * @author Guglielmo Del Sarto
 */
public class BellmanFordAlgorithm {
	
	// Never to be changed
	private final WeightedDigraph G;
	private final int source;
	
	// To be used in methods
	int currentV;
	
	// BID
	private double[] bidTo;
	private DirectedEdge[] edgeBidTo;
	private boolean[] isVertexOnQueueBid;
	private Queue<Integer> queueBid = new LinkedList<>();
	
	// ASK
	private double[] askTo;
	private DirectedEdge[] edgeAskTo;
	private boolean[] isVertexOnQueueAsk;
	private Queue<Integer> queueAsk = new LinkedList<>();
	
	/**
	 * This constructor allows the user to build the class.
	 * Two parameters have to be passed
	 * 
	 * @param G a directed graph
	 * @param inceptionDate the source inception date of the future
	 */
	public BellmanFordAlgorithm(WeightedDigraph G, MonthDates inceptionDate) {
		
		// Store important things...
		this.G = G;
		this.source = inceptionDate.ordinal();
		
		// ...initialize in the constructor: only one initialization is needed...
		bidTo = new double[G.getNumberOfVertices()];
		edgeBidTo = new DirectedEdge[G.getNumberOfVertices()];
		isVertexOnQueueBid = new boolean[G.getNumberOfVertices()];
		askTo = new double[G.getNumberOfVertices()];
		edgeAskTo = new DirectedEdge[G.getNumberOfVertices()];
		isVertexOnQueueAsk = new boolean[G.getNumberOfVertices()];
		
		// ...preliminary tasks to be done
		this.findBid();
		this.findAsk();
	}
	
	/**
	 * This method is designed to get the top bid from s to v
	 * <br>
	 * TO BE NOTED: this method automatically execute the order
	 * 
	 * @param deliveryDate the delivery date of the future we are interested on
	 * @return topBid the highest bid available
	 */
	public double getBidTo(MonthDates deliveryDate) {
		pathBidTo(deliveryDate).forEach(e -> e.executeBid());
		double currentTop = bidTo[deliveryDate.ordinal()];
		findBid();
		return currentTop;
	}
	
	/**
	 * This method is designed to get the top ask from s to v
	 * <br>
	 * TO BE NOTED: this method automatically execute the order
	 * 
	 * @param deliveryDate the delivery date of the future we are interested on
	 * @return topAsk the lowest ask available
	 */
	public double getAskTo(MonthDates deliveryDate) {
		pathAskTo(deliveryDate).forEach(e -> e.executeAsk());
		double currentTop = askTo[deliveryDate.ordinal()];
		findAsk();
		return currentTop;
	}
	
	/**
	 * This method check whether a bid to the specified delivery date is possible
	 * 
	 * @param deliveryDate the delivery date of the future we are interested on
	 * @return isBidAvailable a boolean to check whether a bid is available
	 */
	public boolean hasBidTo(MonthDates deliveryDate) {
		return bidTo[deliveryDate.ordinal()] > Double.NEGATIVE_INFINITY;
	}
	
	/**
	 * This method check whether an ask to the specified delivery date is possible
	 * 
	 * @param deliveryDate the delivery date of the future we are interested on
	 * @return isAskAvailable a boolean to check whether an ask is available
	 */
	public boolean hasAskTo(MonthDates deliveryDate) {
		return askTo[deliveryDate.ordinal()] < Double.POSITIVE_INFINITY;
	}
	
	/*
	 * PRIVATE METHODS FROM NOW ON
	 */
	
	private void findBid() {
		Arrays.fill(isVertexOnQueueBid, false);
		for(int v = 0; v < bidTo.length; v++) {
			bidTo[v] = Double.NEGATIVE_INFINITY; // Initialize to the lowest possible value
		}
		bidTo[source] = 0.0;
		queueBid.add(source);
		isVertexOnQueueBid[source] = true;
		while(!queueBid.isEmpty()) {
			currentV = queueBid.poll();
			isVertexOnQueueBid[currentV] = false;
			relaxBid(currentV);
		}	
	}

	private void findAsk() {
		Arrays.fill(isVertexOnQueueAsk, false);
		for(int v = 0; v < askTo.length; v++) {
			askTo[v] = Double.POSITIVE_INFINITY; // Initialize to the greatest possible value
		}
		askTo[source] = 0.0;
		queueAsk.add(source);
		isVertexOnQueueAsk[source] = true;
		while(!queueAsk.isEmpty()) {
			currentV = queueAsk.poll();
			isVertexOnQueueAsk[currentV] = false;
			relaxAsk(currentV);
		}
	}
	
	/*
	 * This method retrieves the path from source to v (for the current topBid).
	 * This method is necessary as we need to execute the order and move to the next.
	 * We must execute the order in each future of the path.
	 */
	private Iterable<DirectedEdge> pathBidTo(MonthDates v) {
		if (hasBidTo(v)) {
			LinkedList<DirectedEdge> path = new LinkedList<>();
			for(DirectedEdge e = edgeBidTo[v.ordinal()]; e != null; e = edgeBidTo[e.from()]) {
				path.addFirst(e);
			}
			return path;
		}
		return null;
	}
	
	/*
	 * This method retrieves the path from source to v (for the current topAsk).
	 * This method is necessary as we need to execute the order and move to the next.
	 * We must execute the order in each future of the path.
	 */
	private Iterable<DirectedEdge> pathAskTo(MonthDates v) {
		if (hasAskTo(v)) {
			LinkedList<DirectedEdge> path = new LinkedList<>();
			for(DirectedEdge e = edgeAskTo[v.ordinal()]; e != null; e = edgeAskTo[e.from()]) {
				path.addFirst(e);;
			}
			return path;
		}
		return null;
	}
	
	/*
	 * For each node, it determines whether a more costly way to reach that point is
	 * possible or not. If that is possible, the cost is stored.
	 */
	private void relaxBid(int v) {
		G.getAdjacentVertices(v).forEach(e -> {
			if(bidTo[e.to()] < bidTo[v] + e.bid()) {
				bidTo[e.to()] = bidTo[v] + e.bid();
				edgeBidTo[e.to()] = e;
				if(!isVertexOnQueueBid[e.to()]) {
					queueBid.add(e.to());
					isVertexOnQueueBid[e.to()] = true;
				}
			}
		});
	}
	
	/*
	 * For each node, it determines whether a cheaper way to reach that point is
	 * possible or not. If that is possible, the cost is stored.
	 */
	private void relaxAsk(int v) {
		G.getAdjacentVertices(v).forEach(e -> {
			if(askTo[e.to()] > askTo[v] + e.ask()) {
				askTo[e.to()] = askTo[v] + e.ask();
				edgeAskTo[e.to()] = e;
				if(!isVertexOnQueueAsk[e.to()]) {
					queueAsk.add(e.to());
					isVertexOnQueueAsk[e.to()] = true;
				}
			}
		});
	}

}
