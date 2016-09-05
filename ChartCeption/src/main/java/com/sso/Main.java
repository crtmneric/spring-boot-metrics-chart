package com.sso;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

	public void start(Stage primaryStage) {
		try {

			final CategoryAxis xAxis = new CategoryAxis();
			final NumberAxis yAxis = new NumberAxis();
			xAxis.setLabel("Time");
			yAxis.setLabel("Data");
			final LineChart<String, Number> lineChart = new LineChart<String, Number>(xAxis, yAxis);
			lineChart.getXAxis().setAutoRanging(true);
			lineChart.getYAxis().setAutoRanging(true);
			lineChart.setTitle("Memory Monitoring");
			XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
			series.setName("Live Data Flow ");
			BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("/Sample.fxml"));
			Scene scene = new Scene(root, 1200, 800);
			scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.getIcons().add(new Image("icon.png"));
			primaryStage.setTitle("ChartCeption Live Data Flow On Line Chart");
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
