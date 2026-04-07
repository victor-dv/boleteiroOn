package br.com.boleiroOn.config.infra.aws;

import br.com.boleiroOn.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AWSService {
    private final S3Client s3Client;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Value("${aws.region}")
    private String region;


    public String uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessException("O arquivo de imagem não pode estar vazio.");
        }

        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename().replace(" ", "_");

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fileName;

        } catch (IOException e) {
            throw new BusinessException("Erro ao processar o arquivo de imagem: " + e.getMessage());
        } catch (Exception e) {
            throw new BusinessException("Erro ao enviar imagem para o S3: " + e.getMessage());
        }
    }

    public String uploadFile(byte[] bytes, String fileName, String contentType) {
        if (bytes == null || bytes.length == 0) {
            throw new BusinessException("O conteúdo do arquivo não pode estar vazio.");
        }
        return executeUpload(bytes, fileName, contentType);
    }

    private String executeUpload(byte[] bytes, String fileName, String contentType) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(contentType)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(bytes));

            // Retorna a URL completa como o seu sistema já espera
            return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fileName;

        } catch (Exception e) {
            throw new BusinessException("Erro ao enviar arquivo para o S3: " + e.getMessage());
        }
    }
}
