package com.nexuslogistics.system;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class AmazonS3Service implements IAmazonS3Service {
    private static Logger logger = LoggerFactory.getLogger(AmazonS3Service.class);

    @Override
    public String createS3Bucket(String name) {
        AmazonS3 amazonS3 = new CloudConfiguration().getAmazonS3();

        if (!amazonS3.doesBucketExistV2(name)) {
            amazonS3.createBucket(name);
            logger.info("Successfully created '{}' bucket on S3", name);
        }
        return name;
    }

    @Override
    public void uploadFileToS3(String fileName, File file, String bucketName) {
        AmazonS3 amazonS3 = new CloudConfiguration().getAmazonS3();

        PutObjectRequest request = new PutObjectRequest(bucketName, fileName, file);
        amazonS3.putObject(request);
        logger.info("File uploaded successfully to S3 bucket");
    }
}
