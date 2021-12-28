package graph;

import java.util.LinkedList;
import java.util.Queue;

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
	int w;
	
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
	 * This constructur allows the user to build the class.
	 * Two parameters have to be passed
	 * 
	 * @param G a directed graph
	 * @param inceptionDate the source inception date we are interested on
	 */
	public BellmanFordAlgorithm(WeightedDigraph G, int inceptionDate) {
		
		// Store important things...
		this.G = G;
		this.source = inceptionDate;
		
		// ...initialize in the constructor: only one is needed...
		bidTo = new double[G.getNumberOfVertices()];
		edgeBidTo = new DirectedEdge[G.getNumberOfVertices()];
		askTo = new double[G.getNumberOfVertices()];
		edgeAskTo = new DirectedEdge[G.getNumberOfVertices()];
		
		// ...preliminary tasks to be done
		this.findBid();
		this.findAsk();
	}
	
	/**
	 * This method is designed to get the top bid from s to v
	 * <br>
	 * TO BE NOTED: this method automatically execute the order
	 * 
	 * @param deliveryDateID the ID of the vertex associated to delivery date
	 * @return topBid the highest bid available
	 */
	public double getBidTo(int deliveryDateID) {
		for(DirectedEdge e : pathBidTo(deliveryDateID)) {
			e.executeBid();
		}
		double currentTop = bidTo[deliveryDateID];
		findBid();
		return currentTop;
	}
	
	/**
	 * This method is designed to get the top ask from s to v
	 * <br>
	 * TO BE NOTED: this method automatically execute the order
	 * 
	 * @param deliveryDateID the ID of the vertex associated to delivery date
	 * @return topAsk the lowest ask available
	 */
	public double getAskTo(int deliveryDateID) {
		for(DirectedEdge e : pathAskTo(deliveryDateID)) {
			e.executeAsk();
		}
		double currentTop = askTo[deliveryDateID];
		findAsk();
		return currentTop;
	}
	
	/**
	 * This method check whether a bid to the specified delivery date is possible
	 * 
	 * @param deliveryDateID the ID of the vertex associated to delivery date
	 * @return isBidAvailable a boolean to check whether a bid is available
	 */
	public boolean hasBidTo(int deliveryDateID) {
		return bidTo[deliveryDateID] > Double.NEGATIVE_INFINITY;
	}
	
	/**
	 * This method check whether an ask to the specified delivery date is possible
	 * 
	 * @param deliveryDateID the ID of the vertex associated to delivery date
	 * @return isAskAvailable a boolean to check whether an ask is available
	 */
	public boolean hasAskTo(int deliveryDateID) {
		return askTo[deliveryDateID] < Double.POSITIVE_INFINITY;
	}
	
	/*
	 * PRIVATE METHODS FROM NOW ON
	 */
	
	private void findBid() {
		isVertexOnQueueBid = new boolean[G.getNumberOfVertices()];
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
		isVertexOnQueueAsk = new boolean[G.getNumberOfVertices()];
		for(int v = 0; v < askTo.length; v++) {
			askTo[v] = Double.POSITIVE_INFINITY;
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
	
	private Iterable<DirectedEdge> pathBidTo(int v) {
		if (hasBidTo(v)) {
			LinkedList<DirectedEdge> path = new LinkedList<>();
			for(DirectedEdge e = edgeBidTo[v]; e != null; e = edgeBidTo[e.from()]) {
				path.addFirst(e);
			}
			return path;
		}
		return null;
	}
	
	private Iterable<DirectedEdge> pathAskTo(int v) {
		if (hasAskTo(v)) {
			LinkedList<DirectedEdge> path = new LinkedList<>();
			for(DirectedEdge e = edgeAskTo[v]; e != null; e = edgeAskTo[e.from()]) {
				path.addFirst(e);;
			}
			return path;
		}
		return null;
	}
	
	private void relaxBid(int v) {
		for(DirectedEdge e : G.getAdjacentVertices(v)) {
			w = e.to();
			if(bidTo[w] < bidTo[v] + e.bid()) {
				bidTo[w] = bidTo[v] + e.bid();
				edgeBidTo[w] = e;
				if(!isVertexOnQueueBid[w]) {
					queueBid.add(w);
					isVertexOnQueueBid[w] = true;
				}
			}
		}
	}
	
	private void relaxAsk(int v) {
		for(DirectedEdge e : G.getAdjacentVertices(v)) {
			w = e.to();
			if(askTo[w] > askTo[v] + e.ask()) {
				askTo[w] = askTo[v] + e.ask();
				edgeAskTo[w] = e;
				if(!isVertexOnQueueAsk[w]) {
					queueAsk.add(w);
					isVertexOnQueueAsk[w] = true;
				}
			}
		}
	}

}
