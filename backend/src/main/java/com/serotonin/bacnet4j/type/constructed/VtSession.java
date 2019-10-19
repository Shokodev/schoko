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
package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import org.free.bacnet4j.util.ByteQueue;

public class VtSession extends BaseType {
    private static final long serialVersionUID = 3655659977689484183L;
    private final UnsignedInteger localVtSessionId;
    private final UnsignedInteger remoteVtSessionId;
    private final Address remoteVtAddress;

    public VtSession(UnsignedInteger localVtSessionId, UnsignedInteger remoteVtSessionId, Address remoteVtAddress) {
        this.localVtSessionId = localVtSessionId;
        this.remoteVtSessionId = remoteVtSessionId;
        this.remoteVtAddress = remoteVtAddress;
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, localVtSessionId);
        write(queue, remoteVtSessionId);
        write(queue, remoteVtAddress);
    }

    public VtSession(ByteQueue queue) throws BACnetException {
        localVtSessionId = read(queue, UnsignedInteger.class);
        remoteVtSessionId = read(queue, UnsignedInteger.class);
        remoteVtAddress = read(queue, Address.class);
    }

    public UnsignedInteger getLocalVtSessionId() {
        return localVtSessionId;
    }

    public UnsignedInteger getRemoteVtSessionId() {
        return remoteVtSessionId;
    }

    public Address getRemoteVtAddress() {
        return remoteVtAddress;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((localVtSessionId == null) ? 0 : localVtSessionId.hashCode());
        result = PRIME * result + ((remoteVtAddress == null) ? 0 : remoteVtAddress.hashCode());
        result = PRIME * result + ((remoteVtSessionId == null) ? 0 : remoteVtSessionId.hashCode());
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
        final VtSession other = (VtSession) obj;
        if (localVtSessionId == null) {
            if (other.localVtSessionId != null)
                return false;
        }
        else if (!localVtSessionId.equals(other.localVtSessionId))
            return false;
        if (remoteVtAddress == null) {
            if (other.remoteVtAddress != null)
                return false;
        }
        else if (!remoteVtAddress.equals(other.remoteVtAddress))
            return false;
        if (remoteVtSessionId == null) {
            if (other.remoteVtSessionId != null)
                return false;
        }
        else if (!remoteVtSessionId.equals(other.remoteVtSessionId))
            return false;
        return true;
    }
}
