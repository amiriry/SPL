package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Selling service in charge of taking orders from customers.
 * Holds a reference to the {@link MoneyRegister} singleton of the store.
 * Handles {@link BookOrderEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class SellingService extends MicroService{
	private AtomicInteger receiptId = new AtomicInteger(0); // IDs for receipts
	private int currentTick;
	private MoneyRegister bookStoreMoneyRegister;
	private CountDownLatch start;
	private CountDownLatch end;
	// I think that there should be here a list of receipts

	public SellingService(String name, CountDownLatch start, CountDownLatch end) {
		super(name);
		// the singletone instance of money register
		bookStoreMoneyRegister = MoneyRegister.getInstance();
		this.start = start;
		this.end = end;
	}



	@Override
	protected void initialize() {
		// subscribes to the broadcast of terminate
		subscribeBroadcast(TerminateBroadcast.class, (callbackFunction) ->{
			terminate();
			end.countDown();
		});

		// subscribe to the tick broadcast
		subscribeBroadcast(TickBroadcast.class, (callBackFunctionTick) -> {
			currentTick = callBackFunctionTick.getCurrent_tick();
		});


		//subscribe bookOrderEvent and defining its callback(1-9)
		// 1. Gets BookOrderEvent from APIService
		// 2. [create a receipt and save the start time for the receipt]
		//    in addition Checks if the Client have enough money for buying the book
		// 3. if have enough: Sends CheckAvailability(will be received by InventoryService) other wise complete
		// 4. gets a future from 3 : if result is not null. if it is the case
		//    it should decrease the number of books by one and continue with the process
		//    otherwise it should complete with null
		// 5. (if the book is available on 4)Set amount of customer Credit card
		// 6. create receipt with relevant details.
		//    resolve the customers future with the receipt created.
		// 7. update my Money Register
		// 8. Send a request for delivery(will be received by logistic service)
		// 9. complete the task without any attention to the future returned by the message bus
		//-----------------------------------------------------------------------

		// 1.
		subscribeEvent(BookOrderEvent.class, (bookOrderInstance) -> {
			int processTick = this.currentTick;
				synchronized (bookOrderInstance.getBook()) {
					Customer customerIssued = bookOrderInstance.getCustomer();
					BookInventoryInfo orderedBook = bookOrderInstance.getBook();

					// 2.
					if (customerIssued.getAvailableCreditAmount() >= orderedBook.getPrice()) {
						// 3.
						Future<BookInventoryInfo> availabilityFuture = sendEvent(new CheckAvilabilityEvent(orderedBook));
						// 4.
						if (availabilityFuture.get() != null) {

							MoneyRegister.getInstance().chargeCreditCard(customerIssued, orderedBook.getPrice());

							// general information for the reciept
							String sellerName = this.getName();

							// Book information for receipt
							String bookName = orderedBook.getBookTitle();
							int bookPrice = orderedBook.getPrice();
							int clientId = customerIssued.getId();

							// ticks infotmartion
							int orderTick = bookOrderInstance.getTick();
							int issuedTick = this.currentTick;

							// create Receipt
							OrderReceipt myReceipt = new OrderReceipt(sellerName, clientId, bookName,
									bookPrice, issuedTick, orderTick, processTick);

							MoneyRegister.getInstance().file(myReceipt);

							// added the reciept to the customer - this is important - before there were no reciepts to
							// customers
							customerIssued.addReceipt(myReceipt);

							// complete event
							complete(bookOrderInstance, myReceipt);

							sendEvent(new DeliveryEvent(customerIssued, orderedBook));
						}
						// Book iss not available
						else {
							complete(bookOrderInstance, null);
						}
					}
					// Customer doesn't have enough money
					else {
						complete(bookOrderInstance, null);
					}
				}
		});
		start.countDown();
	}

}
