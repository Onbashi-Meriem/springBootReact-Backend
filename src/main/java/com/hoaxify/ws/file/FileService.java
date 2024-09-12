package com.hoaxify.ws.file;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoaxify.ws.configuration.HoaxifyProperties;

@Service
public class FileService {

    @Autowired
    HoaxifyProperties hoaxifyProperties;

    public String saveBase64StringAsFile(String image) {
        String filename = UUID.randomUUID().toString();
        
        Path path = Paths.get(hoaxifyProperties.getStorage().getRoot(), hoaxifyProperties.getStorage().getProfile(),
                filename);
        System.out.println("-----------root"+hoaxifyProperties.getStorage().getRoot());
        System.out.println("-----------profile"+hoaxifyProperties.getStorage().getProfile());
        System.out.println("----------path "+path);

        try {
            OutputStream outputStream = new FileOutputStream(path.toFile());
            byte[] basedecoded = Base64.getDecoder().decode(image.split(",")[1]);
            outputStream.write(basedecoded);
            outputStream.close();
            return filename;
        } catch ( IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
}
