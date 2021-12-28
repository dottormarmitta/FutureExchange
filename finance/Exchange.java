package finance;

import java.util.ArrayList;
import java.util.List;
import graph.DirectedEdge;
import graph.WeightedDigraph;

/**
 * This class builds an exchange for time spreads. It does so implementing 
 * {@link: WeightedDigraph} interface. Indeed, an exchange is a graph, where
 * months are veritces and futures the edges.
 * 
 * @author Guglielmo Del Sarto
 */
public class Exchange implements WeightedDigraph {
	
	private final int V;
	private List<List<DirectedEdge>> adj;
	
	/**
	 * This constructor builds an exchange starting from the number of months
	 *  
	 * @param numberOfMonths the number of months available
	 */
	public Exchange(int numberOfMonths) {
		this.V = numberOfMonths;
		adj = new ArrayList<List<DirectedEdge>>();
		for(int v = 0; v < V; v++) {
			adj.add(new ArrayList<DirectedEdge>());
		}
	}

	@Override
	public int getNumberOfVertices() {
		return V;
	}

	@Override
	public void addEdge(DirectedEdge e) {
		adj.get(e.from()).add(e);
	}

	@Override
	public Iterable<DirectedEdge> getAdjacentVertices(int vertex) {
		return adj.get(vertex);
	}

}
