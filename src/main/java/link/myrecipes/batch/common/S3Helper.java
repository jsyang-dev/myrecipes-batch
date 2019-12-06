package link.myrecipes.batch.common;

import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.List;

public interface S3Helper {
    List<S3Object> listObjects(String prefix);
}
