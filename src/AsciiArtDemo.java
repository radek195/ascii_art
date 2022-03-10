import java.io.IOException;

public class AsciiArtDemo {
    public static void main(String[] args) throws IOException {
        ImageAnalyzer analyzer = new ImageAnalyzer(200, 200, "white");
        analyzer.convertImages();
    }
}
