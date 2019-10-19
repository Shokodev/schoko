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

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.primitive.Date;
import com.serotonin.bacnet4j.type.primitive.Time;
import org.free.bacnet4j.util.ByteQueue;

public class DateTime extends BaseType {
    private static final long serialVersionUID = -5792783146879193344L;
    private final Date date;
    private final Time time;

    public DateTime() {
        this(new GregorianCalendar());
    }

    public DateTime(Date date, Time time) {
        this.date = date;
        this.time = time;
    }

    public DateTime(long millis) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(millis);
        date = new Date(gc);
        time = new Time(gc);
    }

    public DateTime(GregorianCalendar gc) {
        date = new Date(gc);
        time = new Time(gc);
    }

    @Override
    public void write(ByteQueue queue) {
        date.write(queue);
        time.write(queue);
    }

    public DateTime(ByteQueue queue) throws BACnetException {
        date = read(queue, Date.class);
        time = read(queue, Time.class);
    }

    public Date getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public long getTimeMillis() {
        GregorianCalendar gc = new GregorianCalendar(date.getCenturyYear(), date.getMonth().getId() - 1, date.getDay(),
                time.getHour(), time.getMinute(), time.getSecond());
        gc.set(Calendar.MILLISECOND, time.getHundredth() * 10);
        return gc.getTimeInMillis();
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((date == null) ? 0 : date.hashCode());
        result = PRIME * result + ((time == null) ? 0 : time.hashCode());
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
        final DateTime other = (DateTime) obj;
        if (date == null) {
            if (other.date != null)
                return false;
        }
        else if (!date.equals(other.date))
            return false;
        if (time == null) {
            if (other.time != null)
                return false;
        }
        else if (!time.equals(other.time))
            return false;
        return true;
    }
}
