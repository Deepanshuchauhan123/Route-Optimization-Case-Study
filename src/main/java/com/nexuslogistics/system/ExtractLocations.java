package com.nexuslogistics.system;

import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.nexuslogistics.system.DTO.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class ExtractLocations {
    private static Logger logger = LoggerFactory.getLogger(ExtractLocations.class);

    public List<Location> extractVehicleData(String fileName) {
        List<Location> vehicleLocations = new ArrayList<>();

        CloudConfiguration cloudConfiguration = new CloudConfiguration();
        S3Object s3Object = cloudConfiguration.getAmazonS3().getObject(new GetObjectRequest(Constants.BUCKET_NAME, fileName));
        try {
            Scanner scanner = new Scanner(s3Object.getObjectContent());
            scanner.useDelimiter("\n|,");

            // consume header line
            scanner.nextLine();
            while (scanner.hasNextLine()) {

                //TODO: Handle extra empty lines in file
                Location location = new Location();

                // Add depot location that is (0,0)
                if (vehicleLocations.size() == 0) {
                    Location initialLocation = new Location();
                    initialLocation.setStopId(0);
                    initialLocation.setCoordinateX(0);
                    initialLocation.setCoordinateY(0);
                    vehicleLocations.add(initialLocation);
                }
                location.setStopId(Integer.parseInt(scanner.next()));
                location.setCoordinateX(Integer.parseInt(scanner.next()));
                location.setCoordinateY(Integer.parseInt(scanner.next().trim()));
                vehicleLocations.add(location);
            }
            scanner.close();
        } catch (Exception e) {
            logger.error("An error occurred while extracting locations from CSV file", e);
        }
        return vehicleLocations;
    }
}
