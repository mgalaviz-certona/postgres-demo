package dev.demos;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

public class PostgresDemo {
	private final static int PROPERTIES_FILE_INDEX = 0;
	private final static String HOST_KEY = "host";
	private final static String PORT_KEY = "port";
	private final static String DATABASE_KEY = "database";
	private final static String USER_KEY = "user";
	private final static String PASS_KEY = "pass";
	private final static int DT_INDEX = 1;
	private final static int ID_INDEX = 2;
	
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
		if (args.length == 0) {
			System.out.println("No properties file specified!");
			return;
		}
		
		InputStream inputStream = new FileInputStream(args[PROPERTIES_FILE_INDEX]);
		Properties p = new Properties();
		p.load(inputStream);
		String hostName = p.getProperty(HOST_KEY);
		String port = p.getProperty(PORT_KEY);
		String database = p.getProperty(DATABASE_KEY);
		String user = p.getProperty(USER_KEY);
		String pass = p.getProperty(PASS_KEY);
		
		Connection c = null;
        Class.forName("org.postgresql.Driver"); 
        c = DriverManager.getConnection(
        		String.format("jdbc:postgresql://%s:%s/%s"
        				, hostName
        				, port
        				, database)
        		, user
        		, pass);

        //insert data into table with 2 columns: dt timestamp, id uuid 
        PreparedStatement pst = c.prepareStatement("insert into test_table values (?, ?);");
		Date date = new Date();
		UUID uuid = UUID.randomUUID();
		pst.setTimestamp(DT_INDEX, new Timestamp(date.getTime()));
		pst.setObject(ID_INDEX, uuid);
		pst.execute();
		pst.close();
		
		//read data
		Statement st = c.createStatement();
        ResultSet rs = st.executeQuery("SELECT dt, id FROM test_table;");
        while (rs.next()) {
        	Timestamp dtVal = rs.getTimestamp("dt");
        	Date dt = new Date(dtVal.getTime());
        	Object idValue = rs.getObject("id");
        	UUID id = (UUID)idValue;
        	try {
        		System.out.format("Row: id=%s name=%s\n", dt.toString(), id.toString());
        	} catch (Exception e) {
        		System.out.println("Something happened!");
        	}
        }
        rs.close();
        st.close();
        c.close();
	}
}
