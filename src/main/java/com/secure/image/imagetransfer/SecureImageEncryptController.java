package com.secure.image.imagetransfer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

@Controller
public class SecureImageEncryptController {

    private static final String AES_KEY = "1234567890123456";
    private static final String UPLOAD_DIR = "C:/study/minor-1 image output/";

    @PostMapping("/encrypt")
    public String encryptImage(@RequestParam("image") MultipartFile image, Model model) {
        try {
            File encryptedFile = new File(UPLOAD_DIR + image.getOriginalFilename() + ".enc");
            encryptedFile.getParentFile().mkdirs();

            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec secretKeySpec = new SecretKeySpec(AES_KEY.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            InputStream inputStream = image.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(encryptedFile);
            CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream, cipher);
            FileCopyUtils.copy(inputStream, cipherOutputStream);

            cipherOutputStream.close();
            model.addAttribute("message", "Image encrypted successfully: " + encryptedFile.getName());
        } catch (Exception e) {
            model.addAttribute("message", "Encryption failed: " + e.getMessage());
        }
        return "result";
    }
}