package com.nexuslogistics.system;

import java.io.File;

public interface IAmazonS3Service {

    /**
     * Create a bucket on AWS S3.
     *
     * @param name, name of the bucket to be created.
     * @return name of the created bucket.
     */
    String createS3Bucket(String name);

    /**
     * Update a file in AWS S3 bucket.
     *
     * @param fileName,   name of the file.
     * @param file,       file which needs to be uploaded.
     * @param bucketName, bucket where file need to be uploaded.
     */
    void uploadFileToS3(String fileName, File file, String bucketName);
}
