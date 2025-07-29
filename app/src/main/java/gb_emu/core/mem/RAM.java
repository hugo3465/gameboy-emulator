package gb_emu.core.mem;

import java.io.Serializable;

public class RAM implements Serializable {
    private int[] mem;
    private int length;
    private int offset;

    public RAM(int length, int offset) {
        this.mem = new int[length];
        this.length = length;
        this.offset = offset;
    }

    public void write(int address, int value) {
        mem[address - offset] = value;
    }

    public int read(int address) {
        int index = address - offset;
        if (index < 0 || index >= mem.length) {
            throw new IndexOutOfBoundsException("Address: " + address);
        }
        return mem[index];
    }

}