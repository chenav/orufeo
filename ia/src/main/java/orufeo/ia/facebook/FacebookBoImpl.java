package orufeo.ia.facebook;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.cloud.language.v1.Sentiment;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.types.Comment;
import com.restfb.types.Page;
import com.restfb.types.Post;
import com.restfb.types.User;

import orufeo.ia.ConfigurationObject;
import orufeo.ia.gcloud.NplAnalyzer;
import orufeo.ia.gcloud.VisionAnalyzer;


public class FacebookBoImpl implements FacebookBo {

	private static final Logger log = LogManager.getLogger(FacebookBoImpl.class);




	@Override
	public void analyzeImages(String pagename) {

		Connection<Post> myFeed = collectFeed(pagename);

		for (List<Post> myFeedPage : myFeed) {

			for (Post post : myFeedPage) {
				String urlPicture = post.getFullPicture();

				if (null!=urlPicture) {

					System.out.println("Picture :"+urlPicture);

					VisionAnalyzer.getInstance().analyzeLabels(urlPicture);

				}
			}
		}
	}


	@Override
	public void searchPage(String pagename) {

		Connection<Post> myFeed = collectFeed(pagename);

		for (List<Post> myFeedPage : myFeed) {  //paging made in facebook api

			// Iterate over the list of contained data 
			// to access the individual object
			for (Post post : myFeedPage) {

				if (null!=post.getComments() && post.getComments().getData()!=null) {

					for ( Comment comment : post.getComments().getData()) {

						try {
							NplAnalyzer analyzer = NplAnalyzer.getInstance();

							Sentiment feeling = analyzer.analyze(comment.getMessage());

							if (feeling.getScore() < 0 && feeling.getMagnitude()>1) {
								System.out.println("**********************************");
								System.out.println("Date :"+comment.getCreatedTime());
								System.out.println("User :"+comment.getFrom().getName());
								System.out.println("UserId :"+comment.getFrom().getId());
								System.out.println("Message :"+comment.getMessage());
								System.out.println("Sentiment: score="+ feeling.getScore()+", magnitude="+ feeling.getMagnitude());

							}
						} catch (Exception e) {
							System.out.println("ERROR in analysis: "+e.getMessage());
						}

					}
				}

			}
		}


	}


	@Override
	public void analyzeOCR(String pagename) {
		Connection<Post> myFeed = collectFeed(pagename);

		for (List<Post> myFeedPage : myFeed) {

			for (Post post : myFeedPage) {
				String urlPicture = post.getFullPicture();

				if (null!=urlPicture) {

					System.out.println("Picture :"+urlPicture);

					VisionAnalyzer.getInstance().analyzeOCR(urlPicture);


				}
			}
		}

	}


	@Override
	public void analyzeLogo(String pagename) {
		Connection<Post> myFeed = collectFeed(pagename);

		for (List<Post> myFeedPage : myFeed) {

			for (Post post : myFeedPage) {
				String urlPicture = post.getFullPicture();

				if (null!=urlPicture) {

					System.out.println("Picture :"+urlPicture);

					VisionAnalyzer.getInstance().analyzeLogo(urlPicture);


				}
			}
		}

	}


	@Override
	public void analyzeFace(String pagename) {
		Connection<Post> myFeed = collectFeed(pagename);

		for (List<Post> myFeedPage : myFeed) {

			for (Post post : myFeedPage) {
				String urlPicture = post.getFullPicture();

				if (null!=urlPicture) {

					System.out.println("Picture :"+urlPicture);

					VisionAnalyzer.getInstance().analyzeFace(urlPicture);

				}
			}
		}

	}

	//****************************
	//
	//   PRIVATE METHODS
	//
	//
	//****************************

	private FacebookClient initFacebookClient() {

		FacebookClient facebookClient30 = null;

		if (null==ConfigurationObject.ACTIVETOKEN && !ConfigurationObject.ACTIVETOKEN.isEmpty()) {
			AccessToken accessToken = new DefaultFacebookClient(Version.VERSION_3_0).obtainAppAccessToken(ConfigurationObject.MY_APP_ID, ConfigurationObject.MY_APP_SECRET);
			facebookClient30 = new DefaultFacebookClient(accessToken.getAccessToken(), Version.VERSION_3_0);
		} else 
			facebookClient30 = new DefaultFacebookClient(ConfigurationObject.ACTIVETOKEN, Version.VERSION_3_0);

		return facebookClient30;
	}
	
	private Connection<Post> collectFeed(String pagename) {

		FacebookClient facebookClient30 = initFacebookClient();

		//User user = facebookClient23.fetchObject("me", User.class);
		Page page = facebookClient30.fetchObject(pagename, Page.class, Parameter.with("fields","name,id, picture, likes"));
		return  facebookClient30.fetchConnection(page.getId()+"/feed", Post.class, Parameter.with("fields", "comments, full_picture")); //message,picture, likes, from,
	}


	private User searchUser(String id) {

		FacebookClient facebookClient30 = initFacebookClient();

		User user = facebookClient30.fetchObject(id, User.class, Parameter.with("fields","about,age_range, birthday,context, education,email,gender, first_name, last_name, relationship_status, work"));

		return user;
	}


}
