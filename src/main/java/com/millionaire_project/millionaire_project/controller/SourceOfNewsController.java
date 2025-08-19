package com.millionaire_project.millionaire_project.controller;

import com.millionaire_project.millionaire_project.service.SourceOfNewsService;
import com.millionaire_project.millionaire_project.util.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/source-of-news")
public class SourceOfNewsController {

    @Autowired
    private SourceOfNewsService service;

   @PostMapping("/upload")
    public void uploadEvent(@RequestParam("file") MultipartFile file) throws Exception {
        service.uploadExcel(file);
   }

   @GetMapping("/fetch")
    public ResponseBuilder<List> response() {
      return service.response();
   }

    @GetMapping("/download-sample")
    public ResponseEntity<InputStreamResource> downloadSample() throws IOException {
        // Path is relative to resources folder
        ClassPathResource file = new ClassPathResource("static/upcoming.xlsx");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=upcoming.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(file.getInputStream()));
    }


}
