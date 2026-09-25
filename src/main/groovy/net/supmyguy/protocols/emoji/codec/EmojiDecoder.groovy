package net.supmyguy.protocols.emoji.codec

import net.supmyguy.protocols.emoji.domain.alphabet.EmojiAlphabet

class EmojiDecoder {

    String decode(String encoded) {
        List<String> emojis = encoded.codePoints().collect { Integer it -> String.valueOf(Character.toChars(it)) }
        String combinedEmojisBitsSequence = emojis.collect { String it -> EmojiAlphabet.findEmojiBitsSequence(it) }.join("")
        int bitPaddingStart = combinedEmojisBitsSequence.length() - (combinedEmojisBitsSequence.length() % 8)
        String reducedEmojiBitsSequence = combinedEmojisBitsSequence.substring(0, bitPaddingStart)
        int decodedByteGroups = reducedEmojiBitsSequence.length() / 8
        String decoded = (0..<decodedByteGroups).collect { Integer groupId ->
            int leftmost = groupId * 8
            int rightmost = leftmost + 8
            String bits = reducedEmojiBitsSequence.substring(leftmost, rightmost)
            return "${(char) Integer.parseInt(bits, 2)}"
        }.join("")
        return decoded
    }
}
