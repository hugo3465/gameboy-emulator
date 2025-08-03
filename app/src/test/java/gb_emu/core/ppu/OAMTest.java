package gb_emu.core.ppu;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OAMTest {

    private OAM oam;

    @BeforeEach
    public void setup() {
        oam = new OAM();
    }

    @Test
    public void testSetAndReadSpriteObject() {
        OAM.SpriteObject sprite = oam.new SpriteObject(
            40,         // yPosition
            50,         // xPosition
            10,        // tileNumber
            1,           // priority (bit 7)
            true,           // flipX
            false,          // flipY
            1             // pallete (bit 4)
        );

        int address = 0xFE00;

        oam.setSpriteObject(address, sprite);

        OAM.SpriteObject readSprite = oam.readSpriteObject(address);

        assertEquals(sprite.getY(), readSprite.getY(), "Y Position");
        assertEquals(sprite.getX(), readSprite.getX(), "X Position");
        assertEquals(sprite.getTileNumber(), readSprite.getTileNumber(), "Tile Number");
        assertEquals(sprite.getPriority(), readSprite.getPriority(), "Priority");
        assertEquals(sprite.getFlipX(), readSprite.getFlipX(), "FlipX");
        assertEquals(sprite.getFlipY(), readSprite.getFlipY(), "FlipY");
        assertEquals(sprite.getPallete(), readSprite.getPallete(), "Palette");
    }
}