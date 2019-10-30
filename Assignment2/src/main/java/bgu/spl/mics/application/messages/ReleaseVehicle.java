package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;

/**
 * Created by amirshkedy on 19/12/2018.
 */
public class ReleaseVehicle implements Event<DeliveryVehicle> {
    private DeliveryVehicle vehicleToRelease;

    public ReleaseVehicle(DeliveryVehicle vehicleToRelease){
        this.vehicleToRelease = vehicleToRelease;
    }

    public DeliveryVehicle getVehicleToRelease() {
        return vehicleToRelease;
    }
}
