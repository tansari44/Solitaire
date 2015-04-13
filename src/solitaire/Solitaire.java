package solitaire;

import java.io.IOException;
import java.util.Scanner;
import java.util.Random;

/**
 * This class implements a simplified version of Bruce Schneier's Solitaire Encryption algorithm.
 *
 * @author RU NB CS112
 */
public class Solitaire {
  
    /**
     * Circular linked list that is the deck of cards for encryption
     */
    CardNode deckRear;
  
    /**
     * Makes a shuffled deck of cards for encryption. The deck is stored in a circular
     * linked list, whose last node is pointed to by the field deckRear
     */
    public void makeDeck() {
        // start with an array of 1..28 for easy shuffling
        int[] cardValues = new int[28];
        // assign values from 1 to 28
        for (int i=0; i < cardValues.length; i++) {
            cardValues[i] = i+1;
        }
      
        // shuffle the cards
        Random randgen = new Random();
             for (int i = 0; i < cardValues.length; i++) {
                int other = randgen.nextInt(28);
                int temp = cardValues[i];
                cardValues[i] = cardValues[other];
                cardValues[other] = temp;
            }
       
        // create a circular linked list from this deck and make deckRear point to its last node
        CardNode cn = new CardNode();
        cn.cardValue = cardValues[0];
        cn.next = cn;
        deckRear = cn;
        for (int i=1; i < cardValues.length; i++) {
            cn = new CardNode();
            cn.cardValue = cardValues[i];
            cn.next = deckRear.next;
            deckRear.next = cn;
            deckRear = cn;
        }
    }
  
    /**
     * Makes a circular linked list deck out of values read from scanner.
     */
    public void makeDeck(Scanner scanner)
    throws IOException {
        CardNode cn = null;
        if (scanner.hasNextInt()) {
            cn = new CardNode();
            cn.cardValue = scanner.nextInt();
            cn.next = cn;
            deckRear = cn;
        }
        while (scanner.hasNextInt()) {
            cn = new CardNode();
            cn.cardValue = scanner.nextInt();
            cn.next = deckRear.next;
            deckRear.next = cn;
            deckRear = cn;
        }
    }
  
    /**
     * Implements Step 1 - Joker A - on the deck.
     */
    void jokerA() {
        CardNode curr = deckRear.next;
        if(deckRear.cardValue == 27){
            deckRear.cardValue = curr.cardValue;
            curr.cardValue = 27;
        }
        while(curr != deckRear){
            if(curr.cardValue == 27){
                curr.cardValue = curr.next.cardValue;
                curr.next.cardValue = 27;
                break;
            }else{
                curr = curr.next;
            }
          
        }
      
    }
  
    /**
     * Implements Step 2 - Joker B - on the deck.
     */
    void jokerB() {
        CardNode prev = deckRear.next;
        CardNode curr = prev.next;
      
        while(prev != deckRear){
            if(prev.cardValue == 28){
               prev.cardValue = curr.cardValue;
               curr.cardValue = curr.next.cardValue;
               curr.next.cardValue = 28;
               break;
            }else{
            	
                prev = curr;
                curr = curr.next;
            }
          
        }
    }
  
    /**
     * Implements Step 3 - Triple Cut - on the deck.
     */
    void tripleCut() {
        CardNode curr = deckRear.next;
        CardNode end = null;
        CardNode front = null;
        CardNode temp = null;
        boolean twenty_seven = false;
        
        while(curr.next.cardValue != 27 || curr.next.cardValue != 28){
        	curr = curr.next;
        	
        }
        if(curr.next.cardValue == 27){
        	twenty_seven = true;
        }
        end = curr;
        
        if(twenty_seven){
        	while(curr.cardValue != 28){
        		curr = curr.next;
        	}
        	front = curr.next;
        	
        }else{
        	while(curr.cardValue != 27){
        		curr = curr.next;
        	}
        	temp = curr;
        	front = curr.next;
        }
        curr = deckRear;
        
        
        temp.next = curr.next;
        curr.next = end.next;
        end.next = front;

        
    }
  
    /**
     * Implements Step 4 - Count Cut - on the deck.
     */
    void countCut() {      
        CardNode curr = deckRear;
        CardNode prev = deckRear.next;
        int count = 1;
        CardNode temp = null;
        
        while(prev.next != curr){
        		count++;
                prev = prev.next;
                if(count == curr.cardValue){
                	temp = prev;
                }

            }
        prev.next = curr.next;
        curr.next = temp.next;
        temp.next = curr;
    
        
      
    }
  
        /**
         * Gets a key. Calls the four steps - Joker A, Joker B, Triple Cut, Count Cut, then
         * counts down based on the value of the first card and extracts the next card value
         * as key, but if that value is 27 or 28, repeats the whole process until a value
         * less than or equal to 26 is found, which is then returned.
         *
         * @return Key between 1 and 26
         */
    int getKey() {
    	jokerA();
        jokerB();
        tripleCut();
        countCut();
        
        int tmp = 0;
        CardNode curr = deckRear.next;
        
        
        
        return tmp;
        
    }
  
    /**
     * Utility method that prints a circular linked list, given its rear pointer
     *
     * @param rear Rear pointer
     */
    private static void printList(CardNode rear) {
        if (rear == null) {
            return;
        }
        System.out.print(rear.next.cardValue);
        CardNode ptr = rear.next;
        do {
            ptr = ptr.next;
            System.out.print("," + ptr.cardValue);
        } while (ptr != rear);
        System.out.println("\n");
    }

    /**
     * Encrypts a message, ignores all characters except upper case letters
     *
     * @param message Message to be encrypted
     * @return Encrypted message, a sequence of upper case letters only
     */
    public String encrypt(String message) {  
    	char ch;
    	int c;
    	int key;
    	int total;
    	
    	String input = message.replaceAll("\\W", "");
    	char[] charArray = new char[input.length()];
    	
    	for(int i = 0; i < input.length() - 1; i++){
    		ch = input.charAt(i);
    		c = ch-'A'+1;
    		key = getKey();
    		
    		total = key + c;
    		
    		if(total > 26){
    			total = total - 26;
    			ch =  (char) ('A'-1 + total);
    		}else{
    			ch =  (char) ('A'-1 + total);
    		}
    		charArray[i] = ch;
    	}
    	String encryptedInput = new String(charArray);
    	
        return encryptedInput;
    }
  
    /**
     * Decrypts a message, which consists of upper case letters only
     *
     * @param message Message to be decrypted
     * @return Decrypted message, a sequence of upper case letters only
     */
    public String decrypt(String message) {
    	char ch;
    	int c;
    	int key;
    	int total;
    	
    	char[] charArray = new char[message.length()];
    	
    	for(int i = 0; i < message.length(); i++){
    		ch = message.charAt(i);
    		c = ch-'A'+1;
    		key = getKey();
    		
    		if(c > key){
    			total = c - key;
    		}else{
    			c = c + 26;
    			total = c - key;
    		}
    		ch =  (char) ('A'-1 + total);
    		charArray[i] = ch;
    	}
    	String decryptedInput = new String(charArray);
    	
        return decryptedInput;
    }
}
