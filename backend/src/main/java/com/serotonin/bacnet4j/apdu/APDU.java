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

import java.io.Serializable;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.IllegalPduTypeException;
import com.serotonin.bacnet4j.type.constructed.ServicesSupported;
import org.free.bacnet4j.util.ByteQueue;

abstract public class APDU implements Serializable {
    private static final long serialVersionUID = -5844093063653180470L;

    public static APDU createAPDU(ServicesSupported services, ByteQueue queue) throws BACnetException {
        // Get the first byte. The 4 high-order bits will tell us the type of PDU this is.
        byte type = queue.peek(0);
        type = (byte) ((type & 0xff) >> 4);

        if (type == ConfirmedRequest.TYPE_ID)
            return new ConfirmedRequest(services, queue);
        if (type == UnconfirmedRequest.TYPE_ID)
            return new UnconfirmedRequest(services, queue);
        if (type == SimpleACK.TYPE_ID)
            return new SimpleACK(queue);
        if (type == ComplexACK.TYPE_ID)
            return new ComplexACK(queue);
        if (type == SegmentACK.TYPE_ID)
            return new SegmentACK(queue);
        if (type == Error.TYPE_ID)
            return new Error(queue);
        if (type == Reject.TYPE_ID)
            return new Reject(queue);
        if (type == Abort.TYPE_ID)
            return new Abort(queue);
        throw new IllegalPduTypeException(Byte.toString(type));
    }

    abstract public byte getPduType();

    abstract public void write(ByteQueue queue);

    protected int getShiftedTypeId(byte typeId) {
        return typeId << 4;
    }

    abstract public boolean expectsReply();
}
