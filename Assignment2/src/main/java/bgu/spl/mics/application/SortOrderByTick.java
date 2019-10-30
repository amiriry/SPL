package bgu.spl.mics.application;

import bgu.spl.mics.application.messages.BookOrderEvent;

import java.util.Comparator;

/**
 * Created by amirshkedy on 17/12/2018.
 * I used a built in comparator of java for comparing per function
 * So I think we should not use this
 */
public class SortOrderByTick implements Comparator<BookOrderEvent> {
    @Override
    public int compare(BookOrderEvent order1, BookOrderEvent order2) {
        return order2.getTick() - order1.getTick();
    }
}
