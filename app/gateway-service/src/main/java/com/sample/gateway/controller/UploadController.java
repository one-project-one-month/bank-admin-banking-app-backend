package com.sample.gateway.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class UploadController {

    private final RestTemplate restTemplate;

    // The URL for the upload-service, using its service name from docker-compose
    private final String uploadServiceUrl = "http://upload-service:8000/upload";

    public UploadController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> proxyFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileResource);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            return restTemplate.postForEntity(uploadServiceUrl, requestEntity, String.class);
        } catch (Exception e) {
            return ResponseEntity.status(502).body("Error forwarding request to upload service: " + e.getMessage());
        }
    }
}
