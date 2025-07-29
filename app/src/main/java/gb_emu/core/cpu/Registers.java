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

    /**
     * flags
     */
    private boolean zf; // zero flag
    private boolean nf; // subtraction flag
    private boolean cf; // carry flag
    private boolean hf; // half carry flag

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

    public int getBC() {
        return (b << 8) | c;
    }

    public void setBC(int value) {
        b = (value >> 8) & 0xFF;
        c = value & 0xFF;
    }

    public int getDE() {
        return (d << 8) | e;
    }

    public void setDE(int value) {
        d = (value >> 8) & 0xFF;
        e = value & 0xFF;
    }

    public int getHL() {
        return (h << 8) | l;
    }

    public void setHL(int value) {
        h = (value >> 8) & 0xFF;
        l = value & 0xFF;
    }

    public int getAF() {
        int f = 0;
        if (zf)
            f |= 0x80;
        if (nf)
            f |= 0x40;
        if (hf)
            f |= 0x20;
        if (cf)
            f |= 0x10;
        return (a << 8) | f;
    }

    public void setAF(int value) {
        a = (value >> 8) & 0xFF;
        int f = value & 0xF0; // Only upper 4 bits are used

        zf = (f & 0x80) != 0;
        nf = (f & 0x40) != 0;
        hf = (f & 0x20) != 0;
        cf = (f & 0x10) != 0;
    }

    public void incrementPC() {
        this.pc = (pc + 1) & 0xffff; // ensure 16 bit
    }

    public void decrementPC() {
        this.pc = (pc - 1) & 0xffff;
    }

    public void setRegister(String name, int value) {
        switch (name.toUpperCase()) {
            case "A":
                setA(value);
                break;
            case "B":
                setB(value);
                break;
            case "C":
                setC(value);
                break;
            case "D":
                setD(value);
                break;
            case "E":
                setE(value);
                break;
            case "F":
                setF(value);
                break;
            case "H":
                setH(value);
                break;
            case "L":
                setL(value);
                break;
            case "BC":
                setBC(value);
            case "DE":
                setDE(value);
            case "HL":
                setHL(value);
            case "AF":
                setAF(value);
            default:
                throw new IllegalArgumentException("Unknown register: " + name);
        }
    }

    public int getRegister(String name) {
        switch (name.toUpperCase()) {
            case "A":
                return getA();
            case "B":
                return getB();
            case "C":
                return getC();
            case "D":
                return getD();
            case "E":
                return getE();
            case "F":
                return getF();
            case "H":
                return getH();
            case "L":
                return getL();
            case "BC":
                return getBC();
            case "DE":
                return getDE();
            case "HL":
                return getHL();
            case "AF":
                return getAF();
            default:
                throw new IllegalArgumentException("Unknown register: " + name);
        }
    }

    public boolean getFlagZ() {
        return zf;
    }

    public void setFlagZ(boolean value) {
        zf = value;
    }

    public boolean getFlagN() {
        return nf;
    }

    public void setFlagN(boolean value) {
        nf = value;
    }

    public boolean getFlagC() {
        return cf;
    }

    public void setFlagC(boolean value) {
        cf = value;
    }

    public boolean getFlagH() {
        return hf;
    }

    public void setFlagH(boolean value) {
        hf = value;
    }
}
