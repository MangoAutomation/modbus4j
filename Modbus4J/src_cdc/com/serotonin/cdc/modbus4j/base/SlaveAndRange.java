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
 */
package com.serotonin.cdc.modbus4j.base;

public class SlaveAndRange {
    private final int slaveId;
    private final int range;

    public SlaveAndRange(int slaveId, int range) {
        ModbusUtils.validateSlaveId(slaveId, true);

        this.slaveId = slaveId;
        this.range = range;
    }

    public int getRange() {
        return range;
    }

    public int getSlaveId() {
        return slaveId;
    }

    //Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + range;
        result = prime * result + slaveId;
        return result;
    }

    //Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final SlaveAndRange other = (SlaveAndRange) obj;
        if (range != other.range)
            return false;
        if (slaveId != other.slaveId)
            return false;
        return true;
    }
}
