package dzedit;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 * The worst text editor ever:
 * 
 * - Works via command line commands to open and save files! - Only has support
 * for editing one file at a time! - Extremely badly written!
 */
public final class DzEdit {
	private final JFrame frame;
	private final JTextArea textArea;

	// Menu
	private final JMenuBar menuBar;
	private final JMenu fileMenu;
	private final JMenuItem openItem;
	private final JMenuItem saveItem;
	private final JMenuItem saveAsItem;

	private File opened;
	private File defDir;
	private String lastSaved;

	/**
	 * Create a new DzEdit... Although I don't see why you'd want to
	 */
	private DzEdit() {
		frame = new JFrame("DzEdit");
		Container cp = frame.getContentPane();

		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		openItem = new JMenuItem("Open");
		saveItem = new JMenuItem("Save");
		saveAsItem = new JMenuItem("Save As");
		textArea = new JTextArea();

		fileMenu.setPreferredSize(new Dimension(60, 25));
		openItem.setPreferredSize(new Dimension(57, 25));
		saveItem.setPreferredSize(new Dimension(57, 25));
		saveAsItem.setPreferredSize(new Dimension(57, 25));

		openItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JFileChooser choose = new JFileChooser();
				int val = choose.showOpenDialog(frame);
				if (val == JFileChooser.APPROVE_OPTION) {
					boolean goAhead = true;
					if (lastSaved != null && textArea.getText() != null
							&& !lastSaved.equals(textArea.getText())) {
						final int n = JOptionPane
								.showOptionDialog(
										frame,
										"Do you wish to save before\nopening a new file?",
										"Open",
										JOptionPane.YES_NO_CANCEL_OPTION,
										JOptionPane.QUESTION_MESSAGE, null,
										new String[] { "Yes", "No", "Cancel" },
										"Yes");
						switch (n) {
						case 0:
							save();
							break;
						case 1:
							break;
						case 2:
							goAhead = false;
							return;
						}
					}
					open(choose.getSelectedFile());
				}
			}
		});

		saveItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (!"disabled".equals(event.getActionCommand())) {
					save();
				}
			}
		});

		saveAsItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JFileChooser choose = new JFileChooser();
				int val = choose.showOpenDialog(frame);
				if (val == JFileChooser.APPROVE_OPTION) {
					saveAs(choose.getSelectedFile());
				}
			}
		});

		menuBar.add(fileMenu);
		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		fileMenu.add(saveAsItem);
		frame.setJMenuBar(menuBar);
		cp.add(textArea, BorderLayout.CENTER);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(600, 400));
		frame.setPreferredSize(new Dimension(800, 600));
		frame.setLocationRelativeTo(null);
		frame.setResizable(true);
		frame.pack();
		frame.setVisible(true);

		final Scanner scanner = new Scanner(System.in);
		listenForCommands(scanner);

		// Only gets here when listenForCommands loop returns
		// This only happens if the user enters 'close'
		scanner.close();
		frame.dispose();
		System.exit(0);
	}

	/**
	 * Listens for commands. Executes commands. Repeats.
	 * 
	 * @param scanner
	 *            The scanner for scanning
	 */
	private void listenForCommands(final Scanner scanner) {
		final String line = scanner.nextLine();
		if (line.equalsIgnoreCase("close")) {
			return;
		} else if (line.equalsIgnoreCase("save")) {
			save();
		} else if (line.startsWith("saveas")) {
			final String[] split = line.split(" ");
			String filename = null;
			try {
				filename = split[1];
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out
						.println("Must specify filename after command 'saveas'");
			}
			if (filename != null) {
				final List<String> sl = new ArrayList<>(Arrays.asList(split));
				sl.remove(0);
				saveAs(new File(listToString(sl, " ")));
			}
		} else if (line.startsWith("open")) {
			final String[] split = line.split(" ");
			String filename = null;
			try {
				filename = split[1];
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out
						.println("Must specify filename after command 'open'");
			}
			if (filename != null) {
				final List<String> sl = new ArrayList<>(Arrays.asList(split));
				sl.remove(0);
				open(new File(listToString(sl, " ")));
			}
		}
		listenForCommands(scanner);
	}

	private void open(final File open) {
		final List<String> lines = readLines(open);
		final StringBuilder sb = new StringBuilder();
		for (String line : lines) {
			sb.append(line).append("\n");
		}
		textArea.setText(sb.toString());
		opened = open;
		defDir = open.getParentFile();
		if (defDir == null) {
			defDir = new File("./");
		}
		System.out.println("Opened file: " + opened.getName());
		lastSaved = textArea.getText();
	}

	private void save() {
		saveAs(opened);
	}

	private void saveAs(final File destination) {
		if (!writeFile(destination, textArea.getText())) {
			System.out.println("ERROR: COULD NOT SAVE FILE");
		} else {
			System.out.println("Saved contents to file: "
					+ destination.getName());
			open(destination);
		}
		lastSaved = textArea.getText();
	}

	public static void main(String[] args) {
		new DzEdit();
	}

	private String listToString(final List<String> list, final String separator) {
		final StringBuilder sb = new StringBuilder();
		for (String string : list) {
			sb.append(string).append(separator);
		}
		return sb.toString();
	}

	private static List<String> readLines(final File f) {
		final List<String> result = new ArrayList<String>();
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

	private static boolean writeFile(final File out, final String contents) {
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
