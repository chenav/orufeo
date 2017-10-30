package orufeo.ia.gcloud;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;

public class VisionAnalyzer {

	private static VisionAnalyzer INSTANCE = new VisionAnalyzer(); 
	private static final Logger log = LogManager.getLogger(VisionAnalyzer.class);
	private static ImageAnnotatorClient vision;

	private VisionAnalyzer() {

		if (null==vision) {
			try {
				vision =  ImageAnnotatorClient.create();
			} catch (Exception e) {
				log.error("Impossible d'initialiser l'API vision: ",e);
			}
		}
	}

	public static VisionAnalyzer getInstance() {
		return INSTANCE;
	}

	public void analyzeLabels(String urlImage) {

		List<AnnotateImageRequest> requests = new ArrayList<>();

		try {

			ByteString imgBytes = null;

			URL url = new URL(urlImage);
			InputStream is = null;
			try {
				is = url.openStream();
				byte[] imageBytes = IOUtils.toByteArray(is);
				imgBytes = ByteString.copyFrom(imageBytes);
			} catch (IOException e) {
				System.err.printf ("Failed while reading bytes from %s: %s%n", url.toExternalForm(), e.getMessage());
				e.printStackTrace ();
				// Perform any other exception handling that's appropriate.
			} finally {
				if (is != null) { is.close(); }
			}

			if (null!=imgBytes) {
				// Builds the image annotation request
				Image img = Image.newBuilder().setContent(imgBytes).build();
				Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
				AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
						.addFeatures(feat)
						.setImage(img)
						.build();
				requests.add(request);
			}

		} catch (Exception e) {
			log.error("Probleme de recup d'image: ", e);
		}

		// Performs label detection on the image file
		BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
		List<AnnotateImageResponse> responses = response.getResponsesList();

		for (AnnotateImageResponse res : responses) {
			if (res.hasError()) {
				System.out.printf("Error: %s\n", res.getError().getMessage());
			}

			for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {

				annotation.getAllFields().forEach((k, v)-> {
					if (!k.toString().contains("mid"))
						System.out.println(k.getName() +": "+ v.toString());
				});

			}
		}


	}

	public void analyzeOCR(String urlImage) {

		List<AnnotateImageRequest> requests = new ArrayList<>();

		try {

			ByteString imgBytes = null;

			URL url = new URL(urlImage);
			InputStream is = null;
			try {
				is = url.openStream();
				byte[] imageBytes = IOUtils.toByteArray(is);
				imgBytes = ByteString.copyFrom(imageBytes);
			} catch (IOException e) {
				System.err.printf ("Failed while reading bytes from %s: %s%n", url.toExternalForm(), e.getMessage());
				e.printStackTrace ();
				// Perform any other exception handling that's appropriate.
			} finally {
				if (is != null) { is.close(); }
			}

			if (null!=imgBytes) {
				// Builds the image annotation request
				Image img = Image.newBuilder().setContent(imgBytes).build();
				Feature feat = Feature.newBuilder().setType(Type.TEXT_DETECTION).build();
				AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
						.addFeatures(feat)
						.setImage(img)
						.build();
				requests.add(request);
			}

		} catch (Exception e) {
			log.error("Probleme de recup d'image: ", e);
		}

		// Performs label detection on the image file
		BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
		List<AnnotateImageResponse> responses = response.getResponsesList();

		for (AnnotateImageResponse res : responses) {
			if (res.hasError()) {
				System.out.printf("Error: %s\n", res.getError().getMessage());
			}


			// For full list of available annotations, see http://g.co/cloud/vision/docs
			for (EntityAnnotation anno : res.getTextAnnotationsList()) {
				System.out.printf("Text: %s\n", anno.getDescription());
				System.out.printf("Position : %s\n", anno.getBoundingPoly());
			}


		}


	}
	
	public void analyzeLogo(String urlImage) {

		List<AnnotateImageRequest> requests = new ArrayList<>();

		try {

			ByteString imgBytes = null;

			URL url = new URL(urlImage);
			InputStream is = null;
			try {
				is = url.openStream();
				byte[] imageBytes = IOUtils.toByteArray(is);
				imgBytes = ByteString.copyFrom(imageBytes);
			} catch (IOException e) {
				System.err.printf ("Failed while reading bytes from %s: %s%n", url.toExternalForm(), e.getMessage());
				e.printStackTrace ();
				// Perform any other exception handling that's appropriate.
			} finally {
				if (is != null) { is.close(); }
			}

			if (null!=imgBytes) {
				// Builds the image annotation request
				Image img = Image.newBuilder().setContent(imgBytes).build();
				Feature feat = Feature.newBuilder().setType(Type.LOGO_DETECTION).build();
				AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
						.addFeatures(feat)
						.setImage(img)
						.build();
				requests.add(request);
			}

		} catch (Exception e) {
			log.error("Probleme de recup d'image: ", e);
		}

		// Performs label detection on the image file
		BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
		List<AnnotateImageResponse> responses = response.getResponsesList();

		for (AnnotateImageResponse res : responses) {
			if (res.hasError()) {
				System.out.printf("Error: %s\n", res.getError().getMessage());
			}


			// For full list of available annotations, see http://g.co/cloud/vision/docs
			for (EntityAnnotation annotation : res.getLogoAnnotationsList()) {
		        System.out.println(annotation.getDescription());
		      }


		}


	}


}
