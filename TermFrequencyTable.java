/*
 * Author: Kai Bernardini (Kaidb@bu.edu
 * TermFrequencyTable.java
 * Purpose: This takes in two strings (assumed to be documents) makes verything 
 * lowercase, strips it of punctuation, stems the words and treats the strings as a vector
 * Once in vector form where the value for each Xi is assumed to be the frquencey in which
 * that term appears in a string. From there, The angle is measured between an inner product 
 * space of the two vecotd (measures the angle between them and outputs a score based on this (where 90
 * degrees = 0 (orthogonal vectors, entierly independnt, and 1 => the vectors are linearly dependent. 
 * (calculates the cosine simularity (inner product without magnitude
 * 
 * Some assumptions: "" = <0,...>
 * 
 */

 import java.util.*;
import java.util.Arrays;
import java.util.List; 
public class TermFrequencyTable{
    
    private final int SIZE = 101; 
    private Node [] Table = new Node[SIZE];
    private int key= 103; 
    
    private int hash(String s){ // converts a string to a number so it can be hasehd
        int ret = 0;
        s= s.toLowerCase();          
        char[] c = s.toCharArray();
        int len = c.length;
        char[] nope = {'.',',','!','?','"','/'};
        //char[] nope = {};
        for(int i =0; i<len; i++){
            if(inChar(c[i], nope)){ 
                int b = (int) c[i];
                ret+= (b*key)%SIZE;}
        }
        ret = ret %SIZE;
        if(ret <0){System.out.println("OVERLOW");}
        return ret;
    }
    private boolean inChar(char a, char[] b){
       // if(b.length==0){return false;}
        for(int i=0; i< b.length; i++){
            if(a == b[i]){return true;}
        }
        return false;}
    //bucket node for overarching hash table
    public class Node{
        public  String term;
        public  int[] termFreq = {0,0};  // this gives the term frequency in each of two documents for this term
        public  Node next; 
        
        public Node(String data, Node n, int docNum) {//constructor for node and data
            this.term = data;
            this.next = n;
            this.termFreq[docNum] +=1;
        }
        
    }
    /*------------This Method Inserts into a linked list unless the element
     * already exits in which case it modifies the curent count of th item and moves on----------*/
    public void insert(String term, int docNum) {
        if(docNum != 1 && docNum!=0){
            System.out.println("error invalid input... logicaly guessing document!");}                                                         
        else{ 
            String[] Term = term.split(" ");
           // System.out.println("Term is : " + Arrays.toString(Term));
            for(int i =0; i< Term.length; i++){
                if(Term[i].equals(" ") || Term[i].equals("")){continue;}
            int k  = hash(Term[i]);
            Table[k] = insertHelper(Table[k], Term[i], docNum);
            }
        }
    }
    
    private Node insertHelper(Node p, String term, int docNum){
        if(p==null){return new Node(term, p, docNum);}
        else if(p.term.equals(term)){p.termFreq[docNum] +=1; return p;}
        else{p.next = insertHelper(p.next, term, docNum); return p;}}
    
    // returns the cosine similarity of two documents
    // stored in the table (assumed to be vectors 
    double magn0=0.0;
    double magn1=0.0;
    double cosineSimilarity() { 
        int dotp =0;
        for(int i =0; i< SIZE; i++){
            dotp += dot(Table[i]);
        }
        double dot = dotp*1.0;
      //  double ans = dot/(Math.pow((magn0*magn1), .5));
        double ans = dot/((Math.sqrt(magn0)* Math.sqrt(magn1)));
        return ans;
    }
    double dot(Node p){
        if(p==null){return 0;}
        else{int a= p.termFreq[0];
            int b = p.termFreq[1];
//System.out.println("Article " + p.term +" " +a+" " + b+"");
            magn0+=(a*a);
            magn1+=(b*b);
            
            return (a * b) + dot(p.next);}
    }
    public static void main(String[] args){
        System.out.println("Unit test!");
        TermFrequencyTable  L = new TermFrequencyTable();
        System.out.println("testing 'A A B B' ");
        L.insert("A",0);
        L.insert("A",0);
        L.insert("B",0);
        L.insert("B",0);
        L.insert("A",1);
        L.insert("A",1);
        L.insert("B",1);
        L.insert("B",1);
        double b = L.cosineSimilarity();
        System.out.println("Should be: 1.0");
        System.out.println(b);
        if(b==1.0){System.out.println("YEAAAAAAAH BUDDY");}
        
        System.out.println();
        System.out.println("Test number2! Testing A B (0) and C D (1)");
        TermFrequencyTable  D = new TermFrequencyTable();
          D.insert("A",0);
        D.insert("B",0);
         D.insert("C",1);
        D.insert("D",1);
        b = D.cosineSimilarity();
        System.out.println("Should be: 0.0");
        System.out.println(b);
        if(b==0.0){System.out.println("YEAAAAAAAH BUDDY");}
        System.out.println();
        System.out.println("3rd  test! 'CS112 HW10' and 'CS112 HW10 HW10'");
        TermFrequencyTable  A = new TermFrequencyTable();
         A.insert("HW10",0);
          A.insert("CS112",0);
          A.insert("HW10",1);
          A.insert("HW10",1);
          A.insert("CS112",1);
           A.insert("HW10",1);
          A.insert("HW10",1);
          A.insert("CS112",1);
           A.insert("HW10",1);
          A.insert("HW10",1);
          A.insert("CS112",1);
           A.insert("HW10",1);
          A.insert("HW10",1);
          A.insert("CS112",1);
           b = A.cosineSimilarity();
        System.out.println("Should be: 0.9487");
        System.out.println(b);
        if(b== 0.9487){System.out.println("YEAAAAAAAH BUDDY");}
        
        System.out.println("CLOSE ENOUGH");
      
    
TermFrequencyTable  G = new TermFrequencyTable();
G.insert("CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112   HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 CS112 HW10 ", 0);
    G.insert("CS112 HW10",1);
  System.out.println(G.cosineSimilarity());          }


}




