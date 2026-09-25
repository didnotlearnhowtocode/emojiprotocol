package net.supmyguy.protocols.emoji.codec

import net.supmyguy.protocols.emoji.domain.alphabet.EmojiAlphabet

import java.nio.charset.StandardCharsets

class EmojiDecoder {

    String decode(String encoded) {
        List<String> emojis = encoded.codePoints().collect { Integer it -> String.valueOf(Character.toChars(it)) }
        String combinedEmojisBitsSequence = emojis.collect { String it -> EmojiAlphabet.findEmojiBitsSequence(it) }.join("")

        int bitPaddingStart = combinedEmojisBitsSequence.length() - (combinedEmojisBitsSequence.length() % 8)
        String reducedEmojiBitsSequence = combinedEmojisBitsSequence.substring(0, bitPaddingStart)

        int decodedByteGroups = reducedEmojiBitsSequence.length() / 8
        List<String> decodedBytes = (0..<decodedByteGroups).collect { Integer groupId ->
            int leftmost = groupId * 8
            int rightmost = leftmost + 8
            reducedEmojiBitsSequence.substring(leftmost, rightmost)
        }

        StringBuilder decodedMessage = new StringBuilder()
        for (int i = 0; i < decodedBytes.size(); i++) {
            byte b = (byte) Integer.parseInt(decodedBytes[i], 2)
            byte[] decodedSymbolBytes = null
            CharacterSystem charSystem = CharacterSystemRecognizer.recognize(b)
            switch (charSystem) {
                case CharacterSystem.ASCII:
                    decodedSymbolBytes = new BigInteger(decodedBytes[i], 2).toByteArray()
                    break
                case CharacterSystem.UTF8_TWO_BYTES:
                    String combinedBits = decodedBytes[i] + decodedBytes[i + 1]
                    decodedSymbolBytes = new BigInteger(combinedBits, 2).toByteArray()
                    if(decodedSymbolBytes.length == 3) {
                        decodedSymbolBytes = new byte[]{decodedSymbolBytes[1], decodedSymbolBytes[2]}
                    }
                    i += 1
                    break
                case CharacterSystem.UTF8_THREE_BYTES:
                    String combinedBits = decodedBytes[i] + decodedBytes[i + 1] + decodedBytes[i + 2]
                    decodedSymbolBytes = new BigInteger(combinedBits, 2).toByteArray()
                    if(decodedSymbolBytes.length == 4) {
                        decodedSymbolBytes = new byte[]{decodedSymbolBytes[1], decodedSymbolBytes[2], decodedSymbolBytes[3]}
                    }
                    i += 2
                    break
                case CharacterSystem.UTF8_FOUR_BYTES:
                    String combinedBits = decodedBytes[i] + decodedBytes[i + 1] + decodedBytes[i + 2] + decodedBytes[i + 3]
                    decodedSymbolBytes = new BigInteger(combinedBits, 2).toByteArray()
                    if(decodedSymbolBytes.length == 5) {
                        decodedSymbolBytes = new byte[]{decodedSymbolBytes[1], decodedSymbolBytes[2], decodedSymbolBytes[3], decodedSymbolBytes[4]}
                    }
                    i += 3
                    break
                default:
                    break
            }

            if(decodedSymbolBytes) {
                decodedMessage << new String(decodedSymbolBytes, StandardCharsets.UTF_8)
            }
        }

        return decodedMessage.toString()
    }
}
