package bgu.spl.mics;

import bgu.spl.mics.application.messages.ReleaseVehicle;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.services.LogisticsService;
import bgu.spl.mics.application.services.ResourceService;
import bgu.spl.mics.application.services.SellingService;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

    private ConcurrentLinkedQueue<MicroService> microServicesQueue = new ConcurrentLinkedQueue<>();
    private ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<MicroService>> msgToMS = new ConcurrentHashMap<>();
    private ConcurrentHashMap<MicroService, BlockingQueue<Message>> msToQueue = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Event, Future> eventToFuture = new ConcurrentHashMap<>();
    private AtomicBoolean programHasTerminated = new AtomicBoolean(false);


    // instance holder
    private static class MessageBusHolder {
        private static MessageBusImpl instance = new MessageBusImpl() {
        };
    }

    public static MessageBusImpl getInstance() {
        return MessageBusHolder.instance;
    }

    @Override
    public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
        msgToMS.putIfAbsent(type, new ConcurrentLinkedQueue<>());
        synchronized (msgToMS.get(type)) {
            if (!msgToMS.get(type).contains(m)) {
                msgToMS.get(type).add(m);
            }
        }
    }

    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
        msgToMS.putIfAbsent(type, new ConcurrentLinkedQueue<>());
        synchronized (msgToMS.get(type)) {
            if (!msgToMS.get(type).contains(m))
                msgToMS.get(type).add(m);
        }
    }

    @Override
    public <T> void complete(Event<T> e, T result) {

        eventToFuture.get(e).resolve(result);
    }

    // Remove  and add the right microservices from the top queue of microservices that subscribe for the event
    // For round robin implementation
    @Override
    public void sendBroadcast(Broadcast b) {
        ConcurrentLinkedQueue<MicroService> interestedInBroadcast = msgToMS.get(b.getClass());
        if (interestedInBroadcast == null)
            return;
        synchronized (interestedInBroadcast) {
            for (MicroService m : interestedInBroadcast) {
                if (msToQueue.get(m) != null)
                    msToQueue.get(m).add(b);
            }
        }
    }

    @Override
    public <T> Future<T> sendEvent(Event<T> e) {

        Future<T> futureResult = new Future<>();
        eventToFuture.put(e, futureResult);
        ConcurrentLinkedQueue<MicroService> microServicesList = msgToMS.get(e.getClass());
        MicroService ms;
        synchronized (msgToMS.get(e.getClass())) {
            if (microServicesList == null || microServicesList.isEmpty()) {
                futureResult.resolve(null);
                return null;
            }
            ms = microServicesList.poll();
            microServicesList.add(ms);
        }
        synchronized (ms) {
            BlockingQueue<Message> eventsToHandle = msToQueue.get(ms);
            if(eventsToHandle==null){
                futureResult.resolve(null);
                return null;
            }
            eventsToHandle.add(e);
        }
        return futureResult;
    }


    // creates a queue for a micro service
    @Override
    public void register(MicroService m) {

        BlockingDeque<Message> hello = new LinkedBlockingDeque();
        msToQueue.put(m, hello);

    }

    // removes the queue of a micro service that is given
    @Override
    public void unregister(MicroService m) {
        BlockingQueue<Message> queueToErase;
        synchronized (m) {
            queueToErase = msToQueue.remove(m);
        }
        while (!queueToErase.isEmpty()) {
            if (queueToErase.peek() instanceof Event) {
                Event<?> event = (Event) queueToErase.poll();
                Future<?> eventToDelete = eventToFuture.get(event);
                eventToDelete.resolve(null);
            } else {
                queueToErase.poll();
            }
        }
        for (ConcurrentLinkedQueue<MicroService> val : msgToMS.values()) {
            synchronized (val) {
                val.remove(m);
            }
        }
    }

    // Each time it is called trying to find a message
    @Override
    public Message awaitMessage(MicroService m) throws InterruptedException {
        Message toSend = null;
        if (!msToQueue.containsKey(m))
            throw new IllegalStateException("The microservice " + m + " is not registered to message bus");
        else {
            toSend = msToQueue.get(m).take();

        }
        // take is a blocking method of ConcurrentLinkedQueue

        return toSend;
    }


}
