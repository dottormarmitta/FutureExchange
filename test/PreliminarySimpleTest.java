package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import finance.Exchange;
import finance.Future;
import graph.BellmanFordAlgorithm;
import graph.WeightedDigraph;


/**
 * This class is designed to test the correctness of the book.
 * <br>
 * It creates a .csv file with the complete Order Book for the time spread
 * <br>
 * June 2023 &#8594; December 2023"
 * 
 * @author Guglielmo Del Sarto
 */
public class PreliminarySimpleTest {

	public static void main(String[] args) throws IOException {
		
		WeightedDigraph exchange = new Exchange(6);
		
		// Jun-Dec
		double[] bids = {1.04, 0.87};
		long[] bidLot = {100, 1};
		double[] asks = {1.14, 1.16};
		long[] askLot = {10, 100};
		exchange.addEdge(new Future(bids, bidLot, asks, askLot, 0, 4));
		exchange.addEdge(new Future(bids, bidLot, asks, askLot, 4, 0));
		
		// Jun-Jul
		bids = new double[] {0.20, 0.19};
		bidLot = new long[] {2, 100};
		asks = new double[] {0.21, 0.25};
		askLot = new long[] {10, 50};
		exchange.addEdge(new Future(bids, bidLot, asks, askLot, 0, 1));
		exchange.addEdge(new Future(bids, bidLot, asks, askLot, 1, 0));
		
		// Jul-Sep
		bids = new double[] {0.45, 0.35};
		bidLot = new long[] {100, 150};
		asks = new double[] {0.46, 0.50};
		askLot = new long[] {5, 100};
		exchange.addEdge(new Future(bids, bidLot, asks, askLot, 1, 3));
		exchange.addEdge(new Future(bids, bidLot, asks, askLot, 3, 1));
		
		// Jul-Dec
		bids = new double[] {0.87, 0.70};
		bidLot = new long[] {1, 150};
		asks = new double[] {0.90, 0.95};
		askLot = new long[] {10, 100};
		exchange.addEdge(new Future(bids, bidLot, asks, askLot, 1, 4));
		exchange.addEdge(new Future(bids, bidLot, asks, askLot, 4, 1));
		
		// Jul23-Jul24
		bids = new double[] {1.97, 1.80};
		bidLot = new long[] {100, 150};
		asks = new double[] {1.98, 2.05};
		askLot = new long[] {7, 100};
		exchange.addEdge(new Future(bids, bidLot, asks, askLot, 1, 5));
		exchange.addEdge(new Future(bids, bidLot, asks, askLot, 5, 1));
		
		// Dec23-Jul24
		bids = new double[] {1.05, 1.00};
		bidLot = new long[] {100, 150};
		asks = new double[] {1.11, 1.50};
		askLot = new long[] {10, 100};
		exchange.addEdge(new Future(bids, bidLot, asks, askLot, 4, 5));
		exchange.addEdge(new Future(bids, bidLot, asks, askLot, 5, 4));
		
		// Sep-Dec
		bids = new double[] {0.40, 0.15};
		bidLot = new long[] {100, 150};
		asks = new double[] {0.42, 0.50};
		askLot = new long[] {10, 100};
		exchange.addEdge(new Future(bids, bidLot, asks, askLot, 3, 4));
		exchange.addEdge(new Future(bids, bidLot, asks, askLot, 4, 3));
		BellmanFordAlgorithm bf = new BellmanFordAlgorithm(exchange, 0);
		
		// Change location at user convenience
		File file = new File("/Users/guglielmo/Desktop/graphExchange.csv");
		BufferedWriter outputWriter = new BufferedWriter(new FileWriter(file));
		outputWriter.write("BID" + "," + "ASK");
		outputWriter.newLine();
		
		while(bf.hasBidTo(4) || bf.hasAskTo(4)) {
			if(bf.hasBidTo(4) && bf.hasAskTo(4)) {
				outputWriter.write(Double.toString(bf.getBidTo(4)) + "," +
						Double.toString(bf.getAskTo(4)));
				outputWriter.newLine();
			} else if(bf.hasBidTo(4)) {
				outputWriter.write(Double.toString(bf.getBidTo(4)) + "," +
						" ");
				outputWriter.newLine();
			} else {
				outputWriter.write(" " + "," +
						Double.toString(bf.getAskTo(4)));
				outputWriter.newLine();
			}
		}
		
		outputWriter.flush();  
		outputWriter.close();
	}

}
