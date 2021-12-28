package graph;

/**
 * This interface is designed to represent a directed, weighted graph.
 * <br>
 * The API hereby presented takes inspiration from the book "Algorithms"
 * by R. Sedgewick and K. Wayne
 * 
 * @author Guglielmo Del Sarto
 */
public interface WeightedDigraph {
	
	/**
	 * This method gives back the number of vertices in the graph
	 * 
	 * @return numberOfVertices as integer value
	 */
	public int getNumberOfVertices();
	
	/**
	 * This method adds an edge to the graph
	 * 
	 * @param edgeToBeAdded the edge to be added to the graph
	 */
	public void addEdge(DirectedEdge edgeToBeAdded);
	
	/**
	 * This method is designed to get adjacent vertices. This is done
	 * returning edges pointing from the specified vertex
	 * 
	 * @param vertexID the vertex of which we require the adj
	 * @return edgeSetIterable a collection of edges pointing from vertexID
	 */
	Iterable<DirectedEdge> getAdjacentVertices(int vertexID);

}
