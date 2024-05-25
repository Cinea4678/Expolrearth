package cc.cinea.huanyou.service;

import com.spencerwi.either.Either;

import java.io.InputStream;

/**
 * @author cinea
 */
public interface UploadService {
    Either<String, String> uploadImage(InputStream inputStream, String fileName, Long fileSize, String contentType);
}
