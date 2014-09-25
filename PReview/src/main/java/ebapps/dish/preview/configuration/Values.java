package ebapps.dish.preview.configuration;

public class Values {
	
	private static String sUsername = null;
	private static String sPassword = null;
	private static String sDbHost   = null;
	private static String sDbName   = null;
	private static String sPrivs    = null;
	private static String sFullname = null;
	private static int    iId       = 0;
	
	
	public static String getsUsername() {
		return sUsername;
	}
	public void setsUsername(String sUsername) {
		Values.sUsername = sUsername;
	}
	public static String getsPassword() {
		return sPassword;
	}
	public void setsPassword(String sPassword) {
		Values.sPassword = sPassword;
	}
	public static String getsDbHost() {
		return sDbHost;
	}
	public void setsDbHost(String sDbHost) {
		Values.sDbHost = sDbHost;
	}
	public static String getsDbName() {
		return sDbName;
	}
	public void setsDbName(String sDbName) {
		Values.sDbName = sDbName;
	}
	public static String getsPrivs() {
		return sPrivs;
	}
	public static void setsPrivs(String sPrivs) {
		Values.sPrivs = sPrivs;
	}
	public static String getsFullname() {
		return sFullname;
	}
	public static void setsFullname(String sFullname) {
		Values.sFullname = sFullname;
	}
	
	public static int getId() {
		return iId;
	}

	public static void setId(int id)  {
		Values.iId = id;
	}
}
