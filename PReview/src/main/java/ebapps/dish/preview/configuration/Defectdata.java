package ebapps.dish.preview.configuration;

public class Defectdata {
	
	  private int iDefectId = -1;
	  private int iPeerReviewId = -1;
	  private String sDefectType;
	  private String sDefectDesc;
	  private int iSeverity;
	  private String sStatus;
	  private String sCond;
	  
	public int getiDefectId() {
		return iDefectId;
	}
	public void setiDefectId(int iDefectId) {
		this.iDefectId = iDefectId;
	}
	public int getiPeerReviewId() {
		return iPeerReviewId;
	}
	public void setiPeerReviewId(int iPeerReviewId) {
		this.iPeerReviewId = iPeerReviewId;
	}
	public String getsDefectType() {
		return sDefectType;
	}
	public void setsDefectType(String sDefectType) {
		this.sDefectType = sDefectType;
	}
	public String getsDefectDesc() {
		return sDefectDesc;
	}
	public void setsDefectDesc(String sDefectDesc) {
		this.sDefectDesc = sDefectDesc;   
	}
	public int getiSeverity() {
		return iSeverity;
	}
	public void setiSeverity(int iSeverity) {
		this.iSeverity = iSeverity;
	}
	public String getsStatus() {
		return sStatus;
	}
	public void setsStatus(String sStatus) {
		this.sStatus = sStatus;
	}
	public String getsCond() {
		return sCond;
	}
	public void setsCond(String sCond) {
		this.sCond = sCond;
	}
	
}
