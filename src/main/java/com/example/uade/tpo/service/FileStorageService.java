package com.example.uade.tpo.service;

import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {

    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    public FileStorageService() {
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo crear el directorio donde se almacenarán los archivos subidos.", ex);
        }
    }

    public void storeFileFromUrl(String fileUrl) {
        try {
            @SuppressWarnings("deprecation")
            URL url = new URL(fileUrl);
            String fileName = Paths.get(url.getPath()).getFileName().toString();
            Path targetLocation = this.fileStorageLocation.resolve(fileName);

            try (InputStream inputStream = url.openStream()) {
                Files.copy(inputStream, targetLocation);
            }
        } catch (IOException ex) {
            throw new RuntimeException("No se pudo almacenar el archivo desde la URL " + fileUrl + ". ¡Inténtalo de nuevo!", ex);
        }
    }
}
