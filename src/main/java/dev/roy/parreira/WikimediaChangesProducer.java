package dev.roy.parreira;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.net.URI;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.isNull;
import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

@Slf4j
public class WikimediaChangesProducer {

  static final String TOPIC = "wikimedia.recent.change";
  private static KafkaProducer<String, String> kafkaProducer = null;

  /**
   * Singleton for WikimediaChangesProducer
   *
   * @return instance of WikimediaChangesProducer
   */
  public static KafkaProducer<String, String> getProducerInstance() {

    if (isNull(kafkaProducer)) {

      Properties properties = new Properties();
      properties.setProperty(BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
      properties.setProperty(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
      properties.setProperty(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

      kafkaProducer = new KafkaProducer<>(properties);
    }

    return kafkaProducer;
  }

  public void startProduceFromWikimedia() throws InterruptedException {


    EventHandler eventHandler = new WikimediaChangesEventHandler();

    URI wikimediaStreamUri = URI.create("https://stream.wikimedia.org/v2/stream/recentchange");
    EventSource wikimediaEventSource = new EventSource.Builder(eventHandler, wikimediaStreamUri).build();

    // start the producer in another thread
    wikimediaEventSource.start();

    // wait one minute for the wikimediaEventSource thread to proxy to kafka
    TimeUnit.MINUTES.sleep(1);

  }
}
