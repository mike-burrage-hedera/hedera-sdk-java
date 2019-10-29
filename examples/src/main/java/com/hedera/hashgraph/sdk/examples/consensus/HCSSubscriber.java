package com.hedera.hashgraph.sdk.examples.consensus;

import com.hedera.hashgraph.sdk.HederaException;
import com.hedera.hashgraph.sdk.consensus.TopicId;
import com.hedera.hashgraph.sdk.examples.ExampleHelper;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;

public final class HCSSubscriber {

    private HCSSubscriber() {
    }

    public static void main(String[] args) throws HederaException, InterruptedException {
        var topicId = TopicId.fromString(ExampleHelper.hcsTopicId());
        var client = ExampleHelper.createHederaClient();
        var count = 10;

        var listenerThread = new Thread(() -> {
            try (var ts = new com.hedera.hashgraph.sdk.consensus.TopicSubscriber(
                ExampleHelper.mirrorNodeHost(), ExampleHelper.mirrorNodePort())) {
                ts.subscribe(topicId, (tm) -> {
                    var message = tm.getMessage().toByteArray();
                    var sequenceNumber = tm.getSequenceNumber();
                    var runningHash = tm.getRunningHash();
                    System.out.println("{ message: "  + new String(message, StandardCharsets.UTF_8)
                        + ", sequenceNumber: " + sequenceNumber + ", runningHash: "
                        + Hex.toHexString(runningHash.toByteArray()) + " }");
                });
            } catch (Exception x) {
                System.out.println(x);
            }
        });
        listenerThread.start();
        for (var i = 0; i < count; ++i) {
            var message = "TestMessage #" + i;
            System.out.println("Writing " + message + " to " + topicId);
            client.submitMessage(topicId, message.getBytes());
            Thread.sleep(100);
        }
        Thread.sleep(5000);
        listenerThread.interrupt();
    }
}
