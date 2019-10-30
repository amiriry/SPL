package bgu.spl.mics;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 *
 */
public class FutureTest {
    Future<Integer> myfuture = null;

    @Before
    public void setUp() throws Exception{
        myfuture = new Future<>();
    }

    /**
     * test if Future.get() give the right answer
     */
    @Test
    public void testGet() {
        //check 5 different values
        for(int i = 0; i < 4; i++) {
            myfuture.resolve(i);
            assertEquals(myfuture.get().intValue(), i);
            myfuture.hasResolved=false;
        }
    }

    /**
     * Test the getWithTimeout function
     */
    @Test
    public void testGetWithTimeout(){
        // check 5 different values
        for(int i = 0; i < 4; i++){
            Integer num=myfuture.get(50, TimeUnit.MILLISECONDS);
            assertNull(num);

            myfuture.resolve(i);
            assertEquals(myfuture.get(20, TimeUnit.MILLISECONDS).intValue(), i);
            myfuture = new Future<>();
        }
    }

    /**
     * Test if is done is checking the right values
     */
    @Test
    public void testIsDone(){
        assertFalse(myfuture.isDone());
        myfuture.resolve(3);
        assertTrue(myfuture.isDone());
    }

    /**
     * Test that resolve make the status of the future true
     */
    @Test
    public void testResolve(){
        //after adding methods will be possible, will be checked using basic queries and commands
        myfuture.resolve(5);
        assertTrue(myfuture.isDone());
    }


    @After
    public void tearDown() throws Exception{
    }

}