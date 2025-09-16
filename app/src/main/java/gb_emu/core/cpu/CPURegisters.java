package gb_emu.core.cpu;

public class CPURegisters {
    private static final int STACK_POINTER_INITIAL_ADDRESS = 0xfffe;
    private static final int PROGRAM_COUNTER_INITIAL_ADDRESS = 0x0000; // start at 0x0100
    private static final int AF_INITIAL_ADDRESS = 0x01b0;
    private static final int BC_INITIAL_ADDRESS = 0x0013;
    private static final int DE_INITIAL_ADDRESS = 0x00d8;
    private static final int HL_INITIAL_ADDRESS = 0x014d;
    private static final int A_INITIAL_ADDRESS_FOR_GBC = 0x11;

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
    private boolean halt;

    private int interruptEnable = 0x00; // IE register
    private int interruptFlags = 0xE1; // IF register

    public CPURegisters() {
        this.sp = STACK_POINTER_INITIAL_ADDRESS;
        this.pc = PROGRAM_COUNTER_INITIAL_ADDRESS;

        setAF(AF_INITIAL_ADDRESS);
        setBC(BC_INITIAL_ADDRESS);
        setDE(DE_INITIAL_ADDRESS);
        setHL(HL_INITIAL_ADDRESS);

    }

    public CPURegisters(boolean isGbc) {
        this.sp = STACK_POINTER_INITIAL_ADDRESS;
        this.pc = PROGRAM_COUNTER_INITIAL_ADDRESS;

        if (isGbc) {
            setA(A_INITIAL_ADDRESS_FOR_GBC);
        }

        setAF(AF_INITIAL_ADDRESS);
        setBC(BC_INITIAL_ADDRESS);
        setDE(DE_INITIAL_ADDRESS);
        setHL(HL_INITIAL_ADDRESS);

    }

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

    public boolean getHalt() {
        return halt;
    }

    public void setHalt(boolean value) {
        halt = value;
    }

    public void incrementPC() {
        this.pc = (pc + 1) & 0xffff; // ensure 16 bit
    }

    public void incrementPC(int value) {
        this.pc = (pc + value) & 0xffff; // ensure 16 bit
    }

    public void decrementPD() {
        this.pc = (pc - 1) & 0xffff;
    }

    public void incrementSP() {
        this.sp = (sp + 1) & 0xffff; // ensure 16 bit
    }

    public void incrementSP(int value) {
        this.sp = (sp + value) & 0xffff; // ensure 16 bit
    }

    public void decrementSP() {
        this.sp = (sp - 1) & 0xffff;
    }

    public void decrementSP(int value) {
        this.sp = (sp - value) & 0xffff;
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

    public int getInterruptEnable() {
        return interruptEnable & 0xFF;
    }

    public void setInterruptEnable(int value) {
        interruptEnable = value & 0xFF;
    }

    public int getInterruptFlags() {
        return interruptFlags & 0xFF;
    }

    public void setInterruptFlags(int value) {
        interruptFlags = value & 0xFF;
    }
}
