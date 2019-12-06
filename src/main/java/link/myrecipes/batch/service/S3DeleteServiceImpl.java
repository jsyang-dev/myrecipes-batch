package link.myrecipes.batch.service;

import link.myrecipes.batch.common.S3Helper;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.List;

@Service
public class S3DeleteServiceImpl implements S3DeleteService {
    private final S3Helper s3Helper;

    public S3DeleteServiceImpl(S3Helper s3Helper) {
        this.s3Helper = s3Helper;
    }

    @Override
    public List<S3Object> listObjects(String prefix) {
        return this.s3Helper.listObjects(prefix);
    }
}
