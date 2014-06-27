package dzedit;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public final class DzEdit {
	private File opened;
	private JFrame frame;
	private Container cp;
	private JTextArea jta;

	private DzEdit() {
		frame = new JFrame("DzEdit");
		cp = frame.getContentPane();
		frame.setLocationRelativeTo(null);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cp.add(jta = new JTextArea(), BorderLayout.CENTER);

		frame.setResizable(true);
		frame.pack();
		frame.setVisible(true);

		Scanner scanner = new Scanner(System.in);
		listenForCommands(scanner);

		// Only gets here when listenForCommands loop returns
		// This only happens if the user enters 'close'
		scanner.close();
		frame.dispose();
		System.exit(0);
	}

	private void listenForCommands(Scanner scanner) {
		String line = scanner.nextLine();
		if (line.equalsIgnoreCase("close")) {
			return;
		} else if (line.equalsIgnoreCase("save")) {
			save();
		} else if (line.startsWith("saveas")) {
			String[] split = line.split(" ");
			String filename = null;
			try {
				filename = split[1];
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out
						.println("Must specify filename after command 'saveas'");
			}
			if (filename != null) {
				List<String> sl = new ArrayList<>(Arrays.asList(split));
				sl.remove(0);
				saveAs(new File(listToString(sl, " ")));
			}
		} else if (line.startsWith("open")) {
			String[] split = line.split(" ");
			String filename = null;
			try {
				filename = split[1];
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out
						.println("Must specify filename after command 'open'");
			}
			if (filename != null) {
				List<String> sl = new ArrayList<>(Arrays.asList(split));
				sl.remove(0);
				open(new File(listToString(sl, " ")));
			}
		}

		listenForCommands(scanner);
	}

	public void open(File open) {
		List<String> lines = readLines(open);
		StringBuilder sb = new StringBuilder();
		for (String line : lines) {
			sb.append(line).append("\n");
		}
		jta.setText(sb.toString());
		opened = open;

		System.out.println("Opened file: " + opened.getName());
	}

	public void save() {
		if (!writeFile(opened, jta.getText())) {
			System.out.println("ERROR: COULD NOT SAVE FILE");
		} else {
			System.out.println("Saved file: " + opened.getName());
		}
	}

	public void saveAs(File destination) {
		if (!writeFile(destination, jta.getText())) {
			System.out.println("ERROR: COULD NOT SAVE FILE");
		} else {
			System.out.println("Saved contents to file: "
					+ destination.getName());
		}
	}

	public static void main(String[] args) {
		new DzEdit();
	}

	private String listToString(List<String> list, String separator) {
		StringBuilder sb = new StringBuilder();
		for (String string : list) {
			sb.append(string).append(separator);
		}
		return sb.toString();
	}

	private static List<String> readLines(File f) {
		List<String> result = new ArrayList<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(f));
			String s = null;
			while ((s = br.readLine()) != null) {
				result.add(s);
			}
			br.close();
		} catch (IOException e) {
			if (br != null) {
				try {
					br.close();
				} catch (IOException ignore) {
				}
			}
			e.printStackTrace();
		}
		return result;
	}

	private static boolean writeFile(File out, String contents) {
		if (!out.exists()) {
			try {
				out.createNewFile();
			} catch (IOException e) {
			}
		}
		boolean result = false;
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(out));
			writer.write(contents);
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException ignore) {
				}
			}
		}
		return result;
	}
}
