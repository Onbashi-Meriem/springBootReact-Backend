package com.hoaxify.ws.file;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoaxify.ws.configuration.HoaxifyProperties;

@Service
public class FileService {

    @Autowired
    HoaxifyProperties hoaxifyProperties;

    Tika tika = new Tika();

    public String saveBase64StringAsFile(String image) {
        String filename = UUID.randomUUID().toString();
        
        Path path = getProfileImagePath(filename);
        try {
            OutputStream outputStream = new FileOutputStream(path.toFile());
            byte[] basedecoded = decodedImage(image);
            outputStream.write(basedecoded);
            outputStream.close();
            return filename;
        } catch ( IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public String detectType(String value) {

        var type = tika.detect(decodedImage(value));
        System.out.println("+++++++++++"+type);
        return type;
    }
    
    private byte[] decodedImage(String encodedImage) {
        return Base64.getDecoder().decode(encodedImage.split(",")[1]);
    }

    public void deleteProfilImage(String image) {
        if (image == null)
            return;
        Path path = getProfileImagePath(image);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }  

        throw new UnsupportedOperationException("Unimplemented method 'deleteProfilImage'");
    }
    
    private Path getProfileImagePath(String filename) {
        return Paths.get(hoaxifyProperties.getStorage().getRoot(), hoaxifyProperties.getStorage().getProfile(),
                filename);
    }
    
}
