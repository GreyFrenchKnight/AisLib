/* Copyright (c) 2011 Danish Maritime Authority
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package dk.dma.ais.packet;

import java.io.OutputStream;

import dk.dma.ais.message.AisMessage;
import dk.dma.commons.util.io.OutputStreamSink;
import dk.dma.enav.util.function.Consumer;
import dk.dma.enav.util.function.Predicate;

/**
 * 
 * @author Kasper Nielsen
 */
public interface AisPacketStream {

    /** Thrown by a subscriber to indicate that the subscription should be cancelled. */
    RuntimeException CANCEL = new RuntimeException();

    void add(AisPacket p);

    AisPacketStream filter(Predicate<? super AisPacket> predicate);

    AisPacketStream filter(String expression);

    AisPacketStream filterOnMessageType(int... messageTypes);

    Subscription subscribeMessages(Consumer<AisMessage> c);

    Subscription subscribePackets(Consumer<AisPacket> c);

    Subscription subscribePacketSink(OutputStreamSink<AisPacket> sink, OutputStream os);

    public abstract class StreamConsumer<T> implements Consumer<T> {
        /** Invoked immediately before the first message is delivered. */
        public void begin() {}

        /**
         * Invoked immediately after the last message has been delivered.
         * 
         * @param cause
         *            if an exception caused the consumer to be unsubscribed
         */
        public void end(Throwable cause) {}
    }

    interface Subscription {
        void awaitCancelled() throws InterruptedException;

        void cancel();

        boolean isCancelled();
    }
}
