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
import com.serotonin.bacnet4j.type.primitive.SignedInteger;
import org.free.bacnet4j.util.ByteQueue;

public class AtomicWriteFileAck extends AcknowledgementService {
    private static final long serialVersionUID = -3122331020521995628L;

    public static final byte TYPE_ID = 7;

    private final boolean recordAccess;
    private final SignedInteger fileStart;

    public AtomicWriteFileAck(boolean recordAccess, SignedInteger fileStart) {
        this.recordAccess = recordAccess;
        this.fileStart = fileStart;
    }

    @Override
    public byte getChoiceId() {
        return TYPE_ID;
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, fileStart, recordAccess ? 1 : 0);
    }

    AtomicWriteFileAck(ByteQueue queue) throws BACnetException {
        recordAccess = peekTagNumber(queue) == 1;
        fileStart = read(queue, SignedInteger.class, recordAccess ? 1 : 0);
    }

    public boolean isRecordAccess() {
        return recordAccess;
    }

    public SignedInteger getFileStart() {
        return fileStart;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((fileStart == null) ? 0 : fileStart.hashCode());
        result = PRIME * result + (recordAccess ? 1231 : 1237);
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
        final AtomicWriteFileAck other = (AtomicWriteFileAck) obj;
        if (fileStart == null) {
            if (other.fileStart != null)
                return false;
        }
        else if (!fileStart.equals(other.fileStart))
            return false;
        if (recordAccess != other.recordAccess)
            return false;
        return true;
    }
}
