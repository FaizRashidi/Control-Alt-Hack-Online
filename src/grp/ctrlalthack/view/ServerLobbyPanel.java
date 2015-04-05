/**
 * Server lobby panel
 *  
 * @author Arushad Ahmed
 * @arthor_uri http://arushad.org  
 */

package grp.ctrlalthack.view;

import grp.ctrlalthack.net.ClientService;

import javax.swing.JPanel;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

public class ServerLobbyPanel extends CardPanel implements ViewConstants {
//public class ServerLobbyPanel extends JPanel implements ViewConstants {
		
	private JButton btn_start_game;
	private JButton btn_cancel_start_game;	
	private JTable table;
	
	/**
	 * Create the panel.
	 */
	public ServerLobbyPanel(JPanel cards) {
		//super();
		super(cards);		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{30, 0, 0, 0, 0, 0, 30, 0};
		gridBagLayout.rowHeights = new int[]{30, 0, 0, 0, 30, 0, 0, 30, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblStartServer = new JLabel("Server Lobby");
		GridBagConstraints gbc_lblStartServer = new GridBagConstraints();
		gbc_lblStartServer.gridwidth = 3;
		gbc_lblStartServer.insets = new Insets(0, 0, 5, 5);
		gbc_lblStartServer.gridx = 2;
		gbc_lblStartServer.gridy = 1;
		add(lblStartServer, gbc_lblStartServer);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 2;
		gbc_scrollPane.gridy = 3;
		add(scrollPane, gbc_scrollPane);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null},
				{null, null},
			},
			new String[] {
				"New column", "New column"
			}
		));
		scrollPane.setViewportView(table);
		
		btn_start_game = new JButton("Start Game");
		GridBagConstraints gbc_btn_start_game = new GridBagConstraints();
		gbc_btn_start_game.anchor = GridBagConstraints.WEST;
		gbc_btn_start_game.insets = new Insets(0, 0, 5, 5);
		gbc_btn_start_game.gridx = 2;
		gbc_btn_start_game.gridy = 5;
		add(btn_start_game, gbc_btn_start_game);
		
		btn_cancel_start_game = new JButton("Cancel");
		GridBagConstraints gbc_btn_cancel_start_game = new GridBagConstraints();
		gbc_btn_cancel_start_game.anchor = GridBagConstraints.WEST;
		gbc_btn_cancel_start_game.insets = new Insets(0, 0, 5, 5);
		gbc_btn_cancel_start_game.gridx = 3;
		gbc_btn_cancel_start_game.gridy = 5;
		add(btn_cancel_start_game, gbc_btn_cancel_start_game);
		
		btn_cancel_start_game.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				navigateTo(HOME_PANEL);
				getParentLayout().removeLayoutComponent(getInstance());
			}
		});
		
	}

}
