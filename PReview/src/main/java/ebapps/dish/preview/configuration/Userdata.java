package ebapps.dish.preview.configuration;

public class Userdata {

	private int    iId       = -1;
	private String sUsername = null;
	private String sPassword = null;
	private String sPrivs    = null;
	private String sFullname = null;
	private String sEmail    = null;
	private String sStatus   = null;
	
	public int getiId() {
		return iId;
	}
	public void setiId(int iId) {
		this.iId = iId;
	}
	public String getsUsername() {
		return sUsername;
	}
	public void setsUsername(String sUsername) {
		this.sUsername = sUsername;
	}
	public String getsPassword() {
		return sPassword;
	}
	public void setsPassword(String sPassword) {
		this.sPassword = sPassword;
	}
	public String getsPrivs() {
		return sPrivs;
	}
	public void setsPrivs(String sPrivs) {
		this.sPrivs = sPrivs;
	}
	public String getsFullname() {
		return sFullname;
	}
	public void setsFullname(String sFullname) {
		this.sFullname = sFullname;
	}
	public String getsEmail() {
		return sEmail;
	}
	public void setsEmail(String sEmail) {
		this.sEmail = sEmail;
	}
	public String getsStatus() {
		return sStatus;
	}
	public void setsStatus(String sStatus) {
		this.sStatus = sStatus;
	}
	
    public void resetFields()
    {
    	iId       = -1;
    	sUsername = null;
    	sPassword = null;
    	sPrivs    = null;
    	sFullname = null;
    	sEmail    = null;
    	sStatus   = null;
    		
    }
}
