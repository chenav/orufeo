package orufeo.ia;


import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;

import orufeo.ia.facebook.FacebookBo;
import orufeo.ia.facebook.FacebookBoImpl;

import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;


public class App 
{

	public static void main(String... args) throws Exception {

		int mode = 3;

		// Instantiates a client
		try (LanguageServiceClient language = LanguageServiceClient.create()) {

			if (0==mode) {
				// The text to analyze
				String text = "Hello, world!";
				Document doc = Document.newBuilder()
						.setContent(text).setType(Type.PLAIN_TEXT).build();

				// Detects the sentiment of the text
				Sentiment sentiment = language.analyzeSentiment(doc).getDocumentSentiment();

				System.out.printf("Text: %s%n", text);
				System.out.printf("Sentiment: %s, %s%n", sentiment.getScore(), sentiment.getMagnitude());

			}
			
			if (1==mode) {
				
				FacebookBo fbBo = new FacebookBoImpl();
								
				fbBo.searchPage("cocacolafrance");
				
			}
			
			if (2==mode) {
				
				FacebookBo fbBo = new FacebookBoImpl();
								
				fbBo.analyzeImages("cocacolafrance");
				
			}
			
			if (3==mode) {
				
				FacebookBo fbBo = new FacebookBoImpl();
								
				fbBo.analyzeOCR("cocacolafrance");
				
			}
		}

	}


}
