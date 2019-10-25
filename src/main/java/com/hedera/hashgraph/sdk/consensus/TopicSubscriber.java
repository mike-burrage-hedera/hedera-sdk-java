package com.hedera.hashgraph.sdk.consensus;

import com.hedera.mirror.api.proto.java.MirrorGetTopicMessages;
import com.hedera.mirror.api.proto.java.MirrorServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.function.Consumer;

public final class TopicSubscriber implements AutoCloseable {
    ManagedChannel channel;
    MirrorServiceGrpc.MirrorServiceBlockingStub blockingStub;

    public TopicSubscriber(final String mirrorAddress, final int mirrorPort) {
        channel = ManagedChannelBuilder.forAddress(mirrorAddress, mirrorPort)
            .usePlaintext()
            .build();
        blockingStub = MirrorServiceGrpc.newBlockingStub(channel);
    }

    public void subscribe(final TopicId topicId,
                          final Consumer<MirrorGetTopicMessages.MirrorGetTopicMessagesResponse> consumer) {
        final var query = MirrorGetTopicMessages.MirrorGetTopicMessagesQuery.newBuilder()
            .setTopicID(topicId.toProto())
            .build();

        blockingStub.getTopicMessages(query).forEachRemaining(consumer);
    }

    @Override
    public void close() throws Exception {
        channel.shutdown();
    }
}
