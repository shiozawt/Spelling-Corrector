package spell;

public class Trie implements ITrie {
    TrieNode root = new TrieNode();
    int nodeCount = 1;
    int wordCount = 0;

    @Override
    public void add(String word) {
        TrieNode node = root;
        word.toLowerCase();

        for (int i = 0; i < word.length(); ++i) {
            int j = word.charAt(i);
            char c = (char) (j - 'a');

            if (node.children[c] == null) {
                node.children[c] = new TrieNode();
                nodeCount += 1;
                if (i == word.length() - 1) {
                    if (node.children[c].frequency == 0) {
                        wordCount += 1;
                    }
                    node.children[c].incrementValue();
                }
            } else {
                if (i == word.length() - 1) {
                    if (node.children[c].frequency == 0) {
                        wordCount += 1;
                    }
                    node.children[c].incrementValue();
                }
            }
            node = node.children[c];
        }
    }

    @Override
    public INode find(String word) {
        TrieNode node = root;
        word = word.toLowerCase();

        for (int i = 0; i < word.length(); i++) {
            int j = word.charAt(i);
            char c = (char) (j - 'a');

            if (node.children[c] == null) {
                return null;
            } else {
                if (word.length() - 1 == i) {
                    if (node.children[c].frequency != 0) {
                        return node.children[c];
                    } else {
                        return null;
                    }
                } else {
                    node = node.children[c];
                }
            }
        }
        return null;
    }

    @Override
    public int getWordCount() {
        return wordCount;
    }

    @Override
    public int getNodeCount() {
        return nodeCount;
    }

    @Override
    public int hashCode() {
        int maxVal = 0;
        TrieNode node = this.root;
        for (int i = 0; i < 26; i++) {
            if (node.children[i] != null) {
                maxVal = i;
            }
        }
        return maxVal * nodeCount * wordCount;
    }

    @Override
    public boolean equals(Object obj) {
        Trie trie = (Trie)obj;
        if(trie.nodeCount != this.nodeCount){
            return false;
        }
        if(trie.wordCount != this.wordCount){
            return false;
        }
        return equals_helper(this.root, trie.root);
    }

    boolean equals_helper(TrieNode root1, TrieNode root2) {
        for (int i = 0; i < 26; i++){
            if (i == 1){}
            else {
                if (root1.children[i] == null) {
                    if (root2.children[i] != null) {
                        return false;
                    }
                } else {
                    if (root2.children[i] == null) {
                        return false;
                    } else {
                        if (root2.children[i].frequency != root1.children[i].frequency) {
                            return false;
                        }
                        return equals_helper(root1.children[i], root2.children[i]);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder currWord = new StringBuilder();
        StringBuilder output = new StringBuilder();
        strhelp(root, currWord, output);
        return output.toString();
    }

    void strhelp(INode node, StringBuilder currWord, StringBuilder output){
        if(node.getValue() > 0){
            output.append(currWord);
            output.append('\n');
        }

        for (int i = 0; i < 26; i++){
            INode child = node.getChildren()[i];

            if(child != null){
                char val = (char)(i + 'a');
                currWord.append(val);
                strhelp(child, currWord, output);
                currWord.deleteCharAt(currWord.length() -1);
            }
        }
    }
}

class TrieNode implements INode{
    int frequency = 0;
    int alpha = 26;
    TrieNode[] children = new TrieNode[26];

    @Override
    public int getValue() {
        return frequency;
    }

    @Override
    public void incrementValue() {
        frequency +=1;
    }

    @Override
    public INode[] getChildren() {
        return children;
    }
}