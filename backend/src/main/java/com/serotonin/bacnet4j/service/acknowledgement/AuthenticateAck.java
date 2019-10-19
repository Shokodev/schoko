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
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import org.free.bacnet4j.util.ByteQueue;

public class AuthenticateAck extends AcknowledgementService {
    private static final long serialVersionUID = 1433915425430939025L;

    public static final byte TYPE_ID = 24;

    private final UnsignedInteger modifiedRandomNumber;

    public AuthenticateAck(UnsignedInteger modifiedRandomNumber) {
        this.modifiedRandomNumber = modifiedRandomNumber;
    }

    @Override
    public byte getChoiceId() {
        return TYPE_ID;
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, modifiedRandomNumber);
    }

    AuthenticateAck(ByteQueue queue) throws BACnetException {
        modifiedRandomNumber = read(queue, UnsignedInteger.class);
    }

    public UnsignedInteger getModifiedRandomNumber() {
        return modifiedRandomNumber;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((modifiedRandomNumber == null) ? 0 : modifiedRandomNumber.hashCode());
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
        final AuthenticateAck other = (AuthenticateAck) obj;
        if (modifiedRandomNumber == null) {
            if (other.modifiedRandomNumber != null)
                return false;
        }
        else if (!modifiedRandomNumber.equals(other.modifiedRandomNumber))
            return false;
        return true;
    }
}
