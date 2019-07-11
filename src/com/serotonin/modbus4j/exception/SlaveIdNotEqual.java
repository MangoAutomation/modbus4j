package com.serotonin.modbus4j.exception;

public class SlaveIdNotEqual extends ModbusTransportException {
    private static final long serialVersionUID = -1;

    public SlaveIdNotEqual(int requestslaveId,int responslaveseId) {
        super("requestslaveId is "+requestslaveId+" responslaveseId is "+ responslaveseId, requestslaveId);
    }
}
