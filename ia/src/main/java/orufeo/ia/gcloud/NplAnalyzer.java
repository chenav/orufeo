package orufeo.ia.gcloud;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.google.cloud.language.v1.Document.Type;

public class NplAnalyzer {

	private static NplAnalyzer INSTANCE = new NplAnalyzer(); 
	private static final Logger log = LogManager.getLogger(NplAnalyzer.class);
	private static LanguageServiceClient language;

	private NplAnalyzer() {

		if (null==language) {
			try {
				language = LanguageServiceClient.create();
			} catch (Exception e) {
				log.error("Impossible d'initialiser l'API language: ",e);
			}
		}
	}
	
	public static NplAnalyzer getInstance() {
		return INSTANCE;
	}

	public Sentiment analyze(String text) {

		Document doc = Document.newBuilder()
				.setContent(text).setType(Type.PLAIN_TEXT).build();

		// Detects the sentiment of the text
		Sentiment sentiment = language.analyzeSentiment(doc).getDocumentSentiment();

		return sentiment;

	}


}
