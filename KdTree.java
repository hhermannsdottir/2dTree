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
        
        if (!this.contains(p))
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
                
//                StdOut.println("isComparingX: " + parent.isComparingX);
//                StdOut.println("isLeftTree:   " + isLeft);
//                StdOut.printf("(%f, %f)\n", parent.p.x(), parent.p.y());
//                StdOut.printf("(%f, %f), (%f, %f)\n", parent.rect.xmin(),
//                              parent.rect.ymin(), parent.rect.xmax(), 
//                              parent.rect.ymax());
                
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
        StdDraw.setPenRadius();
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
        Queue<Point2D> results = new Queue<Point2D>();
        _range(results, rect, root);   
        return results;       
    }
    
    private void _range(Queue<Point2D> results, RectHV rect, Node x) {
        if (x == null) return;
        if (x.rect.intersects(rect)) {   
            if (rect.contains(x.p)) results.enqueue(x.p);  
            _range(results, rect, x.left);
            _range(results, rect, x.right);  
        }
    }
    
    // a nearest neighbor in the set to p; null if set is empty
    public Point2D nearest(Point2D p) {
        
        if (this.isEmpty()) return null;
        
        Node nearestSoFar = root;
        _nearest(root, nearestSoFar, p);
        
        return nearestSoFar.p;   
        
    }
    
    private void _nearest(Node x, Node nearestSoFar, Point2D p) {
        
        if (x == null) return;
        
        //calculate current nearest distance
        double distanceToNearestSoFar = p.distanceSquaredTo(nearestSoFar.p);
        
        double distanceToX = p.distanceSquaredTo(x.p);
        //update the nearest node so far we found
        if (distanceToX < distanceToNearestSoFar) {
            nearestSoFar = x;
            distanceToNearestSoFar = distanceToX;
        }
        
        if (x.left == null && x.right == null) return;
        
        double leftTreeDistance  = 20.0;
        double rightTreeDistance = 20.0;
        if (x.left != null) {
            leftTreeDistance  = x.left.rect.distanceSquaredTo(p);  
        }
        if (x.right != null) {
            rightTreeDistance = x.right.rect.distanceSquaredTo(p);  
        }
        
        if (leftTreeDistance < rightTreeDistance) {  
            
            if (leftTreeDistance < distanceToNearestSoFar)
                _nearest(x.left, nearestSoFar, p); 
            
        } else if (leftTreeDistance > rightTreeDistance) {
            
            if (rightTreeDistance < distanceToNearestSoFar)
                _nearest(x.right, nearestSoFar, p);
        } else {
            if (leftTreeDistance < distanceToNearestSoFar)
                _nearest(x.left, nearestSoFar, p);
            if (rightTreeDistance < distanceToNearestSoFar)
                _nearest(x.right, nearestSoFar, p);  
        }
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
    
    public static void main(String[] args) {
        
        //PointSET kdtree = new PointSET();
        KdTree kdtree = new KdTree();
        assert kdtree.isEmpty() == true : "It should be empty.";
        Point2D p1 = new Point2D(0.1, 0.2);
        kdtree.insert(p1);
        assert kdtree.isEmpty() == false : "It should not be empty.";
        Point2D p2 = new Point2D(0.45, 0.32);
        kdtree.insert(p2);
        Point2D p3 = new Point2D(0.87, 0.63);
        kdtree.insert(p3);       
        assert kdtree.isEmpty() == false : "It should not be empty.";    
        assert kdtree.size() == 3 : "size should be 3";
        
        assert kdtree.contains(p1) == true : "should contain p1";
        assert kdtree.contains(p2) == true : "should contain p2";
        assert kdtree.contains(p3) == true : "should contain p3";
        
        Point2D p4 = new Point2D(0.03, 0.8);
        assert kdtree.contains(p4) == false : "should not contain p4";
        
        kdtree.draw();
        Queue<Point2D> results = (Queue<Point2D>) kdtree.range(new RectHV(0, 0, 1, 1));
        for (Point2D p : results)
            StdOut.printf("%f, %f\n", p.x(), p.y());
        Point2D searchPoint = new Point2D(0.3, 0.3);
        searchPoint.draw();
        Point2D nearestPoint = kdtree.nearest(searchPoint);
        StdOut.printf("nearest is %f, %f\n", nearestPoint.x(), nearestPoint.y());
    }
    
    
    
}