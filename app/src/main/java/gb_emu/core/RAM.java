package gb_emu.core;

public class RAM {
    private int RAM_CAPACITY = 32 * 1024;
    private int RAM_MAX_VALUE = 255;

    private int[] mem = new int[RAM_CAPACITY];

    public RAM() {
        for(int i = 0; i < mem.length; i++) {
            mem[i] = (int) (Math.random() * RAM_MAX_VALUE); 
        }
    }

    public void write(int position, int value) {
        mem[position] = value;
    }

    public int read(int position) {
        return mem[position];
    }

}