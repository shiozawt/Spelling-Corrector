package spell;

import com.sun.xml.internal.fastinfoset.util.CharArray;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SpellCorrector implements ISpellCorrector {
    Trie trie = new Trie();
    Set<String> dictionary = new TreeSet<>();

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        File newFile = new File(dictionaryFileName);
        Scanner input = new Scanner(newFile);

        while(input.hasNextLine()){
            String newStr = new String();
            String str = new String(input.nextLine());
            for (int i = 0; i < str.length(); i++){
                if (!Character.isWhitespace(str.charAt(i))){
                    newStr = newStr + str.charAt(i);
                }
                else{
                    newStr = newStr.toLowerCase();
                    trie.add(newStr);
                    newStr = "";
                }
            }
            if(!newStr.isEmpty()){
                newStr = newStr.toLowerCase();
                trie.add(newStr);
            }
        }
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        //test input word to make sure it is real value
        inputWord = inputWord.toLowerCase();

        if (trie.find(inputWord) != null){
            return inputWord;
        }

        // run vector through processes
        Set<String> newSet = new TreeSet<>();
        Set<String> newSet2 = new TreeSet<>();
        newSet = deletion(inputWord, newSet);
        newSet = alteration(inputWord, newSet);
        newSet = transposition(inputWord, newSet);
        newSet = insertion(inputWord, newSet);
        //check these values against find function
        dictionary = checkFind(newSet);
        //create vectors of existing values, and run scenarios based on this.
        if(dictionary.isEmpty()){
            //run tests again

            for(String inputWord2: newSet) {
                newSet2 = deletion(inputWord2, newSet2);
                newSet2 = alteration(inputWord2, newSet2);
                newSet2 = transposition(inputWord2, newSet2);
                newSet2 = insertion(inputWord2, newSet2);
            }
            dictionary = checkFind(newSet2);
            if(dictionary.isEmpty()){
                return null;
            }
            else{
                System.out.print("1");
               String returnVal = matchFunction(dictionary);
               return returnVal;
            }
        }
        else{
            System.out.print("2");

            String returnVal = matchFunction(dictionary);
            return returnVal;
        }
    }

    String matchFunction(Set<String> input){


        INode node = new TrieNode();
        int maxVal = 0;
        String maxStr = new String();
        Vector<String> StringVec = new Vector<>();

        for(String word: input){
            node = trie.find(word);
            if (node.getValue() > maxVal){
                maxVal = node.getValue();
                System.out.print(word + node.getValue() + '\n');
            }
        }

        for(String word: input){
            node = trie.find(word);
            if (node.getValue() == maxVal){
                StringVec.add(word);

            }
        }

        Collections.sort(StringVec);
        return StringVec.elementAt(0);

    }

    Set<String> deletion(String word, Set<String> set){
        for(int i = 0; i < word.length(); ++i){
            String str = new String();
            for(int j = 0; j < word.length(); ++j){
                if(i == j){}
                else{
                    str = str + word.charAt(j);
                }
            }
            set.add(str);
        }
        return set;
    }

    Set<String> alteration(String word, Set<String> set){
        for(int i = 0; i < word.length(); i++){
            for (int j = 0; j < 26; j++){
                char[] c = word.toCharArray();
                char val = (char)(j + 'a');
                c[i] = val;
                set.add(String.valueOf(c));
            }
        }
        return set;
    }

    Set<String> transposition(String word, Set<String> set){
        for(int i = 0; i < word.length() - 1; i++){
            char[] c = word.toCharArray();
            char temp = c[i];
            c[i] = c[i+1];
            c[i+1] = temp;
            set.add(String.valueOf(c));
        }
        return set;
    }

    Set<String> insertion(String word, Set<String> set){

        for(int i = 0; i < word.length() + 1; ++i){
            for(int j = 0; j < 26; ++j){
                StringBuilder str = new StringBuilder(word);
                char val = (char)(j + 'a');
                str.insert(i, val);
                set.add(String.valueOf(str));
            }
        }
        return set;
    }

    Set<String> checkFind(Set<String> newSet) {
        Set<String> str = new TreeSet<>();
        for (String word : newSet) {
            if (word.isEmpty()){}
            else {
                if (trie.find(word) != null) {
                    str.add(word);
                }
            }
        }
        return str;
    }

}
