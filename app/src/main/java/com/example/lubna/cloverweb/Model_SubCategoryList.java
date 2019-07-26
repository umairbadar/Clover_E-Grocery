package com.example.lubna.cloverweb;

public class Model_SubCategoryList {

    private String id;
    private String title;
    private String description;
    private String image;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public Model_SubCategoryList(String id, String title, String description, String image) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
    }
}
