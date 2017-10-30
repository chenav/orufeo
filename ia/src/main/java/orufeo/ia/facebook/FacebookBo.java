package orufeo.ia.facebook;

public interface FacebookBo {

	void searchPage(String pagename);
	
	void analyzeImages(String pagename);

	void analyzeOCR(String pagename);
}
