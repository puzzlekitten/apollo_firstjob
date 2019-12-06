package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SampleController implements Initializable {

	File file1;
	private Stage stage;
	Map<javafx.scene.paint.Color, Integer> originColor = new HashMap<javafx.scene.paint.Color, Integer>();
	javafx.scene.paint.Color[][] arr = new javafx.scene.paint.Color[0][0];
	int width = 0;
	int height = 0;
	int errorValue = 8;
	@FXML
	Button inPut;

	@FXML
	Button reimage;

	@FXML
	ImageView iv;

	@FXML
	ImageView ivmoliang;

	@FXML
	ChoiceBox<String> cb1;

	@FXML
	ChoiceBox<String> cb2;

	@FXML
	ChoiceBox<String> cb3;

	@FXML
	ChoiceBox<String> cb4;

	@FXML
	ChoiceBox<String> cb5;

	@FXML
	ChoiceBox<String> cb6;

	@FXML
	ChoiceBox<String> cb7;

	@FXML
	ChoiceBox<String> cb8;

	@FXML
	ChoiceBox<String> cb9;

	@FXML
	ChoiceBox<String> cb10;

	@FXML
	ChoiceBox<String> cb11;

	@FXML
	ChoiceBox<String> cb12;

	@FXML
	ChoiceBox<String> cb13;

	@FXML
	ChoiceBox<String> cb14;

	@FXML
	ChoiceBox<String> cb15;

	@FXML
	ChoiceBox<String> cb16;
	@FXML
	ChoiceBox<String> cb17;

	@FXML
	ChoiceBox<String> database;

	@FXML
	Label lb1;

	@FXML
	Label lb2;

	@FXML
	Label lb3;

	@FXML
	Label lb4;

	@FXML
	Label lb5;

	@FXML
	Label lb6;

	@FXML
	Label lb7;

	@FXML
	Label lb8;

	@FXML
	Label lb9;

	@FXML
	Label lb10;

	@FXML
	Label lb11;

	@FXML
	Label lb12;

	@FXML
	Label lb13;

	@FXML
	Label lb14;

	@FXML
	Label lb15;

	@FXML
	Label lb16;
	@FXML
	Label lb17;

	@FXML
	Label lb201;

	@FXML
	Label lb202;

	@FXML
	Label lb203;

	@FXML
	Label lb204;

	@FXML
	Label lb205;

	@FXML
	Label lb206;

	@FXML
	Label lb207;

	@FXML
	Label lb208;

	@FXML
	Label lb209;

	@FXML
	Label lb210;

	@FXML
	Label lb211;

	@FXML
	Label lb212;

	@FXML
	Label lb213;

	@FXML
	Label lb214;

	@FXML
	Label lb215;

	@FXML
	Label lb216;

	@FXML
	Label lb217;

	@FXML
	Label lb301;

	@FXML
	Label lb302;

	@FXML
	Label lb303;

	@FXML
	Label lb304;

	@FXML
	Label lb305;

	@FXML
	Label lb306;

	@FXML
	Label lb307;

	@FXML
	Label lb308;

	@FXML
	Label lb309;

	@FXML
	Label lb310;

	@FXML
	Label lb311;

	@FXML
	Label lb312;

	@FXML
	Label lb313;

	@FXML
	Label lb314;

	@FXML
	Label lb315;

	@FXML
	Label lb316;

	@FXML
	Label lb317;

	@FXML
	TextField tx101;

	@FXML
	TextField tx102;

	@FXML
	TextField tx103;

	@FXML
	TextField tx104;

	@FXML
	TextField tx105;

	@FXML
	TextField tx106;

	@FXML
	TextField tx107;

	@FXML
	TextField tx108;

	@FXML
	TextField tx109;

	@FXML
	TextField tx110;

	@FXML
	TextField tx111;

	@FXML
	TextField tx112;

	@FXML
	TextField tx113;

	@FXML
	TextField tx114;

	@FXML
	TextField tx115;

	@FXML
	TextField tx116;

	@FXML
	TextField tx117;

	@FXML
	AnchorPane ap1;

	@SuppressWarnings("rawtypes")
	List<ChoiceBox> listcb = new ArrayList<>();
	List<Label> listlb = new ArrayList<>();
	List<Label> listlb2 = new ArrayList<>();
	List<Label> listlb3 = new ArrayList<>();
	List<javafx.scene.control.TextField> listtx = new ArrayList<>();

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(java.net.URL location, ResourceBundle resources) {
		assert cb1 != null : "fx:id=\"myChoices\" was not injected: check your FXML file 'Sample.fxml'.";
		assert cb2 != null : "fx:id=\"myChoices\" was not injected: check your FXML file 'Sample.fxml'.";
		assert cb3 != null : "fx:id=\"myChoices\" was not injected: check your FXML file 'Sample.fxml'.";
		assert cb4 != null : "fx:id=\"myChoices\" was not injected: check your FXML file 'Sample.fxml'.";
		listcb.add(cb1);
		listcb.add(cb2);
		listcb.add(cb3);
		listcb.add(cb4);
		listcb.add(cb5);
		listcb.add(cb6);
		listcb.add(cb7);
		listcb.add(cb8);
		listcb.add(cb9);
		listcb.add(cb10);
		listcb.add(cb11);
		listcb.add(cb12);
		listcb.add(cb13);
		listcb.add(cb14);
		listcb.add(cb15);
		listcb.add(cb16);
		listcb.add(cb17);
		listlb.add(lb1);
		listlb.add(lb2);
		listlb.add(lb3);
		listlb.add(lb4);
		listlb.add(lb5);
		listlb.add(lb6);
		listlb.add(lb7);
		listlb.add(lb8);
		listlb.add(lb9);
		listlb.add(lb10);
		listlb.add(lb11);
		listlb.add(lb12);
		listlb.add(lb13);
		listlb.add(lb14);
		listlb.add(lb15);
		listlb.add(lb16);
		listlb.add(lb17);
		listlb2.add(lb201);
		listlb2.add(lb202);
		listlb2.add(lb203);
		listlb2.add(lb204);
		listlb2.add(lb205);
		listlb2.add(lb206);
		listlb2.add(lb207);
		listlb2.add(lb208);
		listlb2.add(lb209);
		listlb2.add(lb210);
		listlb2.add(lb211);
		listlb2.add(lb212);
		listlb2.add(lb213);
		listlb2.add(lb214);
		listlb2.add(lb215);
		listlb2.add(lb216);
		listlb2.add(lb217);
		listlb3.add(lb301);
		listlb3.add(lb302);
		listlb3.add(lb303);
		listlb3.add(lb304);
		listlb3.add(lb305);
		listlb3.add(lb306);
		listlb3.add(lb307);
		listlb3.add(lb308);
		listlb3.add(lb309);
		listlb3.add(lb310);
		listlb3.add(lb311);
		listlb3.add(lb312);
		listlb3.add(lb313);
		listlb3.add(lb314);
		listlb3.add(lb315);
		listlb3.add(lb316);
		listlb3.add(lb317);
		listtx.add(tx101);
		listtx.add(tx102);
		listtx.add(tx103);
		listtx.add(tx104);
		listtx.add(tx105);
		listtx.add(tx106);
		listtx.add(tx107);
		listtx.add(tx108);
		listtx.add(tx109);
		listtx.add(tx110);
		listtx.add(tx111);
		listtx.add(tx112);
		listtx.add(tx113);
		listtx.add(tx114);
		listtx.add(tx115);
		listtx.add(tx116);
		listtx.add(tx117);
		ivmoliang.setVisible(false);
		for (@SuppressWarnings("rawtypes")
		ChoiceBox choiceBox : listcb) {
			choiceBox.setItems(FXCollections.observableArrayList("胎面胶", "基层胶", "胎侧胶", "垫胶", "胎体", "内衬层", "耐磨胶", "上三角",
					"下三角", "大钢包", "小钢包", "1#带束层", "2#带束层", "3#带束层", "4#带束层", "0度", "钢包钢夹胶", "带束钢夹胶"));
		}
		database.setItems(FXCollections.observableArrayList("模量"));
	}

	// map以value值降序排列 返回list key value
	private static List<Map.Entry<Color, Integer>> sortByValueFloatDesc(Map<Color, Integer> nowPartTwoData) {
		// 这里将map.entrySet()转换成list
		List<Map.Entry<Color, Integer>> list = new ArrayList<Map.Entry<Color, Integer>>(nowPartTwoData.entrySet());
		// 然后通过比较器来实现排序
		Collections.sort(list, new Comparator<Map.Entry<Color, Integer>>() {
			// 降序排序
			@Override
			public int compare(Map.Entry<Color, Integer> o1, Map.Entry<Color, Integer> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});

		return list;
	}

	List<Label> listlb2choosed = new ArrayList<>();
	List<javafx.scene.control.TextField> listtxchoosed = new ArrayList<>();
	List<Label> listlb3choosed = new ArrayList<>();

	@FXML
	public void choose(MouseEvent event) throws IOException {
		for (int i = 0; i <= 16; i++) {
			if (listcb.get(i).getValue() != null) {
				listlb2.get(i).setText((String) listcb.get(i).getValue());
				listlb2.get(i).setUserData(listcb.get(i).getUserData());
				listtx.get(i).setVisible(true);
				listlb2choosed.add(listlb2.get(i));
				listtxchoosed.add(listtx.get(i));
				listlb3choosed.add(listlb3.get(i));
			} else {
				listtx.get(i).setVisible(false);
			}
		}
	}

	public List<Color> customColormodulus(List<Double> list) {
		List<Color> listRcolor = new ArrayList<>();
		if (database.getValue().equals("模量")) {
			ivmoliang.setVisible(true);
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) >= 15) {
					if (list.get(i) > 20) {
						Color color = new Color(1, 0, 0, 1);
						listRcolor.add(color);
					} else {
						Color color = new Color(1, (20-list.get(i)) / 5, 0, 1);
						listRcolor.add(color);
					}
				} else if (list.get(i) >= 10) {
					System.out.println((list.get(i) - 10) / 5);
					Color color = new Color((list.get(i) - 10) / 5, 1, 0, 1);
					listRcolor.add(color);
				} else if (list.get(i) >= 5) {
					Color color = new Color(0, 1, (10 - list.get(i)) / 5, 1);
					listRcolor.add(color);
				} else if (list.get(i) >= 0) {
					Color color = new Color(0, list.get(i) / 5, 1, 1);
					listRcolor.add(color);
				} else {
					Color color = new Color(0, 0, 1, 1);
					listRcolor.add(color);
				}
			}
		}
		return listRcolor;
	}

	// 重绘
	@FXML
	public void reconstructedImage(ActionEvent event) throws IOException {
		// 第一步：將部件模量list导入方法并得到colorlist
		List<Double> listvalue = new ArrayList<>();
		for (int i = 0; i < listtxchoosed.size(); i++) {
			listvalue.add(Double.valueOf(listtxchoosed.get(i).getText()));
		}
		System.out.println(listtxchoosed.size());
		List<Color> listRecolor = new ArrayList<>();
		listRecolor = this.customColormodulus(listvalue);
		for (int i = 0; i < listRecolor.size(); i++) {
			listlb3choosed.get(i).setBackground(new Background(new BackgroundFill(listRecolor.get(i), null, null)));
		}

		// 替换颜色
		WritableImage wi1 = new WritableImage(width, height);
		PixelWriter pw1 = wi1.getPixelWriter();
		System.out.println("开始绘制");
		System.out.println(((Color) listcb.get(0).getUserData()).getRed());
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				// 宽判断
				for (int k = 0; k < listlb2choosed.size(); k++) {
					if (Math.abs(arr[i][j].getRed() * 255
							- ((Color) listlb2choosed.get(k).getUserData()).getRed() * 255) <= errorValue
							&& Math.abs(arr[i][j].getBlue() * 255
									- ((Color) listlb2choosed.get(k).getUserData()).getBlue() * 255) <= errorValue
							&& Math.abs(arr[i][j].getGreen() * 255
									- ((Color) listlb2choosed.get(k).getUserData()).getGreen() * 255) <= errorValue) {
						pw1.setColor(i, j, listRecolor.get(k));
						break;
					}
					if (k == listlb2choosed.size() - 1) {
						pw1.setColor(i, j, (Color) arr[i][j]);
					}
				}

			}
		}
		iv.setImage((Image) wi1);
		System.out.println("完成");
		String current = new File(".").getCanonicalPath();
		File myPath = new File(current.toString());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HHmmss");
		String filename = df.format(new Date()) + "验证图片.png";
		System.out.println(filename.toString());
		BufferedImage bi = SwingFXUtils.fromFXImage(wi1, null);
		ImageIO.write(bi, "png", new File(myPath, filename));
	}

	@FXML
	public void input(ActionEvent event) throws IOException {
		FileChooser fc = new FileChooser();

		fc.setInitialDirectory(new File(System.getProperty("user.home")));
		FileChooser.ExtensionFilter pngExtensionFilter = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
		fc.getExtensionFilters().add(pngExtensionFilter);
		fc.setSelectedExtensionFilter(pngExtensionFilter);
		fc.setTitle("导入印痕png文件");
		file1 = fc.showOpenDialog(stage);
		if (file1 != null) {
			String path = "file:" + file1.getPath();
			Image image = new Image(path);
			iv.setImage(image);
			width = (int) image.getWidth();
			height = (int) image.getHeight();
			PixelReader pr = image.getPixelReader();

			javafx.scene.paint.Color[][] data = new javafx.scene.paint.Color[width][height];
			System.out.println(image.getHeight() + "---" + image.getWidth());
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					javafx.scene.paint.Color color = pr.getColor(i, j);
					data[i][j] = color;
					if (originColor.containsKey(color)) {
						originColor.put(color, originColor.get(color) + 1);
					} else {
						originColor.put(color, 1);
					}
				}
			}
			arr = Arrays.copyOf(data, data.length);
			@SuppressWarnings("static-access")
			List<Entry<Color, Integer>> list = this.sortByValueFloatDesc(originColor);
			for (int i = 1; i <= 17; i++) {
				listlb.get(i - 1).setBackground(new Background(new BackgroundFill(list.get(i).getKey(), null, null)));
				listcb.get(i - 1).setUserData(list.get(i).getKey());
			}

		}

	}
}
