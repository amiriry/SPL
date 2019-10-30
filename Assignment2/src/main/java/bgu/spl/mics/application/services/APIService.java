package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * APIService is in charge of the connection between a client and the store.
 * It informs the store about desired purchases using {@link BookOrderEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class APIService extends MicroService{
	LinkedList<OrderReceipt> custReciepts;
	int currentTick;
	CountDownLatch start;
	CountDownLatch end;

	//list of user orders that will be handled by the API - ordered by tick
	// The list will come from the constructor
	ConcurrentLinkedQueue<BookOrderEvent> orderSchedule;

	public APIService(String name, CountDownLatch start, CountDownLatch end, int tick, ConcurrentLinkedQueue<BookOrderEvent> orderSchedule) {
		super(name);
		this.start = start;
		this.end = end;
		this.orderSchedule = orderSchedule;
		this.currentTick = tick;
		this.custReciepts = new LinkedList<>();
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TerminateBroadcast.class, terminateBroadcast->{
			terminate();
			end.countDown();
		});

		// Subscribe to TickBroadcast and defines its callback(1,2,3)

		//generally, the callback updates the tick, and secondly tries to fetch a bookOrder

		// 1. Take tick and make it the current tick
		// 2. NOW TWO CONDITION NEED TO BE SATISFIED IN A LOOP(before orderExecution) :
		// 	(*)orderSchedule is not empty(then there are still books to be ordered)
		//  (**)current tick is at least equal to the current BOOKORDER tick
		// 3. when 2 is satisfied, it send orderEvent and waits to its future result
		//    the moment it arrives, keep looping until there is no more orders to handle such that 1,2 is true
		// ----------------------------------------------------------------------------------------------------

		// subscribing to responsibilities
		subscribeBroadcast(TickBroadcast.class, tickBroadInstance -> {
			Future<OrderReceipt> orderEventFuture;
			currentTick = tickBroadInstance.getCurrent_tick();
			while(orderSchedule.size() != 0 && currentTick >= orderSchedule.peek().getTick()) {
				BookOrderEvent boe = orderSchedule.poll();
				orderEventFuture = sendEvent(boe);

				if(orderEventFuture.get() != null)
					custReciepts.add(orderEventFuture.get());
			}
		});
		start.countDown();
		}

}
