package bgu.spl.mics.application.passiveObjects;


import bgu.spl.mics.application.Printer;
import bgu.spl.mics.application.services.SellingService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive object representing the store finance management. 
 * It should hold a list of receipts issued by the store.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class MoneyRegister implements Serializable{

	private int totalEarnings = 0;
	private AtomicInteger recieptId = new AtomicInteger(0);
	private ConcurrentLinkedQueue<OrderReceipt> sellingServiceToReceipts;


	private MoneyRegister() {
		sellingServiceToReceipts = new ConcurrentLinkedQueue<>();
	}

    public static class MoneyRegisterHolder{
		private static MoneyRegister myMoneyRegister = new MoneyRegister();
	}

	/**
     * Retrieves the single instance of this class.
     */
	public static MoneyRegister getInstance() {
		return MoneyRegisterHolder.myMoneyRegister;
	}
	
	/**
     * Saves an order receipt in the money register.
     * <p>   
     * @param r		The receipt to save in the money register.
     */
	public void  file (OrderReceipt r) {
		// each order gets its own id
		r.setOrderId(recieptId.getAndIncrement());
		// Add the reciept sent to the selling service list of reciepts
		sellingServiceToReceipts.add(r);
	}
	
	/**
     * Retrieves the current total earnings of the store.  
     */
	public int getTotalEarnings() {
		return totalEarnings;
	}
	
	/**
     * Charges the credit card of the customer a certain amount of money.
     * <p>
     * @param amount 	amount to charge
     */
	public void chargeCreditCard(Customer c, int amount) {
		c.setAvailableAmountInCreditCard(c.getAvailableCreditAmount()-amount);
		totalEarnings = totalEarnings + amount;
	}
	
	/**
     * Prints to a file named @filename a serialized object List<OrderReceipt> which holds all the order receipts 
     * currently in the MoneyRegister
     * This method is called by the main method in order to generate the output.
     */
	public void printOrderReceipts(String filename) {
		LinkedList<OrderReceipt> ordersToPrint = new LinkedList<>();
		for (OrderReceipt reciept : sellingServiceToReceipts) {
			ordersToPrint.add(reciept);
		}
		Printer.print(ordersToPrint, filename);
	}

}
