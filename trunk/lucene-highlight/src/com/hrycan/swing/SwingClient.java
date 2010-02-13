package com.hrycan.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hrycan.search.Searcher;
import com.hrycan.service.SearchResultContainer;
import com.hrycan.service.SearchService;


public class SwingClient extends JPanel  {
	protected static Logger log = Logger.getLogger(Searcher.class);
	private JButton button;
	private JButton button2;
	private SearchService service;

	public SwingClient() {
		super(new BorderLayout());

		ApplicationContext context = new ClassPathXmlApplicationContext("clientcontext.xml");
		service = (SearchService) context.getBean("searchService");
		final String searchTerm = "government";

		button = new JButton("search");
		button.setPreferredSize(new Dimension(200, 80));
		add(button, BorderLayout.NORTH);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Toolkit.getDefaultToolkit().beep();
				try {
					SearchResultContainer resultContainer = service.search(searchTerm);
					long time = resultContainer.getExecutionTime();
					int hitCount = resultContainer.getTotalHitCount();
					String input = resultContainer.getUserInput();
					log.info(hitCount + " matches for " + input + " in " + time + "ms, " 
							+ resultContainer.getSearchResults().size() 
							+ " items returned search");
				} catch (Exception e1) {
					e1.printStackTrace();
				} 
			}
		});

		button2 = new JButton("paginated");
		button2.setPreferredSize(new Dimension(200, 80));
		add(button2, BorderLayout.SOUTH);
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Toolkit.getDefaultToolkit().beep();
				try {
					SearchResultContainer resultContainer = service.search(searchTerm, 5, 10);
					long time = resultContainer.getExecutionTime();
					int hitCount = resultContainer.getTotalHitCount();
					String input = resultContainer.getUserInput();
					log.info(hitCount + " matches for " + input + " in " + time + "ms, " 
							+ resultContainer.getSearchResults().size() 
							+ " items returned - paginated");
				} catch (Exception e1) {
					e1.printStackTrace();
				} 
			}
		});
	}



	/**
	 * Create the GUI and show it.  For thread safety,
	 * this method should be invoked from the
	 * event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		//Create and set up the window.
		JFrame frame = new JFrame("Beeper");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Create and set up the content pane.
		JComponent newContentPane = new SwingClient();
		newContentPane.setOpaque(true); //content panes must be opaque
		frame.setContentPane(newContentPane);

		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}


}
