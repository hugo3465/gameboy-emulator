package gb_emu.core.mem.cartridge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Cartridge {
    private  static final Logger logger = LoggerFactory.getLogger(Cartridge.class);
    
    private byte[] romData;

    public void loadFile(String path) throws IOException {
        File file = new File(path);
        this.romData = new byte[(int) file.length()];
        
        try (FileInputStream fis = new FileInputStream(file)) {
            int bytesRead = fis.read(romData);
            if(bytesRead != romData.length) {
                throw new IOException("Error while reading the ROM file.");
            }
        } catch(Exception e) {
            throw new IOException("Error while reading the ROM file.");
        }
    }
}