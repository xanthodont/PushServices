package cloudservices.client.packets;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import cloudservices.client.ClientService;
import cloudservices.client.IClientService;

public class PacketWriter {
	private Thread writerThread;
    private ClientService client;
    private final BlockingQueue<Packet> queue;
    volatile boolean done;
    
    public PacketWriter(ClientService client) {
    	this.queue = new ArrayBlockingQueue<Packet>(500, true);
    	this.client = client;
    	init();
    }
    
    protected void init() {
        done = false;

        writerThread = new Thread() {
            public void run() {
                writePackets(this);
            }
        };
        writerThread.setName("Packet Writer");
        writerThread.setDaemon(true);
    }
    
    private void writePackets(Thread thisThread) {
    	try {
            // Write out packets from the queue.
            while (!done && (writerThread == thisThread)) {
                Packet packet = nextPacket();
                if (packet != null) {
                    client.getActualClient().send(packet);
                }
            }
            // Flush out the rest of the queue. If the queue is extremely large, it's possible
            // we won't have time to entirely flush it before the socket is forced closed
            // by the shutdown process.
            try {
                while (!queue.isEmpty()) {
                    Packet packet = queue.remove();
                    //writer.write(packet.toXML());
                }
                //writer.flush();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            // Delete the queue contents (hopefully nothing is left).
            queue.clear();

            // Close the stream.
            try {
                //writer.write("</stream:stream>");
                //writer.flush();
            }
            catch (Exception e) {
                // Do nothing
            }
            finally {
                try {
                    //writer.close();
                }
                catch (Exception e) {
                    // Do nothing
                }
            }
        }
        catch (Exception ioe) {
            // The exception can be ignored if the the connection is 'done'
            // or if the it was caused because the socket got closed
        	/*
            if (!(done || connection.isSocketClosed())) {
                done = true;
                // packetReader could be set to null by an concurrent disconnect() call.
                // Therefore Prevent NPE exceptions by checking packetReader.
                if (connection.packetReader != null) {
                        connection.notifyConnectionError(ioe);
                }
            }*/
        }
    }
    
    public void sendPacket(Packet packet) {
    	if (!done) {
            try {
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
    
    public void startup() {
        writerThread.start();
    }

    public void shutdown() {
    	done = true;
        synchronized (queue) {
            queue.notifyAll();
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
}
