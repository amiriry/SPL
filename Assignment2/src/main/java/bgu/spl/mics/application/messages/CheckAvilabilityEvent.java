package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.BookInventoryInfo;

/**
 * Created by amirshkedy on 12/12/2018.
 */
public class CheckAvilabilityEvent implements Event {
    private BookInventoryInfo book;

    public CheckAvilabilityEvent(BookInventoryInfo book){
        this.book = book;
    }

    public BookInventoryInfo getBook() {
        return book;
    }
}
