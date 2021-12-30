/**
 * @author Aaron-Qiu, mgsweet@126.com, student_id:1101584
 */
package Client;

import javax.swing.JFrame;

import States.States;

import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientUI {
	private javax.swing.JButton btnAdd;
	private javax.swing.JButton btnQuery;
	private javax.swing.JButton btnRemove;
	private javax.swing.JFrame jFrame1;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JPanel mainPanel;
	private java.awt.Panel panel1;
	private java.awt.Panel panel2;
	private java.awt.Panel panel3;
	private JFrame frame;
	private JTextArea meaningPane;
	private JTextField wordField;
	private Client dictClient;
	
	public JFrame getFrame() {
		return frame;
	}

	public ClientUI(Client client) {
		dictClient =client;
		initialize();
	}
	
	private Boolean isValid(String word, String meaning, int command) {
		if (word.equals("")) {
			JOptionPane.showMessageDialog(frame, "Please Enter a word.", "Warning", JOptionPane.WARNING_MESSAGE);
			return false;
		} else if (command == States.ADD && meaning.equals("")) {
			JOptionPane.showMessageDialog(frame, "Please Enter the word's definition.", "Warning", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}

	private void initialize() {
		frame = new JFrame("Client: Word Dictionary");
		frame.setMinimumSize(new Dimension(800, 500));

		jFrame1 = new javax.swing.JFrame();
		mainPanel = new javax.swing.JPanel();
		panel1 = new java.awt.Panel();
		jPanel1 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		wordField = new javax.swing.JTextField();
		panel3 = new java.awt.Panel();
		jLabel2 = new javax.swing.JLabel();
		jScrollPane1 = new javax.swing.JScrollPane();
		meaningPane = new javax.swing.JTextArea();
		panel2 = new java.awt.Panel();
		btnAdd = new javax.swing.JButton();
		btnQuery = new javax.swing.JButton();
		btnRemove = new javax.swing.JButton();
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String word = wordField.getText();
				String meaning = meaningPane.getText();
				if (isValid(word, meaning, States.ADD)) {
					int confirm = JOptionPane.showConfirmDialog(frame,  "Confirm to Add a new word?", "Confirm Window", JOptionPane.YES_NO_OPTION);
					if (confirm == JOptionPane.YES_OPTION) {
						int state = dictClient.add(word, meaning);
						if(state == States.UNKNOWN_HOST) {
							JOptionPane.showMessageDialog(frame, "Unknown Host!\nPlease restart with a correct Address and IP.",
									"Warning", JOptionPane.ERROR_MESSAGE);
						} else if (state == States.FAIL) {
							JOptionPane.showMessageDialog(frame, "Word Already in the database!", "Warning", JOptionPane.WARNING_MESSAGE);
						} else if (state == States.TIMEOUT) {
							JOptionPane.showMessageDialog(frame, "Timeout!\nPlease check the server or restart with a correct Address and IP.",
									"Warning", JOptionPane.ERROR_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(frame, "Add Success!", "Tips", JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}
			}
		});
		btnQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String word = wordField.getText();
				if (isValid(word, "", States.QUERY)) {
					String[] resultArr = dictClient.query(word);
					int state = Integer.parseInt(resultArr[0]);
					if (state == States.UNKNOWN_HOST) {
						JOptionPane.showMessageDialog(frame, "Unknown Host!\nPlease restart with a correct Address and IP.", "Warning", JOptionPane.ERROR_MESSAGE);
					} else if (state == States.FAIL) {
						JOptionPane.showMessageDialog(frame, "Query Fail\nWord Not Exist!", "Warning", JOptionPane.WARNING_MESSAGE);
					} else if (state == States.TIMEOUT) {
						JOptionPane.showMessageDialog(frame, "Timeout!\nPlease check the server or restart with a correct Address and IP.",
								"Warning", JOptionPane.ERROR_MESSAGE);
					} else {
						meaningPane.setText(resultArr[1]);
					}
				}
			}
		});
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String word = wordField.getText();
				if (isValid(word, "", States.REMOVE)) {
					int confirm = JOptionPane.showConfirmDialog(frame,  "Confirm to Remove a new word?", "Confirm Window", JOptionPane.YES_NO_OPTION);
					if (confirm == JOptionPane.YES_OPTION) {
						int state = dictClient.remove(word);
						if(state == States.UNKNOWN_HOST) {
							JOptionPane.showMessageDialog(frame, "Unknown Host!\nPlease restart with a correct Address and IP.", "Warning", JOptionPane.ERROR_MESSAGE);
						}
						else if (state == States.FAIL) {
							JOptionPane.showMessageDialog(frame, "Remove Fail\nWord Not Exist!", "Warning", JOptionPane.WARNING_MESSAGE);
						} else if (state == States.TIMEOUT) {
							JOptionPane.showMessageDialog(frame, "Timeout!\nPlease check the server or restart with a correct Address and IP.",
									"Warning", JOptionPane.ERROR_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(frame, "Remove Success!", "Tips", JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}
			}
		});

		frame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		frame.setTitle("Client: Word Dictionary");
		frame.setAlwaysOnTop(true);
		frame.setBackground(new java.awt.Color(0, 0, 255));
		frame.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		frame.setResizable(false);
		frame.getContentPane().setLayout(new java.awt.GridBagLayout());

		mainPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(11, 11, 12, 12));
		mainPanel.setLayout(new java.awt.GridBagLayout());
		frame.getContentPane().add(mainPanel, new java.awt.GridBagConstraints());

		panel1.setBackground(new java.awt.Color(240, 240, 240));
		panel1.setMinimumSize(new java.awt.Dimension(400, 300));
		panel1.setPreferredSize(new java.awt.Dimension(400, 300));

		jPanel1.setPreferredSize(new java.awt.Dimension(400, 40));

		jLabel1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
		jLabel1.setText("Word:");
		jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
		jLabel1.setPreferredSize(new java.awt.Dimension(38, 15));
		jLabel1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		jPanel1.add(jLabel1);
		jLabel1.getAccessibleContext().setAccessibleName("wordLabel");

		wordField.setPreferredSize(new java.awt.Dimension(350, 25));
		jPanel1.add(wordField);

		panel1.add(jPanel1);

		panel3.setBackground(java.awt.Color.lightGray);
		panel3.setPreferredSize(new java.awt.Dimension(400, 300));

		jLabel2.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
		jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		jLabel2.setText("Word Definitions:");
		jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
		jLabel2.setPreferredSize(new java.awt.Dimension(400, 25));
		panel3.add(jLabel2);

		jScrollPane1.setPreferredSize(new java.awt.Dimension(400, 300));

		meaningPane.setColumns(20);
		meaningPane.setRows(5);
		jScrollPane1.setViewportView(meaningPane);

		panel3.add(jScrollPane1);

		panel1.add(panel3);

		frame.getContentPane().add(panel1, new java.awt.GridBagConstraints());
		panel1.getAccessibleContext().setAccessibleDescription("");

		panel2.setBackground(new java.awt.Color(153, 153, 153));
		panel2.setPreferredSize(new java.awt.Dimension(100, 300));
		btnAdd.setBackground(new java.awt.Color(50, 110, 100));
		btnAdd.setForeground(new java.awt.Color(255, 255, 255));
		btnAdd.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
		btnAdd.setText("ADD");
		btnAdd.setMaximumSize(new java.awt.Dimension(100, 100));
		btnAdd.setMinimumSize(new java.awt.Dimension(40, 40));
		btnAdd.setPreferredSize(new java.awt.Dimension(85, 30));
		btnAdd.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
		panel2.add(btnAdd);

		btnQuery.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
		btnQuery.setLabel("QUERY");
		btnQuery.setBackground(new java.awt.Color(50, 50, 110));
		btnQuery.setForeground(new java.awt.Color(255, 255, 255));
		btnQuery.setMaximumSize(new java.awt.Dimension(100, 100));
		btnQuery.setMinimumSize(new java.awt.Dimension(40, 40));
		btnQuery.setPreferredSize(new java.awt.Dimension(85, 30));
		btnQuery.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
		btnQuery.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
		panel2.add(btnQuery);
		btnQuery.getAccessibleContext().setAccessibleName("query");

		btnRemove.setBackground(new java.awt.Color(100, 50, 50));
		btnRemove.setForeground(new java.awt.Color(255, 255, 255));
		btnRemove.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
		btnRemove.setText("REMOVE");
		btnRemove.setMaximumSize(new java.awt.Dimension(100, 100));
		btnRemove.setMinimumSize(new java.awt.Dimension(40, 40));
		btnRemove.setPreferredSize(new java.awt.Dimension(85, 30));
		btnRemove.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
		panel2.add(btnRemove);
		btnRemove.getAccessibleContext().setAccessibleName("remove");

		frame.getContentPane().add(panel2, new java.awt.GridBagConstraints());

	}
}
