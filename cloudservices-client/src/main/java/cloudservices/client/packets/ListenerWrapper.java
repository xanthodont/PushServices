package cloudservices.client.packets;

public class ListenerWrapper {
	private PacketListener packetListener;
    private PacketFilter packetFilter;

    /**
     * Create a class which associates a packet filter with a listener.
     * 
     * @param packetListener the packet listener.
     * @param packetFilter the associated filter or null if it listen for all packets.
     */
    public ListenerWrapper(PacketListener packetListener, PacketFilter packetFilter) {
        this.packetListener = packetListener;
        this.packetFilter = packetFilter;
    }

    /**
     * Notify and process the packet listener if the filter matches the packet.
     * 
     * @param packet the packet which was sent or received.
     */
    public void notifyListener(Packet packet) {
        if (packetFilter == null || packetFilter.accept(packet)) {
            packetListener.processPacket(packet);
        }
    }
}
