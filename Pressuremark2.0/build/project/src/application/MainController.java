package application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class MainController implements Initializable {

	File file1;
	File file2;
	File pic1f;
	File pic2f;
	File pic3f;
	File pic4f;
	File pic5f;
	String pathIn, pathOut;
	private Stage stage;

	@FXML
	private AnchorPane woshitest;

	String testload, pressuremin, perUnitlength, minpoints, gradient;
	Map<String, String> Mapin = new HashMap<String, String>();
	Map<String, Object> Mapout = new HashMap<String, Object>();
	Map<String, String> MapoutExtract = new HashMap<String, String>();

	WritableImage image1;
	WritableImage image2;
	WritableImage image3;

	@FXML
	private Button daoru;

	@FXML
	private TextField guige;

	@FXML
	private TextField huawen;

	@FXML
	private TextField cengji;

	@FXML
	private TextField shangbiao;

	@FXML
	private TextField runwang;

	@FXML
	private TextField qiya;

	@FXML
	private TextField zhijing;

	@FXML
	private TextField zhongliang;

	@FXML
	private TextField xiachenliang;

	@FXML
	private TextField taihao;

	@FXML
	private TextField pressureMin;

	@FXML
	private TextField testLoad;

	@FXML
	private Button calculate;

	@FXML
	private Button shengcheng;

	@FXML
	private ChoiceBox<String> danweichangdu;

	@FXML
	private ChoiceBox<String> biaozhun;

	@FXML
	private ChoiceBox<String> direction;

	@FXML
	private TextField shiyankuang;

	@FXML
	private Text importWarming;

	@FXML
	private Text xiayibu;

	@FXML
	private Text finalWarning;

	@FXML
	private Text pic1w;

	@FXML
	private Button pic1;

	@FXML
	private Text pic2w;

	@FXML
	private Button pic2;

	@FXML
	private Text pic3w;

	@FXML
	private Button pic3;

	@FXML
	private Button help;

	@FXML
	private Text pic4w;

	@FXML
	private Text draww;

	@FXML
	private Button pic4;

	@FXML
	private Button draw;

	@FXML
	private Button Extract;

	@FXML
	private Text pic5w;

	@FXML
	private Button pic5;

	@FXML
	private Button changePressureMin;

	@FXML
	private NumberAxis figure1X;

	@FXML
	private AnchorPane shiyanchuangkou;

	@FXML
	private LineChart<?, ?> figure1;

	@FXML
	private LineChart<?, ?> figure2;

	@FXML
	private NumberAxis figure2X;

	// private SpreadsheetView ssv; //创建表格 不用fxml
	// @FXML
	// private AnchorPane woshitest;

	// 重要，下来菜单如何创建，并且默认值

	@Override
	public void initialize(java.net.URL location, ResourceBundle resources) {
		assert danweichangdu != null : "fx:id=\"myChoices\" was not injected: check your FXML file 'NoteBook.fxml'.";

		assert direction != null : "fx:id=\"myChoices\" was not injected: check your FXML file 'NoteBook.fxml'.";
		// initialize your logic here: all @FXML variables will have been injected
		// => you can add items to "myChoices" here:
		//

		direction.setItems(FXCollections.observableArrayList("竖向", "横向"));
		direction.setValue("竖向");

		biaozhun.setItems(FXCollections.observableArrayList("GB/T  22038-2018"));
		biaozhun.setValue("GB/T  22038-2018");

		danweichangdu.setItems(FXCollections.observableArrayList("A(朝阳：1.143)", "B(总厂：5.08)"));
		danweichangdu.setValue("A(朝阳：1.143)");
		pressureMin.setDisable(true);
		// file2
		// danweichangdu.valueProperty().addListener(new ChangeListener<String>() {
		// @Override
		// public void changed(ObservableValue<? extends String> observable, String
		// oldValue, String newValue) {
		// if (newValue.equals("B")) {
		// pressureMin.setText("1");
		// } else {
		// pressureMin.setText("50");
		// }
		// }
		// });

	}

	@FXML
	public void helpwarm(ActionEvent event) {
		Stage secondStage = new Stage();

		TextArea helpwarming = new TextArea();
		helpwarming.setText(
				"1.本软件用于压力印痕试验数据后处理，并生成相应报告。\n2.操作步骤\n  a.主流程按上部标题依次往后操作，注意每个标题必须点击。\n  b.第一步：先导入径向刚性报告，读取轮胎信息及试验相关参数，如有未读取到或信息不正确的请手动修改。\n  c.第二步：根据图像判断轮胎行驶方向（横或者竖）。\n  d.第三步：选择压力毯类型。\n  e.第四步：导入100%负荷下影片所导出的CSV格式文件（打开影片100%.fsx文件，将其设置到最后帧，\n  然后点击文件保存ASCII格式就能得到CSV文件）。\n  f.第五步：提示导入成功后点击计算，提示成功后点击标题第二步。\n  g.第六步：手动填入测量值，点击画图，然后导入压力印痕图及压力区间。\n  h.第七步：依次点击行列标题，然后点击写入图表，最后点击生成，报告将存在软件对应的文件夹下。\n3.注意事项\n  本软件使用需要将报告的模板文件放于软件的同一目录下，且模板文件不得私自修改。");
		helpwarming.setFont(Font.font(18));
		StackPane secondPane = new StackPane(helpwarming);
		Scene secondScene = new Scene(secondPane, 900, 400);
		secondStage.getIcons().add(new Image("icon1.png"));
		secondStage.setTitle("帮助文档");
		secondStage.setScene(secondScene);
		secondStage.setResizable(false);
		secondStage.show();
	}

	@FXML
	public void extract(ActionEvent event) throws IOException {
		FileChooser fc = new FileChooser();
		// 文件过滤
		fc.setInitialDirectory(new File(System.getProperty("user.home")));
		FileChooser.ExtensionFilter csvExtensionFilter = new FileChooser.ExtensionFilter("xlsx files (*.xlsx)",
				"*.xlsx");
		fc.getExtensionFilters().add(csvExtensionFilter);
		fc.setSelectedExtensionFilter(csvExtensionFilter);
		fc.setTitle("导入径向刚性试验报告");
		file2 = fc.showOpenDialog(stage);
		if (file2 != null) {
			MapoutExtract = ExcelUtil.printExcel(file2.getPath());
		}
		guige.setText(MapoutExtract.get("guige"));
		cengji.setText(MapoutExtract.get("cengji"));
		huawen.setText(MapoutExtract.get("huawen"));
		shangbiao.setText(MapoutExtract.get("shangbiao"));
		runwang.setText(MapoutExtract.get("runwang"));
		qiya.setText(MapoutExtract.get("qiya"));
		testLoad.setText(MapoutExtract.get("testLoad"));
		taihao.setText(MapoutExtract.get("taihao"));
	}

	// @FXML
	// public void changepressureMin(ActionEvent event) {
	// pressureMin.setDisable(false);
	// }

	@FXML
	public void importCsv(ActionEvent event) {
		FileChooser fc = new FileChooser();
		// 文件过滤
		fc.setInitialDirectory(new File(System.getProperty("user.home")));
		FileChooser.ExtensionFilter csvExtensionFilter = new FileChooser.ExtensionFilter("csv files (*.csv)", "*.csv");
		fc.getExtensionFilters().add(csvExtensionFilter);
		fc.setSelectedExtensionFilter(csvExtensionFilter);
		fc.setTitle("导入印痕CSV文件");
		// 新建窗口
		// Scene scene=this.woshitest.getScene();
		// javafx.stage.Window window=scene.getWindow();
		// Stage stage=(Stage) window;
		file1 = fc.showOpenDialog(stage);
		if (file1 != null) {
			pathIn = file1.getPath();
			importWarming.setText("导入" + "\"" + file1.getName().substring(0, file1.getName().length() - 4) + "\""
					+ "成功！" + "\n" + "点击计算");
			Mapin.put("pathIn", pathIn);
			calculate.setDisable(false);
		}
	}

	@SuppressWarnings("unchecked")
	@FXML
	public void calculateCsv(ActionEvent event) throws IOException {
		// this.spreadsheetview_test();
		if (file1 != null) {
			testload = testLoad.getText();
			Mapin.put("testLoad", testload);
			// pressuremin = pressureMin.getText();
			// Mapin.put("pressureMin", pressuremin);
			if (danweichangdu.getValue().equals("A(朝阳：1.143)")) {
				perUnitlength = "1.143";
				minpoints = "25";
				gradient = "4";
				// pressureMin.setText("50");
				// System.out.println("A");
			} else {
				perUnitlength = "5.08";
				minpoints = "5";
				gradient = "3";
				// pressureMin.setText("1");
				// System.out.println("B");
			}

			Mapin.put("perUnitLength", perUnitlength);
			Mapin.put("minpoints", minpoints);
			Mapin.put("gradient", gradient);
			Mapin.put("direction", direction.getValue());
			dataProcess dp = new dataProcess();
			Mapout = dp.start(Mapin);
			pressureMin.setText((String) Mapout.get("pressureMin"));
			if (Mapout.size() > 15) {
				xiayibu.setText("计算成功！请点击第二步......");
			}
		} else {
			importWarming.setText("请先导入csv文件......");
		}
	}

	@FXML
	public void putPic1(ActionEvent event) throws IOException {
		FileChooser fc = new FileChooser();
		// 文件过滤
		fc.setInitialDirectory(new File(System.getProperty("user.home")));
		fc.setTitle("导入印痕图");
		pic1f = fc.showOpenDialog(stage);
		if (pic1f != null) {
			System.out.println(pic1f.getPath());
			pic1w.setText("√");
			Mapin.put("pic1", pic1f.getPath());

		}

	}

	@FXML
	public void putPic3(ActionEvent event) throws IOException {
		FileChooser fc = new FileChooser();
		// 文件过滤
		fc.setInitialDirectory(new File(System.getProperty("user.home")));
		fc.setTitle("压力与时间图");
		pic3f = fc.showOpenDialog(stage);

		if (pic3f != null) {
			System.out.println(pic3f.getPath());
			pic3w.setText("√");
			Mapin.put("pic3", pic3f.getPath());

		}

	}

	@FXML
	public void drawImage(ActionEvent event) throws IOException {
		this.figure1_generation();
		this.figure2_generation();
		draww.setText("√");
	}

	public void figure2_generation() {
		// Step1 Initialization
		figure2.getData().clear();
		// Step2 Add data series to figure
		XYChart.Series seriesXL = new XYChart.Series();
		for (int i = 0; i < Global.figure2X.length; i++) {
			seriesXL.getData().add(new XYChart.Data(Global.figure2X[i], Global.figure2Y[i]));
		}
		figure2.getData().add(seriesXL);
		figure2.setTitle("行间距离和力度的关系");
		figure2X.setLowerBound(Global.minY - 10);
		figure2X.setUpperBound(Global.maxY + 10);
		figure2.setCreateSymbols(false);// 取消图标圆圈
	}

	public void figure1_generation() {
		// Step1 Initialization
		figure1.getData().clear();
		// Step2 Add data series to figure
		XYChart.Series seriesYL = new XYChart.Series();
		for (int i = 0; i < Global.figure1X.length; i++) {
			seriesYL.getData().add(new XYChart.Data(Global.figure1X[i], Global.figure1Y[i]));
		}
		figure1.getData().add(seriesYL);
		figure1.setTitle("列间距离和力度的关系");
		figure1X.setLowerBound(Global.minX - 10);
		figure1X.setUpperBound(Global.maxX + 10);
		figure1.setCreateSymbols(false);// 取消图标圆圈

	}

	@FXML
	public void savepic1(ActionEvent event) throws IOException {
		image1 = figure1.snapshot(new SnapshotParameters(), null);
		image2 = figure2.snapshot(new SnapshotParameters(), null);
		image3 = (WritableImage) Mapout.get("yinhenimage");
		if (image1 != null && image2 != null) {
			finalWarning.setText("写入成功！");
			finalWarning.setFill(Paint.valueOf("#FF82AB"));
			shengcheng.setDisable(false);
		}
	}

	@FXML
	public void produe(ActionEvent event) throws IOException {
		ExcelNode en = new ExcelNode();
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(dateFormat.format(date));

		Map<Integer, Map<Integer, String>> map = new HashMap<Integer, Map<Integer, String>>();
		Map<Integer, String> map1 = new HashMap<Integer, String>();
		Map<Integer, String> map2 = new HashMap<Integer, String>();
		Map<Integer, String> map3 = new HashMap<Integer, String>();
		Map<Integer, String> map4 = new HashMap<Integer, String>();
		Map<Integer, String> map5 = new HashMap<Integer, String>();
		Map<Integer, String> map6 = new HashMap<Integer, String>();
		Map<Integer, String> map7 = new HashMap<Integer, String>();
		Map<Integer, String> map8 = new HashMap<Integer, String>();
		Map<Integer, String> map9 = new HashMap<Integer, String>();
		Map<Integer, String> map10 = new HashMap<Integer, String>();
		Map<Integer, String> map22 = new HashMap<Integer, String>();
		Map<Integer, String> map23 = new HashMap<Integer, String>();
		Map<Integer, String> map24 = new HashMap<Integer, String>();
		Map<Integer, String> map25 = new HashMap<Integer, String>();
		Map<Integer, String> map26 = new HashMap<Integer, String>();

		map22.put(7, ((String) Mapout.get("NO1")).substring(2));
		map.put(22, map22);

		map23.put(7, ((String) Mapout.get("NO2")).substring(2));
		map.put(23, map23);

		map24.put(7, ((String) Mapout.get("NO3")).substring(2));
		map.put(24, map24);

		map25.put(7, ((String) Mapout.get("NO4")).substring(2));
		map.put(25, map25);

		map26.put(7, ((String) Mapout.get("NO5")).substring(2));
		map.put(26, map26);
		
		map8.put(6, dateFormat.format(date));
		map.put(3, map8);

		map1.put(1, guige.getText());
		map.put(6, map1);

		map1.put(2, cengji.getText());
		map.put(6, map1);

		map1.put(3, huawen.getText());
		map.put(6, map1);

		map1.put(4, shangbiao.getText());
		map.put(6, map1);

		map1.put(5, taihao.getText());
		map.put(6, map1);

		map1.put(7, zhongliang.getText());
		map.put(6, map1);

		map2.put(1, "执行标准：" + biaozhun.getValue());
		map.put(8, map2);

		map3.put(1, runwang.getText());
		map.put(12, map3);

		map3.put(2, qiya.getText());
		map.put(12, map3);

		map3.put(3, (String) Mapout.get("testLoad"));
		map.put(12, map3);

		map3.put(4, xiachenliang.getText());
		map.put(12, map3);
		// 替换
		map3.put(5, (String) Mapout.get("saturability"));
		map.put(12, map3);

		map3.put(6, (String) Mapout.get("pressureavg"));
		map.put(12, map3);
		// 印痕控制系数
		map3.put(7, (String) Mapout.get("coefficientOfGrounding"));
		map.put(12, map3);

		map4.put(1, (String) Mapout.get("effectiveGroundingArea"));
		map.put(14, map4);

		map4.put(2, (String) Mapout.get("fullGroundingArea"));
		map.put(14, map4);

		map4.put(3, (String) Mapout.get("xReduceMax"));
		map.put(14, map4);

		map4.put(4, (String) Mapout.get("yReduceMax"));
		map.put(14, map4);
		// 左最短轴
		map4.put(5, (String) Mapout.get("leftmin"));
		map.put(14, map4);
		// 右最短轴
		map4.put(6, (String) Mapout.get("rightmin"));
		map.put(14, map4);
		// 剔除
		map4.put(7, (String) Mapout.get("Mcf"));
		map.put(14, map4);

		map5.put(1, (String) Mapout.get("recArea"));
		map.put(16, map5);

		map5.put(2, (String) Mapout.get("KxdB"));
		map.put(16, map5);

		map5.put(3, (String) Mapout.get("CL"));
		map.put(16, map5);

		map5.put(4, (String) Mapout.get("ISL"));
		map.put(16, map5);

		map5.put(5, (String) Mapout.get("OSL"));
		map.put(16, map5);

		map5.put(7, (String) Mapout.get("coefficisquareRatio"));
		map.put(16, map5);

		map10.put(4, (String) Mapout.get("equation"));
		map.put(34, map10);

		map9.put(2, (String) Mapout.get("NO1"));
		map.put(35, map9);

		map9.put(3, (String) Mapout.get("NO2"));
		map.put(35, map9);

		map9.put(4, (String) Mapout.get("NO3"));
		map.put(35, map9);

		map9.put(5, (String) Mapout.get("NO4"));
		map.put(35, map9);

		map9.put(6, (String) Mapout.get("NO5"));
		map.put(35, map9);

		map6.put(2, (String) Mapout.get("area00120"));
		map.put(36, map6);

		map6.put(3, (String) Mapout.get("area00100"));
		map.put(36, map6);

		map6.put(4, (String) Mapout.get("area0080"));
		map.put(36, map6);

		map6.put(5, (String) Mapout.get("area0060"));
		map.put(36, map6);

		map6.put(6, (String) Mapout.get("area040"));
		map.put(36, map6);

		map7.put(2, (String) Mapout.get("fuhearea00120"));
		map.put(37, map7);

		map7.put(3, (String) Mapout.get("fuhearea00100"));
		map.put(37, map7);

		map7.put(4, (String) Mapout.get("fuhearea00800"));
		map.put(37, map7);

		map7.put(5, (String) Mapout.get("fuhearea00600"));
		map.put(37, map7);

		map7.put(6, (String) Mapout.get("fuhearea00400"));
		map.put(37, map7);

		map7.put(7, (String) Mapout.get("standardDiviation"));
		map.put(37, map7);

		map7.put(8, (String) Mapout.get("variance"));
		map.put(37, map7);

		System.out.println(map.size());
		en.setValue(map);
		en.setSheet(0);
		String current = new File(".").getCanonicalPath();
		File myPath = new File(current.toString());
		String filename = "模板.xlsx";
		String guiGe = guige.getText();
		if (guiGe.contains("/")) {
			guiGe = guiGe.replace('/', '_');
		}
		String outFileName = guiGe + "-" + cengji.getText() + "-" + huawen.getText() + "轮胎接地压力分布报告.xlsx";
		File mobanpath = new File(myPath, filename);
		File outFilePath = new File(myPath, outFileName);
		Mapin.put("mobanpath", mobanpath.getPath());

		ExcelUtil.writeToExcelBySingletSheetEndXlsx(Mapin, en, image1, image2, image3)
				.write(new FileOutputStream(outFilePath));
		if (outFilePath.exists()) {
			finalWarning.setFill(Paint.valueOf("#00EC00"));
			finalWarning.setText("文件生成成功！");
		}
		ExcelUtil.printExcel(mobanpath.getPath());
	}
}
