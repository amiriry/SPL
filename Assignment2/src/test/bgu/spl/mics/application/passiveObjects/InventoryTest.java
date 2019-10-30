package bgu.spl.mics.application.passiveObjects;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InventoryTest {


    private Inventory testInvent;
    private BookInventoryInfo[] testBookInventoryInfo;


    /**
     * setup books for an inventory that is going to be checked
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        BookInventoryInfo book1 = new BookInventoryInfo("superman 1", 20, 1);
        BookInventoryInfo book2 = new BookInventoryInfo("superman 2", 40, 2);
        BookInventoryInfo book3 = new BookInventoryInfo("superman 3", 60, 3);
        testBookInventoryInfo = new BookInventoryInfo[]{book1, book2, book3};
        testInvent = Inventory.getInstance();
        testInvent.load(testBookInventoryInfo);
    }

    /**
     * Checks if getinstance is realy giving an instance of the inventory
     * And if we try to take instance again we get the same instance
     */
    @Test
    public void testGetInstance() {
        assertNotNull(testInvent);
        Inventory testInventB = Inventory.getInstance();
        assertEquals(testInvent, testInventB);
    }

    /**
     * Checks if load loaded the books correctly
     */
    @Test
    public void testLoad(){
        for(int i =0; i < 3; i++){
            assertEquals(testBookInventoryInfo[i].getAmountInInventory(), i+1);
            assertEquals(testBookInventoryInfo[i].getPrice(), (i+1)*20);
        }
    }

    @Test
    public void testTakeThreadSafety(){
    }

    /**
     * Test trying to take a book that doesn't exist and an empty book
     */
    @Test
    public void testTakeEmpty(){
        testInvent.load(new BookInventoryInfo[0]);
        assertEquals(testInvent.take(""), OrderResult.NOT_IN_STOCK);
        assertEquals(testInvent.take("spl"), OrderResult.NOT_IN_STOCK);
    }

    /**
     * Test if take takes the right books as to what string it is given and that
     * the result of this orders are SUCCESSFULLY_TAKEN
     * Tests if try taking a book that doesn't exist gives NOT_IN_STOCK
     */
    @Test
    public void testTake(){
        assertEquals(testInvent.take("harry potter"), OrderResult.NOT_IN_STOCK);
        assertEquals(testInvent.take("superman 1"), OrderResult.SUCCESSFULLY_TAKEN);
        assertEquals(testInvent.take("superman 2"), OrderResult.SUCCESSFULLY_TAKEN);
        assertEquals(testInvent.take("superman 3"), OrderResult.SUCCESSFULLY_TAKEN);
        for(int i =0; i < 3; i++){
            assertEquals(testBookInventoryInfo[i].getAmountInInventory(), i);
        }

        assertEquals(testInvent.take("superman 1"), OrderResult.NOT_IN_STOCK);
    }

    /**
     * Test If Check Availability of books that doesn't exist gives the right answer
     * and check if it gives the price of a cbook that do exists
     */
    @Test
    public void testCheckAvailabiltyAndGetPrice() {
        // test - thread safety
        assertEquals(testInvent.checkAvailabiltyAndGetPrice(""),-1);
        assertEquals(testInvent.checkAvailabiltyAndGetPrice("spl"),-1);
        assertEquals(testInvent.checkAvailabiltyAndGetPrice("superman 1"), 20);

        // test after taken
        testInvent.take("superman 1");
        assertEquals(testInvent.checkAvailabiltyAndGetPrice("superman 1"), -1);
    }

    @After
    public void tearDown(){

    }


}