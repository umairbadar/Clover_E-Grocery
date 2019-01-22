package com.example.lubna.cloverweb;
import android.widget.Button;
public class Food_Model
{
    private String Name;
    private String Phone;
    private int Photo;
    private Button B1;
    public Food_Model(String name, String phone, int photo,Button b1 )
    {
        Name = name;
        Phone = phone;
        Photo = photo;
        B1= b1;
    }
    public String getName()
    {
        return Name;
    }
    public String getPhone()
    {
        return Phone;
    }
    public int getPhoto()
    {
        return Photo;
    }
    public void setName(String name)
    {
        Name = name;
    }
    public void setPhone(String phone)
    {
        Phone = phone;
    }
    public void setPhoto(int photo)
    {
        Photo = photo;
    }
    public Button getB1()
    {
        return B1;
    }
    public void setB1(Button b1)
    {
        B1 = b1;
    }
}
