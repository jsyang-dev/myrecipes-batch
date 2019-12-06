package link.myrecipes.batch.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.List;

@Component
@Slf4j
public class S3HelperImpl implements S3Helper {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    private final S3Client s3Client;

    public S3HelperImpl(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public List<S3Object> listObjects(String prefix) {
        ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder()
                .bucket(this.bucket)
                .prefix(prefix)
                .build();
        ListObjectsV2Response listObjectsV2Response = this.s3Client.listObjectsV2(listObjectsV2Request);
        return listObjectsV2Response.contents();
    }
}
