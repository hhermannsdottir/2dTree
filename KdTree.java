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
        root = this._insert(root, null, p, false); //for the first one, we are comparing x  
    }
    
    // orientation is a flag indicating which orientation we should consider
    // when we divide the Rect
    private Node _insert(Node x, Node parent, Point2D p, boolean isLeft) {
        if (x == null) {
            
            RectHV rect;
            if (parent == null) {
                rect = new RectHV(0.0, 0.0, 1.0, 1.0);
                return new Node(p, rect, null, null, 1, true);
            } else {
                
                StdOut.println("isComparingX: " + parent.isComparingX);
                StdOut.println("isLeftTree:   " + isLeft);
                StdOut.printf("(%f, %f)\n", parent.p.x(), parent.p.y());
                StdOut.printf("(%f, %f), (%f, %f)\n", parent.rect.xmin(),
                              parent.rect.ymin(), parent.rect.xmax(), 
                              parent.rect.ymax());
                
                if (parent.isComparingX && isLeft)
                    rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(),
                                      parent.p.x(), parent.rect.ymax());
                else if (parent.isComparingX && !isLeft)
                    rect = new RectHV(parent.p.x(), parent.rect.ymin(),
                                      parent.rect.xmax(), parent.rect.ymax());
                else if (!parent.isComparingX && isLeft)
                    rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(),
                                      parent.rect.xmax(), parent.p.y());
                else 
                    rect = new RectHV(parent.rect.xmin(), parent.p.y(),
                                      parent.rect.xmax(), parent.rect.ymax());
                
                return new Node(p, rect, null, null, 1, !parent.isComparingX);   
            }
            
        }
        int cmp;
        if (x.isComparingX) 
            cmp = Point2D.X_ORDER.compare(p, x.p);
        else
            cmp = Point2D.Y_ORDER.compare(p, x.p);
        
        if      (cmp < 0)  x.left = _insert(x.left, x, p, true);
        else if (cmp >= 0) x.right = _insert(x.right, x, p, false);

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
        StdDraw.line(0, 0, 0, 1);
        StdDraw.line(0, 1, 1, 1);
        StdDraw.line(1, 1, 1, 0);
        StdDraw.line(0, 0, 1, 0);

        Iterable<Node> nodes = this.levelOrder();

        for (Node node : nodes) {
 
            //draw line
            if (node.isComparingX) {
                StdDraw.setPenRadius();
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
            } else {
                StdDraw.setPenRadius();
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
            }
            
            //draw point
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(.01);
            node.p.draw();   
        }
        
    }
    
    private Iterable<Node> levelOrder() {
       Queue<Node> orders= new Queue<Node>();
       Queue<Node> nodes = new Queue<Node>();
       nodes.enqueue(root);
       while (!nodes.isEmpty()) {
           Node x = nodes.dequeue();
           if (x == null) continue;
           orders.enqueue(x);
           nodes.enqueue(x.left);
           nodes.enqueue(x.right);  
       }
       return orders; 
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
        private boolean isComparingX; //use to keep track of whether comparing x or y
        
        public Node(Point2D p, RectHV rect, Node left, Node right, int N, boolean isComparingX) {
            this.p = p;
            this.rect = rect;
            this.left = left;
            this.right = right;
            this.N = N;
            this.isComparingX = isComparingX;
        }
    }
    
    
    
}