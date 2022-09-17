package dev.roy.parreira;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class WikimediaChangesProducerTest {

  private final WikimediaChangesProducer wikimediaChangesProducer = new WikimediaChangesProducer();

  @Test
  void shouldProxyWikimediaChangesEventsToKafkaMessages() {
    Assertions.assertDoesNotThrow(wikimediaChangesProducer::startProduceFromWikimedia);
  }
}
