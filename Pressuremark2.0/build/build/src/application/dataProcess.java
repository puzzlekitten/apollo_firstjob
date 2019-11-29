package application;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import java.util.Set;

import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class dataProcess {

	public Map start(Map map1) throws IOException {
		String filePath = map1.get("pathIn").toString();// ------
		System.out.println(filePath);
		BufferedReader in = new BufferedReader(new FileReader(filePath));// 打开文件创建数据流
		Map<Point, Object> dataMap = new HashMap<Point, Object>();
		Map<Point, Integer> effectivedataMap = new HashMap<Point, Integer>();// 有效接地点集合
		String str; // 定义String变量用来保存每一次读到的每一行的数据
		String[] data = new String[0];// 定义数组用来保存每个点的数据
		double perUnitLength = Double.valueOf((String) map1.get("perUnitLength")); // 节点单位长度------
		int minpoints = Integer.parseInt((String) map1.get("minpoints"));// 一列最小点数
		int testLoad = new Double((Double.parseDouble((String) map1.get("testLoad")))).intValue();// 用户输入 试验负荷----
		int xReduceMaxPoints = 0;// 纵向最大值节点数
		int xReduceMinPoints = 0;// 纵向边部节点数
		int leftup = 0;// 左上
		int leftdown = 0;// 左下
		int rightup = 0;// 右上
		int rightdown = 0;// 右下
		int width = 0;
		int length = 0;
		int gradient = Integer.parseInt((String) map1.get("gradient"));// 边界梯度
		List<Integer> mapup = new ArrayList<Integer>();// 每列上点
		List<Integer> mapdown = new ArrayList<Integer>();// 每列下点

		Map<Integer, Integer> borderNozero = new HashMap<Integer, Integer>();// x坐标和每列的面积
		Map<Integer, Integer> borderWhithzero = new HashMap<Integer, Integer>();
		Map<String, Object> Mapout = new HashMap<String, Object>();
		while ((str = in.readLine()) != null) {
			if (str.contains("ASCII_DATA @@")) {// 当包含次字符串跳出循环
				break;
			}
		}
		in.readLine();// 过滤掉一行
		in.readLine();// 过滤掉一行
		while ((str = in.readLine()) != null) {
			// 见@@结束
			if (str.startsWith("@@")) {
				break;
			}
			data = Arrays.copyOf(data, data.length + 1); // 扩容如果对内存没有要求可以直接声明一个大容量的数组
			data[data.length - 1] = str;// 获取子串保存到数组中
			// System.out.println(str);
		}
		// System.out.println(data[data.length - 1]);
		// System.out.println(data[0]);
		in.close();
		// 更改方向
		int maxValue = 0;
		if (map1.get("direction").equals("竖向")) {
			for (int i = 0; i < data.length; i++) {
				String[] se = data[i].trim().split(",");
				width = se.length;
				length = data.length;
				// System.out.println(se.length);
				for (int j = 0; j < se.length; j++) {
					Point point = new Point(j, i);
					String power = se[j];
					if (Integer.parseInt(power) > maxValue) {
						maxValue = Integer.parseInt(power);
					}
					// System.out.print(power);
					dataMap.put(point, power);
				}
			}
			// System.out.println("竖向");
		} else {
			for (int i = 0; i < data.length; i++) {
				String[] se = data[i].trim().split(",");
				width = data.length;
				length = se.length;
				// System.out.println(se.length);
				for (int j = 0; j < se.length; j++) {
					Point point = new Point(i, j);
					String power = se[j];
					if (Integer.parseInt(power) > maxValue) {
						maxValue = Integer.parseInt(power);
					}
					// System.out.print(power);
					dataMap.put(point, power);
				}
			}

		}
		// System.out.println("maxValue" + maxValue);
		int pressureMin = 0;// 压力最小筛选值
		if (maxValue > 800) {
			pressureMin = 60;
			maxValue = (maxValue / 100 + 1) * 100;// 压力区间
		} else {
			pressureMin = 1;
			maxValue = (maxValue / 10 + 1) * 10;// 压力区间
		}
		// System.out.println(dataMap.size());
		// 缩量化1：列筛选：当全列大于0的个数和小于10时，删除该列。
		// 第一步：按列把不为0的数取出
		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
		for (Entry<Point, Object> entry : dataMap.entrySet()) {
			// System.out.println(entry.getKey().toString()+entry.getValue());
			if (Integer.parseInt((String) entry.getValue()) > 0) {
				// if (Double.valueOf((String) entry.getValue())>0) {
				// System.out.println(entry.getKey().toString() + entry.getValue());
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("colum", String.valueOf(entry.getKey().getX()));
				result.put("value", 1);
				results.add(result);
			}
		}
		// System.out.println(results.size());
		// 第二步：计算每列不为0的个数
		Map<String, Object> result1 = new HashMap<String, Object>();
		for (Map<String, Object> map : results) {
			String id = map.get("colum").toString();
			Integer value = Integer.parseInt((map.get("value").toString()));
			if (result1.containsKey(id)) {
				Integer temp = Integer.parseInt(result1.get(id).toString());
				value += temp;
			}
			result1.put(id, value);
		}
		// System.out.println(result1.size());
		// 第三步：找出大于50个数的列，存入集合
		Integer[] slip = new Integer[0];// 定义数组用来保存需要的滑动数据
		for (Entry<String, Object> entry : result1.entrySet()) {
			if ((Integer) entry.getValue() > minpoints) {// 最小点数
				// System.out.println(entry.getKey() + ":" + entry.getValue());
				slip = Arrays.copyOf(slip, slip.length + 1);
				slip[slip.length - 1] = Integer.parseInt(entry.getKey().toString());
			}
		}
		Arrays.sort(slip);
		// System.out.println(slip[slip.length - 1] + ":" + slip[0]);
		int yReduceMaxPoints = slip[slip.length - 1] - slip[0] + 1;// 径向最长节点数 报错***
		// System.out.println(yReduceMaxPoints);
		// *对目前集合进行遍历，得出每X中Y的起点和终点。
		// 双重循环按X坐标建立Y集合，找出边界点
		int effectiveGroundingAreaPoints = 0;// 有效接地面积节点数
		for (int i = slip[0]; i <= slip[slip.length - 1]; i++) {
			// Y轴集合
			Map<Integer, String> mapi = new HashMap<Integer, String>();
			for (Entry<Point, Object> entry : dataMap.entrySet()) {
				// 当行数等于1时，所有点的信息
				if (entry.getKey().getX() == i) {
					// 建立新的x轴为i的map
					mapi.put(entry.getKey().getY(), (String) entry.getValue());
				}
			}
			// 删选条件
			int[] pitchs = new int[0];// 将选出Y轴的点全部放入数组，取头尾
			for (int j = 3; j < data.length - 3; j++) {
				if (Integer.parseInt(mapi.get(j)) > pressureMin
						&& ((Integer.parseInt(mapi.get(j - 1)) < pressureMin
								&& Integer.parseInt(mapi.get(j - 2)) < pressureMin)
								|| (Integer.parseInt(mapi.get(j - 1)) < pressureMin
										&& Integer.parseInt(mapi.get(j - 3)) < pressureMin)
								|| (Integer.parseInt(mapi.get(j - 3)) < pressureMin
										&& Integer.parseInt(mapi.get(j - 2)) < pressureMin))
						&& ((Integer.parseInt(mapi.get(j - 1)) > pressureMin
								&& Integer.parseInt(mapi.get(j + 2)) > pressureMin)
								|| (Integer.parseInt(mapi.get(j + 1)) > pressureMin
										&& Integer.parseInt(mapi.get(j + 3)) > pressureMin)
								|| (Integer.parseInt(mapi.get(j + 3)) > pressureMin
										&& Integer.parseInt(mapi.get(j + 2)) > pressureMin)
								|| (Integer.parseInt(mapi.get(j + 4)) > pressureMin
										&& Integer.parseInt(mapi.get(j + 1)) > pressureMin)
								|| (Integer.parseInt(mapi.get(j + 4)) > pressureMin
										&& Integer.parseInt(mapi.get(j + 2)) > pressureMin)
								|| (Integer.parseInt(mapi.get(j + 3)) > pressureMin
										&& Integer.parseInt(mapi.get(j + 4)) > pressureMin))) {
					// System.out.println("X轴=" + i + "Y轴=" + j + "值=" + mapi.get(j));
					pitchs = Arrays.copyOf(pitchs, pitchs.length + 1); // 扩容如果对内存没有要求可以直接声明一个大容量的数组
					pitchs[pitchs.length - 1] = j;// 坐标集
					// System.out.println(j);
				}
				if (Integer.parseInt(mapi.get(j)) > pressureMin
						&& ((Integer.parseInt(mapi.get(j - 1)) > pressureMin
								&& Integer.parseInt(mapi.get(j - 2)) > pressureMin)
								|| (Integer.parseInt(mapi.get(j - 1)) > pressureMin
										&& Integer.parseInt(mapi.get(j - 3)) > pressureMin)
								|| (Integer.parseInt(mapi.get(j - 3)) > pressureMin
										&& Integer.parseInt(mapi.get(j - 2)) > pressureMin)
								|| (Integer.parseInt(mapi.get(j - 4)) > pressureMin
										&& Integer.parseInt(mapi.get(j - 3)) > pressureMin)
								|| (Integer.parseInt(mapi.get(j - 4)) > pressureMin
										&& Integer.parseInt(mapi.get(j - 2)) > pressureMin)
								|| (Integer.parseInt(mapi.get(j - 1)) > pressureMin
										&& Integer.parseInt(mapi.get(j - 4)) > pressureMin))
						&& ((Integer.parseInt(mapi.get(j + 1)) < pressureMin
								&& Integer.parseInt(mapi.get(j + 2)) < pressureMin)
								|| (Integer.parseInt(mapi.get(j + 1)) < pressureMin
										&& Integer.parseInt(mapi.get(j + 3)) < pressureMin)
								|| (Integer.parseInt(mapi.get(j + 3)) < pressureMin
										&& Integer.parseInt(mapi.get(j + 2)) < pressureMin))) {
					// System.out.println("X轴=" + i + "Y轴=" + j + "值=" + mapi.get(j));
					// System.out.println(j);
					pitchs = Arrays.copyOf(pitchs, pitchs.length + 1); // 扩容如果对内存没有要求可以直接声明一个大容量的数组
					pitchs[pitchs.length - 1] = j;// 坐标集
				}
			}
			if (pitchs.length != 0) {
				// 排序取头尾
				Arrays.sort(pitchs);
				// System.out.println(pitchs[0] + " " + pitchs[pitchs.length - 1]);
				int xReducePoints = pitchs[pitchs.length - 1] - pitchs[0] + 1;// 纵向节点数
				// 每列上下节点
				mapup.add(pitchs[0]);
				mapdown.add(pitchs[pitchs.length - 1]);
				borderNozero.put(i, xReducePoints);
				if (xReducePoints > xReduceMaxPoints) {
					xReduceMaxPoints = xReducePoints;
				}
				// 取出有效接地点集
				for (int i2 = pitchs[0]; i2 <= pitchs[pitchs.length - 1]; i2++) {
					Point p = new Point();
					p.setX(i);
					p.setY(i2);
					if (Integer.parseInt((String) dataMap.get(p)) > 0) {
						effectivedataMap.put(p, Integer.parseInt((String) dataMap.get(p)));
					}
					// if(i==132) {
					// System.out.println(i2);
					// }
				}
			} else {
				borderWhithzero.put(i, 0);
			}
		}
		// 短轴
		for (int i = 0; i < mapup.size(); i++) {
			if (mapup.get(i) - mapup.get(i + 1) < gradient) {
				leftup = mapup.get(i);
				break;
			}
		}
		for (int i = mapup.size() - 1; i > -1; i--) {
			if (mapup.get(i) - mapup.get(i - 1) < gradient) {
				rightup = mapup.get(i);
				break;
			}
		}
		for (int i = 0; i < mapdown.size(); i++) {
			if (mapdown.get(i + 1) - mapdown.get(i) < gradient) {
				leftdown = mapdown.get(i);
				break;
			}
		}
		for (int i = mapdown.size() - 1; i > -1; i--) {
			if (mapdown.get(i - 1) - mapdown.get(i) < gradient) {
				rightdown = mapdown.get(i);
				break;
			}
		}
		// System.out.println("上下左右：" + leftup + "-" + rightup + "-" + leftdown + "-" +
		// rightdown);
		// 短轴
		xReduceMinPoints = Math.round(((leftdown - leftup) + (rightdown - rightup)) / 2 + 1);
		Mapout.put("leftmin", String.format("%.2f", (leftdown - leftup + 1) * perUnitLength));
		Mapout.put("rightmin", String.format("%.2f", (rightdown - rightup + 1) * perUnitLength));
		// 计算径向压力和
		Map<Integer, Integer> pressureSumMap = new HashMap<Integer, Integer>();// 有效点集轴向压力和
		Map<Integer, Integer> pressureSumMap1 = new HashMap<Integer, Integer>();// 有效点集轴向压力和
		for (Entry<Point, Integer> entry : effectivedataMap.entrySet()) {
			for (int i = slip[0]; i <= slip[slip.length - 1]; i++) {
				if (entry.getKey().getX() == i) {
					if (pressureSumMap.get(i) != null) {
						pressureSumMap.put(i, entry.getValue() + pressureSumMap.get(i));
					} else {
						pressureSumMap.put(i, entry.getValue());
					}
				}
			}
		}
		pressureSumMap1 = pressureSumMap;
		for (int i = slip[0]; i <= slip[slip.length - 1]; i++) {
			if (pressureSumMap1.get(i) == null) {
				pressureSumMap1.put(i, 0);
			}
		}

		// System.out.println(pressureSumMap.keySet());
		// 建立多项式
		double[] x1 = new double[0];// X轴
		double[] y1 = new double[0];// 点数
		for (Entry<Integer, Integer> entry : pressureSumMap.entrySet()) {
			// System.out.println(entry.getKey() + "," + entry.getValue() + ",");
			x1 = Arrays.copyOf(x1, x1.length + 1);
			x1[x1.length - 1] = entry.getKey();
			y1 = Arrays.copyOf(y1, y1.length + 1);
			y1[y1.length - 1] = entry.getValue();
		}
		WeightedObservedPoints points1 = new WeightedObservedPoints();
		for (int i = 0; i < x1.length; i++) { // 把数据点加入观察的序列
			points1.add(x1[i], y1[i]);
		}

		PolynomialCurveFitter fitter1 = PolynomialCurveFitter.create(2); // 指定多项式阶数
		double[] result2 = fitter1.fit(points1.toList()); // 曲线拟合，结果保存于数组
		// 各项系数
		double c1 = result2[0];
		double b1 = result2[1];
		double a1 = result2[2];
		List<Integer> fillShort1 = new ArrayList<>();// 需要填充的X列
		for (Entry<Integer, Integer> entry : pressureSumMap1.entrySet()) {
			if (entry.getValue() < (a1 * Math.pow(entry.getKey(), 2) + b1 * entry.getKey() + c1)) {
				fillShort1.add(entry.getKey());
			}
		}
		Collections.sort(fillShort1);
		// System.out.print(fillShort1);
		if (fillShort1.get(0) == slip[0]) {
			while (fillShort1.get(1) - fillShort1.get(0) == 1) {
				fillShort1.remove(0);
			}
		}
		fillShort1.remove(0);
		if (fillShort1.get(fillShort1.size() - 1).equals(slip[slip.length - 1])) {

			while (fillShort1.get(fillShort1.size() - 1) - fillShort1.get(fillShort1.size() - 2) == 1) {
				fillShort1.remove(fillShort1.size() - 1);
				// System.out.println("wozaizhe ");
			}
		}
		Map<Integer, Integer> borderfill1 = pressureSumMap1;
		List<Integer> fillXht1 = new ArrayList<>();// 需要填充的X列花纹沟头尾
		// 筛选出头尾列
		int t1 = 0;
		for (int i = 0; i < fillShort1.size() - 1; i++) {
			t1 = t1 + 1;
			if (fillShort1.get(i + 1) - fillShort1.get(i) != 1) {
				fillXht1.add(fillShort1.get(i - t1 + 1) - 1);
				fillXht1.add(fillShort1.get(i) + 1);
				t1 = 0;
			}
		}
		// System.out.println(fillXht.toString());
		// 填充花纹沟
		//
		// System.out.print(fillXht1);
		for (int i = 0; i < fillXht1.size(); i = i + 2) {
			int len1 = pressureSumMap1.get(fillXht1.get(i) - 1);
			int len2 = pressureSumMap1.get(fillXht1.get(i + 1) + 1);
			for (int i1 = fillXht1.get(i) - 1; i1 <= fillXht1.get(i + 1) + 1; i1++) {
				borderfill1.put(i1, Math.round((len1 + len2) / 2));
			}
		}
		// 计算填充过的轴向压力和趋势线
		double[] x2 = new double[0];// X轴
		double[] y2 = new double[0];// 点数
		for (Entry<Integer, Integer> entry : borderfill1.entrySet()) {
			// System.out.println(entry.getKey() + "," + entry.getValue() + ",");
			x2 = Arrays.copyOf(x2, x2.length + 1);
			x2[x2.length - 1] = (entry.getKey() - slip[0]) * perUnitLength;
			y2 = Arrays.copyOf(y2, y2.length + 1);
			y2[y2.length - 1] = entry.getValue();
		}
		WeightedObservedPoints points2 = new WeightedObservedPoints();
		for (int i = 0; i < x2.length; i++) { // 把数据点加入观察的序列
			points2.add(x2[i], y2[i]);
		}

		PolynomialCurveFitter fitter2 = PolynomialCurveFitter.create(2); // 指定多项式阶数
		double[] result3 = fitter2.fit(points2.toList()); // 曲线拟合，结果保存于数组
		// 各项系数
		double c2 = result3[0];
		double b2 = result3[1];
		double a2 = result3[2];
		double qm = -Math.pow(Double.valueOf(b2) / ((slip[slip.length - 1] - slip[0]) * perUnitLength), 2)
				/ (4 * (Double.valueOf(a2) / Math.pow((slip[slip.length - 1] - slip[0]) * perUnitLength, 2)));
		Mapout.put("equation", "y="
				+ String.format("%.10f",
						Double.valueOf(a2) / (Math.pow((slip[slip.length - 1] - slip[0]) * perUnitLength, 2)))
				+ "x^2+"
				+ String.format("%.2f", Double.valueOf(b2) / ((slip[slip.length - 1] - slip[0]) * perUnitLength)) + "x"
				+ "  Qm=" + String.format("%.2f", Double.valueOf(qm)));

		// 计算有效接地面积的方差和标准差
		double[] pressureTotal = new double[0];// 定义数组用来保存需要的滑动数据
		for (Entry<Point, Integer> entry : effectivedataMap.entrySet()) {
			if (entry.getValue() != 0) {
				pressureTotal = Arrays.copyOf(pressureTotal, pressureTotal.length + 1);
				pressureTotal[pressureTotal.length - 1] = Double.valueOf(entry.getValue());
			}
		}
		double standardDiviation = calculate.StandardDiviation(pressureTotal);// 标准差σ=sqrt(s^2)
		double variance = calculate.Variance(pressureTotal);// 方差
		Mapout.put("standardDiviation", String.format("%.2f", Double.valueOf(standardDiviation)));
		Mapout.put("variance", String.format("%.2f", Double.valueOf(variance)));
		// 计算二阶多项趋势线来求接地总面积
		double[] x = new double[0];// X轴
		double[] y = new double[0];// 点数
		for (Entry<Integer, Integer> entry : borderNozero.entrySet()) {
			// System.out.println(entry.getKey() + "," + entry.getValue() + ",");
			borderWhithzero.put(entry.getKey(), entry.getValue());
			x = Arrays.copyOf(x, x.length + 1);
			x[x.length - 1] = entry.getKey();
			y = Arrays.copyOf(y, y.length + 1);
			y[y.length - 1] = entry.getValue();
		}
		WeightedObservedPoints points = new WeightedObservedPoints();
		for (int i = 0; i < x.length; i++) { // 把数据点加入观察的序列
			points.add(x[i], y[i]);
		}

		PolynomialCurveFitter fitter = PolynomialCurveFitter.create(2); // 指定多项式阶数
		double[] result = fitter.fit(points.toList()); // 曲线拟合，结果保存于数组
		// 各项系数
		double c = result[0];
		double b = result[1];
		double a = result[2];
		List<Integer> fillShort = new ArrayList<>();// 需要填充的X列
		// System.out.println("a=" + a + "--b=" + b + "--c=" + c);
		// 取出小于趋势线的列
		for (Entry<Integer, Integer> entry : borderWhithzero.entrySet()) {
			if (entry.getValue() < (a * Math.pow(entry.getKey(), 2) + b * entry.getKey() + c)) {
				fillShort.add(entry.getKey());
			}
		}
		Collections.sort(fillShort);

		// System.out.println(fillShort.toString());
		// 去头
		if (fillShort.get(0) == slip[0]) {
			while (fillShort.get(1) - fillShort.get(0) == 1) {
				fillShort.remove(0);
			}
		}
		fillShort.remove(0);
		// 去尾
		// System.out.println(fillShort.get(fillShort.size() - 1) + "-" +
		// slip[slip.length - 1]);
		// System.out.println(fillShort.get(fillShort.size() - 1) + "-" +
		// fillShort.get(fillShort.size() - 2));
		if (fillShort.get(fillShort.size() - 1).equals(slip[slip.length - 1])) {

			while (fillShort.get(fillShort.size() - 1) - fillShort.get(fillShort.size() - 2) == 1) {
				fillShort.remove(fillShort.size() - 1);
				// System.out.println("wozaizhe ");
			}
		}
		// System.out.println("去头去尾后" + fillShort.toString());
		// 有效接地节点数
		for (Entry<Point, Integer> entry : effectivedataMap.entrySet()) {
			if (entry.getValue() > 0) {
				effectiveGroundingAreaPoints = effectiveGroundingAreaPoints + 1;
			}
		}
		Map<Integer, Integer> borderfill = borderNozero;
		List<Integer> fillXht = new ArrayList<>();// 需要填充的X列花纹沟头尾
		// 筛选出头尾列
		int t = 0;
		for (int i = 0; i < fillShort.size() - 1; i++) {
			t = t + 1;
			if (fillShort.get(i + 1) - fillShort.get(i) != 1) {
				fillXht.add(fillShort.get(i - t + 1) - 1);
				fillXht.add(fillShort.get(i) + 1);
				t = 0;
			}
		}
		System.out.println(fillXht.toString());
		// 填充花纹沟
		//
		for (int i = 0; i < fillXht.size(); i = i + 2) {
			int len1 = borderNozero.get(fillXht.get(i) - 1);
			int len2 = borderNozero.get(fillXht.get(i + 1) + 1);
			for (int i1 = fillXht.get(i) - 1; i1 <= fillXht.get(i + 1) + 1; i1++) {
				borderfill.put(i1, Math.round((len1 + len2) / 2));
			}
		}

		int fullGroundingAreaPoints = 0;// 接地总面积节点数
		for (Entry<Integer, Integer> entry : borderfill.entrySet()) {
			fullGroundingAreaPoints = fullGroundingAreaPoints + entry.getValue();
		}
		// 压力区间和面积

		Map<Point, Integer> area01 = new HashMap<Point, Integer>();
		Map<Point, Integer> area02 = new HashMap<Point, Integer>();
		Map<Point, Integer> area03 = new HashMap<Point, Integer>();
		Map<Point, Integer> area04 = new HashMap<Point, Integer>();
		Map<Point, Integer> area05 = new HashMap<Point, Integer>();
		for (Entry<Point, Integer> entry : effectivedataMap.entrySet()) {
			if (entry.getValue() >= maxValue * 0.8) {
				area05.put(entry.getKey(), entry.getValue());
			} else if (entry.getValue() >= maxValue * 0.6) {
				area04.put(entry.getKey(), entry.getValue());
			} else if (entry.getValue() >= maxValue * 0.4) {
				area03.put(entry.getKey(), entry.getValue());
			} else if (entry.getValue() >= maxValue * 0.2) {
				area02.put(entry.getKey(), entry.getValue());
			} else if (entry.getValue() > 0) {
				area01.put(entry.getKey(), entry.getValue());
			}
		}

		// figure1 figure2 数据
		int maxX = 0;
		int maxY = 0;
		int minX = 0;
		int minY = 0;
		int time = 1;

		for (Entry<Point, Integer> entry : effectivedataMap.entrySet()) {
			if (time == 1) {
				maxX = entry.getKey().getX();
				minX = entry.getKey().getX();
				maxY = entry.getKey().getY();
				minY = entry.getKey().getY();
			}
			if (entry.getKey().getX() > maxX) {
				maxX = entry.getKey().getX();
			}
			if (entry.getKey().getX() < minX) {
				minX = entry.getKey().getX();
			}
			if (entry.getKey().getY() > maxY) {
				maxY = entry.getKey().getY();
			}
			if (entry.getKey().getY() < minY) {
				minY = entry.getKey().getY();
			}

			time = time + 1;
		}
		Global.maxX = maxX * perUnitLength;
		Global.maxY = maxY * perUnitLength;
		Global.minX = minX * perUnitLength;
		Global.minY = minY * perUnitLength;

		// 中间线坐标
		Map<Integer, Integer> mapX = new HashMap<Integer, Integer>();// 行间距离和力的关系，径向中心
		Map<Integer, Integer> mapY = new HashMap<Integer, Integer>();// 列距离与力的关系，轴向中心
		int middleX = Math.round((maxX + minX) / 2);
		int middleY = Math.round((maxY + minY) / 2);
		// Y轴中间点
		for (Entry<Point, Object> entry : dataMap.entrySet()) {
			if (entry.getKey().getX() == middleX) {
				mapX.put(entry.getKey().getY(), Integer.parseInt((String) entry.getValue()));
			}
			if (entry.getKey().getY() == middleY) {
				mapY.put(entry.getKey().getX(), Integer.parseInt((String) entry.getValue()));
			}
		}
		// 排序得到二维数据
		Set<Integer> setmapX = mapX.keySet();
		Object[] arrX = setmapX.toArray();
		double[] arrXvalue = new double[0];
		double[] arrXkey = new double[arrX.length];
		Arrays.sort(arrX);
		for (Object key : arrX) {
			arrXvalue = Arrays.copyOf(arrXvalue, arrXvalue.length + 1);
			arrXvalue[arrXvalue.length - 1] = mapX.get(key);
		}
		for (int i = 0; i < arrXkey.length; i++) {
			arrXkey[i] = Double.valueOf(String.valueOf(arrX[i])) * perUnitLength;
		}
		Global.figure2X = arrXkey.clone();
		Global.figure2Y = arrXvalue.clone();
		// Y轴中心线
		Set<Integer> setmapY = mapY.keySet();
		Object[] arrY = setmapY.toArray();
		double[] arrYvalue = new double[0];
		double[] arrYkey = new double[arrY.length];
		Arrays.sort(arrY);
		for (Object key : arrY) {
			arrYvalue = Arrays.copyOf(arrYvalue, arrYvalue.length + 1);
			arrYvalue[arrYvalue.length - 1] = mapY.get(key);
		}
		for (int i = 0; i < arrYkey.length; i++) {
			arrYkey[i] = Double.valueOf(String.valueOf(arrY[i])) * perUnitLength;
		}
		Global.figure1X = arrYkey.clone();
		Global.figure1Y = arrYvalue.clone();

		// 验证图片
		WritableImage wi = new WritableImage(width, length);
		PixelWriter pw = wi.getPixelWriter();
		// valueOf("#FFD700")
		if (map1.get("direction").equals("竖向")) {
			for (int i = 0; i < data.length; i++) {
				String[] se = data[i].trim().split(",");
				for (int j = 0; j < se.length; j++) {
					if (Integer.parseInt((String) se[j]) != 0) {
						Point p = new Point();
						p.setX(j);
						p.setY(i);
						if (effectivedataMap.containsKey(p)) {
							pw.setColor(j, i, Color.rgb(0, 0, 0));
						} else {
							pw.setColor(j, i, Color.rgb(255, 50, 255
									- (int) Math.round((Integer.parseInt((String) se[j])) / (maxValue * 1.1) * 255)));
						}
					} else {
						pw.setColor(j, i, Color.rgb(255, 255, 255));
					}
				}
				// System.out.println("竖向");
			}
		} else {
			for (int i = 0; i < data.length; i++) {
				String[] se = data[i].trim().split(",");

				for (int j = 0; j < se.length; j++) {
					if (Integer.parseInt((String) se[j]) != 0) {
						Point p = new Point();
						p.setX(i);
						p.setY(j);
						if (effectivedataMap.containsKey(p)) {
							pw.setColor(i, j, Color.rgb(0, 0, 0));
						} else {
							pw.setColor(i, j, Color.rgb(255, 50, 255
									- (int) Math.round((Integer.parseInt((String) se[j])) / (maxValue * 1.1) * 255)));
						}
					} else {
						pw.setColor(i, j, Color.rgb(255, 255, 255));
					}

				}
			}
		}
		String current = new File(".").getCanonicalPath();
		File myPath = new File(current.toString());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HHmmss");
		String filename = df.format(new Date()) + "验证图片.png";
		System.out.println(filename.toString());
		BufferedImage bi = SwingFXUtils.fromFXImage(wi, null);
		ImageIO.write(bi, "png", new File(myPath, filename));

		// 印痕图片
		// 宽
		width = maxX - minX + 51;
		length = maxY - minY + 51;
		WritableImage wi1 = new WritableImage(width, length);
		PixelWriter pw1 = wi1.getPixelWriter();
		// valueOf("#FFD700")
		if (map1.get("direction").equals("竖向")) {
			for (int i = 0; i < data.length; i++) {
				String[] se = data[i].trim().split(",");
				// 高判断
				if (i >= (minY - 25) && (i <= maxY + 25)) {
					for (int j = 0; j < se.length; j++) {
						// 宽判断
						if (j >= (minX - 25) && (j <= maxX + 25)) {
							Point p = new Point();
							p.setX(j);
							p.setY(i);
							if (area01.containsKey(p)) {
								pw1.setColor(j - (minX - 25), i - (minY - 25), Color.rgb(0, 0, 255));
							} else if (area02.containsKey(p)) {
								pw1.setColor(j - (minX - 25), i - (minY - 25), Color.rgb(0, 141, 215));
							} else if (area03.containsKey(p)) {
								pw1.setColor(j - (minX - 25), i - (minY - 25), Color.rgb(255, 255, 0));
							} else if (area04.containsKey(p)) {
								pw1.setColor(j - (minX - 25), i - (minY - 25), Color.rgb(255, 71, 71));
							} else if (area05.containsKey(p)) {
								pw1.setColor(j - (minX - 25), i - (minY - 25), Color.rgb(255, 0, 0));
							} else {
								pw1.setColor(j - (minX - 25), i - (minY - 25), Color.rgb(255, 255, 255));
							}
						}
					}
					// System.out.println("竖向");
				}
			}
		} else {
			for (int i = 0; i < data.length; i++) {
				String[] se = data[i].trim().split(",");
				// 高判断
				if (i >= (minX - 25) && (i <= maxX + 25)) {
					for (int j = 0; j < se.length; j++) {
						// 宽判断
						if (j >= (minY - 25) && (j <= maxY + 25)) {
							Point p = new Point();
							p.setX(i);
							p.setY(j);
							if (area01.containsKey(p)) {
								pw1.setColor(i - (minX - 25), j - (minY - 25), Color.rgb(0, 0, 255));
							} else if (area02.containsKey(p)) {
								pw1.setColor(i - (minX - 25), j - (minY - 25), Color.rgb(0, 141, 215));
							} else if (area03.containsKey(p)) {
								pw1.setColor(i - (minX - 25), j - (minY - 25), Color.rgb(255, 255, 0));
							} else if (area04.containsKey(p)) {
								pw1.setColor(i - (minX - 25), j - (minY - 25), Color.rgb(255, 71, 71));
							} else if (area05.containsKey(p)) {
								pw1.setColor(i - (minX - 25), j - (minY - 25), Color.rgb(255, 0, 0));
							} else {
								pw1.setColor(i - (minX - 25), j - (minY - 25), Color.rgb(255, 255, 255));
							}
						}
					}
				}
			}
		}

		// 径向中心轴
		String xReduceMid = String.format("%.2f",
				borderfill.get(Math.round((slip[slip.length - 1] + slip[0]) / 2)) * perUnitLength);
		// 饱和度（无需单位长度）
		String saturability = String.format("%.2f",
				Double.valueOf(effectiveGroundingAreaPoints) / Double.valueOf(fullGroundingAreaPoints) * 100);
		// 平均压强(用户输入)（需单位长度）
		String pressureavg = String.format("%.2f", Double.valueOf(testLoad) * 9.8
				/ Double.valueOf(effectiveGroundingAreaPoints * Math.pow(perUnitLength, 2)) * 1000);
		// 接地系数（无需单位长度）--
		String coefficientOfGrounding = String.format("%.2f",
				Double.valueOf(xReduceMaxPoints) / Double.valueOf(yReduceMaxPoints));
		// 矩形面积（需单位长度）
		String recArea = String.format("%.2f", (Double.valueOf(xReduceMaxPoints) * perUnitLength)
				* (Double.valueOf(yReduceMaxPoints) * perUnitLength));
		// 矩形系数（需单位长度）
		String KxdB = String.format("%.2f",
				(Double.valueOf(fullGroundingAreaPoints) * Math.pow(perUnitLength, 2)) / Double.valueOf(recArea));
		// ISL****如果碰到花纹沟怎么办（需单位长度）
		String ISL = String.format("%.2f",
				borderNozero.get((int) Math.round((slip[slip.length - 1] - slip[0]) * 0.1 + slip[0])) * perUnitLength);
		// OSL****如果碰到花纹沟怎么办（需单位长度）
		String OSL = String.format("%.2f",
				borderNozero.get((int) Math.round((slip[slip.length - 1] - slip[0]) * 0.9 + slip[0])) * perUnitLength);
		// 矩形比（需单位长度）
		String coefficisquareRatio = String.format("%.2f",
				2 * (Double.valueOf(xReduceMid)) / (Double.valueOf(ISL) + Double.valueOf(OSL)));
		// 有效接地面积
		String effectiveGroundingArea = String.format("%.2f",
				effectiveGroundingAreaPoints * Math.pow(perUnitLength, 2));
		// 接地总面积
		String fullGroundingArea = String.format("%.2f", fullGroundingAreaPoints * Math.pow(perUnitLength, 2));
		// 径向长轴
		String xReduceMax = String.format("%.2f", xReduceMaxPoints * perUnitLength);
		// 径向边缘短轴
		String xReduceMin = String.format("%.2f", xReduceMinPoints * perUnitLength);
		// 印痕控制系数
		String Mcf = String.format("%.2f", Double.valueOf(xReduceMin) / Double.valueOf(xReduceMid));

		// 轴向长轴
		String yReduceMax = String.format("%.2f", Double.valueOf(yReduceMaxPoints) * perUnitLength);
		String area005 = String.format("%.2f",
				((area01.size() + area02.size() + area03.size() + area04.size() + area05.size())
						* Math.pow(perUnitLength, 2)));
		String area004 = String.format("%.2f",
				(area01.size() + area02.size() + area03.size() + area04.size()) * Math.pow(perUnitLength, 2));
		String area003 = String.format("%.2f",
				(area01.size() + area02.size() + area03.size()) * Math.pow(perUnitLength, 2));
		String area002 = String.format("%.2f", (area01.size() + area02.size()) * Math.pow(perUnitLength, 2));
		String area001 = String.format("%.2f", (area01.size()) * Math.pow(perUnitLength, 2));
		// 区域负荷-kg=kpa*mm2/(1000*9.8)
		double fuhearea005 = 0;
		for (Entry<Point, Integer> entry : effectivedataMap.entrySet()) {
			fuhearea005 = fuhearea005 + entry.getValue() * Math.pow(perUnitLength, 2) / 9800;
		}
		double fuhearea05 = 0;
		for (Entry<Point, Integer> entry : area05.entrySet()) {
			fuhearea05 = fuhearea05 + entry.getValue() * Math.pow(perUnitLength, 2) / 9800;
		}
		double fuhearea004 = fuhearea005 - fuhearea05;
		double fuhearea04 = 0;
		for (Entry<Point, Integer> entry : area04.entrySet()) {
			fuhearea04 = fuhearea04 + entry.getValue() * Math.pow(perUnitLength, 2) / 9800;
		}
		double fuhearea003 = fuhearea005 - fuhearea05 - fuhearea04;
		double fuhearea03 = 0;
		for (Entry<Point, Integer> entry : area03.entrySet()) {
			fuhearea03 = fuhearea03 + entry.getValue() * Math.pow(perUnitLength, 2) / 9800;
		}
		double fuhearea002 = fuhearea005 - fuhearea05 - fuhearea04 - fuhearea03;
		double fuhearea001 = 0;
		for (Entry<Point, Integer> entry : area01.entrySet()) {
			fuhearea001 = fuhearea001 + entry.getValue() * Math.pow(perUnitLength, 2) / 9800;
		}
		// System.out.println(String.format("%.2f", fuhearea005));
		System.out.println("有效接地面积（effectiveGroundingArea）：" + effectiveGroundingArea + "\n"
				+ "接地总面积（fullGroundingArea）" + fullGroundingArea + "\n" + "径向长轴（xReduceMax）：" + xReduceMax + "\n"
				+ "轴向长轴（yReduceMax）：" + yReduceMax + "\n" + "饱和度（saturability）：" + saturability + "\n"
				+ "平均压强（pressureavg）：" + pressureavg + "\n" + "接地系数（coefficientOfGrounding）：" + coefficientOfGrounding
				+ "\n" + "矩形面积（recArea）：" + recArea + "\n" + "矩形系数（KxdB）：" + KxdB + "\n" + "CL（xReduceMid）："
				+ xReduceMid + "\n" + "ISL（ISL）：" + ISL + "\n" + "OSL（OSL）：" + OSL + "\n" + "矩形比（coefficisquareRatio）："
				+ coefficisquareRatio + "\n" + "印痕控制系数（area00120）：" + area005 + "\n" + "压力分布0-1200（area00120）："
				+ area005 + "\n" + "压力分布0-1000（area00100）：" + area004 + "\n" + "压力分布0-800（area0080）：" + area003 + "\n"
				+ "压力分布0-600（area0060）：" + area002 + "\n" + "压力分布0-400（area040）：" + area001);
		Mapout.put("effectiveGroundingArea", effectiveGroundingArea);
		Mapout.put("fullGroundingArea", fullGroundingArea);
		Mapout.put("xReduceMax", xReduceMax);
		Mapout.put("yReduceMax", yReduceMax);
		Mapout.put("saturability", saturability);
		Mapout.put("pressureavg", pressureavg);
		Mapout.put("recArea", recArea);
		Mapout.put("KxdB", KxdB);
		Mapout.put("CL", xReduceMid);
		Mapout.put("ISL", ISL);
		Mapout.put("OSL", OSL);
		Mapout.put("coefficientOfGrounding", coefficientOfGrounding);
		Mapout.put("coefficisquareRatio", coefficisquareRatio);
		Mapout.put("Mcf", Mcf);
		Mapout.put("area00120", area005);
		Mapout.put("area00100", area004);
		Mapout.put("area0080", area003);
		Mapout.put("area0060", area002);
		Mapout.put("area040", area001);
		Mapout.put("testLoad", String.valueOf(testLoad));
		Mapout.put("fuhearea00120", String.format("%.2f", fuhearea005));
		Mapout.put("fuhearea00100", String.format("%.2f", fuhearea004));
		Mapout.put("fuhearea00800", String.format("%.2f", fuhearea003));
		Mapout.put("fuhearea00600", String.format("%.2f", fuhearea002));
		Mapout.put("fuhearea00400", String.format("%.2f", fuhearea001));
		Mapout.put("pressureMin", String.valueOf(pressureMin));
		Mapout.put("NO1", "0-" + String.valueOf(maxValue * 1.0));
		Mapout.put("NO2", "0-" + String.valueOf(maxValue * 0.8));
		Mapout.put("NO3", "0-" + String.valueOf(maxValue * 0.6));
		Mapout.put("NO4", "0-" + String.valueOf(maxValue * 0.4));
		Mapout.put("NO5", "0-" + String.valueOf(maxValue * 0.2));
		Mapout.put("yinhenimage", wi1);

		return Mapout;

	}

}
