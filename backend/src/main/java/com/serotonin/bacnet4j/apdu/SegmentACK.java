/*
 * ============================================================================
 * GNU General Public License
 * ============================================================================
 *
 * Copyright (C) 2006-2011 Serotonin Software Technologies Inc. http://serotoninsoftware.com
 * @author Matthew Lohbihler
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * When signing a commercial license with Serotonin Software Technologies Inc.,
 * the following extension to GPL is made. A special exception to the GPL is 
 * included to allow you to distribute a combined work that includes BAcnet4J 
 * without being obliged to provide the source code for any proprietary components.
 */
package com.serotonin.bacnet4j.apdu;

import org.free.bacnet4j.util.ByteQueue;

public class SegmentACK extends AckAPDU {
    private static final long serialVersionUID = -4754176957008435326L;

    public static final byte TYPE_ID = 4;

    /**
     * This parameter shall be TRUE if the Segment-ACK PDU is being sent to indicate a segment received out of order.
     * Otherwise, it shall be FALSE.
     */
    private final boolean negativeAck;

    /**
     * This parameter shall be TRUE when the SegmentACK PDU is sent by a server, that is, when the SegmentACK PDU is in
     * acknowledgment of a segment or segments of a Confirmed-Request PDU.
     * 
     * This parameter shall be FALSE when the SegmentACK PDU is sent by a client, that is, when the SegmentACK PDU is in
     * acknowledgment of a segment or segments of a ComplexACK PDU.
     */
    private final boolean server;

    /**
     * This parameter shall contain the 'sequence-number' of a previously received message segment. It is used to
     * acknowledge the receipt of that message segment and all earlier segments of the message.
     * 
     * If the 'more-follows' parameter of the received message segment is TRUE, then the 'sequence-number' also requests
     * continuation of the segmented message beginning with the segment whose 'sequence-number' is one plus the value of
     * this parameter, modulo 256.
     */
    private final int sequenceNumber;

    /**
     * This parameter shall specify as an unsigned binary integer the number of message segments containing
     * 'original-invokeID' the sender will accept before sending another SegmentACK. See 5.3 for additional details. The
     * value of the 'actual-windowsize' shall be in the range 1 - 127.
     */
    private final int actualWindowSize;

    private boolean expectsResponse;

    public SegmentACK(boolean negativeAck, boolean server, byte originalInvokeId, int sequenceNumber,
            int actualWindowSize, boolean expectsResponse) {
        this.negativeAck = negativeAck;
        this.server = server;
        this.originalInvokeId = originalInvokeId;
        this.sequenceNumber = sequenceNumber;
        this.actualWindowSize = actualWindowSize;
        this.expectsResponse = expectsResponse;
    }

    @Override
    public byte getPduType() {
        return TYPE_ID;
    }

    public int getActualWindowSize() {
        return actualWindowSize;
    }

    public boolean isNegativeAck() {
        return negativeAck;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    @Override
    public boolean isServer() {
        return server;
    }

    @Override
    public void write(ByteQueue queue) {
        queue.push(getShiftedTypeId(TYPE_ID) | (negativeAck ? 2 : 0) | (server ? 1 : 0));
        queue.push(originalInvokeId);
        queue.push(sequenceNumber);
        queue.push(actualWindowSize);
    }

    public SegmentACK(ByteQueue queue) {
        byte b = queue.pop();
        negativeAck = (b & 2) != 0;
        server = (b & 1) != 0;

        originalInvokeId = queue.pop();
        sequenceNumber = queue.popU1B();
        actualWindowSize = queue.popU1B();
    }

    @Override
    public String toString() {
        return "SegmentACK(negativeAck=" + negativeAck + ", server=" + server + ", originalInvokeId="
                + originalInvokeId + ", sequenceNumber=" + sequenceNumber + ", actualWindowSize=" + actualWindowSize
                + ")";
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + actualWindowSize;
        result = PRIME * result + (negativeAck ? 1231 : 1237);
        result = PRIME * result + originalInvokeId;
        result = PRIME * result + sequenceNumber;
        result = PRIME * result + (server ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final SegmentACK other = (SegmentACK) obj;
        if (actualWindowSize != other.actualWindowSize)
            return false;
        if (negativeAck != other.negativeAck)
            return false;
        if (originalInvokeId != other.originalInvokeId)
            return false;
        if (sequenceNumber != other.sequenceNumber)
            return false;
        if (server != other.server)
            return false;
        return true;
    }

    @Override
    public boolean expectsReply() {
        return expectsResponse;
    }
}
