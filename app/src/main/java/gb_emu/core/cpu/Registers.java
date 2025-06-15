package gb_emu.core.cpu;

public class Registers {

    /**
     * 8 Bit Registers
     */
    private int a, b, c, d, e, f, h, l;

    /**
     * 16 Bit Registers
     */
    private int sp; // stack pointer
    private int pc; // program counter

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }

    public int getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }

    public int getE() {
        return e;
    }

    public void setE(int e) {
        this.e = e;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getL() {
        return l;
    }

    public void setL(int l) {
        this.l = l;
    }

    public int getSP() {
        return sp;
    }

    public void setSP(int sp) {
        this.sp = sp;
    }

    public int getPC() {
        return pc;
    }

    public void setPC(int pc) {
        this.pc = pc;
    }

    public void incrementPC() {
        this.pc = (pc + 1) & 0xffff; // ensure 16 bit
    }

    public void decrementPC() {
        this.pc = (pc - 1) & 0xffff;
    }
}
