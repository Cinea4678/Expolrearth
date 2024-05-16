package cc.cinea.huanyou.controller;

import cc.cinea.huanyou.dto.ApiResp;
import cc.cinea.huanyou.service.UploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author cinea
 */
@RestController
@RequestMapping("/upload")
@Tag(name = "Upload", description = "上传")
public class UploadController {

    UploadService uploadService;

    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @Secured("ROLE_USER")
    @PostMapping("/image")
    @Operation(summary = "上传图像")
    ApiResp uploadImages(MultipartFile file) throws IOException {
        var fileName = file.getOriginalFilename();
        var contentType = file.getContentType();
        var contentLength = file.getSize();
        return ApiResp.from(uploadService.uploadImage(file.getInputStream(), fileName, contentLength, contentType));
    }

}
