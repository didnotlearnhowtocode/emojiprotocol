package net.supmyguy.protocols.codec

import net.supmyguy.protocols.domain.alphabet.EmojiAlphabet

class EmojiEncoder {

    String encode(byte[] messageBytes) {
        return convertIntoSextets(messageBytes).collect { String it -> EmojiAlphabet.findEmojiByBits(it) }.join("")
    }

    private List<String> convertIntoSextets(byte[] messageBytes) {
        List<String> sextets = []
        String bitsPadding = "0".repeat(6 - (messageBytes.length * 8) % 6)
        String formattedMessageBitsSequence = messageBytes.collect { Byte b -> formatIntoUnsignedByte(b) }.join("") + bitsPadding
        int sextetsCount = formattedMessageBitsSequence.length() / 6
        (0..<sextetsCount).each { Integer iSextet ->
            int leftmost = iSextet * 6
            int rightmost = leftmost + 6
            sextets << formattedMessageBitsSequence.substring(leftmost, rightmost)
        }
        return sextets
    }

    private String formatIntoUnsignedByte(byte b) {
        int unsigned = b & 0xFF
        return String.format("%8s", Integer.toBinaryString(unsigned)).replace(' ', '0')
    }
}
