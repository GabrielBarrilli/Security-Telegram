package org.gabrielbarrilli.securitytelegram.relatorio;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

@Component
public class ReportImageUtil {

    public BufferedImage getBufferedImage(String resourcePath) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream iconStream = classLoader.getResourceAsStream(resourcePath);
        if (iconStream == null) {
            throw new IOException("Imagem não encontrada no caminho do recurso: " + resourcePath);
        }
        return ImageIO.read(iconStream);
    }
}
