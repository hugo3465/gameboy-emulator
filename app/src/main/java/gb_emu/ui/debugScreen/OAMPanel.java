package gb_emu.ui.debugScreen;

import gb_emu.core.ppu.OAM;
import gb_emu.core.ppu.OAM.SpriteObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OAMPanel extends JPanel {

    private OAM oam;
    private JTable spriteTable;
    private DefaultTableModel tableModel;

    public OAMPanel(OAM oam) {
        this.oam = oam;
        setLayout(new BorderLayout());

        // Table columns
        String[] columnNames = {"Index", "X", "Y", "Tile", "Priority", "Flip X", "Flip Y", "Palette"};

        // Table model
        tableModel = new DefaultTableModel(columnNames, 0);
        spriteTable = new JTable(tableModel);
        spriteTable.setEnabled(false); // Make the table read-only

        // Add table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(spriteTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void updateOAM() {
        // Clear the table
        tableModel.setRowCount(0);

        // Get all sprites and populate the table
        List<SpriteObject> sprites = oam.getAllSprites();
        for (int i = 0; i < sprites.size(); i++) {
            SpriteObject sprite = sprites.get(i);
            tableModel.addRow(new Object[]{
                    i, // Index
                    sprite.getX(), // X position
                    sprite.getY(), // Y position
                    sprite.getTileNumber(), // Tile number
                    sprite.getPriority(), // Priority
                    sprite.getFlipX(), // Flip X
                    sprite.getFlipY(), // Flip Y
                    sprite.getPallete() // Palette
            });
        }
    }
}