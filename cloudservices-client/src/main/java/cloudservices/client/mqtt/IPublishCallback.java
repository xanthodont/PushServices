package cloudservices.client.mqtt;

/**
 *
 * @author andrea
 */
public interface IPublishCallback {

    void published(String topic, byte[] message/*, boolean retained*/);
}
