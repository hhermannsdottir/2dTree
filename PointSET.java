import java.util.TreeSet;

public class PointSET {
    
    private TreeSet<Point2D> treeSet;
    
    // construct an empty set of points
    public PointSET(){                               
       
        treeSet = new TreeSet<Point2D>();
        
    }
    
    
    // is the set empty?
    public boolean isEmpty() {
        return this.treeSet.isEmpty();
    } 
    
    
    // number of points in the set
    public int size() {
        
        return this.treeSet.size();
        
    }
    
    // add the point p to the set (if it is not already in the set)
    public void insert(Point2D p) {                  

        
    }
    
    // does the set contain the point p?
    public boolean contains(Point2D p) {             
        
        
    }
    
    // draw all of the points to standard draw
    public void draw() {                             

        
    }
    
    // all points in the set that are inside the rectangle 
    public Iterable<Point2D> range(RectHV rect) {
        

        
        
    }
    
    // a nearest neighbor in the set to p; null if set is empty
    public Point2D nearest(Point2D p) {
        
        
    }
}