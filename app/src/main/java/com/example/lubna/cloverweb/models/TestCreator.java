package com.example.lubna.cloverweb.models;
import android.content.Context;
import java.util.ArrayList;
import java.util.List;
public class TestCreator
{
    static TestCreator _titleCreator;
    List<Testparent> _testparent;
    public TestCreator(Context context)
    {
        _testparent= new ArrayList<>();
        Testparent title= new Testparent(String.format("caller #id"));
        Testparent title1 = new Testparent(String.format("Caller#id1"));
        _testparent.add(title);
        _testparent.add(title1);
    }
    public static TestCreator get(Context context)
    {
        if(_titleCreator ==null)
            _titleCreator = new TestCreator(context);
        return _titleCreator;
    }
    public List<Testparent> getAll()
    {
        return _testparent;
    }
}
