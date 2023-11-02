package com.melody.j60870.datapack.data;

import com.melody.j60870.datapack.config.ConnectionNettySettings;
import com.melody.j60870.datapack.data.ie.InformationNettyObject;
import com.melody.j60870.datapack.util.HexUtils;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.text.MessageFormat;

public class ASduNetty {

    private final ASduTypeNetty aSduType;
    private final boolean isSequenceOfElements;
    private final CauseOfTransmission causeOfTransmission;
    private final boolean test;
    private final boolean negativeConfirm;
    private final int originatorAddress;
    private final int commonAddress;
    private final InformationNettyObject[] informationObjects;
    private final byte[] privateInformation;
    private final int sequenceLength;

    /**
     * Use this constructor to create standardized ASDUs.
     *
     * @param typeId
     *            type identification field that defines the purpose and contents of the ASDU
     * @param isSequenceOfElements
     *            if {@code false} then the ASDU contains a sequence of information objects consisting of a fixed number
     *            of information elements. If {@code true} the ASDU contains a single information object with a sequence
     *            of elements.
     * @param causeOfTransmission
     *            the cause of transmission
     * @param test
     *            true if the ASDU is sent for test purposes
     * @param negativeConfirm
     *            true if the ASDU is a negative confirmation
     * @param originatorAddress
     *            the address of the originating controlling station so that responses can be routed back to it
     * @param commonAddress
     *            the address of the target station or the broadcast address.
     * @param informationObjects
     *            the information objects containing the actual data
     */
    public ASduNetty(ASduTypeNetty typeId, boolean isSequenceOfElements, CauseOfTransmission causeOfTransmission, boolean test,
            boolean negativeConfirm, int originatorAddress, int commonAddress,
            InformationNettyObject... informationObjects) {

        this.aSduType = typeId;
        this.isSequenceOfElements = isSequenceOfElements;
        this.causeOfTransmission = causeOfTransmission;
        this.test = test;
        this.negativeConfirm = negativeConfirm;
        this.originatorAddress = originatorAddress;
        this.commonAddress = commonAddress;
        this.informationObjects = informationObjects;
        privateInformation = null;
        if (isSequenceOfElements) {
            sequenceLength = informationObjects[0].getInformationElements().length;
        }
        else {
            sequenceLength = informationObjects.length;
        }
    }

    /**
     * Use this constructor to create private ASDU with TypeIDs in the range 128-255.
     *
     * @param typeId
     *            type identification field that defines the purpose and contents of the ASDU
     * @param isSequenceOfElements
     *            if false then the ASDU contains a sequence of information objects consisting of a fixed number of
     *            information elements. If true the ASDU contains a single information object with a sequence of
     *            elements.
     * @param sequenceLength
     *            the number of information objects or the number elements depending depending on which is transmitted
     *            as a sequence
     * @param causeOfTransmission
     *            the cause of transmission
     * @param test
     *            true if the ASDU is sent for test purposes
     * @param negativeConfirm
     *            true if the ASDU is a negative confirmation
     * @param originatorAddress
     *            the address of the originating controlling station so that responses can be routed back to it
     * @param commonAddress
     *            the address of the target station or the broadcast address.
     * @param privateInformation
     *            the bytes to be transmitted as payload
     */
    public ASduNetty(ASduTypeNetty typeId, boolean isSequenceOfElements, int sequenceLength,
            CauseOfTransmission causeOfTransmission, boolean test, boolean negativeConfirm, int originatorAddress,
            int commonAddress, byte[] privateInformation) {

        this.aSduType = typeId;
        this.isSequenceOfElements = isSequenceOfElements;
        this.causeOfTransmission = causeOfTransmission;
        this.test = test;
        this.negativeConfirm = negativeConfirm;
        this.originatorAddress = originatorAddress;
        this.commonAddress = commonAddress;
        informationObjects = null;
        this.privateInformation = privateInformation;
        this.sequenceLength = sequenceLength;
    }
    
    static ASduNetty decode(ByteBuf is, ConnectionNettySettings settings, int aSduLength) throws IOException {

        int typeIdCode = is.readUnsignedByte();

        ASduTypeNetty typeId = ASduTypeNetty.typeFor(typeIdCode);

        if (typeId == null) {
            throw new IOException(MessageFormat.format("Unknown Type Identification: {0}", typeIdCode));
        }

        int currentByte = is.readUnsignedByte();

        boolean isSequenceOfElements = byteHasMask(currentByte, 0x80);

        int numberOfSequenceElements;
        int numberOfInformationObjects;

        int sequenceLength = currentByte & 0x7f;
        if (isSequenceOfElements) {
            numberOfSequenceElements = sequenceLength;
            numberOfInformationObjects = 1;
        }
        else {
            numberOfInformationObjects = sequenceLength;
            numberOfSequenceElements = 1;
        }

        currentByte = is.readUnsignedByte();
        CauseOfTransmission causeOfTransmission = CauseOfTransmission.causeFor(currentByte & 0x3f);
        boolean test = byteHasMask(currentByte, 0x80);
        boolean negativeConfirm = byteHasMask(currentByte, 0x40);

        int originatorAddress;
        if (settings.getCotFieldLength() == 2) {
            originatorAddress = is.readUnsignedByte();
            aSduLength--;
        }
        else {
            originatorAddress = -1;
        }

        int commonAddress;
        if (settings.getCommonAddressFieldLength() == 1) {
            commonAddress = is.readUnsignedByte();
        }
        else {
            commonAddress = is.readUnsignedByte() | (is.readUnsignedByte() << 8);

            aSduLength--;
        }

        InformationNettyObject[] informationObjects;
        byte[] privateInformation;
        if (typeIdCode < 128) {

            informationObjects = new InformationNettyObject[numberOfInformationObjects];

            int ioaFieldLength = settings.getIoaFieldLength();
            for (int i = 0; i < numberOfInformationObjects; i++) {
                informationObjects[i] = InformationNettyObject.decode(is, typeId, numberOfSequenceElements, ioaFieldLength);
            }

            return new ASduNetty(typeId, isSequenceOfElements, causeOfTransmission, test, negativeConfirm, originatorAddress,
                    commonAddress, informationObjects);
        }
        else {
            privateInformation = new byte[aSduLength - 4];
            is.readBytes(privateInformation);
            return new ASduNetty(typeId, isSequenceOfElements, sequenceLength, causeOfTransmission, test, negativeConfirm,
                    originatorAddress, commonAddress, privateInformation);
        }

    }

    private static boolean byteHasMask(int b, int mask) {
        return (b & mask) == mask;
    }

    public ASduTypeNetty getTypeIdentification() {
        return aSduType;
    }

    public boolean isSequenceOfElements() {
        return isSequenceOfElements;
    }

    public int getSequenceLength() {
        return sequenceLength;
    }

    public CauseOfTransmission getCauseOfTransmission() {
        return causeOfTransmission;
    }

    public boolean isTestFrame() {
        return test;
    }

    public boolean isNegativeConfirm() {
        return negativeConfirm;
    }

    public Integer getOriginatorAddress() {
        return originatorAddress;
    }

    public int getCommonAddress() {
        return commonAddress;
    }

    public InformationNettyObject[] getInformationObjects() {
        return informationObjects;
    }

    public byte[] getPrivateInformation() {
        return privateInformation;
    }

    int encode(ByteBuf out, int i, ConnectionNettySettings settings) {

        int origi = i;
        out.writerIndex(i);
        
        out.writeByte(aSduType.getId());
        if (isSequenceOfElements) {
            out.writeByte((byte) (sequenceLength | 0x80));
        }
        else {
            out.writeByte((byte) sequenceLength);
        }

        if (test) {
            if (negativeConfirm) {
                out.writeByte((byte) (causeOfTransmission.getId() | 0xC0));
            }
            else {
                out.writeByte((byte) (causeOfTransmission.getId() | 0x80));
            }
        }
        else {
            if (negativeConfirm) {
                out.writeByte((byte) (causeOfTransmission.getId() | 0x40));
            }
            else {
                out.writeByte((byte) causeOfTransmission.getId());
            }
        }

        if (settings.getCotFieldLength() == 2) {
            out.writeByte((byte) originatorAddress);
        }

        out.writeByte((byte) commonAddress);

        if (settings.getCommonAddressFieldLength() == 2) {
            out.writeByte((byte) (commonAddress >> 8));
        }

        if (informationObjects != null) {
            for (InformationNettyObject informationObject : informationObjects) {
                i += informationObject.encode(out, i, settings.getIoaFieldLength());
            }
        }
        else {
            out.writeBytes(privateInformation);
            i += privateInformation.length;
        }
        return i - origi;
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder().append("ASDU Type: ")
                .append(aSduType.getId())
                .append(", ")
                .append(aSduType)
                .append(", ")
                .append(aSduType.getDescription())
                .append("\nCause of transmission: ")
                .append(causeOfTransmission)
                .append(", test: ")
                .append(isTestFrame())
                .append(", negative con: ")
                .append(isNegativeConfirm())
                .append("\nOriginator address: ")
                .append(originatorAddress)
                .append(", Common address: ")
                .append(commonAddress);

        if (informationObjects != null) {
            for (InformationNettyObject informationObject : informationObjects) {
                builder.append("\n").append(informationObject);
            }
        }
        else {
            builder.append("\nPrivate Information:\n");
            builder.append(HexUtils.bytesToHex(this.privateInformation));
        }

        return builder.toString();

    }

}