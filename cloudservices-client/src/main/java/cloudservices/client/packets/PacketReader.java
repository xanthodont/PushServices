package cloudservices.client.packets;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import cloudservices.client.ClientService;

public class PacketReader {
	private Thread readerThread;
    private ExecutorService listenerExecutor;
    private ClientService client;

    private final BlockingQueue<Packet> queue;
    volatile boolean done;
    
    public PacketReader(ClientService client) {
    	this.queue = new ArrayBlockingQueue<Packet>(500, true);
    	this.client = client;
    	init();
    }
    
    private void init() {
    	done = false;

        readerThread = new Thread() {
            public void run() {
                parsePackets(this);
            }
        };
        readerThread.setName("Packet Reader");
        readerThread.setDaemon(true);
        
        listenerExecutor = Executors.newSingleThreadExecutor(new ThreadFactory() {
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable,
                        "Smack Listener Processor");
                thread.setDaemon(true);
                return thread;
            }
        });
    }
    
    /**
     * 解析包
     * 注：将Packet解析为我们自定义的Packet
     * @param thread
     */
    private void parsePackets(Thread thread) {
    	do {
    		Packet packet = nextPacket();
    		System.out.printf("Reader: has packet -- %s\n", packet);
    		switch (packet.getPacketType()) {
	    		case 0: // text
	    			TextPacket tp = new TextPacket(packet); 
	    			System.out.printf("Packet Reader:%s\n", tp);
	    			break;
	    		case 1: // file
	    			
	    			break;
	    		default: 
	    			break;
    		}
    	} while (!done && thread == readerThread);
    }
    
    /**
     * 处理Packet
     * @param packet
     */
    private void processPacket(Packet packet) {
        if (packet == null) {
            return;
        }

        // 遍历所有的收集器，选择其中合适的进行处理。
        /*
        for (PacketCollector collector: connection.getPacketCollectors()) {
            collector.processPacket(packet);
        }*/

        // 将进来的packet投递给监听器
        listenerExecutor.submit(new ListenerNotification(packet));
    }
    
    public void startup() {
    	readerThread.start();
    }
    
    public void shutdown() {
    	done = true;

        // Shut down the listener executor.
        listenerExecutor.shutdown();
    }
    
    public void putPacket(Packet packet) {
    	if (!done) {
            try {
            	// packet_log 接收到消息
            	System.out.printf("Packet: %s\n", packet);
                queue.put(packet);
            }
            catch (InterruptedException ie) {
                ie.printStackTrace();
                return;
            }
            synchronized (queue) {
                queue.notifyAll();
            }
        }
    }
    
    private Packet nextPacket() {
    	Packet packet = null;
        // Wait until there's a packet or we're done.
        while (!done && (packet = queue.poll()) == null) {
            try {
                synchronized (queue) {
                    queue.wait();
                }
            }
            catch (InterruptedException ie) {
                // Do nothing
            }
        }
        return packet;
    }
    
    private class ListenerNotification implements Runnable {

        private Packet packet;

        public ListenerNotification(Packet packet) {
            this.packet = packet;
        }

        public void run() {
        	/*
            for (ListenerWrapper listenerWrapper : connection.recvListeners.values()) {
                try {
                    listenerWrapper.notifyListener(packet);
                } catch (Exception e) {
                    System.err.println("Exception in packet listener: " + e);
                    e.printStackTrace();
                }
            }*/
        }
    }
}
