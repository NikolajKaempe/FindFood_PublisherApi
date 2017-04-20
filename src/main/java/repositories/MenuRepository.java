package repositories;

import models.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import repositories.repositoryInterfaces.IMenuRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Kaempe on 28-03-2017.
 */
public class MenuRepository implements IMenuRepository{

    private Sql2o sql2o;

    public MenuRepository(Sql2o sql2o){
        this.sql2o = sql2o;
    }

    @Override
    public Collection<Menu> getAll() {
        Collection<Menu> menus;
        String sql =
                "SELECT menuId, menuName, menuDescription, menuImageFilePath " +
                        "FROM Menus";
        try{
            Connection con = sql2o.open();
            menus = con.createQuery(sql)
                    .executeAndFetch(Menu.class);
            menus.forEach(menu -> menu.setMealType(this.getMealTypeFor(menu.getMenuId())));
            menus.forEach(menu -> menu.setRecipes(this.getRecipesFor(menu.getMenuId())));
        }catch (Exception e)
        {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return menus;
    }

    @Override
    public Menu get(int id) {
        if (!this.exists(id)){
            throw new IllegalArgumentException("No menu found with id " + id);
        }
        Menu menu;
        String sql =
                "SELECT menuId, menuName, menuDescription, menuImageFilePath " +
                        "FROM Menus WHERE menuId = :id";
        try{
            Connection con = sql2o.open();
            menu = con.createQuery(sql)
                    .addParameter("id",id)
                    .executeAndFetchFirst(Menu.class);
            menu.setMealType(this.getMealTypeFor(menu.getMenuId()));
            menu.setRecipes(this.getRecipesFor(menu.getMenuId()));
        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return menu;
    }

    @Override
    public int create(Menu model) {
        int id;
        this.failIfInvalid(model);
        Collection<Integer> ingredientRelations = new HashSet<>();
        Collection<Integer> allergyRelations = new HashSet<>();
        String sql =
                "INSERT INTO Menus (menuName, menuDescription, menuImageFilePath, mealTypeId) " +
                        "VALUES (:menuName, :menuDescription, :menuImageFilePath, :mealTypeId)";

        String sqlRecipeRelations =
                "Insert INTO MenuRecipes (recipeId, menuId) " +
                        "VALUES (:recipeId, :menuId)";

        String sqlGetIngredientRelations =
                "SELECT ingredientId FROM MeasuredIngredients " +
                        "WHERE recipeId = :recipeId";

        String sqlIngredientRelationsToUpdate =
                "INSERT INTO MenuIngredients (ingredientId, menuId) " +
                        "VALUES (:ingredientId, :menuId)";

        String sqlGetAllergyRelations =
                "SELECT allergyId FROM RecipeAllergies " +
                        "WHERE recipeId = :recipeId";

        String sqlAllergyRelationsToUpdate =
                "INSERT INTO MenuAllergies (allergyId, menuId) " +
                        "VALUES (:allergyId, :menuId)";


        try{
            Connection con = sql2o.beginTransaction();
            id = Integer.parseInt(con.createQuery(sql, true)
                    .bind(model)
                    .addParameter("mealTypeId",model.getMealType().getMealTypeId())
                    .executeUpdate().getKey().toString());
            model.getRecipes().forEach(recipe ->
                    con.createQuery(sqlRecipeRelations)
                            .addParameter("recipeId",recipe.getRecipeId())
                            .addParameter("menuId",id)
                            .executeUpdate());
            for (Recipe recipe : model.getRecipes()) {
                ingredientRelations.addAll(
                    con.createQuery(sqlGetIngredientRelations)
                        .addParameter("recipeId",recipe.getRecipeId())
                        .executeAndFetch(Integer.class)
                );
                allergyRelations.addAll(
                    con.createQuery(sqlGetAllergyRelations)
                        .addParameter("recipeId",recipe.getRecipeId())
                        .executeAndFetch(Integer.class)
                );
            }
            for(Integer ingredientId : ingredientRelations){
                con.createQuery(sqlIngredientRelationsToUpdate)
                        .addParameter("ingredientId",ingredientId)
                        .addParameter("menuId",id)
                        .executeUpdate();
            }
            for(Integer allergyId : allergyRelations){
                con.createQuery(sqlIngredientRelationsToUpdate)
                        .addParameter("allergyId",allergyId)
                        .addParameter("menuId",id)
                        .executeUpdate();
            }
            con.commit();
        }catch (Exception e)
        {
            e.printStackTrace();
            return 0;
        }

        return id;
    }

    @Override
    public boolean update(Menu model) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public boolean exists(int id) {
        Menu menu;

        String sql = "SELECT menuId " +
                "FROM Menus " +
                "WHERE menuId = :id";
        try{
            Connection con = sql2o.open();
            menu = con.createQuery(sql)
                    .addParameter("id",id)
                    .executeAndFetchFirst(Menu.class);
            if (menu != null) return true;
            return false;
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void failIfInvalid(Menu menu){
        if ( menu == null)
        {
            throw new IllegalArgumentException("Menu cannot be null");
        }
        if (menu.getMenuName() == null || menu.getMenuName().length() < 1) {
            throw new IllegalArgumentException("Parameter `name` cannot be empty");
        }
        if (menu.getMenuDescription() == null || menu.getMenuDescription().length() < 1) {
            throw new IllegalArgumentException("Parameter `description` cannot be empty");
        }
        if (menu.getMealType() == null ) {
            throw new IllegalArgumentException("Parameter `mealType` cannot be null");
        }
        if (menu.getMealType().getMealTypeId() == 0) {
            throw new IllegalArgumentException("Parameter `mealType` has wrong id");
        }
        if (menu.getRecipes() == null){
            throw new IllegalArgumentException("Menu must contain Recipes");
        }
        if (menu.getRecipes().size() == 0){
            throw new IllegalArgumentException("Menu must contain at least 1 Recipe");
        }
        for (Recipe recipe : menu.getRecipes()){
            if (!this.isRelationValid(recipe.getRecipeId())){
                throw new IllegalArgumentException("Recipe with id " +
                        recipe.getRecipeId() + " dos'ent exist");
            }
        }
    }

    @Override
    public MealType getMealTypeFor(int id){
        MealType mealType;
        String sql =
                "SELECT mealTypeId, mealTypeName " +
                    "FROM MealTypes " +
                        "WHERE mealTypeId IN (" +
                        "SELECT mealTypeId FROM Menus " +
                        "WHERE menuId = :id" +
                        ")";
        try{
            Connection con = sql2o.open();
            mealType = con.createQuery(sql)
                    .addParameter("id",id)
                    .executeAndFetchFirst(MealType.class);
        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        return mealType;
    }

    @Override
    public Collection<Recipe> getRecipesFor(int id){
        Collection<Recipe> recipes;
        String sql =
                "SELECT recipeId, recipeName, recipeDescription, recipeImageFilePath FROM Recipes " +
                        "WHERE recipeId IN (" +
                            "SELECT recipeId FROM MenuRecipes " +
                            "WHERE menuId = :id" +
                        ")";
        try{
            Connection con = sql2o.open();
            recipes = con.createQuery(sql)
                    .addParameter("id",id)
                    .executeAndFetch(Recipe.class);
            recipes.forEach(recipe -> recipe.setRecipeType(getRecipeType(recipe.getRecipeId())));
        }catch (Exception e)
        {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return recipes;
    }

    @Override
    public Collection<Ingredient> getIngredientFor(int id) {
        Collection<Ingredient> ingredients;

        String sql =
                "SELECT ingredientId, ingredientName, ingredientDescription " +
                    "FROM Ingredients " +
                    "WHERE ingredientId IN (" +
                        "SELECT ingredientId FROM MeasuredIngredients " +
                        "WHERE recipeId IN (" +
                            "SELECT recipeId FROM MenuRecipes " +
                            "WHERE menuId = :id))";

        try{
            Connection con = sql2o.open();
            ingredients = con.createQuery(sql)
                    .addParameter("id",id)
                    .executeAndFetch(Ingredient.class);
        }catch (Exception e)
        {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return ingredients;
    }

    @Override
    public Collection<Allergy> getAllergiesFor(int id) {
        Collection<Allergy> allergies;

        String sql =
                "SELECT ingredientId FROM MeasuredIngredients " +
                        "WHERE recipeId IN (" +
                        "SELECT recipeId FROM MenuRecipes " +
                        "WHERE menuId = :id)";

        try{
            Connection con = sql2o.open();
            allergies = con.createQuery(sql)
                    .addParameter("id",id)
                    .executeAndFetch(Allergy.class);
        }catch (Exception e)
        {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return allergies;
    }

    private RecipeType getRecipeType(int recipeId){
        RecipeType recipeType;
        String sql =
                "SELECT recipeTypeId, recipeTypeName " +
                    "FROM RecipeTypes " +
                        "WHERE recipeTypeId IN (" +
                        "SELECT recipeTypeId FROM Recipes " +
                        "WHERE recipeTypeId = :id" +
                        ")";
        try{
            Connection con = sql2o.open();
            recipeType = con.createQuery(sql)
                    .addParameter("id",recipeId)
                    .executeAndFetchFirst(RecipeType.class);
        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        return recipeType;
    }

    public boolean isRelationValid(int relationId){
        int id;
        String sql =
                "SELECT recipeId FROM Recipes " +
                        "WHERE recipeId = :id";
        try{
            Connection con = sql2o.open();
            id = con.createQuery(sql)
                    .addParameter("id",relationId)
                    .executeAndFetchFirst(Integer.class);
            if (id > 0) return true;
            return false;
        }catch (Exception e)
        {
            return false;
        }
    }

}
