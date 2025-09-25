package com.sample.upload;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.UploadedFile;
import java.io.IOException;
import java.util.Map;

public class UploadService {

    public static void main(String[] args) throws IOException {
        GitHubService gitHubService = new GitHubService();
        ObjectMapper objectMapper = new ObjectMapper();

        Javalin app = Javalin.create(config -> {
            config.http.maxRequestSize = 100_000_000; // 100 MB
        }).start(8000);

        app.get("/", ctx -> ctx.result("Upload Service (GitHub) is ready."));

        app.post("/upload", ctx -> {
            UploadedFile uploadedFile = ctx.uploadedFile("file");

            if (uploadedFile == null) {
                ctx.status(400).result("No file uploaded. Please use the 'file' form field.");
                return;
            }

            try {
                String originalFilename = uploadedFile.filename();
                long timestamp = System.currentTimeMillis();

                String baseName = originalFilename;
                String extension = "";
                int dotIndex = originalFilename.lastIndexOf('.');
                if (dotIndex > 0 && dotIndex < originalFilename.length() - 1) {
                    baseName = originalFilename.substring(0, dotIndex);
                    extension = originalFilename.substring(dotIndex);
                }

                String newUniqueFilename = baseName + "_" + timestamp + extension;

                String commitMessage = ":sparkles: feat: Upload new file " + newUniqueFilename;
                String fileUrl = gitHubService.uploadFile(
                        newUniqueFilename,
                        uploadedFile.content(),
                        commitMessage
                );

                Map<String, String> response = Map.of(
                    "message", "File '" + newUniqueFilename + "' uploaded successfully to GitHub.",
                    "url", fileUrl + "?raw=true"
                );

                ctx.status(200).json(response);

            } catch (Exception e) {
                ctx.status(500).json(Map.of("error", "Error uploading file: " + e.getMessage()));
            }
        });
    }
}
