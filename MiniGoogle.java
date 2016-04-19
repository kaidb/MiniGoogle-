/*
 * Author: Kai Bernardini (kaidb@bu.edu)
 * Cs-112 Homework 10
 * MiniGoogle.java
 * Purpose: Using Article Table and TermFrequency, evaluates
 * the cosine simularity of every article in a table and 
 * stores the results in a MAX heap. Effectivly allowing you
 * to search for articles (similar to google, hence the name :-)
 */

public class MiniGoogle{
    
    
    /*@Author is Wayne Snyder. Adapted to use Nodes instead of integers with 
     * field CosSim in place of ints. Get Max now returns the Article*/
    public static class MaxHeap {
        
        private final int SIZE = 10;       // initial length of array
        private int next = 0;              // limit of elements in array
        //private int[] A = new int[SIZE];// implements tree by storing elements in level order
        private Node[] A = new Node[SIZE];
        
       
        public static class Node { // pointer class
            public Article data;
            // public Node next;
            public double CosSim;
            //   public int collisions = 0
           
            public Node(Article data, Double sim ){//constructor for node and data
                this.data = data;
                  //this.next = n;
                this.CosSim = sim; // CHECK IF THIS WORKS *&*&(#)@)@*#)
                
            }
             public String toString(){
        return "Cosine Similarity is " + this.CosSim + this.data;
        }
            public Node(){
                this.data= null;
                this.CosSim = 0;
            }
        }
        
        Node copy( Node p ) {
       if( p == null )
          return null; 
       else 
          return new Node(p.data, p.CosSim); 
    }  
        // standard resize to avoid overflow
        
        private void resize() {
            Node[] B = new Node[A.length*2];
            for(int i = 0; i < A.length; ++i)
                B[i] = copy(A[i]);
           A = B; 
        }
        
        // methods to move up and down tree as array
       
        
        // standard swap, using indices in array
        private void swap(int i, int j) {
            if(i <0 || j< 0){System.out.println("ut oh something went wrong" + "i: " + i + " j: " + j  );}
            Node temp = copy(A[i]);
            A[i] = copy(A[j]);
            A[j] = copy(temp);
        }
        
        // basic data structure methods
        
        public boolean isEmpty() {
            return (next == 0);
        }
        
        public int size() {
            return (next);
        }
        
        // insert an integer into array at next available location
        //    and fix any violations of heap property on path up to root
        
        public void insert(Node P) {
            //double k = P.CosSim;
            if(size() == A.length) resize(); 
            A[next] = P; 
            
            int i = next;
            int p = parent(i); 
            while(!isRoot(i) && A[i].CosSim > A[p].CosSim) {
                swap(i,p);
                i = p;
                p = parent(i); 
            }
            
            ++next;
        }
        
        
        // Remove top (maximum) element, and replace with last element in level
        //    order; fix any violations of heap property on a path downwards
        
        public Node getMax() {
            --next;
           // System.out.println(next);
            if(next>0){swap(0,next); }  //changed this               // swap root with last element
            int i = 0;                   // i is location of new key as it moves down tree
            
            // while there is a maximum child and element out of order, swap with max child
            int mc = maxChild(i); 
            while(!isLeaf(i) && A[i].CosSim < A[mc].CosSim) { 
                swap(i,mc);
                i = mc; 
                mc = maxChild(i);
            }
            
            ///     printHeapAsTree(); 
            
           // System.out.println(next);
            return A[next];
        }
        
        
         
       private int parent(int i) { return (i-1) / 2; }
   private int lchild(int i) { return 2 * i + 1; }
   private int rchild(int i) { return 2 * i + 2; }
   
   private boolean isLeaf(int i) { return (lchild(i) >= next); }
   private boolean isRoot(int i) { return i == 0; }
   
   
        int maxChild(int i) {
            if(lchild(i) >= next)
                return -1;
            if(rchild(i) >= next)
                return lchild(i);
            else if(A[lchild(i)].CosSim > A[rchild(i)].CosSim)
                return lchild(i);
            else
                return rchild(i); 
        }
        
        // Apply heapsort to the array A. To use, fill A with keys and then call heapsort
        
        public  void heapSort() {
            next = 0;
            for(int i = 0; i < A.length; ++i)      // turn A into a heap
                insert(A[i]); //add for string
            for(int i = 0; i < A.length; ++i)      // delete root A.length times, which swaps max into
                getMax();                           //  right side of the array
        }
        
        // debug method
        
        private void printHeap() {
            for(int i = 0; i < A.length; ++i)
                System.out.print(A[i].CosSim + " ");
            System.out.println("\t next = " + next);
        }
        
        private void printHeapAsTree() {
            printHeapTreeHelper(0, ""); 
            System.out.println(); 
        }
        
        private void printHeapTreeHelper(int i, String indent) {
            if(i < next) {
                
                printHeapTreeHelper(rchild(i), indent + "   "); 
                System.out.println(indent + A[i].CosSim);
                printHeapTreeHelper(lchild(i), indent + "   "); 
            }
        }
    }
    /*-----------------------------------------------------------------------*/
    private static String preprocess(String s) {
        s = s.toLowerCase();
        char[] a = s.toCharArray();
        String ans = "";
        for(int i =0; i<a.length; i++){
            char c = a[i];
            if( Character.isDigit(c) || 
               Character.isLetter(c)|| Character.isWhitespace(c)){
                ans += (c+"");}
        }
        return ans;
    }
    /*------------------Blacklisted Strings------------------*/
    private static final String [] blackList = { "the", "of", "and", "a", "to", "in", "is", 
        "you", "that", "it", "he", "was", "for", "on", "are", "as", "with", 
        "his", "they", "i", "at", "be", "this", "have", "from", "or", "one", 
        "had", "by", "word", "but", "not", "what", "all", "were", "we", "when", 
        "your", "can", "said", "there", "use", "an", "each", "which", "she", 
        "do", "how", "their", "if", "will", "up", "other", "about", "out", "many", 
        "then", "them", "these", "so", "some", "her", "would", "make", "like", 
        "him", "into", "time", "has", "look", "two", "more", "write", "go", "see", 
        "number", "no", "way", "could", "people",  "my", "than", "first", "water", 
        "been", "call", "who", "oil", "its", "now", "find", "long", "down", "day", 
        "did", "get", "come", "made", "may", "part" }; 
    /*----------------------------------------------------*/
    private static boolean blacklisted(String s) {
        for(int i =0;  i< 100; i++){
            String p = blackList[i];
            if(s.equalsIgnoreCase(p) ){return true;}
        }
        return false;
    }
    
    private static double getCosineSimilarity(String s, String t) {
        TermFrequencyTable  L = new TermFrequencyTable();
        s= preprocess(s);
        t= preprocess(t);
        String[] str0 = s.split(" ");
        String[] str1 = t.split(" ");
        for(int i =0; i <str0.length; i++){
            if(!blacklisted(str0[i])){
                L.insert(str0[i], 0);}
        }
        for(int j = 0; j< str1.length; j++){
            if(!blacklisted(str1[j])){
                L.insert(str1[j],1);}
        }
        double ans = L.cosineSimilarity();
        return ans;
    }
    public static String phraseSearch(String phrase, ArticleTable T) {
        // MiniGoogle G = new MiniGoogle();
        MaxHeap H = new MaxHeap();
        //ArticleTable.Node Q = T.I;
        T.reset();
        int a = T.size2();
       // System.out.println(a);
      //  System.out.println(T.I);
        while(T.hasNext()){
            //String title = T.I.data.getTitle();
            //String body = T.I.data.getBody();
         //  Node insPrev = new Node(T.I, T.I.data);
            Article Z = T.next();
            String titl = Z.getTitle();
            //if(titl.equals("Test2")){System.out.println("good");}
            double sim = getCosineSimilarity(phrase, Z.getBody());
         
           // System.out.println(sim);
          
            if(sim > 0.001){
               
            // System.out.println(sim+  Z.getTitle());
                MaxHeap.Node in = new MaxHeap.Node(Z, sim);
                H.insert(in);
                
            }
           
        }
        
        String Ans = "";
        if(H.isEmpty()){System.out.println("hehe awkward sometihng went wrong");}
       // System.out.println(H.getMax());
        if(!H.isEmpty()){
            MaxHeap.Node one= H.getMax();
            Ans+="Match 1 with cosine similarity of " + one.CosSim + "\n" +one.data.toString() + "\n";
        }
        if(!H.isEmpty()){
            MaxHeap.Node two =  H.getMax();
            Ans +="Match 2 with cosine similarity of " + two.CosSim + "\n" +two.data.toString() + "\n";
        }
        if(!H.isEmpty()){
            MaxHeap.Node three= H.getMax();
            Ans +="Match 3 with cosine similarity of " + three.CosSim + "\n" +three.data.toString() + "\n";
        }
        if(Ans.equals("")){return"There are no matching articles.";}
        return Ans;
    }
    
    
    public static void main(String args[]){
        ArticleTable L = new ArticleTable(); 
         Article q = new Article("The case of the test case, by Kai Bernardini",
                            "It was an average day with average tests");
     L.insert(q);
          Article q2 = new Article("Another Test!", "this one is less cool :(");
           Article q3 = new Article("Another one? you cant be serious", "this one is less cool :( and I don't like it at all");
           Article q4 = new Article(" you cant be serious", "this one is less cool :( and I don't like it at all but who am I to judge as I am nothing but a unit test");
        L.insert(q2);
       L.insert(q3);
        L.insert(q4);
        System.out.println(phraseSearch("this one is less cool",L));
        
    }
    
    
    
    
    
    
    
}