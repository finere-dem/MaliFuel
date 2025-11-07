package com.bamako.fuelqueue.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import org.springframework.stereotype.Component;

@Component
public class QrCodeService {

    private static final int DEFAULT_SIZE = 250;

    public String generateDataUri(String content) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, DEFAULT_SIZE, DEFAULT_SIZE);
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
                String base64 = Base64.getEncoder().encodeToString(outputStream.toByteArray());
                return "data:image/png;base64," + base64;
            }
        } catch (WriterException | IOException ex) {
            throw new IllegalStateException("Failed to generate QR code", ex);
        }
    }
}
