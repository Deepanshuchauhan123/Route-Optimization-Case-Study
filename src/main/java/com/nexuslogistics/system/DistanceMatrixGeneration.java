package com.nexuslogistics.system;

import com.nexuslogistics.system.DTO.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DistanceMatrixGeneration {
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private static Logger logger = LoggerFactory.getLogger(ExtractLocations.class);

    public List<List<Double>> createDistanceMatrix(List<Location> locationDetails) {
        List<List<Double>> distanceMatrix = new ArrayList<>();

        int index = 0;

        logger.info("Calculating distance matrix using coordinates");

        //TODO: It can be further optimized by only calculating half matrix and filling other second half matrix
        for (Location columnLocation : locationDetails) {
            List<Double> rowOrderDistanceList = new ArrayList<>();
            for (Location rowLocation : locationDetails.subList(index, locationDetails.size())) {
                if (rowLocation.equals(columnLocation)) {
                    rowOrderDistanceList.add(0.0);
                } else {
                    // Distance between two points = √((x2 – x1)² + (y2 – y1)²)
                    int xCoordinateDiff = columnLocation.getCoordinateX() - rowLocation.getCoordinateX();
                    int yCoordinateDiff = columnLocation.getCoordinateY() - rowLocation.getCoordinateY();
                    double distance = Math.sqrt((xCoordinateDiff * xCoordinateDiff) + (yCoordinateDiff * yCoordinateDiff));
                    rowOrderDistanceList.add(Double.valueOf(df.format(distance)));
                }
            }
            distanceMatrix.add(rowOrderDistanceList);
        }
        logger.info("Successfully calculated distance matrix using coordinates");
        return distanceMatrix;
    }
}
