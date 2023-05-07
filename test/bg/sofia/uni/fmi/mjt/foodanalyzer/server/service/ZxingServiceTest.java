package bg.sofia.uni.fmi.mjt.foodanalyzer;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.service.ZXingService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.UPCAWriter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ZxingServiceTest {

    public static BufferedImage generateUPCABarcodeImage(String barcodeText) {
        UPCAWriter barcodeWriter = new UPCAWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.UPC_A, 300, 150);

        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    private static ZXingService zXingService;

    @BeforeAll
    static void init() {
        zXingService = ZXingService.getInstance();
    }





    @Test
    public void testZXingServiceDecodesImageCorrectly() throws Exception {
        String barcode = "009800146130";
        BufferedImage bufferedImage = generateUPCABarcodeImage(barcode);
        Path barcodeFile = Files.createTempFile(Path.of("./"), "barcode", ".gif");

        ImageIO.write(bufferedImage, "gif", barcodeFile.toFile());

        assertEquals(barcode, zXingService.getBarcode(barcodeFile.getFileName().toString()));
    }

}
