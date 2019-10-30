package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.*;

import java.util.concurrent.CountDownLatch;

/**
 * Logistic service in charge of delivering books that have been purchased to customers.
 * Handles {@link DeliveryEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LogisticsService extends MicroService {
	// current tick changed by TickBroadcasts
	private int currentTick;
	private CountDownLatch start;
	private CountDownLatch end;

	public LogisticsService(int id, CountDownLatch start, CountDownLatch end) {
		super("logisticsService" + id);
		this.start = start;
		this.end = end;
	}

	@Override
	protected void initialize() {
		// Subscribe to Terminate broadcast - sent by time service
		subscribeBroadcast(TerminateBroadcast.class, (terminateBroadcast)->{
			terminate();
			end.countDown();
		});

		// subs to tickBroadcast and definining its callback to(change current tick to TickBroadcasts tick)
		subscribeBroadcast(TickBroadcast.class, (tickEventInstance) -> {
			currentTick = tickEventInstance.getCurrent_tick();
		});

		//The responsibilities of logistics service(*i+1* executes after *i* is done):
		// 1. Acquire a vehicle - send an event to resource service
		// 2. Deliver the book
		// 3. Release the same vehicle - send an event to resource service

		//all of the responsibilities are handles at the SAME callback when DeliveryEvent is executed.
		// ----------------------------------------------------------------------------

		// 1.
		// Waiting for acquistion of a car - definition of callback : waits until a car gets free
		// Send AcquireVehicleEvent that which will be received and responded by ResourceService
		subscribeEvent(DeliveryEvent.class, (eventInstance) -> {

			Future<Future<DeliveryVehicle>> myAcquisition = sendEvent(new AcquireVehicleEvent());
			// 2.
			// Gets the customer which relates to the specific DeliveryEvent
			Customer customerToDeliver = eventInstance.getCustomerToDeliver();

			if(myAcquisition==null || myAcquisition.get()==null || myAcquisition.get().get()==null){
				complete(eventInstance,null);
				return;
			}
			//after future is ready, we take the relevant info of delivery from customer and use the acquired car
			//to commit it.
					try {
						myAcquisition.get().get().deliver(customerToDeliver.getAddress(), customerToDeliver.getDistance());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					sendEvent(new ReleaseVehicle(myAcquisition.get().get()));
			// 3.
			//finished using the car, so returing its back. no need to worry about the future because
			// we are independent of it
			// check


		});
		//end of definition DeliveryEvent callback.
		start.countDown();
	}

}
