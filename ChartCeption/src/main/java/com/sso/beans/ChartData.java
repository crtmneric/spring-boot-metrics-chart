package com.sso.beans;

public class ChartData {

	private String x;
	private Integer y;

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "ChartData [x=" + x + ", y=" + y + "]";
	}

}
