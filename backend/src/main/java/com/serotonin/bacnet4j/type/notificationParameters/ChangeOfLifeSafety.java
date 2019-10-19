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
package com.serotonin.bacnet4j.type.notificationParameters;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.constructed.StatusFlags;
import com.serotonin.bacnet4j.type.enumerated.LifeSafetyMode;
import com.serotonin.bacnet4j.type.enumerated.LifeSafetyOperation;
import com.serotonin.bacnet4j.type.enumerated.LifeSafetyState;
import org.free.bacnet4j.util.ByteQueue;

public class ChangeOfLifeSafety extends NotificationParameters {
    private static final long serialVersionUID = -3145779272869271053L;

    public static final byte TYPE_ID = 8;

    private final LifeSafetyState newState;
    private final LifeSafetyMode newMode;
    private final StatusFlags statusFlags;
    private final LifeSafetyOperation operationExpected;

    public ChangeOfLifeSafety(LifeSafetyState newState, LifeSafetyMode newMode, StatusFlags statusFlags,
            LifeSafetyOperation operationExpected) {
        this.newState = newState;
        this.newMode = newMode;
        this.statusFlags = statusFlags;
        this.operationExpected = operationExpected;
    }

    @Override
    protected void writeImpl(ByteQueue queue) {
        write(queue, newState, 0);
        write(queue, statusFlags, 1);
        write(queue, newMode, 2);
        write(queue, operationExpected, 3);
    }

    public ChangeOfLifeSafety(ByteQueue queue) throws BACnetException {
        newState = read(queue, LifeSafetyState.class, 0);
        newMode = read(queue, LifeSafetyMode.class, 1);
        statusFlags = read(queue, StatusFlags.class, 2);
        operationExpected = read(queue, LifeSafetyOperation.class, 3);
    }

    @Override
    protected int getTypeId() {
        return TYPE_ID;
    }

    public LifeSafetyState getNewState() {
        return newState;
    }

    public LifeSafetyMode getNewMode() {
        return newMode;
    }

    public StatusFlags getStatusFlags() {
        return statusFlags;
    }

    public LifeSafetyOperation getOperationExpected() {
        return operationExpected;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((newMode == null) ? 0 : newMode.hashCode());
        result = PRIME * result + ((newState == null) ? 0 : newState.hashCode());
        result = PRIME * result + ((operationExpected == null) ? 0 : operationExpected.hashCode());
        result = PRIME * result + ((statusFlags == null) ? 0 : statusFlags.hashCode());
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
        final ChangeOfLifeSafety other = (ChangeOfLifeSafety) obj;
        if (newMode == null) {
            if (other.newMode != null)
                return false;
        }
        else if (!newMode.equals(other.newMode))
            return false;
        if (newState == null) {
            if (other.newState != null)
                return false;
        }
        else if (!newState.equals(other.newState))
            return false;
        if (operationExpected == null) {
            if (other.operationExpected != null)
                return false;
        }
        else if (!operationExpected.equals(other.operationExpected))
            return false;
        if (statusFlags == null) {
            if (other.statusFlags != null)
                return false;
        }
        else if (!statusFlags.equals(other.statusFlags))
            return false;
        return true;
    }
}
