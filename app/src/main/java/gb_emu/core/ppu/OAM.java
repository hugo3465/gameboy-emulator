package gb_emu.core.ppu;

import java.util.ArrayList;
import java.util.List;

import gb_emu.core.mem.RAM;

public class OAM extends RAM {
    private static final int OAM_CAPACITY = 0xA0; // 160 bytes
    private static final int OAM_OFFSET = 0xFE00;
    private static final int SPRITE_LENGHT = 4; // 4 bytes

    public OAM() {
        super(OAM_CAPACITY, OAM_OFFSET);

        // Initialize OAM with default values
        for (int i = 0; i < 0xA0; i++) {
            write(0xFE00 + i, 0x00);
        }
    }

    public List<SpriteObject> getAllSprites() {
        List<SpriteObject> sprites = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            sprites.add(readSpriteObject(i * SPRITE_LENGHT));
        }
        return sprites;
    }

    public SpriteObject readSpriteObject(int startAddress) {
        int yPos = read(startAddress);
        int xPos = read(startAddress + 1);
        int tileNum = read(startAddress + 2);
        int flags = read(startAddress + 3);

        // Extract flags using bitwise
        int priority = (flags >> 7) & 0x1;
        int flipY = (flags >> 6) & 0x1;
        int flipX = (flags >> 5) & 0x1;
        int palette = (flags >> 4) & 0x1;

        return new SpriteObject(
                yPos,
                xPos,
                tileNum,
                priority,
                flipX == 1,
                flipY == 1,
                palette);
    }

    public void setSpriteObject(int startAddress, SpriteObject obj) {
        int flags = 0;
        flags += obj.priority << 7;
        flags += obj.flipY << 6;
        flags += obj.flipX << 5;
        flags += obj.pallete << 4;

        write(startAddress, obj.yPosition);
        write(startAddress + 1, obj.xPosition);
        write(startAddress + 2, obj.tileNumber);
        write(startAddress + 3, flags);
    }

    public class SpriteObject {
        private int yPosition;
        private int xPosition;
        private int tileNumber;
        private int priority;
        private int flipX;
        private int flipY;
        private int pallete;

        public SpriteObject() {
        }

        public SpriteObject(int yPosition, int xPosition, int tileNumber, int priority, boolean flipX, boolean flipY,
                int pallete) {
            this.yPosition = yPosition;
            this.xPosition = xPosition;
            this.tileNumber = tileNumber;
            this.priority = priority;
            setFlipX(flipX);
            setFlipY(flipY);
            this.pallete = pallete;
        }

        public int getX() {
            return xPosition;
        }

        public void setX(int xPosition) {
            this.xPosition = xPosition;
        }

        public int getY() {
            return yPosition;
        }

        public void setY(int yPosition) {
            this.yPosition = yPosition;
        }

        public int getTileNumber() {
            return tileNumber;
        }

        public void setTileNumber(int tileNumber) {
            this.tileNumber = tileNumber;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public boolean getFlipX() {
            return (flipX == 0) ? false : true;
        }

        public void setFlipX(boolean flipX) {
            this.flipX = (flipX == false) ? 0 : 1;
        }

        public boolean getFlipY() {
            return (flipY == 0) ? false : true;
        }

        public void setFlipY(boolean flipY) {
            this.flipY = (flipY == false) ? 0 : 1;
        }

        public int getPallete() {
            return pallete;
        }

        public void setPallete(int pallete) {
            this.pallete = pallete;
        }
    }
}
