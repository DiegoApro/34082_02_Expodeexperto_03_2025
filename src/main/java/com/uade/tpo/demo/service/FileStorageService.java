package com.uade.tpo.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Value("${app.server.url:http://localhost:4002}")
    private String serverUrl;

    private static final List<String> EXTENSIONES_PERMITIDAS = List.of("jpg", "jpeg", "png", "webp", "gif");

    public String guardarImagen(MultipartFile archivo) {
        String nombreOriginal = archivo.getOriginalFilename();
        if (nombreOriginal == null || nombreOriginal.isBlank()) {
            throw new RuntimeException("Nombre de archivo inválido");
        }

        String extension = nombreOriginal.substring(nombreOriginal.lastIndexOf('.') + 1).toLowerCase();
        if (!EXTENSIONES_PERMITIDAS.contains(extension)) {
            throw new RuntimeException("Formato no permitido. Usá: jpg, jpeg, png, webp o gif");
        }

        try {
            Path dirPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            String nombreUnico = UUID.randomUUID() + "." + extension;
            Path destino = dirPath.resolve(nombreUnico);
            archivo.transferTo(destino);

            return serverUrl + "/uploads/" + nombreUnico;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la imagen: " + e.getMessage());
        }
    }
}
