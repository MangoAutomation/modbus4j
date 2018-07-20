package com.serotonin.cdc.modbus4j.locator;

import com.serotonin.cdc.modbus4j.base.ModbusUtils;
import com.serotonin.cdc.modbus4j.base.RangeAndOffset;
import com.serotonin.cdc.modbus4j.code.DataType;
import com.serotonin.cdc.modbus4j.code.RegisterRange;
import com.serotonin.cdc.modbus4j.exception.ModbusIdException;
import com.serotonin.cdc.modbus4j.exception.ModbusTransportException;

abstract public class BaseLocator {
    //
    //
    // Factory methods
    //
    public static BaseLocator coilStatus(int slaveId, int offset) {
        return new BinaryLocator(slaveId, RegisterRange.COIL_STATUS, offset);
    }

    public static BaseLocator inputStatus(int slaveId, int offset) {
        return new BinaryLocator(slaveId, RegisterRange.INPUT_STATUS, offset);
    }

    public static BaseLocator inputRegister(int slaveId, int offset, int dataType) {
        return new NumericLocator(slaveId, RegisterRange.INPUT_REGISTER, offset, dataType);
    }

    public static BaseLocator inputRegisterBit(int slaveId, int offset, int bit) {
        return new BinaryLocator(slaveId, RegisterRange.INPUT_REGISTER, offset, bit);
    }

    public static BaseLocator holdingRegister(int slaveId, int offset, int dataType) {
        return new NumericLocator(slaveId, RegisterRange.HOLDING_REGISTER, offset, dataType);
    }

    public static BaseLocator holdingRegisterBit(int slaveId, int offset, int bit) {
        return new BinaryLocator(slaveId, RegisterRange.HOLDING_REGISTER, offset, bit);
    }

    public static BaseLocator createLocator(int slaveId, int registerId, int dataType, int bit, int registerCount) {
        RangeAndOffset rao = new RangeAndOffset(registerId);
        return createLocator(slaveId, rao.getRange(), rao.getOffset(), dataType, bit, registerCount
        //,StringLocator.ASCII    GWatson 26-07-2011 not available in AJ102 runtime
        );
    }

    /****
     * public static BaseLocator createLocator(int slaveId, int registerId, int dataType, int bit, int registerCount
     * //,Charset charset GWatson 26-07-2011 not available in AJ102 runtime ) { RangeAndOffset rao = new
     * RangeAndOffset(registerId); return createLocator(slaveId, rao.getRange(), rao.getOffset(), dataType, bit,
     * registerCount, charset); }
     * 
     * public static BaseLocator createLocator(int slaveId, int range, int offset, int dataType, int bit, int
     * registerCount) { return createLocator(slaveId, range, offset, dataType, bit, registerCount);//,
     * StringLocator.ASCII); GWatson 26-07-2011 not available in AJ102 runtime }
     *****/
    public static BaseLocator createLocator(int slaveId, int range, int offset, int dataType, int bit, int registerCount) {//, Charset charset) {  GWatson 26-07-2011 not available in AJ102 runtime
        if (dataType == DataType.BINARY) {
            if (BinaryLocator.isBinaryRange(range))
                return new BinaryLocator(slaveId, range, offset);
            return new BinaryLocator(slaveId, range, offset, bit);
        }
        if (dataType == DataType.CHAR || dataType == DataType.VARCHAR)
            return new StringLocator(slaveId, range, offset, dataType, registerCount);//, charset);   GWatson 26-07-2011 not available in AJ102 runtime
        return new NumericLocator(slaveId, range, offset, dataType);
    }

    private final int slaveId;
    protected final int range;
    protected final int offset;

    public BaseLocator(int slaveId, int range, int offset) {
        this.slaveId = slaveId;
        this.range = range;
        this.offset = offset;
    }

    protected void validate(int registerCount) {
        try {
            ModbusUtils.validateOffset(offset);
            ModbusUtils.validateEndOffset(offset + registerCount - 1);
        }
        catch (ModbusTransportException e) {
            throw new ModbusIdException(e);
        }
    }

    abstract public int getDataType();

    abstract public int getRegisterCount();

    public int getSlaveId() {
        return slaveId;
    }

    public int getRange() {
        return range;
    }

    public int getOffset() {
        return offset;
    }

    //    public SlaveAndRange getSlaveAndRange() {
    //        return slaveAndRange;
    //    }

    public int getEndOffset() {
        return offset + getRegisterCount() - 1;
    }

    public Object bytesToValue(byte[] data, int requestOffset) {
        // Determined the offset normalized to the response data.
        return bytesToValueRealOffset(data, offset - requestOffset);
    }

    abstract public Object bytesToValueRealOffset(byte[] data, int offset);

    abstract public short[] valueToShorts(Object value);
}
