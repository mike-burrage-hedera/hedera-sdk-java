package com.hedera.hashgraph.sdk.examples.consensus;

import com.hedera.hashgraph.sdk.HederaException;
import com.hedera.hashgraph.sdk.consensus.SubmitMessageTransaction;
import com.hedera.hashgraph.sdk.consensus.TopicId;
import com.hedera.hashgraph.sdk.examples.ExampleHelper;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

public final class SubmitMessage {
    private SubmitMessage() { }

    public static void main(String[] args) throws HederaException, InterruptedException {
        var client = ExampleHelper.createHederaClient();

        var topicId = TopicId.fromString(ExampleHelper.hcsTopicId());
        var message = "Message " + Instant.now().toString();
        System.out.println("Writing " + message + " to " + topicId);
        var receipt = new SubmitMessageTransaction(client)
            .setTopicId(topicId)
            .setMessage(message.getBytes(StandardCharsets.UTF_8))
            .executeForReceipt();
        System.out.println("Message sent - new sequenceNumber: " + receipt.getTopicSequenceNumber()
            + " runningHash: " + Hex.toHexString(receipt.getTopicRunningHash()));
    }
}
