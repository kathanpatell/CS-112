package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */

public class LittleSearchEngine 
{
    /**
     * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
     * an array list of all occurrences of the keyword in documents. The array list is maintained in 
     * DESCENDING order of frequencies.
     */
    
    HashMap<String,ArrayList<Occurrence>> keywordsIndex;
    
    /**
     * The hash set of all noise words.
     */
    
    HashSet<String> noiseWords;

    private Scanner inputWord;
    
    /**
     * Creates the keyWordsIndex and noiseWords hash tables.
     */
    
    public LittleSearchEngine() 
    {
        keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
        noiseWords = new HashSet<String>(100,2.0f);
    }
    
    /**
     * Scans a document, and loads all keywords found into a hash table of keyword occurrences
     * in the document. Uses the getKeyWord method to separate keywords from other words.
     * 
     * @param docFile Name of the document file to be scanned and loaded
     * @return Hash table of keywords in the given document, each associated with an Occurrence object
     * @throws FileNotFoundException If the document file is not found on disk
     */
    
    public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) throws FileNotFoundException 
    {
        HashMap<String,Occurrence> indexForKeyword = new HashMap<>(1000, 2.0f);  
        inputWord = new Scanner(new File(docFile));
        
        while (inputWord.hasNext()) 
        {
            String word1 = inputWord.next();
            word1 = getKeyword(word1);
            
            if (word1 != null) 
            {                                   
                Occurrence occurrence1 = indexForKeyword.get(word1);
                
                if (occurrence1 != null) 
                {                                
                    occurrence1.frequency++;                               
                }
                
                else 
                {                                                
                    occurrence1 = new Occurrence(docFile, 1);
                    indexForKeyword.put(word1, occurrence1);
                }
            }
        }
      
        return indexForKeyword;
    }
    
    /**
     * Merges the keywords for a single document into the master keywordsIndex
     * hash table. For each keyword, its Occurrence in the current document
     * must be inserted in the correct place (according to descending order of
     * frequency) in the same keyword's Occurrence list in the master hash table. 
     * This is done by calling the insertLastOccurrence method.
     * 
     * @param kws Keywords hash table for a document
     */
    
    public void mergeKeywords(HashMap<String,Occurrence> kws) 
    {
        for (Map.Entry<String, Occurrence> entry : kws.entrySet()) 
        {
            String keyword1 = entry.getKey();
            Occurrence occurringWords = entry.getValue();
           
            ArrayList<Occurrence> wordsList = keywordsIndex.get(keyword1);
            
            if (wordsList != null) 
            {                      
                wordsList.add(occurringWords);                     
                insertLastOccurrence(wordsList);         
            }
            
            else 
            {                               
                wordsList = new ArrayList<>();           
                wordsList.add(occurringWords);                      
                keywordsIndex.put(keyword1, wordsList);   
            }
        }
    }
    
    /**
     * Given a word, returns it as a keyword if it passes the keyword test,
     * otherwise returns null. A keyword is any word that, after being stripped of any
     * trailing punctuation(s), consists only of alphabetic letters, and is not
     * a noise word. All words are treated in a case-INsensitive manner.
     * 
     * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
     * NO OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
     * 
     * If a word has multiple trailing punctuation characters, they must all be stripped
     * So "word!!" will become "word", and "word?!?!" will also become "word"
     * 
     * See assignment description for examples
     * 
     * @param word Candidate word
     * @return Keyword (word without trailing punctuation, LOWER CASE)
     */
    
    public String getKeyword(String word) 
    {
        word = word.toLowerCase();
        
        while (true) 
        {
            if (word.length() == 0) 
            {   
                break;
            }
            
            char lastPartofWord = word.charAt(word.length()-1);
            
            if (lastPartofWord == '?' || lastPartofWord == ':' || lastPartofWord == '.'|| lastPartofWord ==  ',' || lastPartofWord == '!' || lastPartofWord ==  ';') 
            {
                word = word.substring(0, word.length()-1);
            }
            
            else 
            {
                break;
            }
        }
        
        for (int i=0; i < word.length(); i++) 
        {
            char firstPartofWord = word.charAt(i);
            
            if (!Character.isLetter(firstPartofWord)) 
            {
                word = null;
                break;
            }
        }
        
        if (word != null && noiseWords.contains(word)) 
        {
            word = null;
        }
        
        if ((word != null) && (word.length() == 0)) 
        {
            word = null;
        }
       
        return word;
    }
    
    /**
     * Inserts the last occurrence in the parameter list in the correct position in the
     * list, based on ordering occurrences on descending frequencies. The elements
     * 0..n-2 in the list are already in the correct order. Insertion is done by
     * first finding the correct spot using binary search, then inserting at that spot.
     * 
     * @param occs List of Occurrences
     * @return Sequence of mid point indexes in the input list checked by the binary search process,
     *         null if the size of the input list is 1. This returned array list is only used to test
     *         your code - it is not used elsewhere in the program.
     */
    
    public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) 
    {
         ArrayList<Integer> midpointsList = new ArrayList<>();
            
         int midpointIndex;
         int lowerLimit = 0;
         int upperLimit = occs.size() - 2;
         int insertingIndex;
         int insertingFrequency = occs.get(occs.size() - 1).frequency;
           
         while (true) 
         {                                              
             midpointIndex = (upperLimit + lowerLimit) / 2;
             midpointsList.add(midpointIndex);
                
             Occurrence occurringMidpoint = occs.get(midpointIndex);
               
             if (occurringMidpoint.frequency == insertingFrequency) 
             {       
                 insertingIndex = midpointIndex;
                 break;
             }
                
             else if (occurringMidpoint.frequency < insertingFrequency) 
             {   
                 upperLimit = midpointIndex - 1;                          
                    
                 if (lowerLimit > upperLimit) 
                 {
                     insertingIndex = midpointIndex;                       
                     break;
                 }
             }
                
             else 
             {                                                
                 lowerLimit = midpointIndex + 1;                         
                    
                 if (lowerLimit > upperLimit) 
                 {
                     insertingIndex = midpointIndex + 1;                  
                     break;
                 }
             }
         }
           
         if (insertingIndex != occs.size() - 1) 
         {
             Occurrence remainingVal = occs.get(occs.size() - 1);             
             occs.remove(occs.size() - 1);                           
             occs.add(insertingIndex, remainingVal);                        
         }
            
         return midpointsList;
    }
    
    /**
     * This method indexes all keywords found in all the input documents. When this
     * method is done, the keywordsIndex hash table will be filled with all keywords,
     * each of which is associated with an array list of Occurrence objects, arranged
     * in decreasing frequencies of occurrence.
     * 
     * @param docsFile Name of file that has a list of all the document file names, one name per line
     * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
     * @throws FileNotFoundException If there is a problem locating any of the input files on disk
     */
    
    public void makeIndex(String docsFile, String noiseWordsFile) throws FileNotFoundException 
    {
        Scanner inputWord = new Scanner(new File(noiseWordsFile));
        
        while (inputWord.hasNext()) 
        {
            String word = inputWord.next();
            noiseWords.add(word);
        }
        
        inputWord = new Scanner(new File(docsFile));
        
        while (inputWord.hasNext()) 
        {
            String wordFile = inputWord.next();
            HashMap<String,Occurrence> kws = loadKeywordsFromDocument(wordFile);
            mergeKeywords(kws);
        }
        
        inputWord.close();
    }
    
    /**
     * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
     * document. Result set is arranged in descending order of document frequencies. 
     * 
     * Note that a matching document will only appear once in the result. 
     * 
     * Ties in frequency values are broken in favor of the first keyword. 
     * That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same 
     * frequency f1, then doc1 will take precedence over doc2 in the result. 
     * 
     * The result set is limited to 5 entries. If there are no matches at all, result is null.
     * 
     * See assignment description for examples
     * 
     * @param kw1 First keyword
     * @param kw1 Second keyword
     * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
     *         frequencies. The result size is limited to 5 documents. If there are no matches, 
     *         returns null or empty array list.
     */
    
    public ArrayList<String> top5search(String kw1, String kw2) 
    {
        kw1 = kw1.toLowerCase();
        kw2 = kw2.toLowerCase();
        
        ArrayList<String> lastDoc = new ArrayList<>();  
        HashMap<String, String> outputDocument = new HashMap<>();                        
        ArrayList<Occurrence> firstOccurrence = keywordsIndex.get(kw1);
        ArrayList<Occurrence> secondOccurrence = keywordsIndex.get(kw2);
        
        int firstIndex = (firstOccurrence == null) ? - 1 : 0;                        
        int secondIndex = (secondOccurrence == null) ? - 1 : 0;
      
        while ((lastDoc.size() < 5) && (firstIndex >= 0 || secondIndex >= 0)) 
        {  
            Occurrence occurrence1; 
            Occurrence OccurringFirst  = (firstIndex >= 0) ? firstOccurrence.get(firstIndex) : null;
            Occurrence OccurringSecond = (secondIndex >= 0) ? secondOccurrence != null ? secondOccurrence.get(secondIndex) : null : null;
                                                  
            if (OccurringFirst == null) 
            {                                        
                occurrence1 = OccurringSecond;                                        
                secondIndex++;                                               
            }
            
            else if (OccurringSecond == null) 
            {                                  
                occurrence1 = OccurringFirst;
                firstIndex++;
            }
            
            else 
            {                                                     
                if (OccurringFirst.frequency >= OccurringSecond.frequency) 
                {              
                    occurrence1 = OccurringFirst;
                    firstIndex++;
                }
                
                else 
                {
                    occurrence1 = OccurringSecond;
                    secondIndex++;
                }
            }
            
            if (occurrence1 != null) 
            {
                if (!outputDocument.containsKey(occurrence1.document)) 
                {
                    lastDoc.add(occurrence1.document);            

                    outputDocument.put(occurrence1.document, occurrence1.document);
                } 
                
                else { }  
            }
          
            if ((firstIndex >= 0) && (firstIndex == firstOccurrence.size())) 
            {             
                firstIndex = -1;
            }
            
            if (secondOccurrence != null && (secondIndex >= 0) && (secondIndex == secondOccurrence.size())) 
            {             
                secondIndex = -1;
            }
        }
       
        return (lastDoc.size() == 0) ? null : lastDoc;  
    }
}
