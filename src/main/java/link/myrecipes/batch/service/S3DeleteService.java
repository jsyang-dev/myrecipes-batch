package link.myrecipes.batch.service;

import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.List;

public interface S3DeleteService {
    List<S3Object> listObjects(String prefix);
}
