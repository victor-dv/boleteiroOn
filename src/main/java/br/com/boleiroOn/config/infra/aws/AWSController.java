package br.com.boleiroOn.config.infra.aws;

import br.com.boleiroOn.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class AWSController {
    private final AWSService s3Service;


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AWSResponseDto>> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", defaultValue = "geral") String folder) {

        String imageUrl = s3Service.uploadFile(file, folder);
        var response = new AWSResponseDto(imageUrl);

        return ResponseEntity.ok(ApiResponse.success(response, "Upload realizado com sucesso"));
    }
}
