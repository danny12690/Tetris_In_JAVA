/*

The below log is related to the requirements in the previous assignment.
Changes are mentioned in the ChangeLog section.


This is the class to conduct basic tetris operations like Falling of shapes,
Translations and rotations (both clock-wise and anti-clockwise).
A shape if collides with an already stopped shape or when reaching the bottom
stops moving as well as no more rotations are possible.
The interface has the following parts :

1> The motion/fall of objects stop when the mouse pointer hovers over the game
frame and a pause notification appears.
2> Left click moves the object left by one unit. 
3> Right click moves the object right by one unit.
4> The mouse wheel controls the up rotations in clockwise and counter clockwise directions/
5> The scores and level will be displayed on the right as well as the next shape that will fall.

Note : In the program I use shapes[i-5] to access/translate a shape because I am using the same class
Shape2D.java to create two copies for each shape. One to be the playable shape and the other to appear on
the next shape region.



ChangeLog : 
The methods namely lineFilled(), clearRegistered() are added so as to map the registered 
shapes as well as to check if a line is completed.

The method showGameOver() is used to display the game over screen as well as the user's score

Member variables lines,score and level are introduced to keep track of the user's progress.

The speed, scoring factor and size of the objects can be adjusted from the home screen.

The method minRegisteredNearTop() is to check if the already fallen shapes have piled up to the top.
If so, the game over screen is shown and the user can restart the game.

If a user progresses a level , the speed of falling objects is increased by 1 speed point
 */

/**
 *
 * @author Dhananjay Singh
 * NetId : dxs145530
 * UTD Id: 2021250625
 */
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
//Initialize the Game Window//
public class ITetris extends Frame
{
    public ITetris(float speed,int lines,int size,int scoreFactor)
    {
        super("Tetris_Demo");
        addWindowListener(new WindowAdapter(){public void windowClosing(WindowEvent e){System.exit(0);}});
        setSize(450+(size*60),400+(size*60));
        add("Center", new Game(speed,lines,size,scoreFactor));
        show();
    }
}
class Game extends Canvas
{
    Vector registered=new Vector(); //This keeps track of all fallen objects.//
    Vector registeredColor=new Vector(); // This keeps track of all colors of fallen objects.//
    Vector registeredLocation=new Vector(); //This keeps track of the loacation of all fallen objects.//
    float S=0.0f; //The speed Factor.//
    int N=0; //The lines required to clear per level//
    int M=0; //The scoring factor //
    int size=0; //To adjust size of the shapes//
    int level=1; //To map the level change when comleted lines reach the set limit//
    int score=0; //To map the score //
    int lines=0; //To map the number of completed lines//
    int maxX,maxY,minMaxXY,centerX,centerY;
    float xP,yP,xP1,yP1,xP2,yP2;
    int button;
    int k=0;
    //The following variables keep track/randomly select a shape from available set of shapes.//
    int shapeSelector=0; 
    int nextShapeSelector;
    int nextShapeSelector2;
    //These keep track of the mouse wheel direction as well as the rotations since each object can only be rotated by 90 degrees//
    int clock=0;
    int counterClock=0;
    double direction;
    int left,right,top,bottom,xMiddle,yMiddle,leg;
    float pixelSize,rWidth=10.0F, rHeight=10.0F;
    int pause=0;
    /*0 ------ Square
      1 ------ Big triangle
      2 ------ Right triangle
      3 ------ Left trapeziod
      4 ------ Right trapezoid
      5 to 9 -- Copies to be displayed on next shape region
    */
    Shape2D shapes[];
    Game()
    {
        //Doing this ensures easier inclusion of new shapes without much change in the program//
        shapes=new Shape2D[10];
        shapes[0]=new Shape2D();
        shapes[1]=new Shape2D();
        shapes[2]=new Shape2D();
        shapes[3]=new Shape2D();
        shapes[4]=new Shape2D();
        shapes[5]=new Shape2D();
        shapes[6]=new Shape2D();
        shapes[7]=new Shape2D();
        shapes[8]=new Shape2D();
        shapes[9]=new Shape2D();
        //Tracks mouse button clicks//
        addMouseListener(new MouseAdapter()
                {
                    public void mousePressed(MouseEvent evt)
                    {
                        xP=fx(evt.getX());
                        yP=fy(evt.getY());
                        button=evt.getButton();
                        repaint();
                    }
                });
        //Tracks mouse wheel motion//
        addMouseWheelListener(new MouseAdapter()
                {
                    public void mouseWheelMoved(MouseWheelEvent e)
                    {
                        direction=e.getPreciseWheelRotation();// -ive for up ; +ive for down//
                        xP2=fx(e.getX());
                        yP2=fy(e.getY());
                        repaint();
                    }
                });
        //Tracks position of pointer when mouse is in hovering mode//
        addMouseMotionListener(new MouseAdapter()
                {
                    public void mouseMoved( MouseEvent evt)
                    {
                        xP1=fx(evt.getX());
                        yP1=fy(evt.getY());
                        repaint();
                    }
                });
    }
    public Game(float speed,int lines,int size, int scoreFactor)
    {
        //Doing this ensures easier inclusion of new shapes without much change in the program//
        shapes=new Shape2D[10];
        shapes[0]=new Shape2D();
        shapes[1]=new Shape2D();
        shapes[2]=new Shape2D();
        shapes[3]=new Shape2D();
        shapes[4]=new Shape2D();
        shapes[5]=new Shape2D();
        shapes[6]=new Shape2D();
        shapes[7]=new Shape2D();
        shapes[8]=new Shape2D();
        shapes[9]=new Shape2D();
        //Tracks mouse button clicks//
        addMouseListener(new MouseAdapter()
                {
                    public void mousePressed(MouseEvent evt)
                    {
                        xP=fx(evt.getX());
                        yP=fy(evt.getY());
                        button=evt.getButton();
                        repaint();
                    }
                });
        //Tracks mouse wheel motion//
        addMouseWheelListener(new MouseAdapter()
                {
                    public void mouseWheelMoved(MouseWheelEvent e)
                    {
                        direction=e.getPreciseWheelRotation();// -ive for up ; +ive for down//
                        xP2=fx(e.getX());
                        yP2=fy(e.getY());
                        repaint();
                    }
                });
        //Tracks position of pointer when mouse is in hovering mode//
        addMouseMotionListener(new MouseAdapter()
                {
                    public void mouseMoved( MouseEvent evt)
                    {
                        xP1=fx(evt.getX());
                        yP1=fy(evt.getY());
                        repaint();
                    }
                });
    
        S=speed;
        N=lines;
        this.size=size;
        M=scoreFactor;
    }
    
    //Intialize the window/frame properties. //
    void initgr()
    {
        Dimension d=getSize();
        maxX=d.width-1;
        maxY=d.height-1;
        pixelSize=Math.max(rWidth/maxX,rHeight/maxY);
        centerX=maxX/2;
        centerY=maxY/2;
        left = iX(-rWidth/2); right = iX(rWidth/2);
        bottom = iY(-rHeight/2); top = iY(rHeight/2);
        xMiddle = iX(0); yMiddle = iY(0);
        leg=(xMiddle-left)/5;
        
        //To set the shapes coordinates when the program starts for the first time//
        if(shapeSelector==0)
        {
        setShapes(left,leg,top);
        shapeSelector=1;
        
        //Randomly select the falling shape and the next shape//
        nextShapeSelector=5+(int)(Math.random()*((9-5)+1));
        nextShapeSelector2=5+(int)(Math.random()*((9-5)+1));
        }
    }
    
    //to set the coordinates of vertices of the shapes as well as the color to be filled in each//
    void setShapes(int left,int leg,int top)
    {
        Polygon p;
        int x1[]=new int[4];
        int y1[]=new int[4];
        int n=0;
        
        //SQUARE//
        x1[0]=left+2*leg;
        x1[1]=left+3*leg;
        x1[2]=left+3*leg;
        x1[3]=left+2*leg;
        y1[0]=top;
        y1[1]=top;
        y1[3]=top+leg;
        y1[2]=top+leg;
        n=4;
        p=new Polygon(x1,y1,n);
        shapes[0].setShape(p);
        shapes[0].setColor(Color.GREEN);
        
        //ISOSCELES TRIANGLE//
        x1[0]=left+2*leg;
        x1[1]=left+leg;
        x1[2]=left+3*leg;
        y1[0]=top;
        y1[1]=top+leg;
        y1[2]=top+leg;
        n=3;
        p=new Polygon(x1,y1,n);
        shapes[1].setShape(p);
        shapes[1].setColor(Color.YELLOW);
        
        //RIGHT TRANGLE//
        x1[0]=left+2*leg;
        x1[1]=left+leg;
        x1[2]=left+2*leg;
        y1[0]=top;
        y1[1]=top+leg;
        y1[2]=top+leg;
        n=3;
        p=new Polygon(x1,y1,n);
        shapes[2].setShape(p);
        shapes[2].setColor(Color.BLUE);
        
        //PARALLELOGRAM 1//
        x1[0]=left+2*leg;
        x1[1]=left+3*leg;
        x1[2]=left+2*leg;
        x1[3]=left+leg;
        y1[0]=top;
        y1[1]=top;
        y1[2]=top+leg;
        y1[3]=top+leg;
        n=4;
        p=new Polygon(x1,y1,n);
        shapes[3].setShape(p);
        shapes[3].setColor(Color.MAGENTA);
        
        //PARALLELOGRAM 2//
        x1[0]=left+2*leg;
        x1[1]=left+3*leg;
        x1[2]=left+4*leg;
        x1[3]=left+3*leg;
        y1[0]=top;
        y1[1]=top;
        y1[2]=top+leg;
        y1[3]=top+leg;
        n=4;
        p=new Polygon(x1,y1,n);
        shapes[4].setShape(p);
        shapes[4].setColor(Color.red);
        
        //CREATE COPIES OF THE ABOVE SHAPES IN THE SAME ORDER//
        x1[0]=left+7*leg;
        x1[1]=left+8*leg;
        x1[2]=left+8*leg;
        x1[3]=left+7*leg;
        y1[0]=top;
        y1[1]=top;
        y1[3]=top+leg;
        y1[2]=top+leg;
        n=4;
        p=new Polygon(x1,y1,n);
        shapes[5].setShape(p);
        shapes[5].setColor(Color.GREEN);
        x1[0]=left+7*leg;
        x1[1]=left+6*leg;
        x1[2]=left+8*leg;
        y1[0]=top;
        y1[1]=top+leg;
        y1[2]=top+leg;
        n=3;
        p=new Polygon(x1,y1,n);
        shapes[6].setShape(p);
        shapes[6].setColor(Color.yellow);
        x1[0]=left+7*leg;
        x1[1]=left+6*leg;
        x1[2]=left+7*leg;
        y1[0]=top;
        y1[1]=top+leg;
        y1[2]=top+leg;
        n=3;
        p=new Polygon(x1,y1,n);
        shapes[7].setShape(p);
        shapes[7].setColor(Color.BLUE);
        x1[0]=left+7*leg;
        x1[1]=left+8*leg;
        x1[2]=left+7*leg;
        x1[3]=left+6*leg;
        y1[0]=top+leg;
        y1[1]=top+leg;
        y1[2]=top+2*leg;
        y1[3]=top+2*leg;
        n=4;
        p=new Polygon(x1,y1,n);
        shapes[8].setShape(p);
        shapes[8].setColor(Color.MAGENTA);
        x1[0]=left+8*leg;
        x1[1]=left+9*leg;
        x1[2]=left+10*leg;
        x1[3]=left+9*leg;
        y1[0]=top+leg;
        y1[1]=top+leg;
        y1[2]=top+2*leg;
        y1[3]=top+2*leg;
        n=4;
        p=new Polygon(x1,y1,n);
        shapes[9].setShape(p);
        shapes[9].setColor(Color.red);
    }
    int iX(float x){return Math.round(centerX+x/pixelSize);}
    int iY(float y){return Math.round(centerY-y/pixelSize);}
    float fx(int X){return (float)(X-centerX)*pixelSize;}
    float fy(int Y){return (float)(centerY-Y)*pixelSize;}
    
    //To return the vector that stores all the fallen shapes in the order they have fallen//
    public Vector getRegistered()
    {
        return registered;
    }
    
    //This method is responsible for mapping collisions between fallen objects and the falling object//
    /*
    This method compares the median of each falling figure to the median of the fallen figures once
    the bottom part reaches close to the top of registered shapes that have already fallen.
    
    This can be calculated by finding out the maximum of x and y coorinates in each figure.
    */
    public boolean collision(int i)
    {
        Vector collider=new Vector();
        int maxX=Integer.MIN_VALUE;
        int minX=Integer.MAX_VALUE;
        int flag=0;
        int n=0;
        Polygon p=new Polygon(shapes[i].getShape().xpoints,shapes[i].getShape().ypoints,shapes[i].getShape().npoints);
        int x[]=new int[shapes[i].getShape().npoints];
        int y[]=new int[shapes[i].getShape().npoints];
        x=p.xpoints;
        y=p.ypoints;
        n=p.npoints;
        for(int m1=0;m1<n;m1++)
            {
                if(x[m1]>maxX)
                    maxX=x[m1];
                if(x[m1]<minX)
                    minX=x[m1];
            }
        collider=getRegistered();
        for(int m=0;m<collider.size();m++)
        {
            int maxX1=Integer.MIN_VALUE;
            int minX1=Integer.MAX_VALUE;
            Polygon p1=(Polygon)collider.elementAt(m);
            int x1[]=new int[p1.npoints];
            int y1[]=new int[p1.npoints];
            int n1=0;
            x1=p1.xpoints;
            y1=p1.ypoints;
            n1=p1.npoints;
            for(int m1=0;m1<n1;m1++)
            {
                if(x1[m1]>maxX1)
                    maxX1=x1[m1];
                if(x1[m1]<minX1)
                    minX1=x1[m1];
            }
            if(y[2]==y1[0])
            {
                if((n1==4)&&(x1[0]==x1[2]||x1[1]==x1[3]))
                {
                if(Math.abs((maxX+minX)/2-(maxX1+minX1)/2)<=leg)
                    flag=1;
                }
                else if((n1==3)&&n==3)
                {
                if(Math.abs((maxX+minX)/2-(maxX1+minX1)/2)<=leg)
                    flag=1;
                }
                else if((n1==3)&&n==4)
                {
                if(Math.abs((maxX+minX)/2-(maxX1+minX1)/2)<leg)
                    flag=1;
                }
                else
                {
                    if(Math.abs((maxX+minX)/2-(maxX1+minX1)/2)<leg)
                    flag=1;
                }
            }
        }
        if(flag==1)
            return true;
        else
            return false;
    }
    
    /*To move shape left when the left mouse button is clicked.
    This is just a simple translation by reducing the x coordinate of each vertex by one game unit
    that is " LEG " 
    */ 
    public void moveShapeLeft(int i)
    {
        int flag=0;
        int n=0;
        Polygon p=new Polygon(shapes[i-5].getShape().xpoints,shapes[i-5].getShape().ypoints,shapes[i-5].getShape().npoints);
        int x[]=new int[shapes[i-5].getShape().npoints];
        int y[]=new int[shapes[i-5].getShape().npoints];
        x=p.xpoints;
        y=p.ypoints;
        n=p.npoints;
        for(int j=0;j<shapes[i-5].getShape().npoints;j++)
        {
            if(x[j]==left||collision(i-5))
            {
                clock=0;
                counterClock=0;
                flag=1;
            }
        }
        if(flag==1)
        {
            x=x;
            y=y;
        }
        else
        {
            for(int j=0;j<shapes[i-5].getShape().npoints;j++)
                x[j]=x[j]-leg;
        }
        
        //Since this is the falling polygon , The new coordinates will have to be reflected on the original instance//
        //Once collision occurs , the shape is reset to its original form//
        p=new Polygon(x,y,n);
        shapes[i-5].setShape(p);
    }
    
    /*
    To move shape right when the right mouse button is clicked.
    This is just a simple translation by increasing the x coordinate of each vertex by one game unit
    that is " LEG "
    */
    public void moveShapeRight(int i)
    {
        int flag=0;
        int n=0;
        Polygon p=new Polygon(shapes[i-5].getShape().xpoints,shapes[i-5].getShape().ypoints,shapes[i-5].getShape().npoints);
        int x[]=new int[shapes[i-5].getShape().npoints];
        int y[]=new int[shapes[i-5].getShape().npoints];
        x=p.xpoints;
        y=p.ypoints;
        n=p.npoints;
        for(int j=0;j<shapes[i-5].getShape().npoints;j++)
        {
            if(x[j]==left+5*leg||collision(i-5))
            {
                clock=0;
                counterClock=0;
                flag=1;
            }
        }
        if(flag==1)
        {
            x=x;
            y=y;
        }
        else
        {
            for(int j=0;j<shapes[i-5].getShape().npoints;j++)
                x[j]=x[j]+leg;
        }
        
        //Since this is the falling polygon , The new coordinates will have to be reflected on the original instance//
        //Once collision occurs , the shape is reset to its original form//
        p=new Polygon(x,y,n);
        shapes[i-5].setShape(p);
    }
    
    /*
    To rotate the shapes by changing the coordinates as per the shape. Transformation matrix
    was used initially but due to slight error inclusions the shape was getting deformed 
    over a certain number of rotations.
    */ 
    public void rotateCounterClock(int i)
    {
        Polygon p=new Polygon(shapes[i-5].getShape().xpoints,shapes[i-5].getShape().ypoints,shapes[i-5].getShape().npoints);
        int x[]=new int[shapes[i-5].getShape().npoints];
        int y[]=new int[shapes[i-5].getShape().npoints];
        int n=0;
        x=p.xpoints;
        y=p.ypoints;
        n=p.npoints;
        if((i-5)==1)
        {
            if(counterClock==0)
            {
                counterClock++;
            }
            if(counterClock==1)
            {
                x[0]=x[0];x[1]=x[1];x[2]=x[2]-leg;
                y[0]=y[0];y[1]=y[1];y[2]=y[2]+leg;
                counterClock++;
            }
            else if(counterClock==2)
            {
                x[0]=x[0]-leg;x[1]=x[1]+leg;x[2]=x[2]+leg;
                y[0]=y[0]+leg;y[1]=y[1]+leg;y[2]=y[2]-leg;
                counterClock++;
            }
            else if(counterClock==3)
            {
                x[0]=x[0]+leg;x[1]=x[1];x[2]=x[2];
                y[0]=y[0]-leg;y[1]=y[1];y[2]=y[2];
                counterClock++;
            }
            else if(counterClock==4)
            {
                x[0]=x[0];x[1]=x[1]-leg;x[2]=x[2];
                y[0]=y[0];y[1]=y[1]-leg;y[2]=y[2];
                counterClock=0;
            }
        }
        else if((i-5)==2)
        {
            if(counterClock==0)
            {
                counterClock++;
            }
            if(counterClock==1)
            {
                x[0]=x[0];x[1]=x[1];x[2]=x[2];
                y[0]=y[0];y[1]=y[1]-leg;y[2]=y[2];
                counterClock++;
            }
            else if(counterClock==2)
            {
                x[0]=x[0];x[1]=x[1];x[2]=x[2]-leg;
                y[0]=y[0];y[1]=y[1];y[2]=y[2];
                counterClock++;
            }
            else if(counterClock==3)
            {
                x[0]=x[0]-leg;x[1]=x[1];x[2]=x[2]+leg;
                y[0]=y[0];y[1]=y[1]+leg;y[2]=y[2];
                counterClock++;
            }
            else if(counterClock==4)
            {
                x[0]=x[0]+leg;x[1]=x[1];x[2]=x[2];
                y[0]=y[0];y[1]=y[1];y[2]=y[2];
                counterClock=0;
            }
        }
        else if((i-5)==3)
        {
            if(counterClock==0)
            {
                counterClock++;
            }
            if(counterClock==1)
            {
                x[0]=x[0]-leg;x[1]=x[1]-leg;x[2]=x[2]+leg;x[3]=x[3]+leg;
                y[0]=y[0];y[1]=y[1];y[2]=y[2];y[3]=y[3];
                counterClock++;
            }
            else if(counterClock==2)
            {
                x[0]=x[0]+leg;x[1]=x[1]+leg;x[2]=x[2]-leg;x[3]=x[3]-leg;
                y[0]=y[0];y[1]=y[1];y[2]=y[2];y[3]=y[3];
                counterClock++;
            }
            else if(counterClock==3)
            {
                x[0]=x[0]-leg;x[1]=x[1]-leg;x[2]=x[2]+leg;x[3]=x[3]+leg;
                y[0]=y[0];y[1]=y[1];y[2]=y[2];y[3]=y[3];
                counterClock++;
            }
            else if(counterClock==4)
            {
                x[0]=x[0]+leg;x[1]=x[1]+leg;x[2]=x[2]-leg;x[3]=x[3]-leg;
                y[0]=y[0];y[1]=y[1];y[2]=y[2];y[3]=y[3];
                counterClock=0;
            }
        }
        else if((i-5)==4)
        {
            if(counterClock==0)
            {
                counterClock++;
            }
            if(counterClock==1)
            {
                x[0]=x[0]+leg;x[1]=x[1]+leg;x[2]=x[2]-leg;x[3]=x[3]-leg;
                y[0]=y[0];y[1]=y[1];y[2]=y[2];
                counterClock++;
            }
            else if(counterClock==2)
            {
                x[0]=x[0]-leg;x[1]=x[1]-leg;x[2]=x[2]+leg;x[3]=x[3]+leg;
                y[0]=y[0];y[1]=y[1];y[2]=y[2];
                counterClock++;
            }
            else if(counterClock==3)
            {
                x[0]=x[0]+leg;x[1]=x[1]+leg;x[2]=x[2]-leg;x[3]=x[3]-leg;
                y[0]=y[0];y[1]=y[1];y[2]=y[2];
                counterClock++;
            }
            else if(counterClock==4)
            {
                x[0]=x[0]-leg;x[1]=x[1]-leg;x[2]=x[2]+leg;x[3]=x[3]+leg;
                y[0]=y[0];y[1]=y[1];y[2]=y[2];
                counterClock=0;
            }
        }
        
        //Since this is the falling polygon , The new coordinates will have to be reflected on the original instance//
        //Once collision occurs , the shape is reset to its original form//
        p=new Polygon(x,y,n);
        shapes[i-5].setShape(p);
    }
    
    /*
    To rotate the shapes by changing the coordinates as per the shape and as per . Counter clockwise
    rotations that have already taken place. Transformation matrix
    was used initially but due to slight error inclusions the shape was getting deformed 
    over a certain number of rotations.
    */
    public void rotateClock(int i)
    {
        Polygon p=new Polygon(shapes[i-5].getShape().xpoints,shapes[i-5].getShape().ypoints,shapes[i-5].getShape().npoints);
        int x[]=new int[shapes[i-5].getShape().npoints];
        int y[]=new int[shapes[i-5].getShape().npoints];
        int n=0;
        x=p.xpoints;
        y=p.ypoints;
        n=p.npoints;
        if((i-5)==1)
        {
            if(counterClock==0)
            {
                x[0]=x[0];x[1]=x[1]+leg;x[2]=x[2];
                y[0]=y[0];y[2]=y[2];y[1]=y[1]+leg;
                counterClock=4;
            }else
            if(counterClock==2)
            {
                x[0]=x[0];x[1]=x[1];x[2]=x[2]+leg;
                y[0]=y[0];y[1]=y[1];y[2]=y[2]-leg;
                counterClock=0;
            }
            else if(counterClock==3)
            {
                x[0]=x[0]+leg;x[1]=x[1]-leg;x[2]=x[2]-leg;
                y[0]=y[0]-leg;y[1]=y[1]-leg;y[2]=y[2]+leg;
                counterClock=2;
            }
            else if(counterClock==4)
            {
                x[0]=x[0]-leg;x[1]=x[1];x[2]=x[2];
                y[0]=y[0]+leg;y[1]=y[1];y[2]=y[2];
                counterClock=3;
            }
        }
        else if((i-5)==2)
        {
            if(counterClock==0)
            {
                x[0]=x[0]-leg;x[1]=x[1];x[2]=x[2];
                y[0]=y[0];y[2]=y[2];y[1]=y[1];
                counterClock=4;
            }else
            if(counterClock==2)
            {
                x[0]=x[0];x[1]=x[1];x[2]=x[2];
                y[0]=y[0];y[1]=y[1]+leg;y[2]=y[2];
                counterClock=0;
            }
            else if(counterClock==3)
            {
                x[0]=x[0];x[1]=x[1];x[2]=x[2]+leg;
                y[0]=y[0];y[1]=y[1];y[2]=y[2];
                counterClock=2;
            }
            else if(counterClock==4)
            {
                x[0]=x[0]+leg;x[1]=x[1];x[2]=x[2]-leg;
                y[0]=y[0];y[1]=y[1]-leg;y[2]=y[2];
                counterClock=3;
            }
        }
        else if((i-5)==3)
        {
            if(counterClock==0)
            {
                x[0]=x[0]-leg;x[1]=x[1]-leg;x[2]=x[2]+leg;x[3]=x[3]+leg;
                y[0]=y[0];y[1]=y[1];y[2]=y[2];y[3]=y[3];
                counterClock=4;
            }else
            if(counterClock==2)
            {
                x[0]=x[0]+leg;x[1]=x[1]+leg;x[2]=x[2]-leg;x[3]=x[3]-leg;
                y[0]=y[0];y[1]=y[1];y[2]=y[2];y[3]=y[3];
                counterClock=0;
            }
            else if(counterClock==3)
            {
                x[0]=x[0]-leg;x[1]=x[1]-leg;x[2]=x[2]+leg;x[3]=x[3]+leg;
                y[0]=y[0];y[1]=y[1];y[2]=y[2];y[3]=y[3];
                counterClock=2;
            }
            else if(counterClock==4)
            {
                x[0]=x[0]+leg;x[1]=x[1]+leg;x[2]=x[2]-leg;x[3]=x[3]-leg;
                y[0]=y[0];y[1]=y[1];y[2]=y[2];y[3]=y[3];
                counterClock=3;
            }
        }
        else if((i-5)==4)
        {
            if(counterClock==0)
            {
                x[0]=x[0]+leg;x[1]=x[1]+leg;x[2]=x[2]-leg;x[3]=x[3]-leg;
                y[0]=y[0];y[1]=y[1];y[2]=y[2];
                counterClock=4;
            }else
            if(counterClock==2)
            {
                x[0]=x[0]-leg;x[1]=x[1]-leg;x[2]=x[2]+leg;x[3]=x[3]+leg;
                y[0]=y[0];y[1]=y[1];y[2]=y[2];
                counterClock=0;
            }
            else if(counterClock==3)
            {
                x[0]=x[0]+leg;x[1]=x[1]+leg;x[2]=x[2]-leg;x[3]=x[3]-leg;
                y[0]=y[0];y[1]=y[1];y[2]=y[2];
                counterClock=2;
            }
            else if(counterClock==4)
            {
                x[0]=x[0]-leg;x[1]=x[1]-leg;x[2]=x[2]+leg;x[3]=x[3]+leg;
                y[0]=y[0];y[1]=y[1];y[2]=y[2];
                counterClock=3;
            }
        }
        
        //Since this is the falling polygon , The new coordinates will have to be reflected on the original instance//
        //Once collision occurs , the shape is reset to its original form//
        p=new Polygon(x,y,n);
        shapes[i-5].setShape(p);
    }
    
    /* 
    This method checks if the shapes have already piled up near the top of the playable area.
    */
    boolean minRegisteredNearTop()
    {
        Polygon p;
        int flag=0;
        int minY=Integer.MAX_VALUE;
        int n;
        for(int j=0;j<registered.size();j++)
        {
            p=(Polygon)registered.elementAt(j);
            n=p.npoints;
            int y[]=new int[n];
            y=p.ypoints;
            for(int i=0;i<n;i++)
            {
                if(y[i]==top)
                {
                    flag=1;
                    break;
                }
            }
        }
        if(flag==1)
            return true;
        else
            return false;
    }
    
    /*
    This method clears the registered shapes list when the shapes pile up to the very top of the
    playable area. This enables in showing the game over screen.
    */
    public void clearRegistered()
    {
        registered.clear();
        registeredColor.clear();
    }
    
    /*This is the game over screen. We will restart if the user clicks on restart button*/
    void showGameOver(Graphics g1,int gameOver)
    {  
        RestartScreen rs=new RestartScreen(score,level,gameOver);//SEND SCORE AND LEVEL ACHIEVED TO THE SCORE SCREEN//
        if(rs.returnGameStatus()==0)
        {
            new Game();
        }
    }
    
    /*
    To draw the modified/translated/rotated shape onto the game area and to reset the shapes if collision occurs.
    Also to draw the next shape on the upper right region and to pause the game if the mouse hovers to the game frame.
    Also, to emulate the falling behavior as mentioned in the requirement.
    */
    public void drawShape(Graphics g1,int i)
    {
        Point2D p2d[];
        //DRAW SHAPE ON THE NEXT SHAPE PANEL//
        int flag=0;
        //The game over flag//
        int gameOver=0;
        int k=nextShapeSelector2;
        drawRegistered(g1);
        Polygon p=new Polygon(shapes[k].getShape().xpoints,shapes[k].getShape().ypoints,shapes[k].getShape().npoints);
        g1.drawPolygon(p);
        g1.setColor(shapes[k].getColor());
        g1.fillPolygon(p);
        g1.setColor(Color.black);
        
        int x[]=new int[shapes[i-5].getShape().npoints];
        int y[]=new int[shapes[i-5].getShape().npoints];
        p2d=new Point2D[shapes[i-5].getShape().npoints];
        int n=shapes[i-5].getShape().npoints;
        p=new Polygon(shapes[i-5].getShape().xpoints,shapes[i-5].getShape().ypoints,shapes[i-5].getShape().npoints);
        x=p.xpoints;
        y=p.ypoints;
        for(int k1=0;k1<n;k1++)
        {
            if(y[k1]==bottom||collision(i-5))
            {
                clock=0;
                counterClock=0;
                flag=1;
            }
        }
            if(pause==1)
            {
                x=x;
                y=y;
                p=new Polygon(x,y,n);
                g1.drawPolygon(p);
                shapes[i-5].setShape(p);
                g1.setColor(shapes[i-5].getColor());
                g1.fillPolygon(p);
                g1.setColor(Color.black);
                //TO CHECK IF CURSOR IS WITHIN THE SHAPE//
                //THE FOLLOWING CHECK HAS TO BE DONE BECAUSE OF THE DIFFERENCE IN THE IMPLEMENTATION OF TRIANGULAR SHAPES//
                if(i-5==1||i-5==2) //Score = Score - Level x M//
                {
                    p2d[0]=new Point2D(x[0],y[0]);
                    p2d[1]=new Point2D(x[1],y[1]);
                    p2d[2]=new Point2D(x[2],y[2]);
                }
                else
                {
                    p2d[0]=new Point2D(x[0],y[0]);
                    p2d[1]=new Point2D(x[3],y[3]);
                    p2d[2]=new Point2D(x[2],y[2]);
                    p2d[3]=new Point2D(x[1],y[1]);
                }
                Point2D cursor=new Point2D(iX(xP1),iY(yP1));
                if(Tools2D.insidePolygon(cursor, p2d))
                {
                    score=score-level*M; //If the user changes the shape, then the score is reduced accordingly.//
                    setShapes(left,leg,top);
                    nextShapeSelector=nextShapeSelector2;
                    nextShapeSelector2=5+(int)(Math.random()*((9-5)+1));
                }
            }
            else if(flag==0)
            {
                for(int j=0;j<p.npoints;j++)
                y[j]=y[j]+leg;
                p=new Polygon(x,y,n);
                g1.drawPolygon(p);
                shapes[i-5].setShape(p);
                g1.setColor(shapes[i-5].getColor());
                g1.fillPolygon(p);
                g1.setColor(Color.black);
            }
            else
            {
                register(p,shapes[i-5].getColor(),(x[0]+x[1])/2,(y[0]+y[2])/2);
                //THIS IS WHERE THE SHAPES WILL BE RESET TO THEIR ORIGINAL//
                setShapes(left,leg,top);
                drawRegistered(g1);
                if(minRegisteredNearTop()) //Check if shapes have all piled up to the top.//
                {
                    gameOver=1;
                    clearRegistered();
                    showGameOver(g1,gameOver);
                }
                nextShapeSelector=nextShapeSelector2;
                lineFilled();
                nextShapeSelector2=5+(int)(Math.random()*((9-5)+1));
            }
        try
        {
        Thread.sleep((long)(100/S));  //The speed of falling is emulated by changing the refresh rate.//
        }
        catch(Exception e){}
        repaint();
}
    
    /*
    This method checks if any of the rectangle ranging from left to right ie, these are 
    imaginary rectangles that divide the play area into grids, are completely filled or not.
    */
    public void lineFilled()
    {
        int deletableLocations[]=new int[5];
        int count=0;
        int t=0;
        for(int i=10;i>=1;i--)
        {
            count=0;
            for(int j=5;j>=1;j--)
            {
                for(int k=0;k<registeredLocation.size();k++)
                {
                    if((int)registeredLocation.elementAt(k)==i*10+j)
                    {
                        deletableLocations[count]=k;
                        count++;
                    }
                }
            }
           
            if(count==5) //Lines = Lines + 1; Score = Score + Level x M. //
            { // If the number of removed rows in the current Level reaches N, Level = Level + 1, the falling speed FS = FS x (1 + Level x S).//
                lines++;
                score=score+level*M;
                if(lines==N)
                {
                    clearRegistered();
                    S=(S*10+1)/10.0f; //If a user progresses a level, then the speed is increased by 1 speed point//
                    level++;
                }
                t=i;
                /*We also need to shift the upper lines ,1 position down*/
                for(count=0;count<5;count++)
                {
                    registered.removeElementAt(deletableLocations[count]);
                    registeredColor.removeElementAt(deletableLocations[count]);
                    registeredLocation.removeElementAt(deletableLocations[count]);
                }
                for(;t>=1;t--)
                {
                    for(int h=5;h>=1;h--)
                    {
                        for(int k=0;k<registeredLocation.size();k++)
                        {
                        if((int)registeredLocation.elementAt(k)==t*10+h)
                        {
                            int loc1=0;
                            Polygon p=(Polygon)registered.elementAt(k);
                            loc1=(int)registeredLocation.elementAt(k)+10;
                            registered.removeElementAt(k);
                            registeredLocation.removeElementAt(k);
                            registeredLocation.add(k,loc1);
                            int x[]=new int[p.npoints];
                            int y[]=new int[p.npoints];
                            x=p.xpoints;
                            y=p.ypoints;
                            int n=p.npoints;
                            for(int v=0;v<n;v++)
                            {
                                y[v]=y[v]+leg;
                            }
                            p=new Polygon(x,y,n);
                            registered.add(k,p);
                        }
                        }
                    }
                }
            }
            else
                count=0;
        }
    }

    /*
    To keep track of all the shapes that have already touched the bottom or have encountered collision 
    with already registered polygons.
    */
    public void register(Polygon p,Color c,int x,int y)
    {
        int location=0;
        registered.add(p);
        registeredColor.add(c);
        //STORE GRID LOCATION IN THE SAME ORDER OF FALLEN SHAPES//
        /* We need to assign grid numbers to already fallen shapes. This facilitates in checking if
        a line is fully filled or not.
        */
        if(x>left&&x<=left+leg)
        {
            if(y<=top+10*leg&&y>top+9*leg)
                location=101;
            if(y<=top+9*leg&&y>top+8*leg)
                location=91;
            if(y<=top+8*leg&&y>top+7*leg)
                location=81;
            if(y<=top+7*leg&&y>top+6*leg)
                location=71;
            if(y<=top+6*leg&&y>top+5*leg)
                location=61;
            if(y<=top+5*leg&&y>top+4*leg)
                location=51;
            if(y<=top+4*leg&&y>top+3*leg)
                location=41;
            if(y<=top+3*leg&&y>top+2*leg)
                location=31;
            if(y<=top+2*leg&&y>top+leg)
                location=21;
            if(y<=top+leg&&y>top)
                location=11;
        }
        if(x>left+leg&&x<=left+2*leg)
        {
            if(y<=top+10*leg&&y>top+9*leg)
                location=102;
            if(y<=top+9*leg&&y>top+8*leg)
                location=92;
            if(y<=top+8*leg&&y>top+7*leg)
                location=82;
            if(y<=top+7*leg&&y>top+6*leg)
                location=72;
            if(y<=top+6*leg&&y>top+5*leg)
                location=62;
            if(y<=top+5*leg&&y>top+4*leg)
                location=52;
            if(y<=top+4*leg&&y>top+3*leg)
                location=42;
            if(y<=top+3*leg&&y>top+2*leg)
                location=32;
            if(y<=top+2*leg&&y>top+leg)
                location=22;
            if(y<=top+leg&&y>top)
                location=12;
        }
        if(x>left+2*leg&&x<=left+3*leg)
        {
            if(y<=top+10*leg&&y>top+9*leg)
                location=103;
            if(y<=top+9*leg&&y>top+8*leg)
                location=93;
            if(y<=top+8*leg&&y>top+7*leg)
                location=83;
            if(y<=top+7*leg&&y>top+6*leg)
                location=73;
            if(y<=top+6*leg&&y>top+5*leg)
                location=63;
            if(y<=top+5*leg&&y>top+4*leg)
                location=53;
            if(y<=top+4*leg&&y>top+3*leg)
                location=43;
            if(y<=top+3*leg&&y>top+2*leg)
                location=33;
            if(y<=top+2*leg&&y>top+leg)
                location=23;
            if(y<=top+leg&&y>top)
                location=13;
        }
        if(x>left+3*leg&&x<=left+4*leg)
        {
            if(y<=top+10*leg&&y>top+9*leg)
                location=104;
            if(y<=top+9*leg&&y>top+8*leg)
                location=94;
            if(y<=top+8*leg&&y>top+7*leg)
                location=84;
            if(y<=top+7*leg&&y>top+6*leg)
                location=74;
            if(y<=top+6*leg&&y>top+5*leg)
                location=64;
            if(y<=top+5*leg&&y>top+4*leg)
                location=54;
            if(y<=top+4*leg&&y>top+3*leg)
                location=44;
            if(y<=top+3*leg&&y>top+2*leg)
                location=34;
            if(y<=top+2*leg&&y>top+leg)
                location=24;
            if(y<=top+leg&&y>top)
                location=14;
        }
        if(x>left+4*leg&&x<=left+5*leg)
        {
            if(y<=top+10*leg&&y>top+9*leg)
                location=105;
            if(y<=top+9*leg&&y>top+8*leg)
                location=95;
            if(y<=top+8*leg&&y>top+7*leg)
                location=85;
            if(y<=top+7*leg&&y>top+6*leg)
                location=75;
            if(y<=top+6*leg&&y>top+5*leg)
                location=65;
            if(y<=top+5*leg&&y>top+4*leg)
                location=55;
            if(y<=top+4*leg&&y>top+3*leg)
                location=45;
            if(y<=top+3*leg&&y>top+2*leg)
                location=35;
            if(y<=top+2*leg&&y>top+leg)
                location=25;
            if(y<=top+leg&&y>top)
                location=15;
        }
            registeredLocation.add(location);
    }
    
    /*
    To draw all the shapes that have already touched the bottom or have encountered collision
    with previously registered polygons.
    */
    public void drawRegistered(Graphics g1)
    {
        for(int j=0;j<registered.size();j++)
        {
            g1.drawPolygon((Polygon)registered.elementAt(j));
            g1.setColor((Color)registeredColor.elementAt(j));
            g1.fillPolygon((Polygon)registered.elementAt(j));
            g1.setColor(Color.black);
        }
    }
    
    // Overridden paint method and all the control statements.//
    public void paint(Graphics g)
    {
        initgr();
        int i=0;
        g.drawLine(left,top,left+5*leg,top);
        g.drawLine(left,top,left,top+10*leg);
        g.drawLine(left,top+10*leg,left+5*leg,top+10*leg);
        g.drawLine(left+5*leg,top+10*leg,left+5*leg,top);
        g.drawRect(left+7*leg,top+8*leg,2*leg+1,leg+1);
        g.drawString("Level: "+level,left+7*leg,top+3*leg);
        g.drawString("Lines: "+lines,left+7*leg,top+4*leg);
        g.drawString("Score: "+score,left+7*leg,top+5*leg);
        g.drawString("QUIT",left+7*leg+20,top+8*leg+30);
        if(iX(xP1)>=left&&iX(xP1)<=left+5*leg&&iY(yP1)>=top&&iY(yP1)<=top+10*leg)
        {
            pause=1;
            g.drawRect(leg+left,4*leg+top,2*leg+1,leg+1);
            g.drawString("PAUSE",leg+left+20,4*leg+30+top);
        }
        else
            pause=0;
        if(iX(xP)>=left+7*leg&&iX(xP)<=left+9*leg&&iY(yP)>=top+8*leg&&iY(yP)<=top+9*leg)
            System.exit(0);
        if(iX(xP)>left+5*leg&&!(iX(xP)>=left+7*leg&&iX(xP)<=left+9*leg&&iY(yP)>=top+8*leg&&iY(yP)<=top+9*leg)&&button==1)
        {  
            button=0;
            moveShapeLeft(nextShapeSelector);
        }
        if(iX(xP)>left+5*leg&&!(iX(xP)>=left+7*leg&&iX(xP)<=left+9*leg&&iY(yP)>=top+8*leg&&iY(yP)<=top+9*leg)&&button==3)
        {  
            button=0;
            moveShapeRight(nextShapeSelector);
        }
        if(iX(xP2)>left+5*leg&&!(iX(xP2)>=left+7*leg&&iX(xP2)<=left+9*leg&&iY(yP2)>=top+8*leg&&iY(yP2)<=top+9*leg)&&direction<0.0d)
        {
            direction=0.0d;
            rotateCounterClock(nextShapeSelector);
        }
        if(iX(xP2)>left+5*leg&&!(iX(xP2)>=left+7*leg&&iX(xP2)<=left+9*leg&&iY(yP2)>=top+8*leg&&iY(yP2)<=top+9*leg)&&direction>0.0d)
        {
            direction=0.0d;
            rotateClock(nextShapeSelector);
        }
        drawShape(g,nextShapeSelector);
        collision(nextShapeSelector);
    }
}