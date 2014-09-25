package ebapps.dish.preview.configuration;

public class Reviewdata {

	private int iId;
	private String sArtifactType;
	private String sArtifactId;
	private int iNumIterations;
	private String sStatus;
	private String sCondition;
	private String sGroup;
	private String sCreator;
	
	public String getsCreator() {
		return sCreator;
	}
	public void setsCreator(String sCreator) {
		this.sCreator = sCreator;
	}
	public int getiId() {
		return iId;
	}
	public void setiId(int iId) {
		this.iId = iId;
	}
	public String getsArtifactType() {
		return sArtifactType;
	}
	public void setsArtifactType(String sArtifactType) {
		this.sArtifactType = sArtifactType;
	}
	public String getsArtifactId() {
		return sArtifactId;
	}
	public void setsArtifactId(String sArtifactId) {
		this.sArtifactId = sArtifactId;
	}
	public int getiNumIterations() {
		return iNumIterations;
	}
	public void setiNumIterations(int iNumIterations) {
		this.iNumIterations = iNumIterations;
	}
	public String getsStatus() {
		return sStatus;
	}
	public void setsStatus(String sStatus) {
		this.sStatus = sStatus;
	}
	public String getsCondition() {
		return sCondition;
	}
	public void setsCondition(String sCondition) {
		this.sCondition = sCondition;
	}
	public String getsGroup() {
		return sGroup;
	}
	public void setsGroup(String sGroup) {
		this.sGroup = sGroup;
	}
}
