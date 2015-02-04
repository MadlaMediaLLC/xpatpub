package com.han.xpatpub.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.han.xpatpub.R;
import com.han.xpatpub.model.Pub;

public class PubInfoFeatureListAdapter extends ArrayAdapter<Item> {

	private Context context;
	private LayoutInflater mInflater;
	private static final int layout_id = R.layout.row_rated_feature;

	public PubInfoFeatureListAdapter(Context context, List<Item> featureList) {
		super(context, layout_id, featureList);
		this.context = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {		
		RelativeLayout newView = (RelativeLayout) convertView;

		if (newView == null) {
			newView = (RelativeLayout) mInflater.inflate(layout_id, null);
		}

		final Item item = getItem(position);
		if (item != null) {
			ImageView icon = (ImageView) newView.findViewById(R.id.ivFeature);
			TextView percentage = (TextView) newView
					.findViewById(R.id.tvFeaturePercentage);
			TextView feature = (TextView) newView.findViewById(R.id.tvFeature);		
			
			if (item.iconResId != 0) {
				icon.setImageDrawable(context.getResources().getDrawable(item.iconResId));
				
			} else {
				icon.setImageResource(android.R.color.transparent);
			}
			
			percentage.setText(item.percentage + "%");
			feature.setText(item.feature);			
		}

		return newView;
	}
	
	// TODO add correct percentages
	public static List<Item> populateList(Context context, Pub curPub, int featureGroup) {
		List<Item> items = new ArrayList<Item>();
		Resources res = context.getResources();
		int sum = 0;

		switch (featureGroup) {
		case Pub.FEATURE_GROUP_ID_ALES:
			sum = curPub.ratingAlesGeneric 
					+ curPub.ratingAlesLocalCraft
					+ curPub.ratingAlesImportCraft;
			items.add(new Item(R.drawable.less_four_sel,
					getPercentageForItem(sum, curPub.ratingAlesGeneric), 
					res.getString(R.string.ales_on_tap_rating_generic)));
			items.add(new Item(R.drawable.four_to_ten_sel,
					getPercentageForItem(sum, curPub.ratingAlesLocalCraft), 
					res.getString(R.string.ales_on_tap_rating_local_craft)));
			items.add(new Item(R.drawable.more_ten_sel,
					getPercentageForItem(sum, curPub.ratingAlesImportCraft),
					res.getString(R.string.ales_on_tap_rating_import_craft)));
			break;

		case Pub.FEATURE_GROUP_ID_FOOD:
			sum = curPub.ratingFoodNone
					+ curPub.ratingFoodNotGood
					+ curPub.ratingFoodAverage
					+ curPub.ratingFoodVeryGood;
			items.add(new Item(0, 
					getPercentageForItem(sum, curPub.ratingFoodNone), 
					res.getString(R.string.food_none)));
			items.add(new Item(0, 
					getPercentageForItem(sum, curPub.ratingFoodNotGood), 
					res.getString(R.string.food_not_good)));
			items.add(new Item(0, 
					getPercentageForItem(sum, curPub.ratingFoodAverage), 
					res.getString(R.string.food_average)));
			items.add(new Item(0, 
					getPercentageForItem(sum, curPub.ratingFoodVeryGood), 
					res.getString(R.string.food_very_good)));
			break;

		case Pub.FEATURE_GROUP_ID_TUNES:
			sum = curPub.ratingTunesRock
					+ curPub.ratingTunesPop
					+ curPub.ratingTunesRNB
					+ curPub.ratingTunesCountry
					+ curPub.ratingTunesMixed;
			items.add(new Item(R.drawable.live_sel,
					getPercentageForItem(sum, curPub.ratingTunesRock), 
					res.getString(R.string.tunes_rating_rock)));
			items.add(new Item(R.drawable.canned_sel,
					getPercentageForItem(sum, curPub.ratingTunesPop), 
					res.getString(R.string.tunes_rating_pop)));
			items.add(new Item(R.drawable.canned_sel,
					getPercentageForItem(sum, curPub.ratingTunesRNB), 
					res.getString(R.string.tunes_rating_rnb)));
			items.add(new Item(R.drawable.canned_sel,
					getPercentageForItem(sum, curPub.ratingTunesCountry), 
					res.getString(R.string.tunes_rating_country)));
			items.add(new Item(R.drawable.both_sel,
					getPercentageForItem(sum, curPub.ratingTunesMixed), 
					res.getString(R.string.tunes_rating_mixed)));
			break;

		case Pub.FEATURE_GROUP_ID_VIBE:
			sum = curPub.ratingVibeLaidback
					+ curPub.ratingVibeJazzy
					+ curPub.ratingVibeRaunchy
					+ curPub.ratingVibeMeetMarket;
			items.add(new Item(R.drawable.laidback_sel,
					getPercentageForItem(sum, curPub.ratingVibeLaidback), 
					res.getString(R.string.vibe_laidback)));
			items.add(new Item(R.drawable.jazzy_sel,
					getPercentageForItem(sum, curPub.ratingVibeJazzy), 
					res.getString(R.string.vibe_jazzy)));
			items.add(new Item(R.drawable.raunchy_sel,
					getPercentageForItem(sum, curPub.ratingVibeRaunchy), 
					res.getString(R.string.vibe_raunchy)));
			items.add(new Item(R.drawable.meat_market_sel,
					getPercentageForItem(sum, curPub.ratingVibeMeetMarket), 
					res.getString(R.string.vibe_meet_market)));
			break;

		default:
			break;
		}

		return items;
	}
	
	public static int getPercentageForItem(int sum, int count) {		
		return (int) ((float) 100 / sum * count);
	}
}
