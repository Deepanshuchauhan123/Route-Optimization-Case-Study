package com.nexuslogistics.system;

import com.nexuslogistics.system.DTO.Location;
import com.nexuslogistics.system.DTO.TravelRoute;
import org.knowm.xchart.AnnotationText;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShortestPathMap {
    private static Logger logger = LoggerFactory.getLogger(ShortestPathMap.class);

    public void generateShortestPathMap(String vehicleId, List<Location> vehicleData, TravelRoute travelRoute) {
        XYChart chart = new XYChart(800, 600);
        chart.setTitle("Most effective route for the shortest total distance = " + travelRoute.getTravelCost());
        chart.setXAxisTitle("X Coordinate");
        chart.setYAxisTitle("Y Coordinate");

        List<Integer> xData = new ArrayList<>();
        List<Integer> yData = new ArrayList<>();

        logger.info("Creating a chart for the optimal delivery route");
        for (int stop = 0; stop < travelRoute.getTravelPath().length; stop++) {
            int stopPoint = travelRoute.getTravelPath()[stop];
            StringBuffer pointInfo;
            if (stopPoint == 0) {
                xData.add(0);
                yData.add(0);
                pointInfo = new StringBuffer("Depot");
            } else {
                xData.add(vehicleData.get(stopPoint).getCoordinateX());
                yData.add(vehicleData.get(stopPoint).getCoordinateY());
                pointInfo = new StringBuffer("Stop " + stopPoint);
                if (stop == 1) {
                    pointInfo.append(" (First Stop)");
                } else if (stop == travelRoute.getTravelPath().length - 2) {
                    pointInfo.append(" (Last Stop)");
                }
            }
            chart.addAnnotation(new AnnotationText(pointInfo.toString(), xData.get(stop), yData.get(stop) - 0.5, false));
        }

        XYSeries stopPointSeries = chart.addSeries("Stops", xData, yData);
        stopPointSeries.setMarker(SeriesMarkers.TRIANGLE_UP);
        stopPointSeries.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);

        XYSeries routeSeries = chart.addSeries("Route", xData, yData);
        routeSeries.setMarker(SeriesMarkers.NONE);
        routeSeries.setLineColor(Color.GREEN);

        chart.getStyler().setMarkerSize(14);
        chart.getStyler().setAnnotationTextFont(new Font(Font.DIALOG, Font.BOLD, 12));

        try {
            BitmapEncoder.saveBitmap(chart, vehicleId + "-route", BitmapEncoder.BitmapFormat.PNG);

            IAmazonS3Service amazonS3Service = new AmazonS3Service();
            amazonS3Service.uploadFileToS3(vehicleId + "-route.png", new File(vehicleId + "-route.png"), Constants.BUCKET_NAME);

            System.out.println("Optimal path route is uploaded to S3");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
