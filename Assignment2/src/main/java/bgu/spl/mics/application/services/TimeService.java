package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TimeService is the global system timer There is only one instance of this micro-service.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other micro-services about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends MicroService{

	private Timer mytimer = new Timer();
	private int speed;
	private int duration;
	private AtomicInteger tickCounter;
	private CountDownLatch start;
	private CountDownLatch end;

	public TimeService(int speed, int duration, CountDownLatch start, CountDownLatch end) {
		// It is only one service - have no name - so we send the name to the constructor
		super("timeSerivce");
		this.speed = speed;
		this.duration = duration;
		this.start = start;
		this.end = end;
		// changed it to zero
		tickCounter = new AtomicInteger(1);
	}

	public int getSpeed() {
		return speed;
	}

	public int getDuration() {
		return duration;
	}

	public int getCurrentTickCounter(){
		return tickCounter.get();
	}

	@Override
	protected void initialize() {
		// should wait untill start finishes the countdown
		// so far not workinf so I've put in runner the same thing
		try {
			// wait for all services to initialize themselves
			start.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Subscribing to TeminateBroadcast
		subscribeBroadcast(TerminateBroadcast.class, message -> {
			//check
			//System.out.println("I am time service " + this.getName());
			mytimer.cancel();                                    // Cancel the schedule task
			terminate();                                        // Exit the loop inside run of current thread
			end.countDown();                                    // Decrease counter of open threads
		});



		// This is the schedule task that makes the ticks broadcasts
		// This is the main responsibility of TimeService
		mytimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {                        // Override run method of TimerTask, not MicroService!!!!!
				if (tickCounter.get() < duration) {										// where we didn't get to duration yet
					int tickToSend = tickCounter.getAndIncrement();
					sendBroadcast(new TickBroadcast(tickToSend));
				} else {// where we got to duration
					sendBroadcast(new TerminateBroadcast());	// send TerminateBroadcast
				}
			}              // end of TimerTask definition (first argument of scheduleAtFixedRate function)
		}, speed, speed);
		// first speed - time for first execution, second speed - time between each execution after
	}

}
