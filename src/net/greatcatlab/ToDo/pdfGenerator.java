package net.greatcatlab.ToDo;

public class pdfGenerator {
	
	//private static void savePDF() {
//	ArrayList<String[]> resultsList = Database.getTasks();
//
//	try {
//		PDDocument document = new PDDocument();
//		PDPage newPage = new PDPage();
//		document.addPage(newPage);
//		PDPage tasks = document.getPage(0);
//		PDPageContentStream contentStream = new PDPageContentStream(document, tasks);
//		contentStream.setFont(PDType1Font.COURIER, 16);
//		contentStream.setLeading(1.15f);
//
//		//header
//		contentStream.beginText();
//		contentStream.newLineAtOffset(200, 750);
//		contentStream.showText(currentUser + "'s To-Dos");
//		contentStream.newLineAtOffset(0, -15);
//		contentStream.newLineAtOffset(0, -15);
//		contentStream.endText();
//
//		//tasks
//		contentStream.beginText();
//		contentStream.newLineAtOffset(25, 700);
//		for(int i = 0; i < resultsList.size(); i++) {
//			String[] temp = resultsList.get(i);
//			contentStream.newLineAtOffset(0, -15);
//			contentStream.showText(i+1 + ". " + temp[0]);
//		}
//
//		contentStream.endText();
//		contentStream.close();
//		String fileName = chooseSaveLocation();
//		document.save(fileName);
//		document.close();
//	}
//	catch (IOException e1) {
//		// TODO Auto-generated catch block
//		e1.printStackTrace();s
//	}
//}

}
