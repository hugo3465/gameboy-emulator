package gb_emu.core.mem;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RAM implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(RAM.class);

    private int[] mem;
    private int offset;

    public RAM(int length, int offset) {
        this.mem = new int[length];
        this.offset = offset;
    }

    public void write(int address, int value) {
        mem[address - offset] = value;
    }

    public int read(int address) {
        int index = address - offset;
        if (index < 0 || index >= mem.length) {
            // LOGGER.warn("RAM read out of bounds at 0x" + Integer.toHexString(address));
            return 0xFF;
        }
        return mem[index];
    }

}