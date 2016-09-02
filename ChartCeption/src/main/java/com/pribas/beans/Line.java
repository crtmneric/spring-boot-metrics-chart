package com.pribas.beans;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.chart.XYChart;

public class Line {
	private String name;
	private String key;
	private List<ChartData> list = new ArrayList<ChartData>();
	private XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ChartData> getList() {
		return list;
	}

	public void setList(List<ChartData> list) {
		this.list = list;
	}

	public XYChart.Series<String, Number> getSeries() {
		return series;
	}

	public void setSeries(XYChart.Series<String, Number> series) {
		this.series = series;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return "Line [name=" + name + ", key=" + key + ", list=" + list + ", series=" + series + "]";
	}

}
