package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.CheckAvilabilityEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.OrderResult;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;

import java.util.concurrent.CountDownLatch;

/**
 * InventoryService is in charge of the book inventory and stock.
 * Holds a reference to the {@link Inventory} singleton of the store.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */

public class InventoryService extends MicroService{
	private int currentTick;
	private Inventory bookStoreInventory;
	private CountDownLatch start;
	private CountDownLatch end;



	public InventoryService(CountDownLatch start, CountDownLatch end) {
		super("InventoryService");
		this.bookStoreInventory = Inventory.getInstance();
		this.start = start;
		this.end = end;
	}

	@Override
	protected void initialize() {
		// subscribe to terminate broadcast - sent by timer service when duration has passed
		subscribeBroadcast(TerminateBroadcast.class, (callbackFunction )->{
			terminate();
			end.countDown();
		});

		// when getting a tick broadcast - assigning the currentTick of the service
		// with the tick of given in TickBroadcast
		subscribeBroadcast(TickBroadcast.class, (TBInstance) -> {
			this.currentTick = TBInstance.getCurrent_tick();
		});

        //subscribing to checkAvailability and defining its callBack
		// callback Responsibilities:
		// 1. CheckAvailability of a book - as a response to checkAvailability event
		//    that is sent by sellingService
		// ----------------------------------------------------------------------------------------------------
		subscribeEvent(CheckAvilabilityEvent.class, (checkAvailEvent)-> {
			// Check if the book is available
			OrderResult bookStatus = bookStoreInventory.take(checkAvailEvent.getBook().getBookTitle());
			if(bookStatus == OrderResult.SUCCESSFULLY_TAKEN) {
				// If it is available complete the event - which puts the book name in the future
				this.complete(checkAvailEvent, checkAvailEvent.getBook());
			}
			else
				this.complete(checkAvailEvent, null);
		});

		start.countDown();
	}


}
