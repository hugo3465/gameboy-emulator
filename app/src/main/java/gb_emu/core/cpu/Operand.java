package gb_emu.core.cpu;

/**
 * Class that represents any argument
 */
public class Operand {
    public enum Type { REGISTER, IMMEDIATE, MEMORY }

    private Type type;
    private String registerName; // to REGISTER
    private int value;           // to IMMEDIATE or MEMORY address

    public Operand(Type type, String registerName, int value) {
        this.type = type;
        this.registerName = registerName;
        this.value = value;
    }

    public static Operand register(String name) {
        return new Operand(Type.REGISTER, name, 0);
    }

    public static Operand immediate(int value) {
        return new Operand(Type.IMMEDIATE, null, value);
    }

    public static Operand memory(int address) {
        return new Operand(Type.MEMORY, null, address);
    }

    public Type getType() { return type; }

    public String getRegisterName() { return registerName; }

    public int getValue() { return value; }
}
