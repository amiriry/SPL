package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.BookInventoryInfo;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;

/**
 * Created by amirshkedy on 12/12/2018.
 */
public class DeliveryEvent implements Event {
    private Customer customerToDeliver;
    private BookInventoryInfo bookToDeliver;

    public DeliveryEvent(Customer cutomerToDeliver, BookInventoryInfo bookToDeliver){
        this.customerToDeliver = cutomerToDeliver;
        this.bookToDeliver = bookToDeliver;
    }

    public Customer getCustomerToDeliver() {
        return customerToDeliver;
    }

    public BookInventoryInfo getBookToDeliver() {
        return bookToDeliver;
    }

}
