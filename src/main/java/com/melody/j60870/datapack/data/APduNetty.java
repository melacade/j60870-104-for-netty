package com.melody.j60870.datapack.data;

import com.melody.j60870.datapack.config.ConnectionNettySettings;
import io.netty.buffer.ByteBuf;
import lombok.Data;

import java.io.IOException;
import java.text.MessageFormat;

@Data
public final class APduNetty {
    private static final int CONTROL_FIELDS_LENGTH = 4;
    /**
     * Since the length of the control field is control field is 4 octets.
     */
    private static final int MIN_APDU_LENGTH = CONTROL_FIELDS_LENGTH;
    /**
     * The maximum length of APDU for both directions is 253. APDU max = 255 minus start and length octet.
     */
    private static final int MAX_APDU_LENGTH = 253;
    /**
     * START flag of an APDU.
     */
    private static final byte START_FLAG = 0x68;
    
    public enum ApciType {
        /**
         * Numbered information transfer. I format APDUs always contain an ASDU.
         */
        I_FORMAT,
        /**
         * Numbered supervisory functions. S format APDUs consist of the APCI only.
         */
        S_FORMAT,
        
        // Unnumbered control functions.
        
        TESTFR_CON,
        TESTFR_ACT,
        STOPDT_CON,
        STOPDT_ACT,
        STARTDT_CON,
        STARTDT_ACT;
        
        private static APduNetty.ApciType apciTypeFor(byte controlField1) {
            if ((controlField1 & 0x01) == 0) {
                return APduNetty.ApciType.I_FORMAT;
            }
            
            switch ((controlField1 & 0x03)) {
                case 1:
                    return APduNetty.ApciType.S_FORMAT;
                case 3:
                default:
                    return unnumberedFormatFor(controlField1);
            }
            
        }
        
        private static APduNetty.ApciType unnumberedFormatFor(byte controlField1) {
            if ((controlField1 & 0x80) == 0x80) {
                return APduNetty.ApciType.TESTFR_CON;
            }
            else if (controlField1 == 0x43) {
                return APduNetty.ApciType.TESTFR_ACT;
            }
            else if (controlField1 == 0x23) {
                return APduNetty.ApciType.STOPDT_CON;
            }
            else if (controlField1 == 0x13) {
                return APduNetty.ApciType.STOPDT_ACT;
            }
            else if (controlField1 == 0x0B) {
                return APduNetty.ApciType.STARTDT_CON;
            }
            else {
                return APduNetty.ApciType.STARTDT_ACT;
            }
        }
    }
    
    private final int sendSeqNum;
    private final int receiveSeqNum;
    private final APduNetty.ApciType apciType;
    private final ASduNetty aSdu;
    
    public APduNetty(int sendSeqNum, int receiveSeqNum, APduNetty.ApciType apciType, ASduNetty aSdu) {
        this.sendSeqNum = sendSeqNum;
        this.receiveSeqNum = receiveSeqNum;
        this.apciType = apciType;
        this.aSdu = aSdu;
    }
    
    public static APduNetty decode(ByteBuf buf, ConnectionNettySettings settings) throws IOException {
	    
	    if (buf.readByte() != START_FLAG) {
            throw new IOException("Message does not start with START flag (0x68). Broken connection.");
        }
        
        
        int length = readApduLength(buf);
        
        byte[] aPduControlFields = readControlFields(buf);
        
        APduNetty.ApciType apciType = APduNetty.ApciType.apciTypeFor(aPduControlFields[0]);
        switch (apciType) {
            case I_FORMAT:
                int sendSeqNum = seqNumFrom(aPduControlFields[0], aPduControlFields[1]);
                int receiveSeqNum = seqNumFrom(aPduControlFields[2], aPduControlFields[3]);
                
                int aSduLength = length - CONTROL_FIELDS_LENGTH;
                
                return new APduNetty(sendSeqNum, receiveSeqNum, apciType, ASduNetty.decode(buf, settings, aSduLength));
            case S_FORMAT:
                return new APduNetty(0, seqNumFrom(aPduControlFields[2], aPduControlFields[3]), apciType, null);
            
            default:
                return new APduNetty(0, 0, apciType, null);
        }
        
    }
    
    private static int seqNumFrom(byte b1, byte b2) {
        return ((b1 & 0xfe) >> 1) + ((b2 & 0xff) << 7);
    }
    
    private static int readApduLength(ByteBuf is) throws IOException {
        int length = is.readUnsignedByte();
        
        if (length < MIN_APDU_LENGTH || length > MAX_APDU_LENGTH) {
            String msg = MessageFormat
                    .format("APDU has an invalid length must be between 4 and 253.\nReceived length was: {0}.", length);
            throw new IOException(msg);
        }
        return length;
    }
    
    private static byte[] readControlFields(ByteBuf is) {
        byte[] aPduControlFields = new byte[CONTROL_FIELDS_LENGTH];
        aPduControlFields[0] = is.readByte();
        aPduControlFields[1] = is.readByte();
        aPduControlFields[2] = is.readByte();
        aPduControlFields[3] = is.readByte();
        return aPduControlFields;
    }
    
    public int encode(ByteBuf buffer, ConnectionNettySettings settings) {
        
        buffer.writeByte(START_FLAG);
        int length = CONTROL_FIELDS_LENGTH;
        buffer.writerIndex(2);
        if (apciType == APduNetty.ApciType.I_FORMAT) {
            buffer.writeShortLE(sendSeqNum);
            buffer.writeShortLE(receiveSeqNum);
            length += aSdu.encode(buffer, 6, settings);
        }
        else if (apciType == APduNetty.ApciType.STARTDT_ACT) {
            buffer.writeByte(0x07);
            setV3To5zero(buffer);
        }
        else if (apciType == APduNetty.ApciType.STARTDT_CON) {
            buffer.writeByte(0x0b);
            setV3To5zero(buffer);
        }
        else if (apciType == APduNetty.ApciType.STOPDT_ACT) {
            buffer.writeByte(0x13);
            setV3To5zero(buffer);
        }
        else if (apciType == APduNetty.ApciType.STOPDT_CON) {
            buffer.writeByte(0x23);
            setV3To5zero(buffer);
        }
        else if (apciType == APduNetty.ApciType.S_FORMAT) {
            buffer.writeByte(0x01);
            buffer.writeByte(0x00);
            writeReceiveSeqNumTo(buffer);
        } else if (apciType == ApciType.TESTFR_ACT) {
            buffer.writeByte(0x43);
            setV3To5zero(buffer);
        } else if (apciType == ApciType.TESTFR_CON) {
            buffer.writeByte(0x83);
            setV3To5zero(buffer);
        }
        buffer.setByte(1,buffer.readableBytes()-2);
        return length + 2;
        
    }
    
    private static void setV3To5zero(ByteBuf buffer) {
        buffer.writeByte(0);
        buffer.writeByte(0);
        buffer.writeByte(0);
        
    }
    
    private void writeReceiveSeqNumTo(ByteBuf buffer) {
        buffer.writeShortLE(receiveSeqNum);
    }
    
    public APduNetty.ApciType getApciType() {
        return apciType;
    }
    
    public int getSendSeqNumber() {
        return sendSeqNum;
    }
    
    public int getReceiveSeqNumber() {
        return receiveSeqNum;
    }
    
    public ASduNetty getASdu() {
        return aSdu;
    }
}
