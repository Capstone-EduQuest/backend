package com.eduquest.backend.infrastructure.s3.client;

import com.eduquest.backend.domain.file.dto.S3FileDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
class EduQuestS3ClientTest {

    @Autowired
    private EduQuestS3Client sut;

    @Autowired
    private S3Client s3Client;

    @Autowired
    private S3Presigner s3Presigner;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    private final String testKeyPrefix = "test/integration/";
    private final List<String> createdKeys = new ArrayList<>();

    @AfterEach
    void tearDown() {
        // best-effort cleanup: delete any objects we created during the test
        for (String key : createdKeys) {
            try {
                sut.deleteObject(key);
            } catch (Exception ignored) {
                // ignore cleanup failures
            }
        }
        createdKeys.clear();
    }

    @Test
    void integration_putAndGetPresignedUrl_andDelete() throws Exception {
        // Skip test if no S3 endpoint configured
        String endpoint = System.getenv("AWS_S3_ENDPOINT");
        Assumptions.assumeTrue(endpoint != null && !endpoint.isBlank(), "AWS_S3_ENDPOINT not set - skipping integration test");
        // load an actual image from src/test/resources/images/sample.png
        InputStream is = getClass().getResourceAsStream("/images/sample.png");
        Assumptions.assumeTrue(is != null, "No sample image found in classpath (/images/sample.png) - skipping");

        byte[] data;
        try (InputStream in = is) {
            data = in.readAllBytes();
        } catch (IOException e) {
            Assumptions.assumeTrue(false, "Failed to read sample image - skipping: " + e.getMessage());
            return;
        }

        String key = testKeyPrefix + UUID.randomUUID() + "-sample-image.png";
        S3FileDto file = S3FileDto.of("sample-image.png", "image/png", "png", data);

        // put object
        String returnedKey = sut.putObject(key, file);
        // record for cleanup
        createdKeys.add(key);
        assertEquals(key, returnedKey);

        // ensure headObject works
        HeadObjectResponse head = s3Client.headObject(b -> b.bucket(bucket).key(key));
        assertNotNull(head);

        // presigned url
        Optional<String> urlOpt = sut.getPresignedUrl(key);
        assertTrue(urlOpt.isPresent());
        String url = urlOpt.get();
        assertFalse(url.isBlank());
        // should be a valid URL/URI
        URI.create(url);

        // delete object
        sut.deleteObject(key);
        createdKeys.remove(key);

        // after delete, headObject should fail
        try {
            s3Client.headObject(b -> b.bucket(bucket).key(key));
            fail("Expected object to be deleted");
        } catch (S3Exception e) {
            // expected
        }
    }

    @Test
    void getPresignedUrl_whenObjectNotFound_shouldReturnEmptyStringWrappedOptional() {
        String endpoint = System.getenv("AWS_S3_ENDPOINT");
        Assumptions.assumeTrue(endpoint != null && !endpoint.isBlank(), "AWS_S3_ENDPOINT not set - skipping integration test");

        String key = testKeyPrefix + "does-not-exist.png";

        Optional<String> urlOpt = sut.getPresignedUrl(key);

        assertTrue(urlOpt.isPresent());
        assertEquals("", urlOpt.get());
    }
}

