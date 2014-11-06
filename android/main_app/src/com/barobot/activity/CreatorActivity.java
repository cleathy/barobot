package com.barobot.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.barobot.BarobotMain;
import com.barobot.R;
import com.barobot.common.Initiator;
import com.barobot.gui.database.BarobotData;
import com.barobot.gui.dataobjects.Engine;
import com.barobot.gui.dataobjects.Ingredient_t;
import com.barobot.gui.dataobjects.Liquid_t;
import com.barobot.gui.dataobjects.Recipe_t;
import com.barobot.gui.dataobjects.Slot;
import com.barobot.gui.fragment.IngredientListFragment;
import com.barobot.gui.fragment.RecipeAttributesFragment;
import com.barobot.gui.utils.Distillery;
import com.barobot.gui.utils.LangTool;
import com.barobot.hardware.Arduino;
import com.barobot.hardware.devices.BarobotConnector;
import com.barobot.other.InternetHelpers;

//    and_oid:ba_ckground="@drawable/background"

public class CreatorActivity extends BarobotMain{
	private int[] slot_nums = {0,0,0,0,0,0,0,0,0,0,0,0,0};
	private int[] ids;
	private int[] drops;
	private int[] dropIds;
	private List<Ingredient_t> ingredients;
	private int drink_size = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creator);
		ingredients = new ArrayList<Ingredient_t>();
	}
	@Override
	protected void onResume() {
		super.onResume();
		UpdateData();
		setFullScreen();
	}

	private void UpdateData(){
		SetupBottles();
		SetupDrops();
		UpdateSlots();	
		UpdateButtons();
	}
	private void SetupBottles()
	{
		ids = new int[13];
		ids[1] = R.id.bottle_button1;
		ids[2] = R.id.bottle_button2;
		ids[3] = R.id.bottle_button3;
		ids[4] = R.id.bottle_button4;
		ids[5] = R.id.bottle_button5;
		ids[6] = R.id.bottle_button6;
		ids[7] = R.id.bottle_button7;
		ids[8] = R.id.bottle_button8;
		ids[9] = R.id.bottle_button9;
		ids[10] = R.id.bottle_button10;
		ids[11] = R.id.bottle_button11;
		ids[12] = R.id.bottle_button12;
	}
	private void SetupDrops()
	{
		drops = new int[13];
		for(int idx=1; idx <= 12 ; idx++)
		{
			drops[idx] = 0;
		}
		dropIds = new int[6];
		dropIds[0] = 0;
		dropIds[1] = R.drawable.drop_1;
		dropIds[2] = R.drawable.drop_2;
		dropIds[3] = R.drawable.drop_3;
		dropIds[4] = R.drawable.drop_4;
		dropIds[5] = R.drawable.drop_5;
	}

	private void UpdateButtons() {
		TextView drinkSizeBox		= (TextView) findViewById(R.id.drink_size);
		Button save_as_drink		= (Button) findViewById(R.id.save_as_drink);
		Button clear_list			= (Button) findViewById(R.id.clear_list);
		Button start_btn			= (Button) findViewById(R.id.creator_pour_button);
		IngredientListFragment frag = (IngredientListFragment) getFragmentManager().findFragmentById(R.id.fragment_ingredient_list);
		RecipeAttributesFragment attrFrag = (RecipeAttributesFragment) getFragmentManager().findFragmentById(R.id.fragment_attributes);

		if(drink_size > 0 ){
			drinkSizeBox.setVisibility(View.VISIBLE);
			save_as_drink.setVisibility(View.VISIBLE);
			clear_list.setVisibility(View.VISIBLE);
			drinkSizeBox.setText("" + drink_size + "ml");
			start_btn.setVisibility(View.VISIBLE);
		}else{
			drinkSizeBox.setVisibility(View.INVISIBLE);
			save_as_drink.setVisibility(View.INVISIBLE);
			clear_list.setVisibility(View.INVISIBLE);
			start_btn.setVisibility(View.INVISIBLE);
			if(frag!=null){
				frag.hide();
			}
			attrFrag.hide();
		}
	}

	private void UpdateSlots() {
		List<Slot> bottles = Engine.GetInstance().loadSlots();
		Log.w("BOTTLE_SETUP length",""+bottles.size());
		for(Slot slot : bottles)
		{
			if (slot.position > 0 && slot.position <= ids.length ){
				TextView tview = (TextView) findViewById(ids[slot.position]);
				if (slot.status == Slot.STATUS_EMPTY || slot.product == null || slot.getName().equals("Empty")) {
					tview.setText("");	
					tview.setEnabled(false);
				} else {
					tview.setEnabled(true);
			//		tview.setText( slot.getName() + "\n\n\n+"+ slot.dispenser_type +"ml");	
					tview.setText( slot.getName());	
				}	
			}
		}
		IngredientListFragment frag = (IngredientListFragment) getFragmentManager().findFragmentById(R.id.fragment_ingredient_list);
		if(frag!=null){
			frag.hide();
		}
		BarobotConnector barobot = Arduino.getInstance().barobot;
		barobot.lightManager.turnOffLeds(barobot.main_queue);
	}

	public void ShowIngredients(boolean setLeds )
	{
		BarobotConnector barobot	= Arduino.getInstance().barobot;
		IngredientListFragment frag = (IngredientListFragment) getFragmentManager().findFragmentById(R.id.fragment_ingredient_list);
		if(frag!=null){
			if(ingredients.size() > 0 ){
				frag.ShowIngredients(ingredients);	
			}else{
				frag.hide();
			}
		}
		RecipeAttributesFragment attrFrag = (RecipeAttributesFragment) getFragmentManager().findFragmentById(R.id.fragment_attributes);
		if(attrFrag!=null){
			attrFrag.SetAttributes(Distillery.getSweet(ingredients), Distillery.getSour(ingredients)
				, Distillery.getBitter(ingredients), Distillery.getStrength(ingredients));
		}
		for (int i = 1; i<=12 ; i++){							// 1 - 12
			barobot.bottleBacklight( barobot.main_queue, i-1, slot_nums[i] );		// 0 -11
		}
	}

	public void onBottleClicked(View view)
	{
		int viewID = view.getId();
		int position = 0;
		for (int i = 1; i<=12 ; i++)
		{
			if (viewID == ids[i])
			{
				position = i;
				break;
			}
		}
		if (position != 0)
		{
			Slot slot =  BarobotData.GetSlot(position);
			try {
				if (slot.product != null){
					Ingredient_t ingredient = new Ingredient_t();
					ingredient.liquid = slot.product.liquid;
					ingredient.quantity = slot.dispenser_type;
					addIngredient(position, ingredient);
					drink_size += slot.dispenser_type;

				}
			} catch (NullPointerException e) {
				InternetHelpers.raportError(lastException, "NullPointerException");
				e.printStackTrace();
			}
			
			ShowIngredients(true);
			CalculateDrops();
			UpdateButtons();
		}else{
			Log.w("BOTTLE_SETUP", "onBottleClicked called by an unknown view");
		}
	}
	
	public void onClearButtonClicked(View view)
	{
		clear(true);
	}

	private void clear( boolean setLeds ){
		ingredients.clear();
		drink_size = 0;
		for (int i = 1; i<=12 ; i++){
			slot_nums[i] = 0;
		}
		if(setLeds){
			BarobotConnector barobot = Arduino.getInstance().barobot;
			barobot.lightManager.setAllLeds(barobot.main_queue, "ff", 5, 5, 5, 5);	// disable all leds
			ShowIngredients(setLeds);
		}
		runOnUiThread(new Runnable() {  
             @Override
             public void run() {
     			UpdateButtons();
            	CalculateDrops();
             }});
	}

	public void onAddRecipeButtonClicked (View view)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

	    LayoutInflater inflater = getLayoutInflater();
	    final View dialogView = inflater.inflate(R.layout.dialog_add_recipe, null);
	    
	    builder.setView(dialogView)
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		})
		.setPositiveButton("Add", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				TextView nameView = (TextView) dialogView.findViewById(R.id.recipe_name);
				String name = nameView.getText().toString();
				CreateDrink(name, true);	
			}
		});
	    AlertDialog ad = builder.create();
	    ad.show();
	}

	public void pourStart() {
		//final Button xb2 = (Button) this.findViewById(R.id.choose_pour_button);
		//final Button xb2 = (Button) this.findViewById(R.id.creator_pour_button);
		//xb2.setEnabled(false);
		final ProgressDialog progress = new ProgressDialog(this);
		progress.setTitle("Preparing drink...");
		progress.setMessage("Please wait");
		progress.show();

		Thread t = new Thread(new Runnable() {  
	         @Override
	         public void run() {
		        	final Recipe_t tempRecipe = CreateDrink("Unnamed Drink", false);
		     		//xb2.setEnabled(false);
		     		Thread t = new Thread(new Runnable() {
		                 @Override
		                 public void run() {
		                 	Engine.GetInstance().Pour(tempRecipe, "creator");
		                 	clear(false);
		                 }});
		     		t.start();
		    //    	wt.setReady();
		        	progress.dismiss();
		        	gotoMainMenu(null);
	         }});
		t.start();
	}

	/*
		runOnUiThread(new Runnable() {
 			@Override
 			public void run() {
//  				xb2.setEnabled(true);
 			}
 		});
	*/

	public void onPourRecipeButtonClicked (View view){
		pourStart();
		/*
		new AlertDialog.Builder(this)
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setTitle("")
		.setMessage("Prosze wstawić szklankę z lodem i wcisnąć START")
		.setPositiveButton("START",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,int which) {
						pourStart();
					}
				}).setNegativeButton("Anuluj", null).show();*/
	}

	private void addIngredient(int position, Ingredient_t ing)
	{
		slot_nums[position]++;
		Ingredient_t existing = findIngredient(ing.liquid);
		if (existing == null){
			ingredients.add(ing);
		}else{
			existing.quantity += ing.quantity;
		}
	}
	
	Ingredient_t findIngredient(Liquid_t liquid)
	{
		for(Ingredient_t ing : ingredients)
		{
			if (ing.liquid.id == liquid.id)
			{
				return ing;
			}
		}
		return null;
	}

	public void CalculateDrops()
	{
		SetupDrops();
		Initiator.logger.i("CalculateDrops size ingredients : ", ""+ingredients.size() );
		List<Integer> sequence = Engine.GetInstance().GenerateSequence(ingredients);
		if(sequence == null){
			Initiator.logger.e("CalculateDrops sequence", "size null" );
			return;
		}

		for(Integer num : sequence) {
			drops[num]++;
		}

		for (int idx=1; idx <= 12 ; idx ++)
		{
			TextView tview = (TextView) findViewById(ids[idx]);
			int counter = drops[idx];
			if (counter > 5) counter = 5;
			
			for(Drawable myOldDrawable : tview.getCompoundDrawables())
			{
				if (myOldDrawable != null) {
					myOldDrawable.setCallback(null);
				}
			}
			tview.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, dropIds[counter]);
		}
	}

	private Recipe_t CreateDrink(String name, Boolean showOnList)
	{
		Recipe_t recipe = new Recipe_t();
		recipe.name = name;
		recipe.unlisted = !showOnList;
		//if(showOnList){
			recipe.insert();
			Engine ee = Engine.GetInstance();
			ee.addRecipe(recipe, ingredients);
			if(showOnList){
				ee.invalidateData();
				LangTool.InsertTranslation(recipe.id, "recipe", name);
			}
		//}
		return recipe;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	public void gotoMainMenu(View view){
		this.finish();
		overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
	}
	
/*
	public void onQueueFinished() {
		clear();
		new AlertDialog.Builder(this)
	    .setTitle("Success!")
	    .setMessage("Finished pouring")
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // continue with delete
	        }
	     })
	    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // do nothing
	        }
	     })
	     .show();
	}
*/
}
