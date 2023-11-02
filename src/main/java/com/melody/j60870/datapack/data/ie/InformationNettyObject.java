package com.melody.j60870.datapack.data.ie;

import com.melody.j60870.datapack.data.ASduTypeNetty;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Every Information Object contains:
 * <ul>
 * <li>The Information Object Address (IOA) that is 1, 2 or 3 bytes long.</li>
 * <li>A set of Information Elements or a sequence of information element sets. The type of information elements in the
 * set and their order depend on the ASDU's TypeId and is the same for all information objects within one ASDU. If the
 * sequence bit is set in the ASDU then the ASDU contains a single Information Object containing a sequence of
 * information element sets. If the sequence bit is not set the ASDU contains a sequence of information objects each
 * containing only single information elements sets.</li>
 * </ul>
 */

public class InformationNettyObject {
    
    private final int informationObjectAddress;
    private final InformationNettyElement[][] informationElements;
    
    public InformationNettyObject(int informationObjectAddress, InformationNettyElement[][] informationElements) {
        this.informationObjectAddress = informationObjectAddress;
        this.informationElements = informationElements;
    }
    
    public InformationNettyObject(int informationObjectAddress, InformationNettyElement... informationElement) {
        this(informationObjectAddress, new InformationNettyElement[][] { informationElement });
    }
    
    public static InformationNettyObject decode(ByteBuf is, ASduTypeNetty aSduType, int numberOfSequenceElements,
                                                int ioaFieldLength) throws IOException {
        InformationNettyElement[][] informationElements = new InformationNettyElement[0][0];
        
        int informationObjectAddress = readInformationObjectAddress(is, ioaFieldLength);
        
        switch (aSduType) {
            // 1
            case M_SP_NA_1:

                informationElements = new InformationNettyElement[numberOfSequenceElements][1];
                for (int i = 0; i < numberOfSequenceElements; i++) {
                    informationElements[i][0] = new IeSinglePointWithQualityNetty(is);
                }
                break;
//            // 2
            case M_SP_TA_1:
                informationElements = new InformationNettyElement[][] { { new IeSinglePointWithQualityNetty(is), new IeTime24Netty(is) } };
                break;
            // 3
            case M_DP_NA_1:
                informationElements = new InformationNettyElement[numberOfSequenceElements][1];
                for (int i = 0; i < numberOfSequenceElements; i++) {
                    informationElements[i][0] = new IeDoublePointWithQualityNetty(is);
                }
                break;
            // 4
            case M_DP_TA_1:
                informationElements = new InformationNettyElement[][] { { new IeDoublePointWithQualityNetty(is), new IeTime24Netty(is) } };
                break;
            // 5
            case M_ST_NA_1:
                informationElements = new InformationNettyElement[numberOfSequenceElements][2];
                for (int i = 0; i < numberOfSequenceElements; i++) {
                    informationElements[i][0] = new IeValueWithTransientStateNetty(is);
                    informationElements[i][1] = new IeQualityNetty(is);
                }
                break;
            // 6
            case M_ST_TA_1:
                informationElements = new InformationNettyElement[][] {
                        { new IeValueWithTransientStateNetty(is), new IeQualityNetty(is), new IeTime24Netty(is) } };
                break;
            // 7
            case M_BO_NA_1:
                informationElements = new InformationNettyElement[numberOfSequenceElements][2];
                for (int i = 0; i < numberOfSequenceElements; i++) {
                    informationElements[i][0] = new IeBinaryStateInformationNetty(is);
                    informationElements[i][1] = new IeQualityNetty(is);
                }
                break;
            // 8
            case M_BO_TA_1:
                informationElements = new InformationNettyElement[][] {
                        { new IeBinaryStateInformationNetty(is), new IeQualityNetty(is), new IeTime24Netty(is) } };
                break;
            // 9
            case M_ME_NA_1:
                informationElements = new InformationNettyElement[numberOfSequenceElements][2];
                for (InformationNettyElement[] informationElementCombination : informationElements) {
                    informationElementCombination[0] = new IeNormalizedValueNetty(is);
                    informationElementCombination[1] = new IeQualityNetty(is);
                }
                break;
            // 10
            case M_ME_TA_1:
                informationElements = new InformationNettyElement[][] {
                        { new IeNormalizedValueNetty(is), new IeQualityNetty(is), new IeTime24Netty(is) } };
                break;
            // 11
            case M_ME_NB_1:
                informationElements = new InformationNettyElement[numberOfSequenceElements][2];
                for (InformationNettyElement[] informationElementCombination : informationElements) {
                    informationElementCombination[0] = new IeScaledValueNetty(is);
                    informationElementCombination[1] = new IeQualityNetty(is);
                }
                break;
            // 12
            case M_ME_TB_1:
                informationElements = new InformationNettyElement[][] {
                        { new IeScaledValueNetty(is), new IeQualityNetty(is), new IeTime24Netty(is) } };
                break;
            // 13
            case M_ME_NC_1:
                informationElements = new InformationNettyElement[numberOfSequenceElements][2];
                for (InformationNettyElement[] informationElementCombination : informationElements) {
                    informationElementCombination[0] = new IeShortFloatNetty(is);
                    informationElementCombination[1] = new IeQualityNetty(is);
                }
                break;
            // 14
            case M_ME_TC_1:
                informationElements = new InformationNettyElement[][] {
                        { new IeShortFloatNetty(is), new IeQualityNetty(is), new IeTime24Netty(is) } };
                break;
            // 15
            case M_IT_NA_1:
                informationElements = new InformationNettyElement[numberOfSequenceElements][1];
                for (InformationNettyElement[] informationElementCombination : informationElements) {
                    informationElementCombination[0] = IeBinaryCounterReadingNetty.decode(is);
                }
                break;
            // 16
            case M_IT_TA_1:
                informationElements = new InformationNettyElement[][] {
                        { IeBinaryCounterReadingNetty.decode(is), new IeTime24Netty(is) } };
                break;
            // 17
            case M_EP_TA_1:
                informationElements = new InformationNettyElement[][] {
                        { new IeSingleProtectionEventNetty(is), new IeTime16Netty(is), new IeTime24Netty(is) } };
                break;
            // 18
            case M_EP_TB_1:
                informationElements = new InformationNettyElement[][] { { new IeProtectionStartEventNetty(is),
                        new IeProtectionQualityNetty(is), new IeTime16Netty(is), new IeTime24Netty(is) } };
                break;
            // 19
            case M_EP_TC_1:
                informationElements = new InformationNettyElement[][] { { new IeProtectionOutputCircuitInformationNetty(is),
                        new IeProtectionQualityNetty(is), new IeTime16Netty(is), new IeTime24Netty(is) } };
                break;
            // 20
            case M_PS_NA_1:
                informationElements = new InformationNettyElement[numberOfSequenceElements][2];
                for (InformationNettyElement[] informationElementCombination : informationElements) {
                    informationElementCombination[0] = new IeStatusAndStatusChangesNetty(is);
                    informationElementCombination[1] = new IeQualityNetty(is);
                }
                break;
            // 21
            case M_ME_ND_1:
                informationElements = new InformationNettyElement[numberOfSequenceElements][1];
                for (InformationNettyElement[] informationElementCombination : informationElements) {
                    informationElementCombination[0] = new IeNormalizedValueNetty(is);
                }
                break;
            // 30
            case M_SP_TB_1:
                informationElements = new InformationNettyElement[][] {
                        { new IeSinglePointWithQualityNetty(is), IeTime56Netty.decode(is) } };
                break;
            // 31
            case M_DP_TB_1:
                informationElements = new InformationNettyElement[][] {
                        { new IeDoublePointWithQualityNetty(is), IeTime56Netty.decode(is) } };
                break;
            // 32
            case M_ST_TB_1:
                informationElements = new InformationNettyElement[][] {
                        { new IeValueWithTransientStateNetty(is), new IeQualityNetty(is), IeTime56Netty.decode(is) } };
                break;
            // 33
            case M_BO_TB_1:
                informationElements = new InformationNettyElement[][] {
                        { new IeBinaryStateInformationNetty(is), new IeQualityNetty(is), IeTime56Netty.decode(is) } };
                break;
            // 34
            case M_ME_TD_1:
                informationElements = new InformationNettyElement[][] {
                        { new IeNormalizedValueNetty(is), new IeQualityNetty(is), IeTime56Netty.decode(is) } };
                break;
            // 35
            case M_ME_TE_1:
                informationElements = new InformationNettyElement[][] {
                        { new IeScaledValueNetty(is), new IeQualityNetty(is), IeTime56Netty.decode(is) } };
                break;
            // 36
            case M_ME_TF_1:
                informationElements = new InformationNettyElement[][] {
                        { new IeShortFloatNetty(is), new IeQualityNetty(is), IeTime56Netty.decode(is) } };
                break;
            // 37
            case M_IT_TB_1:
                informationElements = new InformationNettyElement[][] {
                        { IeBinaryCounterReadingNetty.decode(is), IeTime56Netty.decode(is) } };
                break;
            // 38
            case M_EP_TD_1:
                informationElements = new InformationNettyElement[][] {
                        { new IeSingleProtectionEventNetty(is), new IeTime16Netty(is), IeTime56Netty.decode(is) } };
                break;
            // 39
            case M_EP_TE_1:
                informationElements = new InformationNettyElement[][] { { new IeProtectionStartEventNetty(is),
                        new IeProtectionQualityNetty(is), new IeTime16Netty(is), IeTime56Netty.decode(is) } };
                break;
            // 40
            case M_EP_TF_1:
                informationElements = new InformationNettyElement[][] { { new IeProtectionOutputCircuitInformationNetty(is),
                        new IeProtectionQualityNetty(is), new IeTime16Netty(is), IeTime56Netty.decode(is) } };
                break;
            // 45
            case C_SC_NA_1:
                informationElements = new InformationNettyElement[][] { { new IeSingleCommandNetty(is) } };
                break;
            // 46
            case C_DC_NA_1:
                informationElements = new InformationNettyElement[][] { { new IeDoubleCommandNetty(is) } };
                break;
            // 47
            case C_RC_NA_1:
                informationElements = new InformationNettyElement[][] { { new IeRegulatingStepCommandNetty(is) } };
                break;
            // 48
            case C_SE_NA_1:
                informationElements = new InformationNettyElement[][] {
                        { new IeNormalizedValueNetty(is), new IeQualifierOfSetPointCommandNetty(is) } };
                break;
            // 49
            case C_SE_NB_1:
                informationElements = new InformationNettyElement[][] {
                        { new IeScaledValueNetty(is), new IeQualifierOfSetPointCommandNetty(is) } };
                break;
            // 50
            case C_SE_NC_1:
                informationElements = new InformationNettyElement[][] {
                        { new IeShortFloatNetty(is), new IeQualifierOfSetPointCommandNetty(is) } };
                break;
            // 51
            case C_BO_NA_1:
                informationElements = new InformationNettyElement[][] { { new IeBinaryStateInformationNetty(is) } };
                break;
            // 58
            case C_SC_TA_1:
                informationElements = new InformationNettyElement[][] { { new IeSingleCommandNetty(is), IeTime56Netty.decode(is) } };
                break;
            // 59
            case C_DC_TA_1:
                informationElements = new InformationNettyElement[][] { { new IeDoubleCommandNetty(is), IeTime56Netty.decode(is) } };
                break;
            // 60
            case C_RC_TA_1:
                informationElements = new InformationNettyElement[][] {
                        { new IeRegulatingStepCommandNetty(is), IeTime56Netty.decode(is) } };
                break;
            // 61
            case C_SE_TA_1:
                informationElements = new InformationNettyElement[][] {
                        { new IeNormalizedValueNetty(is), new IeQualifierOfSetPointCommandNetty(is), IeTime56Netty.decode(is) } };
                break;
            // 62
            case C_SE_TB_1:
                informationElements = new InformationNettyElement[][] {
                        { new IeScaledValueNetty(is), new IeQualifierOfSetPointCommandNetty(is), IeTime56Netty.decode(is) } };
                break;
            // 63
            case C_SE_TC_1:
                informationElements = new InformationNettyElement[][] {
                        { new IeShortFloatNetty(is), new IeQualifierOfSetPointCommandNetty(is), IeTime56Netty.decode(is) } };
                break;
            // 64
            case C_BO_TA_1:
                informationElements = new InformationNettyElement[][] {
                        { new IeBinaryStateInformationNetty(is), IeTime56Netty.decode(is) } };
                break;
            // 70
            case M_EI_NA_1:
                informationElements = new InformationNettyElement[][] { { new IeCauseOfInitializationNetty(is) } };
                break;
            // 100
            case C_IC_NA_1:
                informationElements = new InformationNettyElement[][] { { new IeQualifierOfInterrogationNetty(is) } };
                break;
            // 101
            case C_CI_NA_1:
                informationElements = new InformationNettyElement[][] { { new IeQualifierOfCounterInterrogationNetty(is) } };
                break;
            // 102
            case C_RD_NA_1:
                informationElements = new InformationNettyElement[0][0];
                break;
            // 103
            case C_CS_NA_1:
                informationElements = new InformationNettyElement[][] { { IeTime56Netty.decode(is) } };
                break;
            // 104
            case C_TS_NA_1:
                informationElements = new InformationNettyElement[][] { { new IeFixedTestBitPattern(is) } };
                break;
            // 105
            case C_RP_NA_1:
                informationElements = new InformationNettyElement[][] { { new IeQualifierOfResetProcessCommandNetty(is) } };
                break;
            // 106
            case C_CD_NA_1:
                informationElements = new InformationNettyElement[][] { { new IeTime16Netty(is) } };
                break;
            // 107
            case C_TS_TA_1:
                informationElements = new InformationNettyElement[][] {
                        { IeTestSequenceCounterNetty.decode(is), IeTime56Netty.decode(is) } };
                break;
            // 110
            case P_ME_NA_1:
                informationElements = new InformationNettyElement[][] {
                        { new IeNormalizedValueNetty(is), new IeQualifierOfParameterOfMeasuredValuesNetty(is) } };
                break;
            // 111
            case P_ME_NB_1:
                informationElements = new InformationNettyElement[][] {
                        { new IeScaledValueNetty(is), new IeQualifierOfParameterOfMeasuredValuesNetty(is) } };
                break;
            // 112
            case P_ME_NC_1:
                informationElements = new InformationNettyElement[][] {
                        { new IeShortFloatNetty(is), new IeQualifierOfParameterOfMeasuredValuesNetty(is) } };
                break;
            // 113
            case P_AC_NA_1:
                informationElements = new InformationNettyElement[][] { { IeQualifierOfParameterActivationNetty.decode(is) } };
                break;
            // 120
            case F_FR_NA_1:
                informationElements = new InformationNettyElement[][] {
                        { IeNameOfFileNetty.decode(is), IeLengthOfFileOrSectionNetty.decode(is), IeFileReadyQualifierNetty.decode(is) } };
                break;
            // 121
            case F_SR_NA_1:
                informationElements = new InformationNettyElement[][] { { IeNameOfFileNetty.decode(is), IeNameOfSectionNetty.decode(is),
                        IeLengthOfFileOrSectionNetty.decode(is), IeSectionReadyQualifierNetty.decode(is) } };
                break;
            // 122
            case F_SC_NA_1:
                informationElements = new InformationNettyElement[][] {
                        { IeNameOfFileNetty.decode(is), IeNameOfSectionNetty.decode(is), IeSelectAndCallQualifierNetty.decode(is) } };
                break;
            // 123
            case F_LS_NA_1:
                return new InformationNettyObject(informationObjectAddress, IeNameOfFileNetty.decode(is), IeNameOfSectionNetty.decode(is),
                        IeLastSectionOrSegmentQualifierNetty.decode(is), IeChecksumNetty.decode(is));
            // 124
            case F_AF_NA_1:
                return new InformationNettyObject(informationObjectAddress, IeNameOfFileNetty.decode(is), IeNameOfSectionNetty.decode(is),
                        IeAckFileOrSectionQualifierNetty.decode(is));

            // 125
            case F_SG_NA_1:
                return new InformationNettyObject(informationObjectAddress, IeNameOfFileNetty.decode(is), IeNameOfSectionNetty.decode(is),
                        new IeFileSegmentNetty(is));

            // 126
            case F_DR_TA_1:
                informationElements = new InformationNettyElement[numberOfSequenceElements][];
                for (int i = 0; i < numberOfSequenceElements; i++) {
                    informationElements[i] = valuesAsArray(IeNameOfFileNetty.decode(is), IeLengthOfFileOrSectionNetty.decode(is),
                            IeStatusOfFileNetty.decode(is), IeTime56Netty.decode(is));
                }
                break;
            // 127
            case F_SC_NB_1:
                return new InformationNettyObject(informationObjectAddress, IeNameOfFileNetty.decode(is), IeTime56Netty.decode(is),
                        IeTime56Netty.decode(is));
            default:
                throw new IOException(
                        "Unable to parse Information Object because of unknown Type Identification: " + aSduType);
        }
        
        return new InformationNettyObject(informationObjectAddress, informationElements);
        
    }
    
    private static InformationNettyElement[] valuesAsArray(InformationNettyElement... informationElements) {
        return informationElements;
    }
    
    private static int readInformationObjectAddress(ByteBuf is, int ioaFieldLength) throws IOException {
        int informationObjectAddress = 0;
        for (int i = 0; i < ioaFieldLength; i++) {
            informationObjectAddress |= (is.readUnsignedByte() << (8 * i));
        }
        return informationObjectAddress;
    }
    
    public int encode(ByteBuf buffer, int i, int ioaFieldLength) {
        int origi = i;
        buffer.writeByte((byte) informationObjectAddress);
        if (ioaFieldLength > 1) {
            buffer.writeByte((byte) (informationObjectAddress >> 8));
            if (ioaFieldLength > 2) {
                buffer.writeByte((byte) (informationObjectAddress >> 16));
            }
        }
        
        for (InformationNettyElement[] informationElementCombination : informationElements) {
            for (InformationNettyElement informationElement : informationElementCombination) {
                i += informationElement.encode(buffer, i);
            }
        }
        
        return i - origi;
    }
    
    public int getInformationObjectAddress() {
        return informationObjectAddress;
    }
    
    /**
     * Returns the information elements as a two dimensional array. The first dimension of the array is the index of the
     * sequence of information element sets. The second dimension is the index of the information element set. For
     * example an information object containing a single set of three information elements will have the dimension
     * [1][3]. Note that you will have to cast the returned <code>InformationElement</code>s to a concrete
     * implementation in order to access the data inside them.
     *
     * @return the information elements as a two dimensional array.
     */
    public InformationNettyElement[][] getInformationElements() {
        return informationElements;
    }
    
    @Override
    public String toString() {
        
        StringBuilder builder = new StringBuilder("IOA: " + informationObjectAddress);
        
        if (informationElements.length > 1) {
            int i = 1;
            for (InformationNettyElement[] informationElementSet : informationElements) {
                builder.append("\nInformation Element Set ").append(i).append(':');
                for (InformationNettyElement informationElement : informationElementSet) {
                    builder.append('\n');
                    builder.append(informationElement.toString());
                }
                i++;
            }
        }
        else {
            for (InformationNettyElement[] informationElementSet : informationElements) {
                for (InformationNettyElement informationElement : informationElementSet) {
                    builder.append("\n");
                    builder.append(informationElement.toString());
                }
            }
        }
        
        return builder.toString();
        
    }

}