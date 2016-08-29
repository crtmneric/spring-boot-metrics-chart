package com.pribas;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class SampleController {

	@FXML
	private Button btnStop;
	@FXML
	private TextField txtInterval;
	@FXML
	private TextField txtUser;
	@FXML
	private PasswordField txtPass;
	@FXML
	private ListView<String> lstView;
	@FXML
	private ListView<String> lstKeys;
	@FXML
	private LineChart<String, Number> lineChart;
	@FXML
	private CheckBox chckAnimation;
	@FXML
	private CheckBox chckSave;

	@FXML
	private Label lblInt;

	@FXML
	private VBox vPane;
	@FXML
	private VBox vBoks;
	@FXML
	private Label lblKey;
	@FXML
	private Label lblURL;
	@FXML
	private ImageView imgSuc;
	@FXML
	private ImageView imgSuc1;
	@FXML
	private TabPane paneTab;

	private Timeline timeline;
	private Timeline timelineRef;
	private List<String> urls;
	private List<String> keys;
	private int urlCounter = 0;
	private int keyCounter = 0;
	private Map<String, List<Line>> hosts = new HashMap<String, List<Line>>();
	private int counter = 0;

	@FXML
	private void stopClicked(ActionEvent event) throws IOException {
		if (counter > 1) {
			timeline.stop();
		}
	}

	@FXML
	private void textChanged(KeyEvent keyEvent) {
		if (txtInterval.getText().length() > 1) {
			txtInterval.clear();
		}
	}

	private void refreshList() {

		lstView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		lstKeys.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		List<String> URLsAdded = new ArrayList<>();
		List<String> KEYsAdded = new ArrayList<>();
		if (!URLsAdded.contains("URLS")) {
			URLsAdded.add("              URLS            ");
		}
		if (!KEYsAdded.contains("KEYS")) {
			KEYsAdded.add("              KEYS            ");
		}
		String confFile = "Configures.txt";
		File filetoRead = new File(confFile);
		try {
			filetoRead.createNewFile();
		} catch (IOException e1) {
			showSomeCoolAlertMessage(AlertType.INFORMATION, "No file", "No file found", "No file to read..");
		}
		LineIterator it = null;
		try {
			it = FileUtils.lineIterator(filetoRead, "UTF-8");
		} catch (IOException e) {

			showSomeCoolAlertMessage(AlertType.INFORMATION, "No line", "Line not found!", "No line to read..");
		}
		while (it.hasNext()) {
			String line = it.nextLine();
			if (!line.isEmpty()) {
				if (line.substring(0, 4).equals("url:") && !line.startsWith("#")) {
					String urlNew = line.substring(4, line.length());
					if (!URLsAdded.contains(urlNew)) {
						URLsAdded.add(urlNew);
					}
				}
				if (line.substring(0, 4).equals("key:") && !line.startsWith("#")) {
					String keyNew = line.substring(4, line.length());
					if (!KEYsAdded.contains(keyNew)) {
						KEYsAdded.add(keyNew);
					}

				}
				if (line.substring(0, 4).equals("usr:") && !line.startsWith("#")) {
					String keyNew = line.substring(4, line.length());
					if (txtUser.getText().isEmpty()) {
						txtUser.setText(keyNew);
					}

				}
				if (line.substring(0, 5).equals("pass:") && !line.startsWith("#")) {
					String keyNew = line.substring(5, line.length());
					if (txtPass.getText().isEmpty()) {
						txtPass.setText(keyNew);
					}

				}
			}

		}

		if (URLsAdded.isEmpty()) {
			showSomeCoolAlertMessage(AlertType.ERROR, "ERROR", "Configures.txt File is Empty!",
					"The Configure file in ur execute path is an empty textfile!");
		}
		ObservableList<String> obList = FXCollections.observableList(URLsAdded);
		ObservableList<String> keyList = FXCollections.observableList(KEYsAdded);
		lstView.setItems(obList);
		lstKeys.setItems(keyList);
	}

	public void autoRefreshList() {
		try {
			timelineRef = new Timeline(new KeyFrame(Duration.millis(5000), ae -> {
				refreshList();
			}));
			timelineRef.setCycleCount(Animation.INDEFINITE);
			timelineRef.play();
		} catch (

		Exception ex) {
			showSomeCoolAlertMessage(AlertType.ERROR, "ERROR", "Configures.txt File is Empty!",
					"The Configure file in ur execute path is an empty textfile!");
			ex.printStackTrace();
		}

	}

	private void sendDataToChart() {
		try {
			urls = lstView.getSelectionModel().getSelectedItems();
			keys = lstKeys.getSelectionModel().getSelectedItems();
			if (urls.size() >= 1 && keys.size() >= 1) {
				if (counter >= 1) {
					timeline.stop();
				}
				hosts.clear();
				if (!urls.isEmpty() && !keys.isEmpty()) {
					for (String url : urls) {
						if (url != null && !url.contains("URL")) {
							String serviceName = url.substring(27, url.length() - 8);
							List<Line> lines = new ArrayList<Line>();
							for (String key : keys) {
								if (key != null && !key.contains("KEY")) {
									Line tempLine = new Line();
									tempLine.setName(serviceName + "-" + key);
									tempLine.setKey(key);
									lines.add(tempLine);
								}
							}
							hosts.put(url, lines);
						}

					}
					getSeries(Integer.valueOf(txtInterval.getText()) * 1000);
				}
			}

		} catch (

		Exception ex) {
			showSomeCoolAlertMessage(AlertType.INFORMATION, "Information", "Empty List!", "Empty List Bro!");
			ex.printStackTrace();
		}
	}

	@FXML
	private void keyClicked(javafx.scene.input.MouseEvent mouseEvent) {
		sendDataToChart();
		keyCounter++;
		lblKey.setVisible(true);
		imgSuc.setVisible(true);
		lblKey.setText(keys.size() + " KEY VALUE SELECTED!");
	}

	@FXML
	private void itemSelected(javafx.scene.input.MouseEvent mouseEvent) {
		sendDataToChart();
		urlCounter++;
		lblURL.setVisible(true);
		imgSuc1.setVisible(true);
		lblURL.setText(urls.size() + " URL VALUE SELECTED!");
	}

	@FXML
	private void checkStateChanged(ActionEvent tEvent) {
		boolean state = chckAnimation.isSelected();
		if (state) {
			lineChart.setAnimated(true);
		} else {
			lineChart.setAnimated(false);
		}
	}

	@FXML
	private void saveChecked(ActionEvent tEvent) throws IOException {
		boolean state = chckSave.isSelected();
		if (state) {
			if (txtPass.getText().isEmpty() || txtUser.getText().isEmpty()) {
				showSomeCoolAlertMessage(AlertType.ERROR, "Empty Value", "Check Username or Password",
						"Empty value dedected Check username or password");
			} else {
				String confFile = "Configures.txt";
				File filetoRead = new File(confFile);
				try {
					filetoRead.createNewFile();
				} catch (IOException e1) {
					showSomeCoolAlertMessage(AlertType.INFORMATION, "No file", "No file found", "No file to read..");
				}
				LineIterator it = null;
				try {
					it = FileUtils.lineIterator(filetoRead, "UTF-8");
				} catch (IOException e) {

					showSomeCoolAlertMessage(AlertType.INFORMATION, "No line", "Line not found!", "No line to read..");
				}
				boolean notFound = true;
				while (it.hasNext()) {
					String line = it.nextLine();
					if (!line.isEmpty()) {
						if ((line.substring(0, 4).equals("usr:") && !line.startsWith("#"))
								|| (line.substring(0, 5).equals("pass:") && !line.startsWith("#"))) {
							notFound = false;
						}
					}
				}
				if (notFound) {
					FileUtils.writeStringToFile(filetoRead, "\nusr:" + txtUser.getText() + "\n", true);
					FileUtils.writeStringToFile(filetoRead, "pass:" + txtPass.getText() + "\n", true);
					showSomeCoolAlertMessage(AlertType.INFORMATION, "Succeed", "Added",
							"Succeessfully added to configure file!");
				} else {
					showSomeCoolAlertMessage(AlertType.INFORMATION, "Already written!", "Already written in file.",
							"These credantials are already written in configure file!");
				}

			}
		}

	}

	@FXML
	private void tabChange(javafx.event.Event event) {
		refreshList();
		vBoks.setPrefHeight(paneTab.getHeight());
		vBoks.setPrefWidth(paneTab.getWidth() - 100);
		vPane.setPrefHeight(paneTab.getHeight());
		vPane.setPrefWidth(paneTab.getWidth());
	}

	@FXML
	private void chartSelected(javafx.event.Event event) {
		keyCounter = 0;
		urlCounter = 0;
		if (urlCounter >= 1 && keyCounter >= 1) {
			lblKey.setVisible(false);
			lblURL.setVisible(false);
			imgSuc.setVisible(false);
			imgSuc1.setVisible(false);
			vBoks.setPrefHeight(paneTab.getHeight());
			vBoks.setPrefWidth(paneTab.getWidth() - 100);
			vPane.setPrefHeight(paneTab.getHeight());
			vPane.setPrefWidth(paneTab.getWidth());
		}

	}

	private void getSeries(int interval) {
		try {
			counter++;
			lineChart.getData().clear();
			jsonProccess jp = new jsonProccess();
			for (Map.Entry<String, List<Line>> m : hosts.entrySet()) {
				for (Line line : m.getValue()) {
					lineChart.getData().add(line.getSeries());
				}

			}

			if (counter > 1) {
				timeline.stop();
			}
			timeline = new Timeline(new KeyFrame(Duration.millis(interval), ae -> {

				for (Map.Entry<String, List<Line>> m : hosts.entrySet()) {
					putLineToChart(m, jp);
				}
				for (XYChart.Series<String, Number> s : lineChart.getData()) {
					for (XYChart.Data<String, Number> d : s.getData()) {
						Tooltip t = new Tooltip();

						t.install(d.getNode(), new Tooltip(String.format("Time =%s \nValue= %s\nName=%s", d.getXValue(),
								d.getYValue().toString(), s.getName())));
						// hackTooltipStartTiming(t);

					}
				}
			}));
			timeline.setCycleCount(Animation.INDEFINITE);
			timeline.play();
		} catch (Exception e) {
			showSomeCoolAlertMessage(AlertType.ERROR, "ERROR", "Wrong Key or URL Value!",
					"The Key or URL value which u writed on textfield is wrong!");

		}
	}

	public static void hackTooltipStartTiming(Tooltip tooltip) {
		try {
			Class<?> clazz = tooltip.getClass().getDeclaredClasses()[1];
			Constructor<?> constructor = clazz.getDeclaredConstructor(Duration.class, Duration.class, Duration.class,
					boolean.class);
			constructor.setAccessible(true);
			Object tooltipBehavior = constructor.newInstance(new Duration(100), // open
					new Duration(5000), // visible
					new Duration(5000), // close
					false);
			Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
			fieldBehavior.setAccessible(true);
			fieldBehavior.set(tooltip, tooltipBehavior);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void putLineToChart(Map.Entry<String, List<Line>> m, jsonProccess jp) {
		String url = m.getKey();

		try {
			String auth = txtUser.getText() + ":" + txtPass.getText();
			Map<String, String> jsonFromURL = jp.getJsonFromURL(url, auth);
			if (jsonFromURL == null) {
				lineChart.getData().clear();
				timeline.stop();
				showSomeCoolAlertMessage(AlertType.ERROR, "ERROR", "Wrong Key or URL Value!",
						"The Key or URL value which u writed on textfield is wrong!");
			} else {
				for (Line line : m.getValue()) {
					line.getSeries().setName(line.getName());
					line.getSeries().getData().clear();
					ChartData chartData = new ChartData();
					chartData.setX(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

					chartData.setY(Integer.parseInt(jsonFromURL.get(line.getKey())));

					line.getList().add(chartData);
					for (ChartData data : line.getList()) {
						line.getSeries().getData().add(new Data<String, Number>(data.getX(), data.getY()));
					}
				}
			}

		} catch (IOException e) {
			timeline.stop();
			showSomeCoolAlertMessage(AlertType.ERROR, "ERROR", "Wrong Key, URL or Username/Password Value!",
					"The Key or URL value which u writed on textfield is wrong!");
		}
	}

	private void showSomeCoolAlertMessage(AlertType alertType, String title, String header, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.show();
	}
}
