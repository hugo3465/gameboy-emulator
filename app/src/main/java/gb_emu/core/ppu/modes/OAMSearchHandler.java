package gb_emu.core.ppu.modes;

import java.util.ArrayList;
import java.util.List;

import gb_emu.core.ppu.PPURegisters;
import gb_emu.core.ppu.OAM;
import gb_emu.core.ppu.OAM.SpriteObject;

public class OAMSearchHandler implements PPUMode {
    private OAM oam;
    private PPURegisters registers;
    private List<SpriteObject> visibleSprites;

    public OAMSearchHandler(OAM oam, PPURegisters registers) {
        this.oam = oam;
        this.registers = registers;
        this.visibleSprites = new ArrayList<>();
    }

    /**
     * Will get all visible Sprites from the OAM
     */
    @Override
    public void tick() {
        int LCDC = registers.getLCDC();
        int LY = registers.getLY();
        int spriteHeight = ((LCDC & 0x04) != 0) ? 16 : 8; // 8 or 16 px height
        List<SpriteObject> sprites = oam.getAllSprites();

        for (SpriteObject obj : sprites) {
            if (LY >= obj.getY() && LY < (obj.getY() + spriteHeight)) {
                visibleSprites.add(obj);
            }
        }

        // For debug porpuses
        // Don't know why is being hollow
        System.out.println(visibleSprites.toString());
    }

    @Override
    public void start() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'start'");
    }

    public List<SpriteObject> getVisibleSprites() {
        return this.visibleSprites;
    }
}
