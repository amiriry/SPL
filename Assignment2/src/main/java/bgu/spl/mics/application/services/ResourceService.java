package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AcquireVehicleEvent;
import bgu.spl.mics.application.messages.ReleaseVehicle;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;

import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;

/**
 * ResourceService is in charge of the store resources - the delivery vehicles.
 * Holds a reference to the {@link ResourcesHolder} singleton of the store.
 * This class may not hold references for objects which it is not responsible for:
 * {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class ResourceService extends MicroService{
	private int currentTick;
	private int id;
	private CountDownLatch start;
	private CountDownLatch end;
	private ResourcesHolder resources;
	private LinkedList<Future<DeliveryVehicle>> allVehicleSent = new LinkedList<>();

	public ResourceService(int id, CountDownLatch start, CountDownLatch end) {
		super("resourceService" + id);
		this.start = start;
		this.end = end;
		this.resources = ResourcesHolder.getInstance();
	}

	@Override
	protected void initialize() {
		// Subscribe to Terminate broad casts - sent by time service
		subscribeBroadcast(TerminateBroadcast.class, (terminateBroadcast)->{
			for(Future<DeliveryVehicle> future: allVehicleSent){
				if(!future.isDone())
					future.resolve(null);
			}
			terminate();
			end.countDown();
		});

		// change current tick to TickBroadcasts tick
		subscribeBroadcast(TickBroadcast.class, (tickCallBack) -> {
			currentTick = tickCallBack.getCurrent_tick();
		});

		// subscribe to acquireVehicleEvent(sent by logistic service) and defining its callback:
		// Responsibilities of ResourceService Callback:
		// 1. Wait for a vehicle to be free
		// 2.
		// 2. After The delivery is done - release the vehicle
		//    Gets the event from
		// ----------------------------------------------------------------------------------------------------


		subscribeEvent(AcquireVehicleEvent.class, (acquireVehicInstance) -> {
			// acquireVehicle function of ResourceHolder returns future
			// future for a promised vehicle
			Future<DeliveryVehicle> vehicleFuture = new Future<>();
			vehicleFuture = resources.acquireVehicle();

			// The function should wait until there's a car = the future is done
			allVehicleSent.add(vehicleFuture);
			complete(acquireVehicInstance, vehicleFuture);
		});

		// 3.
		// This class have a reference to ResourceHolder - so It can use the release function of it
		// and release the delivery car needed

		subscribeEvent(ReleaseVehicle.class, (releaseVehicleInstance) ->{
			resources.releaseVehicle(releaseVehicleInstance.getVehicleToRelease());
			complete(releaseVehicleInstance, null);
		});

		start.countDown();
	}

}
