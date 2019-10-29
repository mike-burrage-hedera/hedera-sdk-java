package com.hedera.hashgraph.sdk.examples.consensus;

import com.hedera.hashgraph.sdk.HederaException;
import com.hedera.hashgraph.sdk.consensus.CreateTopicTransaction;
import com.hedera.hashgraph.sdk.crypto.Key;
import com.hedera.hashgraph.sdk.crypto.ed25519.Ed25519PrivateKey;
import com.hedera.hashgraph.sdk.examples.ExampleHelper;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public final class CreateTopic {

    private CreateTopic() {
    }

    public static void main(String[] args) throws HederaException {
        var client = ExampleHelper.createHederaClient();
        var newAdminKey = Ed25519PrivateKey.generate();
        var newAdminPublicKey = newAdminKey.getPublicKey();
        System.out.println("topic admin private key = " + newAdminKey);
        System.out.println("topic admin public key = " + newAdminPublicKey);

        var record = new CreateTopicTransaction(client)
            .setAdminKey(Key.fromString(newAdminPublicKey.toString()))
            .setValidStartTime(Instant.now().minus(1, ChronoUnit.HOURS))
            .setTopicMemo("TEST TOPIC")
            .executeForRecord();

        System.out.println("Topic created: " + record.getReceipt().getTopicId());
    }
}
