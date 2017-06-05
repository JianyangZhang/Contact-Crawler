import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import javax.swing.*;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.awt.event.*;

public class LaunchWindow extends JFrame {
	private static final int STATUS_PENDING = 0;
	private static final int STATUS_RUNNING = 1;
	private static final int STATUS_DONE = 2;
	private static final int STATUS_FAILED = 3;

	private JTextField textField_username;
	private JTextField textField_keyword;
	private JPasswordField passwordField_password;

	private JButton button_crawl;
	private JButton button_export;

	private static JLabel label_status;

	/*
	 * public static void main(String[] args) { LaunchWindow loginWindow = new
	 * LaunchWindow(); }
	 */
	public LaunchWindow() {
		// initialize the launch window
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dim = tk.getScreenSize();
		this.setSize(600, 400);
		int xPos = (dim.width / 2) - (this.getWidth() / 2);
		int yPos = (dim.height / 2) - (this.getHeight() / 2);
		this.setLocation(xPos, yPos);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Email Crawler");
		this.setResizable(false);
		this.setLayout(new GridLayout(6, 1));

		// warning text
		JPanel introPanel = new JPanel();
		JLabel label_intro = new JLabel(
				"<html><font color='red'>Provide an Linkedin account with 50+ connections</font><html>");
		introPanel.add(label_intro);
		this.add(introPanel);

		// input field
		JPanel usernamePanel = new JPanel();
		textField_username = new JTextField("", 15);
		textField_username.setToolTipText("input a valid LinkedIn account");
		JLabel label_username = new JLabel("LinkedIn username* ");
		usernamePanel.add(label_username);
		usernamePanel.add(textField_username);
		this.add(usernamePanel);

		JPanel passwordPanel = new JPanel();
		passwordField_password = new JPasswordField("", 15);
		passwordField_password.setToolTipText("input the LinkedIn account's password");
		JLabel label_password = new JLabel("password*                  ");
		passwordPanel.add(label_password);
		passwordPanel.add(passwordField_password);
		this.add(passwordPanel);

		JPanel keywordPanel = new JPanel();
		textField_keyword = new JTextField("", 15);
		textField_keyword.setToolTipText("input a keyword that the crawled emails will be based upon");
		JLabel label_keyword = new JLabel("keyword*                    ");
		keywordPanel.add(label_keyword);
		keywordPanel.add(textField_keyword);
		this.add(keywordPanel);

		// confirm buttons
		JPanel confirmPanel = new JPanel();
		button_crawl = new JButton("crawl");
		button_crawl.setToolTipText("start to crawl emails from LinkedIn");
		button_crawl.setFocusPainted(false);
		button_crawl.setContentAreaFilled(false);
		confirmPanel.add(button_crawl);
		ListenForCrawlButton listenForCrawlBtn = new ListenForCrawlButton();
		button_crawl.addActionListener(listenForCrawlBtn);

		button_export = new JButton("export");
		button_export.setToolTipText("export the crawled emails to Excel");
		button_export.setFocusPainted(false);
		button_export.setContentAreaFilled(false);
		confirmPanel.add(button_export);
		ListenForExportButton listenForExportBtn = new ListenForExportButton();
		button_export.addActionListener(listenForExportBtn);
		this.add(confirmPanel);

		// status text
		JPanel statusPanel = new JPanel();
		JLabel label_statusPrefix = new JLabel("Status: ");
		label_status = new JLabel("<html><font color='orange'>pending</font><html>");
		statusPanel.add(label_statusPrefix);
		statusPanel.add(label_status);
		this.add(statusPanel);
	}

	public void launch() {
		// finalize the launch window
		textField_username.setText("wangwent@usc.edu");
		passwordField_password.setText("19940916");
		textField_keyword.setText("google");
		this.pack();
		this.setVisible(true);
	}

	private class ListenForCrawlButton implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == button_crawl) {
				System.out.println("start the BrowerDriver");
				LaunchWindow.changeStatus(STATUS_RUNNING);

				// launch the browser driver
				BrowserDriver br = new BrowserDriver();
				System.setProperty("webdriver.chrome.driver", "C:\\\\Users\\jiany\\emailcrawlers\\chromedriver.exe");
				WebDriver dr = new ChromeDriver();
				dr.get("http://www.linkedin.com");

				// br.signInLinkedin("wangwent@usc.edu", "19940916", dr);
				br.signInLinkedin(textField_username.getText(), passwordField_password.getPassword(), dr);
				try {
					br.searchKeyword(textField_keyword.getText(), dr);
					br.getPeopleInfo(dr);
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		}
	}

	private class ListenForExportButton implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == button_export) {
				System.out.println("start to export");
			}
		}
	}

	/**
	 * change status text flag: 0-pending 1-running 2-done 3-failed
	 **/
	private static void changeStatus(int flag) {
		switch (flag) {
			case STATUS_PENDING:
				label_status.setText("<html><font color='orange'>pending</font><html>");
				break;
			case STATUS_RUNNING:
				label_status.setText("<html><font color='blue'>running</font><html>");
				break;
			case STATUS_DONE:
				label_status.setText("<html><font color='green'>done!</font><html>");
				break;
			case STATUS_FAILED:
				label_status.setText("<html><font color='red'>failed</font><html>");
				break;
			default:
				label_status.setText("<html><font color='black'>unkown</font><html>");
				break;
		}
	}
}
