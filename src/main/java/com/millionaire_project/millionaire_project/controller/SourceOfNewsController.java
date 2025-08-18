package com.millionaire_project.millionaire_project.controller;

import com.millionaire_project.millionaire_project.service.SourceOfNewsService;
import com.millionaire_project.millionaire_project.util.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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


}
