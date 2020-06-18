package com.Hashqi.try_on_client.Page.messagePage;

public class Contacts {
    private String name,message;
    private String imageUrl;
    private Long ID;

    public Contacts(String name, String message, String imageUrl, Long ID) {
        this.name = name;
        this.message = message;
        this.imageUrl = imageUrl;
        this.ID = ID;
    }

    public String getName(){
        return name;
    }
    public String getMessage(){
        return message;
    }
    public String getImageUrl(){
        return imageUrl;
    }

    public Long getID() {
        return ID;
    }
}
