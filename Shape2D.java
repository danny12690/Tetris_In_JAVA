/*
This class is responsible for maintaining the different shapes available in the program
to be present as instances. This encourages object oriented approach as well as 
makes the further addition of new shapes easier.
 */


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

/**
 *
 * @author Dhananjay Singh
 * NetId : dxs145530
 * UTD Id: 2021250625
 */
public class Shape2D 
{
    Polygon p;
    Color c;
    Polygon getShape()
    {
        return p;
    }
    public void setShape(Polygon p1)
    {
        p=new Polygon(p1.xpoints,p1.ypoints,p1.npoints);
    }
    public void setColor(Color c)
    {
        this.c=c;
    }
    public Color getColor()
    {
        return c;
    }
}
