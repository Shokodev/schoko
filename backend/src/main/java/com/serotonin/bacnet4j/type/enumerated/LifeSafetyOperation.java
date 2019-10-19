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
package com.serotonin.bacnet4j.type.enumerated;

import com.serotonin.bacnet4j.type.primitive.Enumerated;
import org.free.bacnet4j.util.ByteQueue;

public class LifeSafetyOperation extends Enumerated {
    private static final long serialVersionUID = -8453182789389518551L;
    public static final LifeSafetyOperation none = new LifeSafetyOperation(0);
    public static final LifeSafetyOperation silence = new LifeSafetyOperation(1);
    public static final LifeSafetyOperation silenceAudible = new LifeSafetyOperation(2);
    public static final LifeSafetyOperation silenceVisual = new LifeSafetyOperation(3);
    public static final LifeSafetyOperation reset = new LifeSafetyOperation(4);
    public static final LifeSafetyOperation resetAlarm = new LifeSafetyOperation(5);
    public static final LifeSafetyOperation resetFault = new LifeSafetyOperation(6);
    public static final LifeSafetyOperation unsilence = new LifeSafetyOperation(7);
    public static final LifeSafetyOperation unsilenceAudible = new LifeSafetyOperation(8);
    public static final LifeSafetyOperation unsilenceVisual = new LifeSafetyOperation(9);

    public static final LifeSafetyOperation[] ALL = { none, silence, silenceAudible, silenceVisual, reset, resetAlarm,
            resetFault, unsilence, unsilenceAudible, unsilenceVisual, };

    public LifeSafetyOperation(int value) {
        super(value);
    }

    public LifeSafetyOperation(ByteQueue queue) {
        super(queue);
    }
}
