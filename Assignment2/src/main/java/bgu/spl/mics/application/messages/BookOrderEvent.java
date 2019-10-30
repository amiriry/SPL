package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Message;
import bgu.spl.mics.application.passiveObjects.BookInventoryInfo;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.Inventory;

import java.io.Serializable;

/**
 * Event for BookOrders
 * BookStoreRunner creates it so it have access to Customers and the book they want to buy
 * Each API service have a list of it
 */
public class BookOrderEvent implements Event, Serializable {
    private BookInventoryInfo book;
    private Customer customer;
    private int tick;

    public BookOrderEvent(BookInventoryInfo book, Customer customer, int tick) {
        this.customer = customer;
        this.book = book;
        this.tick = tick;
    }

    public BookInventoryInfo getBook() {
        return book;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getTick() {
        return tick;
    }

}
