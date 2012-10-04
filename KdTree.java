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
    
    // return number of Nodes rooted at x
    private int size(Node x) {
        if (x == null) return 0;
        else return x.N;
    }
    
    // add the point p to the set (if it is not already in the set)
    public void insert(Point2D p) {                  
        root = this._insert(root, null, p, true, false); //for the first one, we are comparing x  
    }
    
    // orientation is a flag indicating which orientation we should consider
    // when we divide the Rect
    private Node _insert(Node x, Node parent, Point2D p, boolean isComparingX, boolean isLeft) {
        if (x == null) {
            
            RectHV rect;
            if (parent == null)
                rect = new RectHV(0.0, 0.0, 1.0, 1.0);
            else {
                
                StdOut.println("isComparingX: " + isComparingX);
                StdOut.println("isLeftTree:   " + isLeft);
                if (isComparingX && isLeft)
                    rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(),
                                      parent.p.x(), parent.rect.ymax());
                else if (isComparingX && !isLeft)
                    rect = new RectHV(parent.p.x(), parent.rect.ymin(),
                                      parent.rect.xmax(), parent.rect.ymax());
                else if (!isComparingX && isLeft)
                    rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(),
                                      parent.rect.xmax(), parent.p.y());
                else 
                    rect = new RectHV(parent.rect.xmin(), parent.p.y(),
                                      parent.rect.xmax(), parent.rect.ymax());
                
            }
            return new Node(p, rect, null, null, 1);
        }
        int cmp;
        if (isComparingX) 
            cmp = Point2D.X_ORDER.compare(p, x.p);
        else
            cmp = Point2D.Y_ORDER.compare(p, x.p);
        
        if      (cmp < 0)  x.left = _insert(x.left, x, p, !isComparingX, true);
        else if (cmp >= 0) x.right = _insert(x.right, x, p, !isComparingX, false);

        x.N = size(x.left) + size(x.right) + 1; //update the count
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
        
        if (cmp < 0) return _containsPoint(x.left, p, !isComparingX);
        else  return _containsPoint(x.right, p, !isComparingX);
    }
    
    // draw all of the points to standard draw
    public void draw() {                             
        
                StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        Iterable<Point2D> points = this.levelOrder();
        
        
        for (Point2D point : points) {
            point.draw();
            
        }
        
    }
    
    private Iterable<Point2D> levelOrder() {
       Queue<Point2D> points= new Queue<Point2D>();
       Queue<Node> nodes = new Queue<Node>();
       nodes.enqueue(root);
       while (!nodes.isEmpty()) {
           Node x = nodes.dequeue();
           if (x == null) continue;
           points.enqueue(x.p);
           nodes.enqueue(x.left);
           nodes.enqueue(x.right);  
       }
       return points; 
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
        private Node left;
        private Node right;
        private int N; //keeping track of whether the tree is empty or not
        
        public Node(Point2D p, RectHV rect, Node left, Node right, int N) {
            this.p = p;
            this.rect = rect;
            this.left = left;
            this.right = right;
            this.N = N;
        }
    }
    
    
    
}