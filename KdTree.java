public class KdTree {
    
    private Node root; 
    
    // construct an empty set of points
    public KdTree(){                               
        root = null;     
    }
    
    
    // is the set empty?
    public boolean isEmpty() {
        return this.size() == 0;
    } 
    
    
    // number of points in the set
    public int size() {
        
        if (root == null) return 0;
        else              return root.N;
        
    }
    
    // add the point p to the set (if it is not already in the set)
    public void insert(Point2D p) {                  
        root = this._insert(root, p, true, false); //for the first one, we are comparing x
        
    }
    
    // orientation is a flag indicating which orientation we should consider
    // when we divide the Rect
    private Node _insert(Node x, Point2D p, boolean isComparingX, boolean isLeft) {
        if (x == null) {
            
            RectHV rect;
            if (this.root == null)
                rect = new RectHV(0.0, 0.0, 1.0, 1.0);
            else {
                if (isComparingX && isLeft)
                    rect = new RectHV(this.root.rect.xmin(), this.root.rect.ymin(),
                                      this.root.p.x(), this.root.rect.ymax());
                else if (isComparingX && !isLeft)
                    rect = new RectHV(this.root.p.x(), this.root.rect.ymin(),
                                      this.root.rect.xmax(), this.root.rect.ymax());
                else if (!isComparingX && isLeft)
                    rect = new RectHV(this.root.rect.xmin(), this.root.rect.ymin(),
                                      this.root.rect.xmax(), this.root.p.y());
                else 
                    rect = new RectHV(this.root.rect.xmin(), this.root.p.y(),
                                      this.root.rect.xmax(), this.root.rect.ymax());
                
            }
            return new Node(p, rect, null, null, 1);
        }
        int cmp;
        if (isComparingX) 
            cmp = Point2D.X_ORDER.compare(p, x.p);
        else
            cmp = Point2D.Y_ORDER.compare(p, x.p);
        
        if      (cmp < 0) return _insert(x.lb, p, !isComparingX, true);
        else if (cmp >= 0) return _insert(x.rt, p, !isComparingX, false);
        
        
        x.N = x.lb.N + x.rt.N + 1; //update the count
        return x;  
    }
    
    // does the set contain the point p?
    public boolean contains(Point2D p) {             

       return _containsPoint(root, p, true);
        
    }
    
    private boolean _containsPoint(Node x, Point2D p, boolean isComparingX) {
        if (x == null) return false;
        if (x.p.equals(p)) return true;
        
        int cmp;
        if (isComparingX) 
            cmp = Point2D.X_ORDER.compare(p, x.p);
        else
            cmp = Point2D.Y_ORDER.compare(p, x.p);
        
        if (cmp < 0) return _containsPoint(x.lb, p, !isComparingX);
        else  return _containsPoint(x.rt, p, !isComparingX);
    }
    
    // draw all of the points to standard draw
    public void draw() {                             

        
    }
    
    // all points in the set that are inside the rectangle 
    public Iterable<Point2D> range(RectHV rect) {
        
        return null;
        
        
    }
    
    // a nearest neighbor in the set to p; null if set is empty
    public Point2D nearest(Point2D p) {
       return null; 
        
    }
    
    private static class Node {
        private Point2D p; //the point
        private RectHV rect; // the axis-aligned rectangle corresponding to node
        private Node lb;
        private Node rt;
        private int N; //keeping track of whether the tree is empty or not
        
        public Node(Point2D p, RectHV rect, Node lb, Node rt, int N) {
            this.p = p;
            this.rect = rect;
            this.lb = lb;
            this.rt = rt;
            this.N = N;
        }
    }
    
    
    
}