package application;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.ClientAnchor.AnchorType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

public class ExcelUtil {
	/**
	 * 写入excel模板，对单个sheet页写入,主要在使用之前一定要，使用printExcel方法，（在最下面）
	 * 
	 * @param templatePath:是文件模板路径，excelNode
	 *            数据对象
	 */
	public static XSSFWorkbook writeToExcelBySingletSheetEndXlsx(Map map, ExcelNode excelNode, WritableImage image1,
			WritableImage image2,WritableImage image3) {
		try {
			InputStream in = new FileInputStream((String) map.get("mobanpath"));
			XSSFWorkbook workbook = new XSSFWorkbook(in);
			XSSFSheet sheet = workbook.getSheetAt(excelNode.getSheet());
			excelNode.getValue().forEach((k, v) -> {
				XSSFRow row = sheet.getRow(k);
				v.forEach((k1, v1) -> {
					row.getCell(k1).setCellValue(v1);
				});
			});

			// 画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
			XSSFDrawing patriarch = sheet.createDrawingPatriarch();
			BufferedImage bufferImg2 = null;
			BufferedImage bufferImg3 = null;


			if (map.get("pic2") != null) {
				// 读入图片2
				ByteArrayOutputStream byteArrayOut2 = new ByteArrayOutputStream();
				bufferImg2 = ImageIO.read(new File((String) map.get("pic2")));
				ImageIO.write(bufferImg2, "png", byteArrayOut2);
				// 插入图片2
				XSSFClientAnchor anchor2 = new XSSFClientAnchor(10000 * 55, 10000 * 10, -10000 * 55, -10000 * 10,
						(short) 6, 18, (short) 9, 34);
				anchor2.setAnchorType(AnchorType.MOVE_DONT_RESIZE);
				patriarch.createPicture(anchor2,
						workbook.addPicture(byteArrayOut2.toByteArray(), XSSFWorkbook.PICTURE_TYPE_JPEG));
				byteArrayOut2.close();
			}

			if (map.get("pic3") != null) {
				// 读入图片3
				ByteArrayOutputStream byteArrayOut3 = new ByteArrayOutputStream();
				bufferImg3 = ImageIO.read(new File((String) map.get("pic3")));
				ImageIO.write(bufferImg3, "png", byteArrayOut3);
				// 插入图片3
				XSSFClientAnchor anchor3 = new XSSFClientAnchor(10000 * 5, 10000 * 5, -10000 * 5, -10000 * 5, (short) 1,
						40, (short) 9, 46);
				anchor3.setAnchorType(AnchorType.MOVE_DONT_RESIZE);
				patriarch.createPicture(anchor3,
						workbook.addPicture(byteArrayOut3.toByteArray(), XSSFWorkbook.PICTURE_TYPE_JPEG));
				byteArrayOut3.close();
			}

			if (image1 != null) {
				// 读入图片4
				ByteArrayOutputStream byteArrayOut4 = new ByteArrayOutputStream();
				// bufferImg4 = ImageIO.read(new File((String) map.get("pic4")));
				ImageIO.write(SwingFXUtils.fromFXImage(image1, null), "png", byteArrayOut4);
				// 插入图片4
				XSSFClientAnchor anchor4 = new XSSFClientAnchor(10000 * 5, 10000 * 5, -10000 * 5, -10000 * 5, (short) 1,
						48, (short) 4, 55);
				anchor4.setAnchorType(AnchorType.MOVE_DONT_RESIZE);
				patriarch.createPicture(anchor4,
						workbook.addPicture(byteArrayOut4.toByteArray(), XSSFWorkbook.PICTURE_TYPE_JPEG));
				byteArrayOut4.close();
			}

			if (image2 != null) {
				// 读入图片5
				ByteArrayOutputStream byteArrayOut5 = new ByteArrayOutputStream();
				// bufferImg5 = ImageIO.read(new File((String) map.get("pic5")));
				ImageIO.write(SwingFXUtils.fromFXImage(image2, null), "png", byteArrayOut5);
				// 插入图片5
				XSSFClientAnchor anchor5 = new XSSFClientAnchor(10000 * 5, 10000 * 5, -10000 * 5, -10000 * 5, (short) 4,
						48, (short) 9, 55);
				anchor5.setAnchorType(AnchorType.MOVE_DONT_RESIZE);
				patriarch.createPicture(anchor5,
						workbook.addPicture(byteArrayOut5.toByteArray(), XSSFWorkbook.PICTURE_TYPE_JPEG));
				byteArrayOut5.close();
			}
			
			if (image3 != null) {
				// 读入图片6
				ByteArrayOutputStream byteArrayOut6 = new ByteArrayOutputStream();
				// bufferImg5 = ImageIO.read(new File((String) map.get("pic5")));
				ImageIO.write(SwingFXUtils.fromFXImage(image3, null), "png", byteArrayOut6);
				// 插入图片5
				XSSFClientAnchor anchor6 = new XSSFClientAnchor(10000 * 10, 10000 * 10, -10000 * 10, -10000 * 10,
						(short) 2, 18, (short) 6, 34);
				anchor6.setAnchorType(AnchorType.MOVE_DONT_RESIZE);
				patriarch.createPicture(anchor6,
						workbook.addPicture(byteArrayOut6.toByteArray(), XSSFWorkbook.PICTURE_TYPE_JPEG));
				byteArrayOut6.close();
			}
			in.close();
			
			return workbook;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String checkRowCell(XSSFCell cell, int i, int j) {
		String jieguo = null;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			//System.out.println(cell.getRichStringCellValue().getString() + "=" + i + ":" + j);
			jieguo = cell.getRichStringCellValue().getString();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				System.out.println(String.valueOf(cell.getDateCellValue()) + "=" + i + ":" + j);
				jieguo = String.valueOf(cell.getDateCellValue());
			} else {
				//System.out.println(cell.getNumericCellValue() + "=" + i + ":" + j);
				jieguo = String.format("%.2f", cell.getNumericCellValue());
			}
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			//System.out.println(cell.getBooleanCellValue() + "=" + i + ":" + j);
			jieguo = String.format("%.2f", cell.getBooleanCellValue());
			break;
		default:
		}

		return jieguo;
	}

	public static Map<String, String> printExcel(String path) throws IOException {
		Map<String, String> MapoutExtract = new HashMap<String, String>();
		File file = new File(path);
		XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(file));
		XSSFSheet sheet = wb.getSheetAt(0);
		int rowcount = sheet.getLastRowNum() - sheet.getFirstRowNum();
		int rowcount1 = sheet.getPhysicalNumberOfRows();
		//System.out.println(rowcount1);
		//System.out.println("该excle的总行数为：" + (rowcount + 1) + "行 ！");

		for (int i = 0; i < rowcount + 1; i++) {
			Row row = sheet.getRow(i);
			if (row != null) {
				for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
					XSSFCell cell = (XSSFCell) row.getCell(j);
					if (cell != null) {
						ExcelUtil.checkRowCell(cell, i, j);
						if (i == 6 && j == 1) {
							MapoutExtract.put("guige", ExcelUtil.checkRowCell(cell, i, j));
						}
						if (i == 6 && j == 4) {
							MapoutExtract.put("cengji", ExcelUtil.checkRowCell(cell, i, j));
						}
						if (i == 7 && j == 1) {
							MapoutExtract.put("huawen", ExcelUtil.checkRowCell(cell, i, j));
						}
						if (i == 7 && j == 4) {
							MapoutExtract.put("testLoad", ExcelUtil.checkRowCell(cell, i, j));
						}
						if (i == 8 && j == 7) {
							MapoutExtract.put("qiya", ExcelUtil.checkRowCell(cell, i, j));
						}
						if (i == 8 && j == 1) {
							MapoutExtract.put("runwang", ExcelUtil.checkRowCell(cell, i, j));
						}
						if (i == 9 && j == 4) {
							MapoutExtract.put("taihao", ExcelUtil.checkRowCell(cell, i, j));
						}
						if (i == 9 && j == 7) {
							MapoutExtract.put("shangbiao", ExcelUtil.checkRowCell(cell, i, j));
						}
					}
				}
			}
		}
		return MapoutExtract;
	}

}
