package chuuuevi.github.io.server.biz;

import java.nio.charset.StandardCharsets;

public class LongToFixedWidthBytes {

    public static final int ARRAY_LENGTH = 20;
    private static final byte[] LONG_MIN = "-9223372036854775808".getBytes(StandardCharsets.UTF_8);

    private LongToFixedWidthBytes() {}

    /**
     * 将一个 long 值转换为固定长度为 20 的 byte 数组。
     * 数值的 ASCII 字符从数组末尾开始存放，前面不足的部分用空格填充。
     *
     * @param value 输入的 long 整数。
     * @return 一个长度为 20 的 byte 数组。
     */
    public static byte[] write(final long value, byte[] longBytes) {
        if (longBytes == null || longBytes.length < ARRAY_LENGTH) {
            throw new IllegalArgumentException("No exception should be thrown for an array with more than " + ARRAY_LENGTH + " elements.");
        }

        if (value == Long.MIN_VALUE) {
            System.arraycopy(LONG_MIN, 0, longBytes, 0, LONG_MIN.length);
            return longBytes;
        }

        int byteOffset = ARRAY_LENGTH - 1;
        final boolean negate = value < 0;
        boolean writeSign = false;
        long normalization = Math.abs(value);
        do {
            if (normalization == 0 && byteOffset == ARRAY_LENGTH - 1) {
                longBytes[byteOffset] = (byte) ('0');
                byteOffset--;
            }
            if (normalization > 0) {
                // to ASCII byte
                longBytes[byteOffset] = (byte) ('0' + (normalization % 10));
                byteOffset--;
                normalization /= 10;
            }
            if (normalization == 0) {
                if (negate && !writeSign) {
                    longBytes[byteOffset] = (byte) ('-');
                    byteOffset--;
                    writeSign = true;
                    continue;
                }
                longBytes[byteOffset] = (byte) (' ');
                byteOffset--;
            }
        } while (byteOffset >= 0);

        return longBytes;
    }

    public static void write(final long value, SetByte callback) {
        if (value == Long.MIN_VALUE) {
            for(int i =0; i < LONG_MIN.length; i++) {
                callback.set(i, LONG_MIN[i]);
            }
            return;
        }

        int byteOffset = ARRAY_LENGTH - 1;
        final boolean negate = value < 0;
        boolean writeSign = false;
        long normalization = Math.abs(value);
        do {
            if (normalization == 0 && byteOffset == ARRAY_LENGTH - 1) {
                callback.set(byteOffset, (byte) ('0'));
                byteOffset--;
            }
            if (normalization > 0) {
                // to ASCII byte
                callback.set(byteOffset, (byte) ('0' + (normalization % 10)));
                byteOffset--;
                normalization /= 10;
            }
            if (normalization == 0) {
                if (negate && !writeSign) {
                    callback.set(byteOffset,  (byte) ('-'));
                    byteOffset--;
                    writeSign = true;
                    continue;
                }
                callback.set(byteOffset,  (byte) (' '));
                byteOffset--;
            }
        } while (byteOffset >= 0);
    }

    public interface SetByte {
        void set(int index, byte value);
    }
}
