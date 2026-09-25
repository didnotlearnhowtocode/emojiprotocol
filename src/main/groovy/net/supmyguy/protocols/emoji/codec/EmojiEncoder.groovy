package net.supmyguy.protocols.emoji.codec

import net.supmyguy.protocols.emoji.domain.alphabet.EmojiAlphabet

import java.nio.charset.StandardCharsets

class EmojiEncoder {

    String encode(String decoded) {
        return convertIntoEmojiBitGroups(decoded.getBytes(StandardCharsets.UTF_8)).collect { String it -> EmojiAlphabet.findEmojiByBits(it) }.join("")
    }

    private List<String> convertIntoEmojiBitGroups(byte[] messageBytes) {
        List<String> groups = []
        String bitsPadding = "0".repeat(6 - (messageBytes.length * 8) % 6)
        String formattedMessageBitsSequence = messageBytes.collect { Byte b -> formatIntoUnsignedByte(b) }.join("") + bitsPadding
        int groupsCount = formattedMessageBitsSequence.length() / 6
        (0..<groupsCount).each { Integer iSextet ->
            int leftmost = iSextet * 6
            int rightmost = leftmost + 6
            groups << formattedMessageBitsSequence.substring(leftmost, rightmost)
        }
        return groups
    }

    private String formatIntoUnsignedByte(byte b) {
        int unsigned = b & 0xFF
        return String.format("%8s", Integer.toBinaryString(unsigned)).replace(' ', '0')
    }
}
