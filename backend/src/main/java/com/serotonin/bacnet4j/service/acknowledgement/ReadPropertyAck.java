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
package com.serotonin.bacnet4j.service.acknowledgement;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import org.free.bacnet4j.util.ByteQueue;

public class ReadPropertyAck extends AcknowledgementService {
    private static final long serialVersionUID = -2645125939312830968L;

    public static final byte TYPE_ID = 12;

    private final ObjectIdentifier eventObjectIdentifier;
    private final PropertyIdentifier propertyIdentifier;
    private final UnsignedInteger propertyArrayIndex;
    private final Encodable value;

    public ReadPropertyAck(ObjectIdentifier eventObjectIdentifier, 
    					   PropertyIdentifier propertyIdentifier,
            			   UnsignedInteger propertyArrayIndex, 
            			   Encodable value) {
        this.eventObjectIdentifier = eventObjectIdentifier;
        this.propertyIdentifier = propertyIdentifier;
        this.propertyArrayIndex = propertyArrayIndex;
        this.value = value;
    }

    @Override
    public byte getChoiceId() {
        return TYPE_ID;
    }

    @Override
    public String toString() {
        return "ReadPropertyAck(" + value + ")";
    }

    public ObjectIdentifier getEventObjectIdentifier() {
        return eventObjectIdentifier;
    }

    public UnsignedInteger getPropertyArrayIndex() {
        return propertyArrayIndex;
    }

    public PropertyIdentifier getPropertyIdentifier() {
        return propertyIdentifier;
    }

    public Encodable getValue() {
        return value;
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, eventObjectIdentifier, 0);
        write(queue, propertyIdentifier, 1);
        writeOptional(queue, propertyArrayIndex, 2);
        writeEncodable(queue, value, 3);
    }

    ReadPropertyAck(ByteQueue queue) throws BACnetException {
        eventObjectIdentifier = read(queue, ObjectIdentifier.class, 0);
        propertyIdentifier = read(queue, PropertyIdentifier.class, 1);
        propertyArrayIndex = readOptional(queue, UnsignedInteger.class, 2);
        value = readEncodable(queue, eventObjectIdentifier.getObjectType(), propertyIdentifier, propertyArrayIndex, 3);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((eventObjectIdentifier == null) ? 0 : eventObjectIdentifier.hashCode());
        result = PRIME * result + ((propertyArrayIndex == null) ? 0 : propertyArrayIndex.hashCode());
        result = PRIME * result + ((propertyIdentifier == null) ? 0 : propertyIdentifier.hashCode());
        result = PRIME * result + ((value == null) ? 0 : value.hashCode());
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
        final ReadPropertyAck other = (ReadPropertyAck) obj;
        if (eventObjectIdentifier == null) {
            if (other.eventObjectIdentifier != null)
                return false;
        }
        else if (!eventObjectIdentifier.equals(other.eventObjectIdentifier))
            return false;
        if (propertyArrayIndex == null) {
            if (other.propertyArrayIndex != null)
                return false;
        }
        else if (!propertyArrayIndex.equals(other.propertyArrayIndex))
            return false;
        if (propertyIdentifier == null) {
            if (other.propertyIdentifier != null)
                return false;
        }
        else if (!propertyIdentifier.equals(other.propertyIdentifier))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        }
        else if (!value.equals(other.value))
            return false;
        return true;
    }
}
