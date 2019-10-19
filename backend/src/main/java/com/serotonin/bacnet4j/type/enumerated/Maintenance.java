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

public class Maintenance extends Enumerated {
    private static final long serialVersionUID = -8105102280345072070L;
    public static final Maintenance none = new Maintenance(0);
    public static final Maintenance periodicTest = new Maintenance(1);
    public static final Maintenance needServiceOperational = new Maintenance(2);
    public static final Maintenance needServiceInoperative = new Maintenance(3);

    public static final Maintenance[] ALL = { none, periodicTest, needServiceOperational, needServiceInoperative, };

    public Maintenance(int value) {
        super(value);
    }

    public Maintenance(ByteQueue queue) {
        super(queue);
    }
}
