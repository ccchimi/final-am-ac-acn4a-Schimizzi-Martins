package com.app.tasteit;

public class CommunityRecipe {

    private String title;
    private String description;
    private String imageUrl;
    private String cookingTime;
    private String author;

    public CommunityRecipe() {
    }

    public CommunityRecipe(String title, String description, String imageUrl, String cookingTime, String author) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.cookingTime = cookingTime;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCookingTime() {
        return cookingTime;
    }

    public String getAuthor() {
        return author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setCookingTime(String cookingTime) {
        this.cookingTime = cookingTime;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}