package dev.roy.parreira;

public class Main {
  public static void main(String[] args) throws InterruptedException {
    final WikimediaChangesProducer wikimediaChangesProducer = new WikimediaChangesProducer();
    wikimediaChangesProducer.startProduceFromWikimedia();
  }
}
