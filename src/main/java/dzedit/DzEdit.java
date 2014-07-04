package dzedit;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
 * An open source Java text editor, with the aim of being very simple
 * 
 * DzEdit is operable from the command line and by some GUI for loading and
 * saving files. DzEdit is a work in progress.
 * 
 * @author DziNeIT
 * @see {@link https://github.com/DziNeIT/dzedit}
 */
public final class DzEdit {
	// Components
	private final JFrame frame;
	private final JTextArea textArea;
	private final JMenuBar menuBar;
	private final JMenu fileMenu;
	private final JMenuItem openItem;
	private final JMenuItem saveItem;
	private final JMenuItem saveAsItem;
	private final JMenu optionsMenu;
	private final JMenuItem options;

	// Other
	private File file;
	private String last;

	/**
	 * Main constructor for DzEdit. Creates everything, such as the JFrame and
	 * all its components as well as configuring them, adding their listeners
	 * and showing them on the screen.
	 * 
	 * After this, the constructor runs listenForCommands(), which repeats until
	 * the user closes the program
	 */
	private DzEdit() {
		frame = new JFrame("DzEdit");
		final Container cp = frame.getContentPane();

		// Create components
		menuBar = new JMenuBar();

		// File menu
		fileMenu = new JMenu("File");
		openItem = new JMenuItem("Open");
		saveItem = new JMenuItem("Save");
		saveAsItem = new JMenuItem("Save As");

		fileMenu.setPreferredSize(new Dimension(60, 25));
		openItem.setPreferredSize(new Dimension(57, 25));
		saveItem.setPreferredSize(new Dimension(57, 25));
		saveAsItem.setPreferredSize(new Dimension(57, 25));

		// Options menu
		optionsMenu = new JMenu("Options");
		options = new JMenuItem("Options");

		optionsMenu.setPreferredSize(new Dimension(60, 25));
		options.setPreferredSize(new Dimension(57, 25));

		// Other components
		textArea = new JTextArea();

		// Add components

		// File menu
		menuBar.add(fileMenu);
		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		fileMenu.add(saveAsItem);

		// Options menu
		menuBar.add(optionsMenu);
		optionsMenu.add(options);

		frame.setJMenuBar(menuBar);
		cp.add(textArea, BorderLayout.CENTER);

		// Add listeners

		// Opens a GUI for opening files when the 'Open' button is clicked
		openItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JFileChooser choose = new JFileChooser();
				if (choose.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
					if (last != null && textArea.getText() != null
							&& !last.equals(textArea.getText())) {
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
							return;
						}
					}
					open(choose.getSelectedFile());
				}
			}
		});

		// Saves the current file when the 'Save' button is clicked
		saveItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (!"disabled".equals(event.getActionCommand()))
					save();
			}
		});

		// Opens a GUI for saving files when the 'Save As' button is clicked
		saveAsItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JFileChooser choose = new JFileChooser();
				if (choose.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
					saveAs(choose.getSelectedFile());
			}
		});

		// JFrame settings + show the JFrame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(600, 400));
		frame.setPreferredSize(new Dimension(800, 600));
		frame.setLocationRelativeTo(null);
		frame.setResizable(true);
		frame.pack();
		frame.setVisible(true);

		final Scanner sc = new Scanner(System.in);
		listenForCommands(sc);

		// Cleanup afterwards
		sc.close();
		frame.dispose();
		System.exit(0);
	}

	/**
	 * Listens for commands on a loop
	 * 
	 * @param sc
	 *            The Scanner to listen to
	 */
	private void listenForCommands(final Scanner sc) {
		final String ln = sc.nextLine();
		if (ln.equalsIgnoreCase("close"))
			return;
		else if (ln.equalsIgnoreCase("save"))
			save();
		else if (ln.startsWith("saveas")) {
			final String[] s = ln.split(" ");
			String f = null;
			try {
				f = s[1];
			} catch (final ArrayIndexOutOfBoundsException e) {
				System.out
						.println("Must specify filename after command 'saveas'");
			}
			if (f != null) {
				final List<String> l = new ArrayList<>(Arrays.asList(s));
				l.remove(0);
				saveAs(new File(listToString(l, " ")));
			}
		} else if (ln.startsWith("open")) {
			final String[] s = ln.split(" ");
			String f = null;
			try {
				f = s[1];
			} catch (final ArrayIndexOutOfBoundsException e) {
				System.out
						.println("Must specify filename after command 'open'");
			}
			if (f != null) {
				final List<String> l = new ArrayList<>(Arrays.asList(s));
				l.remove(0);
				open(new File(listToString(l, " ")));
			}
		}
		listenForCommands(sc);
	}

	/**
	 * Opens the specified File to the editor
	 * 
	 * @param f
	 *            The File to open
	 */
	private void open(final File f) {
		textArea.setText(read(file = f));
		System.out.println("Opened file: " + file.getName());
		last = textArea.getText();
	}

	/**
	 * Saves current file
	 */
	private void save() {
		saveAs(file);
	}

	/**
	 * Saves the contents of the current File to the given destination File
	 * 
	 * @param destination
	 *            The File to write the contents to
	 */
	private void saveAs(final File destination) {
		if (!writeFile(destination, textArea.getText()))
			System.out.println("ERROR: COULD NOT SAVE FILE");
		else {
			System.out.println("Saved contents to file: "
					+ destination.getName());
			open(destination);
		}
		last = textArea.getText();
	}

	public static void main(String[] args) {
		new DzEdit();
	}

	private String listToString(final List<String> list, final String separator) {
		final StringBuilder sb = new StringBuilder();
		for (final String string : list)
			sb.append(string).append(separator);
		sb.setLength(sb.length() - separator.length());
		return sb.toString();
	}

	/**
	 * Reads the contents of a File
	 * 
	 * @param f
	 *            The File to read
	 * 
	 * @return The contents of the File as a String
	 */
	private static String read(final File f) {
		try {
			return new String(Files.readAllBytes(f.toPath()));
		} catch (final IOException e) {
			return null;
		}
	}

	/**
	 * Writes a String to a File
	 * 
	 * @param f
	 *            The target File to write contents to
	 * @param s
	 *            The contents to write to the File
	 * 
	 * @return Whether we succeeded in writing the contents to the File
	 */
	private static boolean writeFile(final File f, final String s) {
		try {
			Files.write(f.toPath(), s.getBytes());
			return true;
		} catch (final IOException e) {
			return false;
		}
	}
}
