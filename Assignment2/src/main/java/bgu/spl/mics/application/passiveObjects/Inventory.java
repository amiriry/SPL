package bgu.spl.mics.application.passiveObjects;



import bgu.spl.mics.application.Printer;

import java.awt.print.Book;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing the store inventory.
 * It holds a collection of {@link BookInventoryInfo} for all the
 * books in the store.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Inventory implements Serializable{


	private ConcurrentHashMap<BookInventoryInfo, Integer> booksInventory;
	private static AtomicInteger at = new AtomicInteger(0);

	private Inventory() {
		booksInventory = new ConcurrentHashMap<>();
	}
	public static class InventoryHolder{
		private static Inventory myInv = new Inventory();
	}

	/**
     * Retrieves the single instance of this class.
     */
	public static Inventory getInstance() {
		return InventoryHolder.myInv;
	}


	
	/**
     * Initializes the store inventory. This method adds all the items given to the store
     * inventory.
     * <p>
     * @param inventory 	Data structure containing all data necessary for initialization
     * 						of the inventory.
     */
	public void load (BookInventoryInfo[ ] inventory ) {
		for (BookInventoryInfo book: inventory) {
			int bookIndex = at.getAndIncrement();
			this.booksInventory.put(book, bookIndex);
		}
	}
	
	/**
     * Attempts to take one book from the store.
     * <p>
     * @param book 		Name of the book to take from the store
     * @return 	an {@link Enum} with options NOT_IN_STOCK and SUCCESSFULLY_TAKEN.
     * 			The first should not change the state of the inventory while the 
     * 			second should reduce by one the number of books of the desired type.
     */
	public OrderResult take (String book) {
		OrderResult currentResult;
		synchronized (book) {
			currentResult = OrderResult.NOT_IN_STOCK;

			// going over all books in hashmap
			for (Map.Entry<BookInventoryInfo, Integer> entry : booksInventory.entrySet()) {
				BookInventoryInfo currentBook = entry.getKey();

				// Check if the current book have the same name
				if (currentBook.getBookTitle().equals(book)) {
					// Check if there are books in the store of that name
					if (currentBook.getAmountInInventory() > 0) {
						// If there is we can order one - so the result is successfully taken
						currentResult = OrderResult.SUCCESSFULLY_TAKEN;
						// And change the amount in the store - decrease by one
						currentBook.tookOneBook();
					}
				}
			}
		}
		return currentResult;
	}
	
	/**
     * Checks if a certain book is available in the inventory.
     * <p>
     * @param book 		Name of the book.
     * @return the price of the book if it is available, -1 otherwise.
     */
	public int checkAvailabiltyAndGetPrice(String book) {
		int price = -1;

		// changed the for loop - and need to check about getAmount method and synchronized
		for(Map.Entry<BookInventoryInfo, Integer> entry: booksInventory.entrySet()){
			BookInventoryInfo currentBook = entry.getKey();
			if(currentBook.getBookTitle() == book){
				if(currentBook.getAmountInInventory() > 0){
					price = currentBook.getPrice();
				}
			}
		}
		return price;
	}

	/**
     * 
     * <p>
     * Prints to a file name @filename a serialized object HashMap<String,Integer> which is a 
     * Map of all the books in the inventory. The keys of the Map (type {@link String})
     * should be the titles of the books while the values (type {@link Integer}) should be
     * their respective available amount in the inventory. 
     * This method is called by the main method in order to generate the output.
     */
	public void printInventoryToFile(String filename){
		// put inventory into a serializable object
		HashMap<String, Integer> listToPrint = new HashMap<>();
		for (BookInventoryInfo book : booksInventory.keySet()) {
			listToPrint.put(book.getBookTitle(), book.getAmountInInventory());
		}
		Printer.print(listToPrint, filename);
	}
}
