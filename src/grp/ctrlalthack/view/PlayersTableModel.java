/**
 * Custom AbstractTableModel for displaying courses
 * 
 * @author Arushad Ahmed
 * @arthor_uri http://arushad.org  
 */

package grp.ctrlalthack.view;

import grp.ctrlalthack.model.Player;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class PlayersTableModel extends AbstractTableModel {
	

	//stores the courses
	private ArrayList<Player> players;
	
	//the column titles
	private static final String[] COLUMNS = new String[]{"#", "Player Name","Creds", "Cash"};
	
	/**
	 * Constructor
	 */
	public PlayersTableModel() {
		this(new ArrayList<Player>());
	}
	
	/**
	 * Constructor
	 */
	public PlayersTableModel( ArrayList<Player> players ) {
		if (players == null) {
			players = new  ArrayList<Player>();
		} 
		this.players = players;
	}
	
	/**
	 * Updates the list of courses
	 */
	public void updateData( ArrayList<Player> players ) {
		this.players = players;
	}
	
	/**
	 * Returns the number of columns in the table
	 */
	@Override	
	public int getColumnCount() {
		return COLUMNS.length;
	}
		
	/**
	 * Returns the number of rows in the table
	 */
	@Override
	public int getRowCount() {		
		return this.players.size();
	}

	/**
	 * Returns the value at a given cell
	 */
	@Override
	public Object getValueAt(int row, int col) {
		Player player = this.players.get(row);
		//return the corresponding field
		switch(col) {
			case 0:
				return row;
			case 1:
				return player.getPlayerName();
			case 2:
				return player.getHackerCreds();
			case 3:
				return player.getCash();
		}
		return null;
	}
		
	/**
	 * Returns the name of a given column
	 */
	@Override
	public String getColumnName(int col) {
	   return COLUMNS[col];
	}

}
