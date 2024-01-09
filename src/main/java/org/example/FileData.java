package org.example;
import java.awt.*;
import java.util.Set;
public class FileData {
    private String text;
    private String type;
    private Set<Color> colors;
    public FileData(String text,String type,Set<Color> colors){
        this.text=text;
        this.type=type;
        this.colors=colors;
    }
    public String getText(){return text;}
    public String getType(){return type;}
    public Set<Color> getColors(){return colors;}
    public void setText(String text){this.text=text;}
    public void setType(String type){this.type=type;}
    public void setColors(Set<Color> colors){this.colors=colors;}
}
