package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import calendar.MonthDates;
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
		
		WeightedDigraph exchange = new Exchange(MonthDates.size());
		
		// Jun-Dec
		double[] bids = {1.04, 0.87};
		long[] bidLot = {100, 1};
		double[] asks = {1.14, 1.16};
		long[] askLot = {10, 100};
		exchange.addEdge(new Future(bids, bidLot, asks, askLot, MonthDates.Jun23, MonthDates.Dec23));
		exchange.addEdge(new Future(bids, bidLot, asks, askLot, MonthDates.Dec23, MonthDates.Jun23));
		
		// Jun-Jul
		bids = new double[] {0.20, 0.19};
		bidLot = new long[] {2, 100};
		asks = new double[] {0.21, 0.25};
		askLot = new long[] {10, 50};
		exchange.addEdge(new Future(bids, bidLot, asks, askLot, MonthDates.Jun23, MonthDates.Jul23));
		exchange.addEdge(new Future(bids, bidLot, asks, askLot, MonthDates.Jul23, MonthDates.Jun23));
		
		// Jul-Sep
		bids = new double[] {0.45, 0.35};
		bidLot = new long[] {100, 150};
		asks = new double[] {0.46, 0.50};
		askLot = new long[] {5, 100};
		exchange.addEdge(new Future(bids, bidLot, asks, askLot, MonthDates.Jul23, MonthDates.Sep23));
		exchange.addEdge(new Future(bids, bidLot, asks, askLot, MonthDates.Sep23, MonthDates.Jul23));
		
		// Jul-Dec
		bids = new double[] {0.87, 0.70};
		bidLot = new long[] {1, 150};
		asks = new double[] {0.90, 0.95};
		askLot = new long[] {10, 100};
		exchange.addEdge(new Future(bids, bidLot, asks, askLot, MonthDates.Jul23, MonthDates.Dec23));
		exchange.addEdge(new Future(bids, bidLot, asks, askLot, MonthDates.Dec23, MonthDates.Jul23));
		
		// Jul23-Jul24
		bids = new double[] {1.97, 1.80};
		bidLot = new long[] {100, 150};
		asks = new double[] {1.98, 2.05};
		askLot = new long[] {7, 100};
		exchange.addEdge(new Future(bids, bidLot, asks, askLot, MonthDates.Jul23, MonthDates.Jul24));
		exchange.addEdge(new Future(bids, bidLot, asks, askLot, MonthDates.Jul24, MonthDates.Jul23));
		
		// Dec23-Jul24
		bids = new double[] {1.05, 1.00};
		bidLot = new long[] {100, 150};
		asks = new double[] {1.11, 1.50};
		askLot = new long[] {10, 100};
		exchange.addEdge(new Future(bids, bidLot, asks, askLot, MonthDates.Dec23, MonthDates.Jul24));
		exchange.addEdge(new Future(bids, bidLot, asks, askLot, MonthDates.Jul24, MonthDates.Dec23));
		
		// Sep23-Dec23
		bids = new double[] {0.40, 0.15};
		bidLot = new long[] {100, 150};
		asks = new double[] {0.42, 0.50};
		askLot = new long[] {10, 100};
		exchange.addEdge(new Future(bids, bidLot, asks, askLot, MonthDates.Sep23, MonthDates.Dec23));
		exchange.addEdge(new Future(bids, bidLot, asks, askLot, MonthDates.Dec23, MonthDates.Sep23));
		
		// Define the wanted dates:
		MonthDates inceptionDate = MonthDates.Jun23;
		MonthDates deliveryDate = MonthDates.Dec23;
		
		// Create the bellman-ford algorithm structure
		BellmanFordAlgorithm bf = new BellmanFordAlgorithm(exchange, inceptionDate);
		
		// Write the .csv file
		String storePath = "/Users/guglielmo/Desktop/Exchange_graph/JPM_Exchange/src/test/results/book.csv";
		File file = new File(storePath);
		BufferedWriter outputWriter = new BufferedWriter(new FileWriter(file));
		outputWriter.write("BID" + "," + "ASK");
		outputWriter.newLine();
		while(bf.hasBidTo(deliveryDate) || bf.hasAskTo(deliveryDate)) {
			if(bf.hasBidTo(deliveryDate) && bf.hasAskTo(deliveryDate)) {
				outputWriter.write(Double.toString(bf.getBidTo(deliveryDate)) + "," +
						Double.toString(bf.getAskTo(deliveryDate)));
				outputWriter.newLine();
			} else if(bf.hasBidTo(deliveryDate)) {
				outputWriter.write(Double.toString(bf.getBidTo(deliveryDate)) + "," +
						" ");
				outputWriter.newLine();
			} else {
				outputWriter.write(" " + "," +
						Double.toString(bf.getAskTo(deliveryDate)));
				outputWriter.newLine();
			}
		}
		
		outputWriter.flush();  
		outputWriter.close();
		
	}

}
