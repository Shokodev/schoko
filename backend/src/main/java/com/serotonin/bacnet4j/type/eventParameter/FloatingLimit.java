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
package com.serotonin.bacnet4j.type.eventParameter;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.constructed.DeviceObjectPropertyReference;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import org.free.bacnet4j.util.ByteQueue;

public class FloatingLimit extends EventParameter {
    private static final long serialVersionUID = -3074551768740502706L;

    public static final byte TYPE_ID = 4;

    private final UnsignedInteger timeDelay;
    private final DeviceObjectPropertyReference setpointReference;
    private final Real lowDiffLimit;
    private final Real highDiffLimit;
    private final Real deadband;

    public FloatingLimit(UnsignedInteger timeDelay, DeviceObjectPropertyReference setpointReference, Real lowDiffLimit,
            Real highDiffLimit, Real deadband) {
        this.timeDelay = timeDelay;
        this.setpointReference = setpointReference;
        this.lowDiffLimit = lowDiffLimit;
        this.highDiffLimit = highDiffLimit;
        this.deadband = deadband;
    }

    @Override
    protected void writeImpl(ByteQueue queue) {
        write(queue, timeDelay, 0);
        write(queue, setpointReference, 1);
        write(queue, lowDiffLimit, 2);
        write(queue, highDiffLimit, 3);
        write(queue, deadband, 4);
    }

    public FloatingLimit(ByteQueue queue) throws BACnetException {
        timeDelay = read(queue, UnsignedInteger.class, 0);
        setpointReference = read(queue, DeviceObjectPropertyReference.class, 1);
        lowDiffLimit = read(queue, Real.class, 2);
        highDiffLimit = read(queue, Real.class, 3);
        deadband = read(queue, Real.class, 4);
    }

    @Override
    protected int getTypeId() {
        return TYPE_ID;
    }

    public UnsignedInteger getTimeDelay() {
        return timeDelay;
    }

    public DeviceObjectPropertyReference getSetpointReference() {
        return setpointReference;
    }

    public Real getLowDiffLimit() {
        return lowDiffLimit;
    }

    public Real getHighDiffLimit() {
        return highDiffLimit;
    }

    public Real getDeadband() {
        return deadband;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((deadband == null) ? 0 : deadband.hashCode());
        result = PRIME * result + ((highDiffLimit == null) ? 0 : highDiffLimit.hashCode());
        result = PRIME * result + ((lowDiffLimit == null) ? 0 : lowDiffLimit.hashCode());
        result = PRIME * result + ((setpointReference == null) ? 0 : setpointReference.hashCode());
        result = PRIME * result + ((timeDelay == null) ? 0 : timeDelay.hashCode());
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
        final FloatingLimit other = (FloatingLimit) obj;
        if (deadband == null) {
            if (other.deadband != null)
                return false;
        }
        else if (!deadband.equals(other.deadband))
            return false;
        if (highDiffLimit == null) {
            if (other.highDiffLimit != null)
                return false;
        }
        else if (!highDiffLimit.equals(other.highDiffLimit))
            return false;
        if (lowDiffLimit == null) {
            if (other.lowDiffLimit != null)
                return false;
        }
        else if (!lowDiffLimit.equals(other.lowDiffLimit))
            return false;
        if (setpointReference == null) {
            if (other.setpointReference != null)
                return false;
        }
        else if (!setpointReference.equals(other.setpointReference))
            return false;
        if (timeDelay == null) {
            if (other.timeDelay != null)
                return false;
        }
        else if (!timeDelay.equals(other.timeDelay))
            return false;
        return true;
    }
}
