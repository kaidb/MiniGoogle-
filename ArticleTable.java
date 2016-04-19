/*
 * ArticleTable.java
 * 
 * This is an unordered list of articles. Dumb, dumb, dumb. You will rewrite this as a binary tree. 
 *
 * Author: Alexander Breen (abreen@bu.edu) and Wayne Snyder (waysnyder@gmail.com)
 * Date: March 24, 2014
 */

public class ArticleTable {
    private final int SIZE = 2503; 
    private Node [] Table = new Node[SIZE];
    
    /*private keys*/
    private final int key = 2521;
    
    
    public Node head = null; //head of master list
    public  Node I = null;    //iterator
    
    /*motivation for creating incresingly large numbers is to increase randomness and reduce collisions 
     * This is essentialy a double hash. */
    
    private int Hash(Article a){  // Uses the title of the article as a key and hasehs the titlte into a number and then hashes that number to a value
        String b = a.getTitle(); 
        return hash(hash(b));}
    private int Hash(String b){  // ALTERNATE VERSION: takes a String assumed to be the title 
        
        return hash(hash(b));
    }
    /**************STRING HASH ****************************************/
    private int hash(String s){ // converts a string to a number so it can be hasehd
        int ret = 0;
        s= s.toLowerCase();
        char[] c = s.toCharArray();
        int len = c.length;
        for(int i =0; i<len; i++){
            int b = (int) c[i];
            ret+= b;
        }
        return ret; 
    }
    /***********************INT-HASH*************************************************/
    private int hash(int k) { 
        // System.out.println("HASH LONG "+k);  //debugging
        int a = (k * key ) % SIZE;
        int b= (int) a;
        if(b> SIZE|| a< 0){System.out.println("ERROR TOO BIG A KEY!");} //for debugging overflow errors 
        return b;
    }
    //   public String toString(){return "null";}
    public int size2(){
        return sizeHelp(head);
    }
    private int sizeHelp(Node P){
        if(P==null){
            return 0;}
        else{
            return 1 + sizeHelp(P.next2);}
    }
    
    
    public int badBucket(){
        int fin = 0;
        int mer = 0;
        int aver = 0;
        int count =0;
        for(int i =0; i< SIZE; i++){
            int buk = countBucket(Table[i]);
            if(buk==0){mer +=1;}
            if(buk!=0){ aver+= buk; count+=1;}
            if(buk>4){fin+=1;
                System.out.println(buk);
                
            }
        }
        System.out.println("The average bucket size is " + (aver/count));
        System.out.println("There are " + mer + " empty buckets :-(  *NOO LOVE NOOOO LOVE NOOO LO-OV-OVE OVEEEEEEE " );
        return fin;
    }
    private int countBucket(Node p){
        if(p==null){return 0;
        }
        else{return 1 + countBucket(p.next);}}
    
    
    public void insert(Article a) {   
        // insert a into the table using the title of a as the hash key
        int k = Hash(a);
        //   System.out.println("INSERT " +k);
        Table[k] = new Node(a, Table[k]);
        head = new Node(a, head);
      
        //  System.out.println(Table[k].data.getTitle() + " ---- " + Table[k].data.getBody()); // debugging purposeses 
    }
    
    //no reason for 2 inserts, ask about this. 
    private Node insertHelper(Article a, Node p) {     // inserts a new node into the list, and returns the result 
        if(!member(a)) {          
            head = new Node(a,p);                            
            return new Node(a,p);}            
            return p;
}




public void remove(String title) {
    delete(title); 
}
/*----------------------------------------------------------------*/
public void delete(String title) { //deletes a Article        
    int k = Hash(title);
    // System.out.println(k);
    Table[k] = deleteHelper(title, Table[k]);
}
private Node deleteHelper(String title, Node p){
    
//         System.out.println(title);  //  <--- debugging 
//        System.out.println(p.data.getTitle());
//        System.out.println(p.data.getTitle().compareTo(title));
//        System.out.println(p.next);
    if(p== null){
        return p;}
    
    else if((p.data.getTitle()).compareTo(title)==0){
        /************ITERATOR DELETE**************/
        head = deleteML(title, head); //deletes the node from the "Master List"    //things I changed 
        return p.next;}
    else{
        p.next= deleteHelper(title, p.next); return p;
    }
}

public  Node deleteML(String title, Node t) {
    if (t == null)                             // Case 1: tree is null
        return t;
    else if (title.compareTo(t.data.getTitle()) == 0)  
        return t.next2; 
    else {
        t.next = deleteML(title, t.next2); 
        return t;
    }
}
/*---------------------------------------------------------------------*/




public Article lookup(String title) { // returns the article with the given title or null if not found
    int k = Hash(title);
    //  System.out.println("LOOKUP " + k);
    // debugging 
    return lookupHelper(title, Table[k]);
}
private Article lookupHelper(String title, Node p){
    if(p==null){return null;}
    else if((p.data.getTitle()).compareTo(title)==0) {return p.data;}
    else{return lookupHelper(title, p.next);}
}
public  class Node { // pointer class
    public Article data;
    public Node next;
    public Node next2;
    //   public int collisions = 0;
    
    
    
    
    public Node(Article data, Node n) {//constructor for node and data
        this.data = data;
        this.next = n;
        this.next2= n; // CHECK IF THIS WORKS *&*&(#)@)@*#)
        
    }
    
    public Node(Article data) {// constructor for just data
        this(data, null);
    }
}


public static String toStrings(Article a){
    if(a==null){return "null";}
    else{return a.toString();}}

//everything is initialized to null. This is ok since we do not allow duplicates

public void reset() {
    
    //System.out.println(head.data + "HEADTEST");//
    I= copy(head);            
}

Node copy( Node p ) {
       if( p == null )
          return null; 
       else 
          return new Node(p.data, copy( p.next )); 
    }  

public void initialize(Article[] A) { // Inserts a bunch of articles into a hash table
    for(int i = 0; i < A.length; ++i) 
        insert(A[i]); 
}

public boolean hasNext() {
    return !(I==null);
}
public void add(Article a) {
    //head = new Node(a, head); 
    insert(a); 
} 


public boolean member(Article a) {
    return (lookup(a.getTitle()) != null); 
}
//        
public boolean member(String title) {
    return (lookup(title) != null); 
}
//    
//        public Article lookup(String title) {
//            Node n = lookup(head,title); 
//            if(n != null)
//                return n.data; 
//            return null; 
//        }

//        private  Node lookup(Node t, String key) {
//            if (t == null)
//                return null;
//            else if (key.compareTo(t.data.getTitle()) == 0) {
//                return t;
//            } else 
//                return lookup(t.next,key); 
//        }
//        
//        public  Node lookup(Node t, Article a) {
//            if (t == null)
//                return null;
//            else if (a.getTitle().compareTo(t.data.getTitle()) == 0) {
//                return t;
//            } else 
//                return lookup(t.next, a);
//        }

//    public int length() {
//        
//        return length(head); 
//    }
//    public int length(Node t) {
//        if(t == null)
//            return 0;
//        else
//            return 1 + length(t.next); 
//    }

// Recursively reconstructs tree without the key n in it

public  Node delete(String title, Node t) {
    if (t == null)                             // Case 1: tree is null
        return t;
    else if (title.compareTo(t.data.getTitle()) == 0)  
        return t.next; 
    else {
        t.next = delete(title, t.next); 
        return t;
    }
}


public Article next() {
    Article a = I.data;
    I = copy(I.next2);
    return a;
}

public String MasterList(){
    return MasHel(head);
}
private String MasHel(Node P){
    if(P==null){return "";}
    else{return P.data.getTitle() + " " + MasHel(P.next2);}
}

public static void main(String[] args){
    
    ArticleTable L = new ArticleTable(); 
    
    System.out.println("Unit Test for ArticleTable:");
    System.out.println();
    
    System.out.println("Test 1: Insert ");
    System.out.println("");
    System.out.println("Should be:");
    
    System.out.println("The case of the test case, by Kai Bernardin");
    System.out.println("============================================");
    System.out.println("It was an average day with average tests");
    System.out.println("");
    Article q = new Article("The case of the test case, by Kai Bernardini",
                            "It was an average day with average tests");
    L.insert(q);
    Article b = L.lookup("The case of the test case, by Kai Bernardini");
    // System.out.println(b.getTitle() + " " + b.getBody());
    System.out.println(b.toString());
    System.out.println("");
    
    System.out.println("Test 2: Insert ");
    Article q2 = new Article("Another Test!", "this one is less cool :(");
    System.out.println("Should be:");
    System.out.println("Another Test");
    System.out.println("=============");
    System.out.println("this one is less cool :(");
    L.insert(q2);
    System.out.println("");
    
    
    Article b2 = L.lookup("Another Test!");
    System.out.println("should not be blank");
    L.insert(b2);
    L.insert(q2);
    L.reset();
    L.next();
    System.out.println(toStrings(b2));
    //System.out.println(b2.getTitle() + " " + b2.getBody());
    System.out.println("");
    System.out.println("Test 2.5");
    
    L.reset();
    while(L.hasNext()) {
        Article a = L.next(); 
        System.out.println(toStrings(a));
    }
    L.delete(b2.getTitle());
    
    System.out.println("");
    System.out.println("Test 3: Delete");
    System.out.println("Should be:");
    System.out.println("null");
    b2 = L.lookup("Another Test!");
    //  System.out.println(b2.getTitle() + " " + b2.getBody());}
    System.out.println(toStrings(b2));
    
    
    
    
    
    
    
} 
}
