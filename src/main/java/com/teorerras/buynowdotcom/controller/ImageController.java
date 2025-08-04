package com.teorerras.buynowdotcom.controller;

import com.teorerras.buynowdotcom.dtos.ImageDto;
import com.teorerras.buynowdotcom.model.Image;
import com.teorerras.buynowdotcom.response.ApiResponse;
import com.teorerras.buynowdotcom.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/images")
public class ImageController {
    private final IImageService iImageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> uploadImages(@RequestParam("files") List<MultipartFile> files, @RequestParam("productId") Long productId) {
            List<ImageDto> imageDtos = iImageService.saveImages(productId, files);
            return ResponseEntity.ok(new ApiResponse("Images uploaded successfully", imageDtos));
    }

    @DeleteMapping("/image/{id}/delete")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long id) {
            iImageService.deleteImageById(id);
            return ResponseEntity.ok(new ApiResponse("Image deleted successfully", null));
    }

    @PutMapping("/image/{id}/update")
    public ResponseEntity<ApiResponse> updateImage(@RequestBody MultipartFile file, @PathVariable Long id) {
            iImageService.updateImage(file, id);
            return ResponseEntity.ok(new ApiResponse("Images updated successfully", null));
    }

    @SneakyThrows
    @GetMapping("/image/{imageId}/download")
    public ResponseEntity<ByteArrayResource> downloadImage(@PathVariable Long imageId) {
            Image image = iImageService.getImageById(imageId);
            ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType())).header(HttpHeaders.CONTENT_DISPOSITION, "attachments; filename=\""+image.getFileName() + "\"").body(resource);
    }
}
