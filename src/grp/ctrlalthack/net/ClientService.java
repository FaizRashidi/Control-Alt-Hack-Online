/**
 * Sends requests from the Client GUI to the server and receives back responses
 * The Client GUI interacts with this class to communicate with the server
 * 
 * @author Arushad Ahmed
 * @arthor_uri http://arushad.org  
 */

package grp.ctrlalthack.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JOptionPane;

import grp.ctrlalthack.net.Command;
import grp.ctrlalthack.net.InvalidResponseException;
import grp.ctrlalthack.net.Response;

public class ClientService {
	
	public static final int DEFAULT_SERVER_PORT = 1234;
	
	private String server_ip; //server ip
	private int server_port; //port number of the server
	private Socket client; //socket for connection
	private String player_name; //name of the player
	private String server_password; //server password
	//object streams
	private ObjectOutputStream output;
	private ObjectInputStream input;		
	private boolean running; 	//flag to keep class running	
	private ReentrantLock sendResponseCommandLock; //lock for concurrent access to send/receive data from server	
		
	//constant for DATA UPDATED Command
	public static final Command DATA_UPDATED_CHECKER = new Command("DATA UPDATED", null);
	
	//TODO
	//private ClientView client_gui; //client GUI
	
	/**
	 * Constructor
	 */
	public ClientService(String server_ip, int server_port, String server_password, String player_name) {
		this.server_ip = server_ip;
		this.setPort(server_port);
		this.player_name = player_name;
		this.server_password = server_password;
		this.sendResponseCommandLock = new ReentrantLock();		
	}	
	
	/**
	 * @return the player_name
	 */
	public String getPlayerName() {
		return this.player_name;
	}

	/**
	 * Sets the server port
	 */
	private void setPort(int port) {
		if ( port < 1 || port > 65535 ) { //invalid port number
    		throw new IllegalArgumentException("Invalid Port Number");
    	} 
		this.server_port = port;
	}
	
	/**
	 * Runs the client
	 */
	public void runClient() {		
		//run if only if not running
		if ( !this.isRunning() ) { 
			//connect to the server
			boolean connected = false;
			try {
				this.client = new Socket(this.server_ip, this.server_port);
				this.input = new ObjectInputStream(this.client.getInputStream());
				this.output = new ObjectOutputStream(this.client.getOutputStream());
				this.output.flush();
				connected = true;
			} catch (IOException e) { //abort if connection cannot be initialized				
				this.showError("Error connecting to server");
				this.stop();				
			}
			
			//run the client
			if ( connected ) {
				try {
					// initialize flag
					this.running = this.wasAccepted();
					// initialize update listener, checks for new updates in the data
					Thread bacgkround_thread = new Thread(new UpdateListener());
					bacgkround_thread.start();
					// wait until GUI is closed
					while (this.isRunning()) {
		
					}
				} finally {
					if (this.isRunning()) { // close connections if they were not closed
						this.stop();
					}
				}
			}
		}		
	}
	
	/**
	 * Runnable to poll the server for update signals in the background
	 */
	private class UpdateListener implements Runnable {
		@Override
		public void run() {			
			while(isRunning()) {
				try {
					//reset the cache and update the results table if there were updates
					if (wasUpdated()) {
						//TODO
					}					
					Thread.sleep(500); //run every 500ms
				} catch (InterruptedException e) {				
				} catch (NoResponseException e) {				
				} catch (Exception e) {
					showError(e.getMessage());
				}
			}
		}		
	}
	
	/**
	 * Closes the connection to the server
	 */
	private void closeConnection() {
		try {
			//notify the server that the connection is being closed
			this.sendCommand(new Command("TERMINATE",null));
			//close the connections
			this.input.close();			
			this.output.close();
			this.client.close();
		} catch (NullPointerException | SocketException e) { //abort on any error
			return;
		} catch (IOException e) {			
			this.showError("Error closing connection with server");
			return;
		}
	}
			
	/**
	 * Checks if server is running
	 */
	public boolean isRunning() {
		return this.running;
	}
	
	/**
	 * Sets the running flag to false and close the connections
	 */
	public void stop() {
		if ( this.isRunning() ) {
			this.closeConnection();
			this.running = false;
		}
	}	
	
	/**
	 * Check if the connection was accepted
	 */
	public boolean wasAccepted() {
		//build the params map
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("password", this.server_password);
		Command command = new Command("INITIATE", params);
		//send the command to the server
		Response reply = this.sendResponseCommand(command);
		//display error if operation wasn't successful
		if ( !reply.getKeyword().equals("SUCCESS") ) {
			throw new InvalidResponseException("Invalid response received from server for " + command.getKeyword());
		}
		return true;
	}
	
	/**
	 * Check if data was updated since last request
	 */
	public boolean wasUpdated() {
		//send commad to server and get back response
		Response reply = this.sendResponseCommand(DATA_UPDATED_CHECKER);		
				
		//initialize output value
		boolean was_updated = false;
				
		//get the result from the response
		if ( reply.getKeyword().equals("DATA UPDATED") ) {
			was_updated = (boolean) reply.getParam("updated");
		} else {
			throw new InvalidResponseException("Invalid response received from server for DATA UPDATED");
		}
				
		return was_updated;
	}
		
	/**
	 * Sends a command to the server and get a response back
	 * @param request
	 */
	private Response sendResponseCommand( Command request ) {
		this.sendResponseCommandLock.lock();
		Response reply = null;
		try {
			this.sendCommand(request);		
			reply = this.getResponse();
			if (reply == null) {
				throw new NoResponseException();
			} else if ( reply.getKeyword().equals("ERROR") ) {
				String msg = (String)reply.getParam("message");
				throw new ErrorResponseException(msg);
			} else if ( reply.getKeyword().equals("FAIL") ) {
				String msg = (String)reply.getParam("message");
				throw new FailResponseException(msg);
			}
		} finally {
			this.sendResponseCommandLock.unlock();
		}
		return reply;
	}		
	
	/**
	 * Sends a command to the server
	 * @param request
	 */
	private void sendCommand( Command request ) {
		try {
			this.output.writeObject(request); 
	        this.output.flush();
	        this.output.reset();
		} catch ( SocketException e ) { //give up if can't talk to server
			if (this.isRunning()) {
				this.showError("Connection to server lost.\nThis client will now close");
				this.stop();
				//TODO
			}
		} catch ( IOException e ) {			
			this.showError("Error sending request to server\n" + request.toString() + "\n" + e.getMessage());					
	    }
	}
	
	/**
	 * Gets a response from the server
	 */
	private Response getResponse() {			
		Response reply = null;
		try {
			reply = ( Response ) this.input.readObject();
			if ( reply.getKeyword().equals("TERMINATE") ) {
				this.showError("The server has closed the connection.\nThis client will now close");			
				this.stop();
				//TODO
			} 
		} catch ( SocketException e ) { //give up if can't talk to server
			if (this.isRunning()) {
				this.showError("Connection to server lost.\nThis client will now close");
				this.stop();
				//TODO
			}
		} catch ( IOException e ) {
        	this.showError("Error reading response from server\n" + e.getMessage());        
        } catch ( ClassCastException | ClassNotFoundException e ) {
        	this.showError("Unknown response received from server");        	
        }		
		return reply;
	}
	
	/**
	 * Displays an error message	
	 */
	public void showError(String message) {
		JOptionPane.showMessageDialog(null, message, "Network Error!", JOptionPane.ERROR_MESSAGE);
	}
		
}