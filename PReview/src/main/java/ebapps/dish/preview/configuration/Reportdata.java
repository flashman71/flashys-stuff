package ebapps.dish.preview.configuration;

public class Reportdata {

	private static String sReviewStatus ="";
	private static String sReviewGroup  ="";
	private static String sReviewCond   ="";
	private static String sDateParam    ="";
	
	private static String sDefectType   ="";
	private static String sDefectStatus ="";
	private static int    iDefectSev    = -1;
	private static String sDefectCond   ="";
	
	public static String getsReviewStatus() {
		return sReviewStatus;
	}
	public static void setsReviewStatus(String sReviewStatus) {
		Reportdata.sReviewStatus = sReviewStatus;
	}
	public static String getsReviewGroup() {
		return sReviewGroup;
	}
	public static void setsReviewGroup(String sReviewGroup) {
		Reportdata.sReviewGroup = sReviewGroup;
	}
	public static String getsReviewCond() {
		return sReviewCond;
	}
	public static void setsReviewCond(String sReviewCond) {
		Reportdata.sReviewCond = sReviewCond;
	}
	public static String getsDateParam() {
		return sDateParam;
	}
	public static void setsDateParam(String sDateParam) {
		Reportdata.sDateParam = sDateParam;
	}
	public static String getsDefectType() {
		return sDefectType;
	}
	public static void setsDefectType(String sDefectType) {
		Reportdata.sDefectType = sDefectType;
	}
	public static String getsDefectStatus() {
		return sDefectStatus;
	}
	public static void setsDefectStatus(String sDefectStatus) {
		Reportdata.sDefectStatus = sDefectStatus;
	}
	public static int getsDefectSev() {
		return iDefectSev;
	}
	public static void setsDefectSev(String sDefectSev) {
		Reportdata.iDefectSev = Integer.parseInt(sDefectSev);
	}
	public static String getsDefectCond() {
		return sDefectCond;
	}
	public static void setsDefectCond(String sDefectCond) {
		Reportdata.sDefectCond = sDefectCond;
	}
	

}
