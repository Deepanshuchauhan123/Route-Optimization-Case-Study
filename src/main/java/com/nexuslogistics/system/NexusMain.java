package com.nexuslogistics.system;

import com.nexuslogistics.system.DTO.Location;
import com.nexuslogistics.system.DTO.TravelRoute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class NexusMain {

    ExtractLocations readInput;
    DistanceMatrixGeneration distanceMatrixGeneration;
    ShortestPathFind shortestPathFind;
    ShortestPathMap shortestPathMap;
    ResourceCreation resourceCreation;
    String s3DeliveryMapCoordinatesPath;

    private static Logger logger = LoggerFactory.getLogger(NexusMain.class);

    public void findShortestPathToDeliver() {
        readInput = new ExtractLocations();
        distanceMatrixGeneration = new DistanceMatrixGeneration();
        shortestPathFind = new ShortestPathFind();
        shortestPathMap = new ShortestPathMap();
        resourceCreation = new ResourceCreation();
        s3DeliveryMapCoordinatesPath = Constants.DELIVERY_MAP_COORDINATES_PATH;

        // Create resources automatically
        String filename = resourceCreation.createRequiredResources();
        String vehicleId = filename.substring(0, filename.length() - 4); // Remove .csv from file name

        List<Location> vehicleData = readInput.extractVehicleData(filename);
        List<List<Double>> distanceMatrix = distanceMatrixGeneration.createDistanceMatrix(vehicleData);
        TravelRoute travelRoute = shortestPathFind.findShortestPathAndCostToTravel(distanceMatrix, vehicleId);
        shortestPathMap.generateShortestPathMap(vehicleId, vehicleData, travelRoute);
    }

    private static String getFileNameFromUrl(String fileUrl) throws MalformedURLException {
        URL url = new URL(fileUrl);
        String path = url.getPath();
        String[] pathComponents = path.split("/");

        // Return the last component as the file name
        return pathComponents[pathComponents.length - 1];
    }
}
