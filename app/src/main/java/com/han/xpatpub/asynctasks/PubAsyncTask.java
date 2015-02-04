package com.han.xpatpub.asynctasks;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.han.xpatpub.activity.AddPubActivity;
import com.han.xpatpub.activity.FeatureActivity;
import com.han.xpatpub.activity.PubInfoActivity;
import com.han.xpatpub.activity.ReviewActivity;
import com.han.xpatpub.model.Action;
import com.han.xpatpub.model.General;
import com.han.xpatpub.model.GlobalData;
import com.han.xpatpub.model.Pub;
import com.han.xpatpub.network.PubParser;
import com.han.xpatpub.network.PubWebService;
import com.han.xpatpub.network.Result;

public class PubAsyncTask extends AbstractedAsyncTask {
	
	private Pub pub;
	private String[] featureKeysToUpdate;
	
	public static final int PUB_ALEADY_EXIST = 2;
	
    public PubAsyncTask(Activity activity) {
		super(activity);		
	}
    
    public PubAsyncTask(Context context) {
		super(context);		
	}
	
	public PubAsyncTask(Activity activity, Pub pub) {
		this(activity);
		this.pub = pub;
	}
	
	public PubAsyncTask(Activity activity, Pub pub, String[] featureKeysToUpdate) {
		this(activity, pub);
		this.featureKeysToUpdate = featureKeysToUpdate;
	}

	@Override
	protected Integer doInBackground(String... params) {
		super.doInBackground(params);
		if (curAction.equals(Action.ACTION_UPDATE_FEATURE)) {
			nResult = PubWebService.ratePub(pub, featureKeysToUpdate);
			
			if (nResult == Result.FAIL) {
				nResult = PubWebService.addRowInREST(pub);
				
				if (nResult == Result.SUCCESS) {
					nResult = PubWebService.ratePub(pub, featureKeysToUpdate);
				}
			}	
			
//			nResult = PubWebService.deletePubFeaure(nPubID);
//			nResult = Parser.SUCCESS;
//
//			String strPubFeature = params[2];
//			
//			if (nResult == Parser.SUCCESS) {
//				String[] arrStr = strPubFeature.split(",");
//				
//				for (int i = 0; i < arrStr.length; i++) {
//					nResult = PubWebService.addPubFeature(nPubID, Integer.parseInt(arrStr[i]));
//					if (nResult == Parser.FAIL) {
//						return nResult;
//					}
//				}
//			}	
			
		} else if (curAction.equals(Action.ACTION_VOTE)) {
			String msgVoteType = params[1];
			nResult = PubWebService.vote(pub, msgVoteType);
			
			if (nResult == Result.FAIL) {
				nResult = PubWebService.addRowInREST(pub);
				
				if (nResult == Result.SUCCESS) {
					nResult = PubWebService.vote(pub, msgVoteType);
				}
			}	
			
		} else if (curAction.equals(Action.ACTION_GET_PUB_INFO)) {
			String msgPubId = params[1];
			
			nResult = PubWebService.getPubInfo(msgPubId);

//			if (nResult == Parser.SUCCESS) {
//				nResult = MessageWebService.readMyMessage();
//			}
			
		} else if (curAction.equals(Action.ACTION_GET_PUB_INFO_FROM_FEATURE_COUNTER)) {
			
			nResult = PubWebService.getPubInfoFromFeatureCounter(String.valueOf(pub.id));
			
			if (nResult == Result.FAIL) {
				nResult = PubWebService.addRowInREST(pub);
				
				if (nResult == Result.SUCCESS) {
					nResult = PubWebService.getPubInfoFromFeatureCounter(String.valueOf(pub.id));
				}
			}

//			if (nResult == Parser.SUCCESS) {
//				nResult = MessageWebService.readMyMessage();
//			}
			
		} else if (curAction.equals(Action.ACTION_ADD_PUB)) {
			
			String strPubName = params[1];
			String strPubType = params[2];
			String strLat = params[3];
			String strLng = params[4];
			String strPubCountry = params[5];
			String strPubCategory = params[6];
			String strPubIcon = params[7];
			String strPubFounder = params[8];
			String strPubFeature = params[9];
			
			nResult = PubWebService.searchPub(strLat, strLng, "", "", Double.toString(GlobalData.MILES_FOR_METER_10));
			
			if (nResult == Result.SUCCESS) {
				int nCount = PubParser.arrSearchPub.size();
				if (nCount > 0) {
					nResult = PUB_ALEADY_EXIST;
					
				} else {
					forceAddPub(strPubName, strPubType, strLat, strLng, strPubCountry, strPubCategory, strPubIcon, strPubFounder, strPubFeature);
//					nResult = PubWebService.addPub(strPubName, strPubType, strLat, strLng, strPubCountry, strPubCategory, strPubIcon, strPubFounder);
//					if (nResult == Parser.SUCCESS) {
//						int nAddPubId = PubParser.nAddPubID;
//						
//						String[] arrStr = strPubFeature.split(",");
//						
//						for (int i = 0; i < arrStr.length; i++) {
//							nResult = PubWebService.addPubFeature(nAddPubId, Integer.parseInt(arrStr[i]));
//							if (nResult == Parser.FAIL) {
//								continue;	// TODO maybe break instead of continue..?
//							}
//						}
//					}
				}
			}
//			nResult = addPub(strName, strType, strIsHaveLiveMusic, strIsHaveTVSports, strIsHavePubGames);
			
		} else if (curAction.equals(Action.ACTION_ADD_PUB_FORCE)) {			
			String strPubName = params[1];
			String strPubType = params[2];
			String strLat = params[3];
			String strLng = params[4];
			String strPubCountry = params[5];
			String strPubCategory = params[6];
			String strPubIcon = params[7];
			String strPubFounder = params[8];
			String strPubFeature = params[9];
			
			forceAddPub(strPubName, strPubType, strLat, strLng, strPubCountry, strPubCategory, strPubIcon, strPubFounder, strPubFeature);
		
		} else if (curAction.equals(Action.ACTION_PUB_RATE)) {
			
			String strPubID = params[1];
//			String strRateOptionID = params[2];
//			String strRate = params[3];
			String[] strRate = new String[5];
			
			strRate[0] = params[2];
			strRate[1] = params[3];
			strRate[2] = params[4];
			strRate[3] = params[5];
			strRate[4] = params[6];
			
			nResult = PubWebService.addRate(strPubID, strRate);
			
		} else if (curAction.equals(Action.ACTION_PUB_DETAIL)) {
			String strID = params[1];
			nResult = PubWebService.pubDetail(strID);
			
//		} else if (curAction.equals(Action.ACTION_CHECK_BEFORE_SEARCH_PUB)) {
//			String strLat = params[1];
//			String strLng = params[2];
//			String strPubType = params[3];
//			String strPubFeature = params[4];
//			String strRadius = params[5];
//			
//			nResult = PubWebService.checkBeforeSearchForPubs(strLat, strLng, strPubType, strPubFeature, strRadius);					
			
		} else if (curAction.equals(Action.ACTION_SEARCH_PUB)) {			
			String strLat = params[1];
			String strLng = params[2];
			String strPubType = params[3];
			String strPubFeature = params[4];
			String strRadius = params[5];
			
			nResult = PubWebService.searchPub(strLat, strLng, strPubType, strPubFeature, strRadius);
			if (nResult == Result.SUCCESS) {
				GlobalData.arrAllPub = PubParser.arrSearchPub;
			}			
		}
		
		return nResult;
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		if (curAction == Action.ACTION_UPDATE_FEATURE) {
			if (nResult == Result.SUCCESS) {
				ReviewActivity reviewActivity = (ReviewActivity) parent;
				reviewActivity.successUpdateFeature();
			}		
			
		} else if (curAction == Action.ACTION_VOTE) {
			if (nResult == Result.SUCCESS) {
				PubInfoActivity pubInfoActivity = (PubInfoActivity) parent;
				pubInfoActivity.getPubInfo();
				SharedPreferences prefs = parent.getSharedPreferences(General.SHARED_PREFS, 0);
				prefs.edit().putString(String.valueOf(pub.id), GlobalData.currentUser.session_id).commit();
			}	
			
		} else if (curAction.equals(Action.ACTION_GET_PUB_INFO)) {
			if (nResult == Result.SUCCESS) {
				if (activity instanceof PubInfoActivity) {
					((PubInfoActivity) activity).setPercentages();
				}
			
			} else if (nResult == Result.FAIL) {
				if (activity instanceof PubInfoActivity) {
					((PubInfoActivity) activity).removePercentages();
				}			
			}
			
		} else if (curAction.equals(Action.ACTION_GET_PUB_INFO_FROM_FEATURE_COUNTER)) {
			if (nResult == Result.SUCCESS) {
				if (activity instanceof PubInfoActivity) {
					((PubInfoActivity) activity).setPercentages();
				}
			
			} else if (nResult == Result.FAIL) {
				if (activity instanceof PubInfoActivity) {
					((PubInfoActivity) activity).removePercentages();
				}				
			}
			
		} else if (curAction == Action.ACTION_ADD_PUB) {
			if (nResult == Result.SUCCESS) {
				AddPubActivity addPubActivity = (AddPubActivity) parent;
				addPubActivity.successSubmit();
				
			} else if (nResult == PUB_ALEADY_EXIST) {
				AddPubActivity addPubActivity = (AddPubActivity) parent;
				addPubActivity.pubExistAleady();
			}
			
		} else if (curAction == Action.ACTION_ADD_PUB_FORCE) {
			if (nResult == Result.SUCCESS) {
				AddPubActivity addPubActivity = (AddPubActivity) parent;
				addPubActivity.successSubmit();
				
			} else if (nResult == Result.FAIL) {
				AddPubActivity addPubActivity = (AddPubActivity) parent;
				addPubActivity.pubCantBeAdded();
			}
			
		} else if (curAction == Action.ACTION_PUB_RATE) {
			if (nResult == Result.SUCCESS) {
				ReviewActivity critiqueActivity = (ReviewActivity) parent;
				critiqueActivity.successUpdateFeature();
			}
			
//		} else if (curAction == ACTION_UPDATE_FEATURE) {
//			if (nResult == Parser.SUCCESS) {
//				ReviewActivity critiqueActivity = (ReviewActivity) parent;
//				critiqueActivity.successUpdateFeature();
//			}
//			
		} else if (curAction == Action.ACTION_PUB_DETAIL) {
			if (nResult == Result.SUCCESS) {

			}	
			
//		} else if (curAction == Action.ACTION_CHECK_BEFORE_SEARCH_PUB) {
//			if (nResult == Result.SUCCESS) {
//				if (parent instanceof FeatureActivity) {
//					FeatureActivity featureActivity = (FeatureActivity) parent;
//					featureActivity.sucessSearchPub();
//					featureActivity.setButtonEnabled(true);
//
//				} else if (parent instanceof ReviewActivity) { // TODO make this work also
//					ReviewActivity critiqueActivity = (ReviewActivity) parent;
//					critiqueActivity.successSearch();
//
//				} else if (parent instanceof AddPubActivity) { // TODO make this work also
//					AddPubActivity addPubActivity = (AddPubActivity) parent;
//					addPubActivity.successSearch();
//				}
//			}
			
		} else if (curAction == Action.ACTION_SEARCH_PUB) {
			if (nResult == Result.SUCCESS) {
				if (parent instanceof FeatureActivity) {
					FeatureActivity featureActivity = (FeatureActivity) parent;
					featureActivity.sucessSearchPub();					

				} else if (parent instanceof ReviewActivity) {
					ReviewActivity critiqueActivity = (ReviewActivity) parent;
					critiqueActivity.successSearch();

				} else if (parent instanceof AddPubActivity) {
					AddPubActivity addPubActivity = (AddPubActivity) parent;
					addPubActivity.successSearch();
				}
			}
			
			if (parent instanceof FeatureActivity) {
				FeatureActivity featureActivity = (FeatureActivity) parent;
				featureActivity.setButtonEnabled(true);
			}
		}
	}
	
	private void forceAddPub(String strPubName, String strPubType, String strLat, 
			String strLng, String strPubCountry, String strPubCategory, String strPubIcon, 
			String strPubFounder, String strPubFeature) {
		nResult = PubWebService.addPub(strPubName, strPubType, strLat, strLng, strPubCountry, strPubCategory, strPubIcon, strPubFounder);
		if (nResult == Result.SUCCESS) {
			int nAddPubId = PubParser.nAddPubID;
			
			String[] arrStr = strPubFeature.split(",");
			
			for (int i = 0; i < arrStr.length; i++) {
				nResult = PubWebService.addPubFeature(nAddPubId, Integer.parseInt(arrStr[i]));
				if (nResult == Result.FAIL) {
					continue;	// TODO maybe break instead of continue..?
				}
			}
		}
	}
}
