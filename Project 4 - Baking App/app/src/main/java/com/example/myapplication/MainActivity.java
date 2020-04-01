package com.example.myapplication;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecipeDishAdapter.OnRecipeDishListener {
    public List<RecipeDish> mRecipeDishes;
    public RecipeDish mRecipeDishSingle;
    private RecipeDishAdapter recipeDishAdapter;
    private static final String TAG = "MainActivity";
    private RecyclerView recipeDish_recycleview;
    public List<Step> mStepsList;
    public List<Ingredient> mIngredientsList;
    Boolean intentFromWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recipeDish_recycleview = findViewById(R.id.dish_RV);
        //Setting the title
        setTitle("Select A Dish");
        Intent widgetIntent = getIntent();
        Bundle widgetBundle = widgetIntent.getExtras();

        //TODO handel the widget DetailActivityClick
        RecipeInterface recipeInterface = RecipeClient.getClient().create(RecipeInterface.class);
        Call<List<RecipeDish>> call = recipeInterface.getRecipeDish();

        call.enqueue(new Callback<List<RecipeDish>>() {
            @Override
            public void onResponse(Call<List<RecipeDish>> call, Response<List<RecipeDish>> response) {
                mRecipeDishes = response.body();
                recipeDishAdapter.setRecipeDishList(mRecipeDishes);
            }

            @Override
            public void onFailure(Call<List<RecipeDish>> call, Throwable t) {
            }
        });
        recipeDish_recycleview.setLayoutManager(new LinearLayoutManager(this));
        mRecipeDishes = new ArrayList<>();
        recipeDishAdapter = new RecipeDishAdapter(this, mRecipeDishes, this);
        recipeDish_recycleview.setAdapter(recipeDishAdapter);
    }

    @Override
    public void onRecipeDishClick(int itemclicked) {
        intentFromWidget = getIntent().getBooleanExtra("intentFromWidget", false);

        //Handling the clicks for widget vs. general
        if (intentFromWidget) {
            Intent intent = new Intent(MainActivity.this, IngredientsWidget.class);
            RecipeDish recipeDishWidget = recipeDishAdapter.getmRecipeDishList().get(itemclicked);
            intent.putExtra("dishName", recipeDishWidget);
            Bundle bundle = new Bundle();
            bundle.putSerializable("DishFromWidgetBundle", recipeDishWidget);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            //Getting an instance for the AppWidgetManager
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            //Getting all the Id's
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientsWidget.class));
            //Setting values in the static method created in IngredientWidget provider class
            IngredientsWidget.getAllIngredientsAndUpdateWidget(this, appWidgetManager, appWidgetIds, recipeDishWidget.getIngredients(), recipeDishWidget);
            this.finishAffinity();
        } else {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            RecipeDish recipeDish = recipeDishAdapter.getmRecipeDishList().get(itemclicked);
            intent.putExtra("Dish", recipeDish);
            startActivity(intent);
        }
    }
}
