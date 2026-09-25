package net.supmyguy.protocols.emoji.codec

import spock.lang.Specification

class EmojiCodecTest extends Specification {

    EmojiCodec codec

    def setup() {
        codec = new EmojiCodec().tap { EmojiCodec it ->
            it.encoder = new EmojiEncoder()
            it.decoder = new EmojiDecoder()
        }
    }

    def 'should encode and decode correctly basic ascii-only message'() {
        when:
        String encoded = codec.encode(ascii)
        String decoded = codec.decode(encoded)

        then:
        encoded != ascii
        decoded == expected

        where:
        ascii                                          || expected
        "Hello, world!"                                || "Hello, world!"
        "The quick brown fox jumps over the lazy dog." || "The quick brown fox jumps over the lazy dog."
        "1234567890 !@#\$%^&*()_+-=[]{};:',.<>/?"      || "1234567890 !@#\$%^&*()_+-=[]{};:',.<>/?"
    }

    def 'should encode and decode correctly 2 bytes UTF-8 polish characters message'() {
        when:
        String encoded = codec.encode(utf8)
        String decoded = codec.decode(encoded)

        then:
        encoded != utf8
        decoded == expected

        where:
        utf8                                        || expected
        "Zażółć gęślą jaźń."                        || "Zażółć gęślą jaźń."
        "Pchnąć w tę łódź jeża lub ośm skrzyń fig." || "Pchnąć w tę łódź jeża lub ośm skrzyń fig."
        "ĄĆĘŁŃÓŚŹŻ ąćęłńóśźż"                       || "ĄĆĘŁŃÓŚŹŻ ąćęłńóśźż"
    }

    def 'should encode and decode correctly 2 bytes UTF-8 foreign languages characters message'() {
        when:
        String encoded = codec.encode(utf8)
        String decoded = codec.decode(encoded)

        then:
        encoded != utf8
        decoded == expected

        where:
        utf8                                                    || expected
        "C'est déjà l'été. Où êtes-vous ?"                      || "C'est déjà l'été. Où êtes-vous ?"
        "Über den Wolken gibt es keinen Regen."                 || "Über den Wolken gibt es keinen Regen."
        "È possibile mangiare crème brûlée después de la cena." || "È possibile mangiare crème brûlée después de la cena."
        "Smørrebrød, fjäll, garçon, naïve, façade."             || "Smørrebrød, fjäll, garçon, naïve, façade."
    }

    def 'should encode and decode correctly 3 bytes UTF-8 characters message'() {
        when:
        String encoded = codec.encode(utf8)
        String decoded = codec.decode(encoded)

        then:
        encoded != utf8
        decoded == expected

        where:
        utf8                                  || expected
        "こんにちは世界"                      || "こんにちは世界"
        "你好，世界"                           || "你好，世界"
        "안녕하세요 세계"                            || "안녕하세요 세계"
        "Привет, мир!"                        || "Привет, мир!"
        "مرحبا بالعالم"                       || "مرحبا بالعالم"
        "Hello 世界! Привет мир! こんにちは!" || "Hello 世界! Привет мир! こんにちは!"
    }

    def 'should encode and decode correctly 4 bytes UTF-8 characters message'() {
        when:
        String encoded = codec.encode(utf8)
        String decoded = codec.decode(encoded)

        then:
        encoded != utf8
        decoded == expected

        where:
        utf8                  || expected
        "😀 😃 😄 😁 😆 😅 😂 🤣 😊 😇" || "😀 😃 😄 😁 😆 😅 😂 🤣 😊 😇"
        'Hello 😀 world!'      || "Hello 😀 world!"
        "🚀 🌍 🌎 🌏 🛰️ 👨‍💻 💻 🔥"  || "🚀 🌍 🌎 🌏 🛰️ 👨‍💻 💻 🔥"
        "I ❤️ programming 🤓"  || "I ❤️ programming 🤓"
        "𐍈 𐐷 𝄞 𝕏 𝟘 𝒜"         || "𐍈 𐐷 𝄞 𝕏 𝟘 𝒜"
    }

    def 'should encode and decode correctly 2 or 3 or 4 bytes or combined UTF-8 characters message'() {
        when:
        String encoded = codec.encode(utf8)
        String decoded = codec.decode(encoded)

        then:
        encoded != utf8
        decoded == expected

        where:
        utf8                                                               || expected
        "Hello, 世界! Привет! こんにちは! 안녕하세요! Zażółć gęślą jaźń! 😀 🚀 𐍈" || "Hello, 世界! Привет! こんにちは! 안녕하세요! Zażółć gęślą jaźń! 😀 🚀 𐍈"
        "Java → UTF-8 → emoji → UTF-8 → Java 😎"                            || "Java → UTF-8 → emoji → UTF-8 → Java 😎"
    }

    def 'should encode and decode correctly edge cases messages'() {
        when:
        String encoded = codec.encode(utf8)
        String decoded = codec.decode(encoded)

        then:
        encoded != utf8
        decoded == expected

        where:
        utf8                   || expected
        ""                     || ""
        "a"                    || "a"
        "ą"                    || "ą"
        "€ "                   || "€ "
        "😀 "                   || "😀 "
        "ąąąąąąąąąąąąąąąąąą"   || "ąąąąąąąąąąąąąąąąąą"
        "€€€€€€€€€€€€€€€€€€€€" || "€€€€€€€€€€€€€€€€€€€€"
        "😀😀😀😀😀😀😀😀😀😀"           || "😀😀😀😀😀😀😀😀😀😀"
    }

    def 'should encode and decode correctly whitespaces and control characters'() {
        when:
        String encoded = codec.encode(utf8)
        String decoded = codec.decode(encoded)

        then:
        encoded != utf8
        decoded == expected

        where:
        utf8                                || expected
        "Hello world"                       || "Hello world"
        "Hello  world"                      || "Hello  world"
        "   leading and trailing spaces   " || "   leading and trailing spaces   "
        "Hello\nworld"                      || "Hello\nworld"
        "\"quoted text\""                   || "\"quoted text\""
        "'text with apostrophes'"           || "'text with apostrophes'"
    }

    def 'should encode and decode correctly code or json or other structured messages'() {
        when:
        String encoded = codec.encode(utf8)
        String decoded = codec.decode(encoded)

        then:
        encoded != utf8
        decoded == expected

        where:
        [utf8, expected] << [
                [
                        """public static void main(String[] args) {
System.out.println("Hello, 世界! 😀");}""",
                        """public static void main(String[] args) {
System.out.println("Hello, 世界! 😀");}"""
                ],
                [
                        """{
    "message": "Zażółć gęślą jaźń 🚀",
    "status": "OK",
    "value": 42
}
""",
                        """{
    "message": "Zażółć gęślą jaźń 🚀",
    "status": "OK",
    "value": 42
}
"""
                ]
        ]
    }

    def 'should encode and decode correctly - evil message'() {
        when:
        String encoded = codec.encode(utf8)
        String decoded = codec.decode(encoded)

        then:
        encoded != utf8
        decoded == expected

        where:
        utf8               || expected
        "Aą€😀Bć£🚀C你好D𐍈E" || "Aą€😀Bć£🚀C你好D𐍈E"
    }
}
