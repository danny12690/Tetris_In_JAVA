// Tools2D.java: Class to be used in other program files.
// Uses: Point2D and Triangle.

/* CGDemo is a companion of the textbook
L. Ammeraal and K. Zhang, Computer Graphics for Java Programmers, 
2nd Edition, Wiley, 2006.
Copyright (C) 2006  Janis Schubert, Kang Zhang, Leen Ammeraal 
This program is free software; you can redistribute it and/or 
modify it under the terms of the GNU General Public License as 
published by the Free Software Foundation; either version 2 of 
the License, or (at your option) any later version. 
This program is distributed in the hope that it will be useful, 
but WITHOUT ANY WARRANTY; without even the implied warranty of 
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
See the GNU General Public License for more details.  
You should have received a copy of the GNU General Public 
License along with this program; if not, write to 
the Free Software Foundation, Inc., 51 Franklin Street, 
Fifth Floor, Boston, MA  02110-1301, USA. 
*/

class Tools2D
{  static float area2(Point2D A, Point2D B, Point2D C)
   {  return (A.x - C.x) * (B.y - C.y) - (A.y - C.y) * (B.x - C.x);
   }

   static boolean insideTriangle(Point2D A, Point2D B, Point2D C,
      Point2D P) // ABC is assumed to be counter-clockwise
   {  return
        Tools2D.area2(A, B, P) >= 0 &&
        Tools2D.area2(B, C, P) >= 0 &&
        Tools2D.area2(C, A, P) >= 0;
   }
   //Point inside polygon test//
   static boolean insidePolygon(Point2D p, Point2D[] pol)
   {
       int n=pol.length,j=n-1;
       boolean b=false;
       int x=p.x;
       int y=p.y;
       for(int i=0;i<n;i++)
       {
           if(pol[j].y<=y && y<pol[i].y && Tools2D.area2(pol[j],pol[i],p)>0 || pol[i].y<=y&&pol[j].y>y&&Tools2D.area2(pol[i],pol[j],p)>0)
               b=!b;
           j=i;
       }return b;
   }
   static void triangulate(Point2D[] P, Triangle[] tr)
   {  // P contains all n polygon vertices in CCW order.
      // The resulting triangles will be stored in array tr.
      // This array tr must have length n - 2.
      int n = P.length, j = n - 1, iA=0, iB, iC;
      int[] next = new int[n];
      for (int i=0; i<n; i++)
      {  next[j] = i;
         j = i;
      }
      for (int k=0; k<n-2; k++)
      {  // Find a suitable triangle, consisting of two edges
         // and an internal diagonal:
         Point2D A, B, C;
         boolean triaFound = false;
         int count = 0;
         while (!triaFound && ++count < n)
         {  iB = next[iA]; iC = next[iB];
            A = P[iA]; B = P[iB]; C = P[iC];
            if (Tools2D.area2(A, B, C) >= 0)
            {  // Edges AB and BC; diagonal AC.
               // Test to see if no other polygon vertex
               // lies within triangle ABC:
               j = next[iC];
               while (j != iA && !insideTriangle(A, B, C, P[j]))
                  j = next[j];
               if (j == iA)
               {  // Triangle ABC contains no other vertex:
                  tr[k] = new Triangle(A, B, C);
                  next[iA] = iC;
                  triaFound = true;
               }
            }
            iA = next[iA];
         }
         if (count == n)
         {  System.out.println("Not a simple polygon" +
              " or vertex sequence not counter-clockwise.");
            System.exit(1);
         }
      }
   }

   static float distance2(Point2D P, Point2D Q)
   {  float dx = P.x - Q.x, dy = P.y - Q.y;
      return dx * dx + dy * dy;
   }
}