package com.nexuslogistics.system;

import com.nexuslogistics.system.DTO.TravelRoute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ShortestPathFind {
    static final Integer INFINITY = Integer.MAX_VALUE;
    private static Logger logger = LoggerFactory.getLogger(ShortestPathFind.class);

    /**
     * Using Bitmasking in Dynamic programming approach to solve shortest path to travel.
     * Time Complexity: O(n2.2^n)
     * Space: O(n*z^n)
     *
     * @param distanceMatrix, distance matrix for distance between various stops
     * @param vehicleId,      id if vehicle for file name
     * @return optimal travel route.
     */
    public TravelRoute findShortestPathAndCostToTravel(List<List<Double>> distanceMatrix, String vehicleId) {
        int totalStopPoints = distanceMatrix.size();
        double[][] dp = new double[1 << totalStopPoints][totalStopPoints];
        int[][] travelRoute = new int[1 << totalStopPoints][totalStopPoints];

        // Assigning to a larger value as cost, so we can find out minimum cost by comparing
        for (double[] row : dp) {
            Arrays.fill(row, INFINITY);
        }
        dp[1][0] = 0;
        for (int mask = 1; mask < (1 << totalStopPoints); mask += 2) {
            for (int middleLoop = 0; middleLoop < totalStopPoints; middleLoop++) {
                if ((mask & (1 << middleLoop)) == 0) continue;
                for (int innerLoop = 0; innerLoop < totalStopPoints; innerLoop++) {
                    if ((mask & (1 << innerLoop)) != 0) continue;
                    int nextMask = mask | (1 << innerLoop);
                    double newCost = dp[mask][middleLoop] + distanceMatrix.get(middleLoop).get(innerLoop);
                    if (newCost < dp[nextMask][innerLoop]) {
                        dp[nextMask][innerLoop] = newCost;
                        travelRoute[nextMask][innerLoop] = middleLoop;
                    }
                }
            }
        }

        double minCost = Integer.MAX_VALUE;
        int lastCity = -1;
        for (int loop = 1; loop < totalStopPoints; loop++) {
            double currentCost = dp[(1 << totalStopPoints) - 1][loop] + distanceMatrix.get(loop).get(0);
            if (currentCost < minCost) {
                minCost = currentCost;
                lastCity = loop;
            }
        }

        //path calculation

        int[] optimalPath = new int[totalStopPoints + 1];
        int mask = (1 << totalStopPoints) - 1;
        for (int i = totalStopPoints - 1; i >= 0; i--) {
            optimalPath[i] = lastCity;
            int temp = lastCity;
            lastCity = travelRoute[mask][lastCity];
            mask ^= (1 << temp); // Remove the last city from the mask
        }
        optimalPath[totalStopPoints] = 0; // Return to starting city

        StringBuffer content = new StringBuffer("TotalDistance,Route\n" + minCost + ",");
        for (int point : optimalPath) {
            content.append(point + "-");
        }
        String fileContent = content.toString().substring(0, content.length() - 1);
        //Create Optimal path Output file and push it to S3
        try {
            File file = new File(vehicleId + "-route.csv");
            FileWriter writer = new FileWriter(file);
            writer.write(fileContent);
            writer.close();

            IAmazonS3Service amazonS3Service = new AmazonS3Service();
            amazonS3Service.uploadFileToS3(file.getName(), file, Constants.BUCKET_NAME);
            logger.info("Successfully uploaded optimal path route output to S3");
        } catch (IOException e) {
            logger.error("Encountered an error while creating optimal path file");
        }

        // Print the path
        System.out.println("The shortest path is: " + fileContent);
        System.out.println("Minimum cost = " + minCost);
        return new TravelRoute(minCost, optimalPath);
    }
}
