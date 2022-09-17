package dev.roy.parreira;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.MessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

@Slf4j
public class WikimediaChangesEventHandler implements EventHandler {

  private final KafkaProducer<String, String> wikimediaChangesProducer = WikimediaChangesProducer.getProducerInstance();

  @Override
  public void onOpen() {
    log.info("Starting listen to Wikimedia Changes events");
  }

  @Override
  public void onClosed() {
    log.info("Closing Wikimedia Changes event handler and kafka producer");
    wikimediaChangesProducer.close();
  }

  @Override
  public void onMessage(String event, MessageEvent messageEvent) {
    log.info("Receiving messageEvent: {}", messageEvent.getData());

    //MessageEvent data is the message to kafka producer
    wikimediaChangesProducer.send(new ProducerRecord<>(WikimediaChangesProducer.TOPIC, messageEvent.getData()));

    log.trace("Finish send message event data to kafka");
  }

  @Override
  public void onComment(String comment) {
    log.info("On comment triggered");
  }

  @Override
  public void onError(Throwable t) {
    log.error("Error on stream reading", t);
  }
}
