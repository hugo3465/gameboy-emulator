package gb_emu.ui.debugScreen;

import gb_emu.core.cpu.CPURegisters;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class CPURegistersPanel extends JPanel {

    private CPURegisters registers;
    private Map<String, JLabel> registerLabels;

    public CPURegistersPanel(CPURegisters registers) {
        this.registers = registers;
        this.registerLabels = new LinkedHashMap<>();

        setLayout(new GridLayout(0, 2, 10, 10)); // Two columns: Register Name and Value

        // Add all registers to the panel
        addRegister("A", registers.getA());
        addRegister("B", registers.getB());
        addRegister("C", registers.getC());
        addRegister("D", registers.getD());
        addRegister("E", registers.getE());
        addRegister("F", registers.getF());
        addRegister("H", registers.getH());
        addRegister("L", registers.getL());
        addRegister("SP", registers.getSP());
        addRegister("PC", registers.getPC());
        addRegister("BC", registers.getBC());
        addRegister("DE", registers.getDE());
        addRegister("HL", registers.getHL());
        addRegister("AF", registers.getAF());
    }

    private void addRegister(String name, int value) {
        JLabel nameLabel = new JLabel(name + ":");
        JLabel valueLabel = new JLabel(String.format("0x%04X", value)); // Display in hexadecimal format
        registerLabels.put(name, valueLabel);

        add(nameLabel);
        add(valueLabel);
    }

    public void updateRegisters() {
        // Update the values of all registers
        registerLabels.get("A").setText(String.format("0x%02X", registers.getA()));
        registerLabels.get("B").setText(String.format("0x%02X", registers.getB()));
        registerLabels.get("C").setText(String.format("0x%02X", registers.getC()));
        registerLabels.get("D").setText(String.format("0x%02X", registers.getD()));
        registerLabels.get("E").setText(String.format("0x%02X", registers.getE()));
        registerLabels.get("F").setText(String.format("0x%02X", registers.getF()));
        registerLabels.get("H").setText(String.format("0x%02X", registers.getH()));
        registerLabels.get("L").setText(String.format("0x%02X", registers.getL()));
        registerLabels.get("SP").setText(String.format("0x%04X", registers.getSP()));
        registerLabels.get("PC").setText(String.format("0x%04X", registers.getPC()));
        registerLabels.get("BC").setText(String.format("0x%04X", registers.getBC()));
        registerLabels.get("DE").setText(String.format("0x%04X", registers.getDE()));
        registerLabels.get("HL").setText(String.format("0x%04X", registers.getHL()));
        registerLabels.get("AF").setText(String.format("0x%04X", registers.getAF()));
    }
}
