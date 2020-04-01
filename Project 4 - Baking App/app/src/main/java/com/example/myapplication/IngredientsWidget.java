package com.example.myapplication;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidget extends AppWidgetProvider {

    String getAllIngredientsForWidget;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String DishName, String DishIngredients) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
        Intent intent = new Intent(context, MainActivity.class);
        //making an boolean to differenciate between going to MainActivity from Widget Vs. regular startup
        Boolean intentFromWidget = true;
        intent.putExtra("intentFromWidget", true);
        //clearing the back stack to create a new task
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        //open the pending intent if textview is clicked
        views.setOnClickPendingIntent(R.id.widget_dish_ingredient_textview, pendingIntent);
        views.setTextViewText(R.id.widget_dish_name_textview, DishName);
        views.setTextViewText(R.id.widget_dish_ingredient_textview, DishIngredients);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
        Intent intent = new Intent(context, MainActivity.class);
        //making an boolean to differenciate between going to MainActivity from Widget Vs. regular startup
        Boolean intentFromWidget = true;
        intent.putExtra("intentFromWidget", true);
        //clearing the back stack to create a new task
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        //open the pending intent if textview is clicked
        views.setOnClickPendingIntent(R.id.widget_dish_ingredient_textview, pendingIntent);
        views.setOnClickPendingIntent(R.id.widget_dish_ingredient_textview, pendingIntent);
        views.setTextViewText(R.id.widget_dish_name_textview, "Please Click             ");
        views.setTextViewText(R.id.widget_dish_ingredient_textview, "   Here               ");
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void getAllIngredientsAndUpdateWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, List<Ingredient> allIngredients, RecipeDish recipeDish) {
        String WidgetRecipeDish = recipeDish.getName();
        String getAllIngredientsForWidget;
        int count = 1;
        StringBuilder IngredientsBuilder = new StringBuilder();
        for (Ingredient ingredient : allIngredients) {
            IngredientsBuilder.append("Item " + count + ": " + ingredient.getIngredient());
            IngredientsBuilder.append("Quantity: " + ingredient.getQuantity() + " ");
            IngredientsBuilder.append("");
            IngredientsBuilder.append(ingredient.getMeasure());
            IngredientsBuilder.append("\n");
            count++;
        }
        getAllIngredientsForWidget = IngredientsBuilder.toString();
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, WidgetRecipeDish, getAllIngredientsForWidget);
        }
    }
}

