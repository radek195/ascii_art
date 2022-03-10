import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ImageAnalyzer {
    private int maxHeight;
    private int maxWidth;
    private String font;
    private int pixelSize;

    public ImageAnalyzer(int maxHeight, int maxWidth, String font) {
        this.maxHeight = maxHeight;
        this.maxWidth = maxWidth;
        this.font = font;
    }

    public void convertImages() throws IOException {
        File dir = new File("src/images");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                BufferedImage img = ImageIO.read(child);
                this.pixelSize = Math.max(img.getHeight(), img.getWidth()) / Math.max(maxHeight, maxWidth);
                int desiredImgWidth = img.getWidth() / pixelSize;
                int desiredImgHeight = img.getHeight() / pixelSize;
                int[][] rawBrightnessArray = createRawBrightnessArray(img);
                int[][] desiredSizeBrightnessArray = createDesiredSizeBrightnessArray(rawBrightnessArray, pixelSize, desiredImgWidth, desiredImgHeight);
                createImage(desiredSizeBrightnessArray, child.getName().substring(0, child.getName().length() - 4));
            }
        }
    }


    public int[][] createRawBrightnessArray(BufferedImage img) {
        //create an array of brightness of each pixel
        int[][] brightnessArray = new int[img.getHeight()][img.getWidth()];

        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color c = new Color(img.getRGB(j, i));

                brightnessArray[i][j] = c.getBlue() + c.getGreen() + c.getRed();
            }
        }

        return brightnessArray;
    }

    public int[][] createDesiredSizeBrightnessArray(int[][] rawArray, int pixelSize, int desiredImgWidth, int desiredImgHeight) {
        int[][] desiredSizeArray = new int[desiredImgHeight][desiredImgWidth];

        for (int i = 0; i < rawArray.length - pixelSize; i++) {
            for (int j = 0; j < rawArray[0].length - pixelSize; j++) {
                int currentBrightness = desiredSizeArray[i / pixelSize][j / pixelSize];
                desiredSizeArray[i / pixelSize][j / pixelSize] = currentBrightness + rawArray[i][j];
            }
        }

        return desiredSizeArray;
    }

    public void createImage(int[][] desiredSizeBrightnessArray, String filename) throws IOException {
        String blackFont = "_.,-=+:;cba!?0123456789$W#@Ñ";
        String whiteFont = "Ñ@#W$876543210?!abc;:+==-,._";
        String density;

        if (font.equals("black")) {
            density = blackFont;
        } else if (font.equals("white")) {
            density = whiteFont;
        } else {
            throw new IllegalArgumentException("Please put black or white depending on editor that you use!");
        }

        double maxBrightness = pixelSize * pixelSize * (255 * 3);

        //create txt file
        File image = new File("src/txts/" + filename + ".txt");
        image.createNewFile();

        //clear txt file
        FileWriter writer = new FileWriter("src/txts/" + filename + ".txt");
        writer.write("");

        //create image array
        for (int i = 0; i < desiredSizeBrightnessArray.length - 1; i++) {
            for (int j = 0; j < desiredSizeBrightnessArray[0].length - 1; j++) {
                double value = desiredSizeBrightnessArray[i][j] / maxBrightness;
                double brightnessIndex = value * (density.length() - 1);
                writer.append(density.charAt((int) brightnessIndex));
                writer.append(density.charAt((int) brightnessIndex));
            }
            writer.append("\n");
        }
        writer.close();
    }

}
