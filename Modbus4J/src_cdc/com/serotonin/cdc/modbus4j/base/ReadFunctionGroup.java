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

import java.util.ArrayList;
import java.util.List;

import com.serotonin.cdc.modbus4j.code.RegisterRange;

public class ReadFunctionGroup {
    private final SlaveAndRange slaveAndRange;
    private final int functionCode;
    private final List locators = new ArrayList();
    private int startOffset = 65536;
    private int length = 0;

    public ReadFunctionGroup(KeyedModbusLocator locator) {
        slaveAndRange = locator.getSlaveAndRange();
        functionCode = RegisterRange.getReadFunctionCode(slaveAndRange.getRange());
        add(locator);
    }

    public void add(KeyedModbusLocator locator) {
        if (startOffset > locator.getOffset())
            startOffset = locator.getOffset();
        if (length < locator.getEndOffset() - startOffset + 1)
            length = locator.getEndOffset() - startOffset + 1;
        locators.add(locator);
    }

    public int getStartOffset() {
        return startOffset;
    }

    public int getEndOffset() {
        return startOffset + length - 1;
    }

    public SlaveAndRange getSlaveAndRange() {
        return slaveAndRange;
    }

    public int getLength() {
        return length;
    }

    public int getFunctionCode() {
        return functionCode;
    }

    public List getLocators() {
        return locators;
    }
}
