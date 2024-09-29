package com.nexuslogistics.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Create the required resources for running the Shortest path Algorithm.
 */
public class ResourceCreation {

    IAmazonS3Service amazonS3Service;

    private static final String FILENAME = "RJ-02-CH0001.csv";
    private static final String S3_BUCKET = "nexus";
    private static Logger logger = LoggerFactory.getLogger(ResourceCreation.class);

    public String createRequiredResources() {
        amazonS3Service = new AmazonS3Service();
        amazonS3Service.createS3Bucket(S3_BUCKET);

        try {
            File file = new File(FILENAME);

            FileWriter writer = new FileWriter(file);
            writer.write("StopID,X,Y\n" +
                    "1,12,5\n" +
                    "2,-3,7\n" +
                    "3,21,-13\n" +
                    "4,8,2\n" +
                    "5,-5,10\n" +
                    "6,15,-8\n" +
                    "7,-10,6\n" +
                    "8,19,12\n" +
                    "9,-7,-9\n" +
                    "10,0,5\n" +
                    "11,11,-6\n" +
                    "12,-8,-2\n" +
                    "13,6,9\n" +
                    "14,22,-5\n" +
                    "15,-12,4");
            writer.close();
            logger.info("File content wrote successfully");

            amazonS3Service.uploadFileToS3(FILENAME, file, S3_BUCKET);

        } catch (IOException e) {
            logger.error("An error occurred while creating required resources for Shortest path Algorithm");
            e.printStackTrace();
        }
        return FILENAME;
    }
}
