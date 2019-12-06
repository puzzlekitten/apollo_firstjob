package application;

import java.util.Map;

public class ExcelNode {
	private int sheet; // sheet页
	// 第一个map key 是 row 多少行，第二个map key是cell 多少列，vlaue 是值
	private Map<Integer, Map<Integer, String>> value;

	// 设置多个sheet页，第一个map key 是sheet页，第二个map key 是 row 多少行，第三个map key是cell 多少列，vlaue
	// 是值
	private Map<Integer, Map<Integer, Map<Integer, String>>> setSheets;

	public int getSheet() {
		return sheet;
	}

	public void setSheet(int sheet) {
		this.sheet = sheet;
	}

	public Map<Integer, Map<Integer, String>> getValue() {
		return value;
	}

	public void setValue(Map<Integer, Map<Integer, String>> value) {
		this.value = value;
	}

	public Map<Integer, Map<Integer, Map<Integer, String>>> getSetSheets() {
		return setSheets;
	}

	public void setSetSheets(Map<Integer, Map<Integer, Map<Integer, String>>> setSheets) {
		this.setSheets = setSheets;
	}

}
