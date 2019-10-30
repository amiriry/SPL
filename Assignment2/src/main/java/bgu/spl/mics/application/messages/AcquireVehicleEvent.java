package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;

/**
 * A marker event to mark an event as AcquireVechicleEvent
 * The sender: LogisticsService send to resource holder because
 * it doesnt have a reference to it
 * The reciver: ResourceHolder gets it and try to acquire vehicle
 * with its own functions
 */
public class AcquireVehicleEvent implements Event<Future<DeliveryVehicle>> {

    public AcquireVehicleEvent(){
    }

}
