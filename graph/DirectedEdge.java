package graph;

/**
 * This interface is designed to represent a weighted edge.
 * <br>
 * This edge has two weight according to the circumstances: either bid or ask.
 * <br>
 * The clear reference here is to to a traded financial instrument.
 * <br>
 * The API hereby presented takes inspiration from the book "Algorithms"
 * by R. Sedgewick and K. Wayne
 * 
 * @author Guglielmo Del Sarto
 */
public interface DirectedEdge {
	
	/**
	 * Get the bid (i.e. the weight of the edge).
	 * 
	 * @return currentBid as double (-&#8734; if not available)
	 */
	public double bid();
	
	/**
	 * This method executes a single-lot bid order
	 */
	public void executeBid();
	
	/**
	 * Get the ask (i.e. the weight of the edge).
	 * 
	 * @return currentAsk as double (&#8734; if not available)
	 */
	public double ask();
	
	/**
	 * This method executes a single-lot ask order
	 */
	public void executeAsk();
	
	/**
	 * This method returns the vertex from which the edge is pointing
	 * 
	 * @return fromID the ID of the vertex
	 */
	public int from();
	
	/**
	 * This method returns the vertex to which the edge is pointing
	 * 
	 * @return toID the ID of the vertex
	 */
	public int to();

}
