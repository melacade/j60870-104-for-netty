package com.melody.j60870.datapack.config;

import lombok.Data;

@Data
public final class ConnectionNettySettings {
    public int messageFragmentTimeout = 5000;
    public int cotFieldLength = 2;
    public int commonAddressFieldLength = 2;
    public int ioaFieldLength = 3;
    public int maxTimeNoAckReceived = 15000;
    public int maxTimeNoAckSent = 10000;
    public int maxIdleTime = 20000;
    public int maxUnconfirmedIPdusReceived = 1;
    public int maxNumOfOutstandingIPdus = 12;
    public ConnectionNettySettings() {
    }
    
    public ConnectionNettySettings(ConnectionSettings connectionSettings) {
        this.messageFragmentTimeout = connectionSettings.getMessageFragmentTimeout();
        this.cotFieldLength = connectionSettings.getCotFieldLength();
        this.commonAddressFieldLength = connectionSettings.getCommonAddressFieldLength();
        this.ioaFieldLength = connectionSettings.getIoaFieldLength();
        this.maxTimeNoAckReceived = connectionSettings.getMaxTimeNoAckReceived();
        this.maxTimeNoAckSent = connectionSettings.getMaxTimeNoAckSent();
        this.maxIdleTime = connectionSettings.getMaxIdleTime();
        this.maxUnconfirmedIPdusReceived = Math.max(connectionSettings.getMaxUnconfirmedIPdusReceived(), 1);
    }
    public ConnectionNettySettings getCopy() {
        ConnectionNettySettings settings = new ConnectionNettySettings();
        settings.messageFragmentTimeout = this.messageFragmentTimeout;
        settings.cotFieldLength = this.cotFieldLength;
        settings.commonAddressFieldLength = this.commonAddressFieldLength;
        settings.ioaFieldLength = this.ioaFieldLength;
        settings.maxTimeNoAckReceived = this.maxTimeNoAckReceived;
        settings.maxTimeNoAckSent = this.maxTimeNoAckSent;
        settings.maxIdleTime = this.maxIdleTime;
        settings.maxUnconfirmedIPdusReceived = this.maxUnconfirmedIPdusReceived;
        return settings;
    }
}