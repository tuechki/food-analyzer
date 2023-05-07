package bg.sofia.uni.fmi.mjt.foodanalyzer.server.service;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ZXingService {
    private static ZXingService instance = new ZXingService();
    private ZXingService() {
    }

    public static ZXingService getInstance() {
        return instance;
    }

    public String getBarcode(String pathToFile)
        throws IOException, NotFoundException, FormatException, ChecksumException {
        BufferedImage image = ImageIO.read(new File(pathToFile));
        BinaryBitmap bitmap = convertImageToBinaryBitmap(image);

        Reader reader = new MultiFormatReader();
        Result result = reader.decode(bitmap);

        return result.getText();
    }

    private BinaryBitmap convertImageToBinaryBitmap(BufferedImage image) {
        int[] pixels = image.getRGB(0, 0,
            image.getWidth(), image.getHeight(),
            null, 0, image.getWidth());
        RGBLuminanceSource source = new RGBLuminanceSource(image.getWidth(),
            image.getHeight(),
            pixels);
        return new BinaryBitmap(new HybridBinarizer(source));
    }
}
