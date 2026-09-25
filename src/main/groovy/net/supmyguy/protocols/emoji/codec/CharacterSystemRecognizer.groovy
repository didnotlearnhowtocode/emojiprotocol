package net.supmyguy.protocols.emoji.codec

import static net.supmyguy.protocols.emoji.codec.CharacterSystem.*

class CharacterSystemRecognizer {

    static CharacterSystem recognize(byte lead) {
        int unsigned = lead & 0xFF

        if(isAscii(unsigned)) {
            return ASCII
        }
        else if(isTwoBytesUTF(unsigned)) {
            return UTF8_TWO_BYTES
        }
        else if(isThreeBytesUTF(unsigned)) {
            return UTF8_THREE_BYTES
        }
        else if (isFourBytesUTF(unsigned)) {
            return UTF8_FOUR_BYTES
        }
        else {
            return null
        }
    }

    private static boolean isAscii(int b) {
        return (b & 0x80) == 0
    }

    private static boolean isTwoBytesUTF(int b) {
        return (b & 0xE0) == 0xC0
    }

    private static boolean isThreeBytesUTF(int b) {
        return (b & 0xF0) == 0xE0
    }

    private static boolean isFourBytesUTF(int b) {
        return (b & 0xF8) == 0xF0
    }
}
