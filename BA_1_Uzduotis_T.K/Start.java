import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Start {

	public static void main(String[] args) {

		
		String inputFolderPath = "katalogo direktorija"; // Iveskite katalogo direktorija
		File folder = new File(inputFolderPath);
		String outputFilePatch = inputFolderPath + "\\" + "failu_komentarai.txt";

		ArrayList<FoldersAndFiles> filesNames = new ArrayList<>();
		ArrayList<String> comments = new ArrayList<>();

		listFilesInFolder(folder, filesNames);

		putCommentsToList(filesNames, comments);

		fileWriter(comments, outputFilePatch);

	}

	// einame per folderiu objektu lista
	public static void putCommentsToList(ArrayList<FoldersAndFiles> filesNames, ArrayList<String> comments) {
		for (FoldersAndFiles eilutes : filesNames) {
			String contents = fileReading(eilutes.filePath);
			searchingComments(contents, eilutes.fileName, comments);
		}
	}

	// metodas skirtas failo nuskaitymui, paduodamas failo kelias, grazinamas viso
	// failo vientisas stringas
	public static String fileReading(String inputFilePath) {

		String lines = null;
		StringBuilder b = new StringBuilder();

		try (FileReader fr = new FileReader(inputFilePath); BufferedReader br = new BufferedReader(fr)) {
			while ((lines = br.readLine()) != null) {
				b.append(lines).append('\n');
			}
		} catch (Exception e) {
			System.out.println("File not found: " + e.getMessage());
		}
		return b.toString();
	}

	// metodas is List'o iraso komentarus i faila
	public static void fileWriter(ArrayList<String> comments, String outputFilePatch) {

		try (FileWriter fw = new FileWriter(outputFilePatch); BufferedWriter bw = new BufferedWriter(fw)) {
			for (String commentsToFile : comments)
				fw.write(commentsToFile + "\n");

		} catch (Exception e) {
			System.out.println("File not found: " + e.getMessage());
		}
	}

	// metodas skirtas ieskoti komentaru
	public static void searchingComments(String contents, String fileName, ArrayList<String> comments) {

		String fileNameToList = ("=====  " + fileName + "  =====");

		int counter = 0;

		String slComment = "//[^\r\n]*";
		String mlComment = "/\\*[\\s\\S]*?\\*/";
		String strLit = "\"(?:\\\\.|[^\\\\\"\r\n])*\"";
		String chLit = "'(?:\\\\.|[^\\\\'\r\n])+'";
		String any = "[\\s\\S]";

		Pattern p = Pattern.compile(String.format("(%s)|(%s)|%s|%s|%s", slComment, mlComment, strLit, chLit, any));

		Matcher m = p.matcher(contents);

		comments.add("\n" + fileNameToList + "\n");

		while (m.find()) {
			String hit = m.group();
			if (m.group(1) != null) {
				counter++;
				comments.add(Integer.toString(counter) + ". " + hit);
				// System.out.println("SingleLine :: " + hit); //.replace("\n", "\\n"));
			}
			if (m.group(2) != null) {
				counter++;
				comments.add(Integer.toString(counter) + ". " + hit);
				// System.out.println("MultiLine :: " + hit); //.replace("\n", "\\n"));
			}
		}
	}

	// metodas skirtas folderio nuskaitymui ir visu esanciu failu keliu sudejimui i
	// lista
	public static void listFilesInFolder(File folder, ArrayList<FoldersAndFiles> filesName) {

		File[] files = folder.listFiles();

		for (File fileEntry : files) {
			if (fileEntry.isDirectory()) {
				listFilesInFolder(fileEntry, filesName);
			} else {
				if (fileEntry.isFile()) {
					String temp = fileEntry.getName();
					if (!(temp.substring(temp.lastIndexOf('.') + 1, temp.length()).toLowerCase()).equals("ico")
							&& !(temp.substring(temp.lastIndexOf('.') + 1, temp.length()).toLowerCase())
									.equals("txt")) {

						String filePath = (folder.getAbsolutePath() + "\\" + fileEntry.getName());

						FoldersAndFiles fAf = new FoldersAndFiles(filePath, fileEntry.getName());

						filesName.add(fAf);
					}
				}
			}
		}
	}
}
