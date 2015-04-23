package sql;

public class SQL {

	public static final String BASE_INFO = "BASE_INFO";
	public static final String CONTACT_INFO= "CONTACT_INFO";
	public static final String CHAT_INFO= "CHAT_INFO";
	// ****************************************************************
	// All queries related
	// ****************************************************************
	/**
	 * Query to create the BASE_INFO table.
	 */
	public static final String CREATE_BASE_INFO = "CREATE TABLE IF NOT EXISTS "
			+ BASE_INFO + " ( user_id VARCHAR(50) PRIMARY KEY,"
			+ " user_name VARCHAR(50),"
			+ " password VARCHAR(50),"
			+ " age VARCHAR(50),"
			+ " gender VARCHAR(50),"
			+ " last_location VARCHAR(100),"
			+ " total_distance VARCHAR(100),"
			+ " total_time VARCHAR(100))";
	
	/**
	 * Query to create the CONTACT_INFO table, for the normalization
	 */
	public static final String CREATE_CONTACT_INFO = "CREATE TABLE IF NOT EXISTS "
			+ CONTACT_INFO + " ( _id INTEGER PRIMARY KEY AUTO_INCREMENT,"
			+ " user_id VARCHAR(50),"
			+ " contact_id VARCHAR(50),"
			+ " status VARCHAR(50))";
	
	/**
	 * Query to create the CHAT_INFO table, for the normalization
	 */
	public static final String CREATE_CHAT_INFO = "CREATE TABLE IF NOT EXISTS "
			+ CHAT_INFO + " ( _id INTEGER PRIMARY KEY AUTO_INCREMENT,"
			+ " sender_id VARCHAR(50),"
			+ " receiver_id VARCHAR(50),"
			+ " timestamp VARCHAR(50),"
			+ " content VARCHAR(1000))";
	
	/**
	 * Query to insert a row into the baseinfo table.
	 */
	public static final String INSERT_BASE = "insert into " + BASE_INFO
			+ " (user_id, user_name, password, age, gender, last_location, total_distance, total_time)"
			+ " values (?, ?, ?, ?, ?, ?, ?, ?)";
	
	/**
	 * Query to update running record
	 */
	public static final String UPDATE_RUNING_RECORD = "update " + BASE_INFO
			+ " set last_location = ?, total_distance = ?, total_time = ?"
			+ " where user_id = ?";
	
	/**
	 * Query to check if the contact exists
	 */
	public static final String CHECK_IF_CONTACT_EXISTS = "select *"
			+ " from " + CONTACT_INFO
			+ " where user_id = ? AND contact_id = ? ";
	
	/**
	 * Query to insert a row into the contactinfo table.
	 */
	public static final String INSERT_CONTACT = "insert into " + CONTACT_INFO
			+ " (user_id, contact_id, status)"
			+ " values (?, ?, ?)";
	
	/**
	 * Query to insert a row into the contactinfo table.
	 */
	public static final String INSERT_CHAT = "insert into " + CHAT_INFO
			+ " (sender_id, receiver_id, timestamp, content)"
			+ " values (?, ?, ?, ?)";
	
	/**
	 * Query to update running record
	 */
	public static final String UPDATE_CONTACT = "update " + CONTACT_INFO
			+ " set status = 'active'"
			+ " where user_id = ? AND contact_id = ?";
	
	/**
	 * Query to delete a row from the table.
	 */
	public static final String DELETE_CONTACT = "delete from " + CONTACT_INFO
			+ " where user_id = ? AND contact_id = ?";
	
	/**
	 * Query to delete a row from the table.
	 */
	public static final String REJECT_CONTACT = "delete from " + CONTACT_INFO
			+ " where user_id = ? AND contact_id = ? AND status = 'pending'";
	
	/**
	 * Query to delete a row from the chat table.
	 */
	public static final String DELETE_CHAT = "delete from " + CHAT_INFO
			+ " where receiver_id = ? ";
	
	/**
	 * Query to get user information
	 */
	public static final String GET_USER_INFO = "select *"
			+ " from " + BASE_INFO
			+ " where user_id = ? ";
	
	/**
	 * Query to get all contact information
	 */
	public static final String GET_CHAT_INFO = "select *"
			+ " from " + CHAT_INFO
			+ " where receiver_id = ? ";
	
	/**
	 * Query to check if the contact exists
	 */
	public static final String GET_CONTACT_INFO = "select *"
			+ " from " + CONTACT_INFO
			+ " where user_id = ? ";
	
	/**
	 * Query to check if the contact exists
	 */
	public static final String GET_USERS = "select *"
			+ " from " + BASE_INFO;
}
