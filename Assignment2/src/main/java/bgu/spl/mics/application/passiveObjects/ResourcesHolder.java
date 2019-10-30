package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.Future;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Passive object representing the resource manager.
 * You must not alter any of the given public methods of this class.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class ResourcesHolder {

	private ConcurrentLinkedQueue<Future> vehcFutures;
	private ConcurrentLinkedQueue<DeliveryVehicle> freeVech;

	private ResourcesHolder() {

		freeVech = new ConcurrentLinkedQueue<>();
		vehcFutures = new ConcurrentLinkedQueue<>();

	}
	public static class ResourcesHolderHolder{
		private static final ResourcesHolder myResourceHolder = new ResourcesHolder();
	}
	/**
     * Retrieves the single instance of this class.
     */
	public static ResourcesHolder getInstance() {
		return ResourcesHolderHolder.myResourceHolder;
	}
	
	/**
     * Tries to acquire a vehicle and gives a future object which will
     * resolve to a vehicle.
     * <p>
     * @return 	{@link Future<DeliveryVehicle>} object which will resolve to a 
     * 			{@link DeliveryVehicle} when completed.   
     */
	public synchronized Future<DeliveryVehicle> acquireVehicle() {
		Future<DeliveryVehicle> currFuture = new Future<>();


		if(!freeVech.isEmpty()){

			synchronized (freeVech){
				currFuture.resolve(freeVech.poll());
			}
		}


		else{
			vehcFutures.add(currFuture);
		}
		return currFuture;
	}
	
	/**
     * Releases a specified vehicle, opening it again for the possibility of
     * acquisition.
     * <p>
     * @param vehicle	{@link DeliveryVehicle} to be released.
     */
	public synchronized void releaseVehicle(DeliveryVehicle vehicle) {
		if(vehcFutures.isEmpty())
			freeVech.add(vehicle);
		else
			vehcFutures.poll().resolve(vehicle);
	}
	
	/**
     * Receives a collection of vehicles and stores them.
     * <p>
     * @param vehicles	Array of {@link DeliveryVehicle} instances to store.
     */
	public void load(DeliveryVehicle[] vehicles) {
		for(DeliveryVehicle vehicleToAdd: vehicles){
			this.freeVech.add(vehicleToAdd);
		}
	}
}
