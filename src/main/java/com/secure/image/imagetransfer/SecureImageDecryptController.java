package com.secure.image.imagetransfer;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;

@RestController
public class SecureImageDecryptController {

    private static final String AES_KEY = "1234567890123456";
    private static final String UPLOAD_DIR = "C:/study/minor-1 image output/";

    @GetMapping("/decrypt")
    public ResponseEntity<FileSystemResource> decryptImage(@RequestParam("filename") String filename) {
        try {
            File encryptedFile = new File(UPLOAD_DIR + filename);
            File decryptedFile = new File(UPLOAD_DIR + "decrypted_" + filename.replace(".enc", ""));

            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec secretKeySpec = new SecretKeySpec(AES_KEY.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

            FileInputStream fis = new FileInputStream(encryptedFile);
            CipherInputStream cis = new CipherInputStream(fis, cipher);
            FileOutputStream fos = new FileOutputStream(decryptedFile);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = cis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            cis.close();
            fos.close();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + decryptedFile.getName())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new FileSystemResource(decryptedFile));

        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}