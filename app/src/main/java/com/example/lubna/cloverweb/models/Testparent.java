package com.example.lubna.cloverweb.models;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import java.util.List;
import java.util.UUID;
public class Testparent implements ParentObject
{
    private List<Object> childtlist;
    private UUID _id;
    private String title;
    public Testparent(String title)
    {
        this.title = title;
        _id=UUID.randomUUID();
    }
    public UUID get_id()
    {
        return _id;
    }
    public void set_id(UUID _id)
    {
        this._id = _id;
    }
    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }
    @Override
    public List<Object> getChildObjectList()
    {
        return childtlist;
    }
    @Override
    public void setChildObjectList(List<Object> list)
    {
     childtlist=list;
    }
}
