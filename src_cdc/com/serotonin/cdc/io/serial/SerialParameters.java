package com.serotonin.cdc.io.serial;

import gnu.io.SerialPort;

/**
 * @author Matthew Lohbihler
 *
 */
public class SerialParameters {
    private String commPortId;
    private String portOwnerName;
    private int baudRate = -1;
    private int flowControlIn = SerialPort.FLOWCONTROL_NONE;
    private int flowControlOut = SerialPort.FLOWCONTROL_NONE;
    private int dataBits = SerialPort.DATABITS_8;
    private int stopBits = SerialPort.STOPBITS_1;
    private int parity = SerialPort.PARITY_NONE;
    
    public int getBaudRate() {
        return baudRate;
    }
    public void setBaudRate(int baudRate) {
        this.baudRate = baudRate;
    }
    public String getCommPortId() {
        return commPortId;
    }
    public void setCommPortId(String commPortId) {
        this.commPortId = commPortId;
    }
    public int getDataBits() {
        return dataBits;
    }
    public void setDataBits(int dataBits) {
        this.dataBits = dataBits;
    }
    public int getFlowControlIn() {
        return flowControlIn;
    }
    public void setFlowControlIn(int flowControlIn) {
        this.flowControlIn = flowControlIn;
    }
    public int getFlowControlOut() {
        return flowControlOut;
    }
    public void setFlowControlOut(int flowControlOut) {
        this.flowControlOut = flowControlOut;
    }
    public int getParity() {
        return parity;
    }
    public void setParity(int parity) {
        this.parity = parity;
    }
    public int getStopBits() {
        return stopBits;
    }
    public void setStopBits(int stopBits) {
        this.stopBits = stopBits;
    }
    public String getPortOwnerName() {
        return portOwnerName;
    }
    public void setPortOwnerName(String portOwnerName) {
        this.portOwnerName = portOwnerName;
    }
}
