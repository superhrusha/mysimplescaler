package hello;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

@RestController
public class ScaleRequestController {

    @RequestMapping("/thumbnail")
    public ResponseEntity<Object> scaleImage(@RequestParam(value = "url") String url,
                                             @RequestParam(value = "height") int height,
                                             @RequestParam(value = "width") int width) {

        BufferedImage originalImage;

        try {

            originalImage = ImageIO.read(new URL(url));

        } catch (MalformedURLException e) {

            return badUrl(e);

        } catch (IOException e) {

            return issueAccessingImage(e);
        }

        try {
            BufferedImage resultingImage = ScalingManager.lpad(originalImage, height, width);

            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

                ImageIO.write(resultingImage, "jpg", out);
                out.flush();

                byte[] resultingBytes = out.toByteArray();

                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resultingBytes);
            }
        } catch (Exception e) {
            Error response = new Error("Something went wrong.", e.getMessage());
            return ResponseEntity.status(500).contentType(MediaType.APPLICATION_JSON).body(response);
        }
    }

    private ResponseEntity<Object> issueAccessingImage(IOException e) {
        Error response = new Error("Problem occurred while trying to access the image at the specified url", e.getMessage());
        return ResponseEntity.status(500).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    private ResponseEntity<Object> badUrl(MalformedURLException e) {
        Error response = new Error("url is malformed", e.getMessage());
        return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    static class Error {
        String message;
        String cause;

        public String getMessage() {
            return message;
        }

        public String getCause() {
            return cause;
        }

        Error(String message, String cause) {

            this.message = message;
            this.cause = cause;
        }
    }
}


