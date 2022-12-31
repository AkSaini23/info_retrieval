import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class QueryExpansion {
    public ArrayList<String> splitString(String query) {
        ArrayList<String> words_in_query = new ArrayList<>();
        StringBuilder temp = new StringBuilder();
        //splitting our query into single words and storing it into a arraylist
        for (int x = 0; x < query.length(); x++) {
            if (Character.isWhitespace(query.charAt(x))) {
                words_in_query.add(String.valueOf(temp));
                temp = new StringBuilder("");
            } else {
                temp.append(query.charAt(x));
            }
            if (x == query.length() - 1) {
                words_in_query.add(String.valueOf(temp));
            }
        }
        return words_in_query;
    }


    public ArrayList<ArrayList<String>> expendQuery(String query, ArrayList<String> words_in_query) {

        System.out.println(words_in_query);

        //list stores the synonym of words from words_in_query list
        ArrayList<ArrayList<String>> synonym = new ArrayList<>();
        for (String wordForm : words_in_query) {
            //setting path for the WordNet Directory
            File f = new File("WordNet\\2.1\\dict");
            System.setProperty("wordnet.database.dir", f.toString());

            WordNetDatabase database = WordNetDatabase.getFileInstance();
            Synset[] synsets = database.getSynsets(wordForm);
            //  Display the word forms and definitions for synsets retrieved

            if (synsets.length > 0) {
                ArrayList<String> al = new ArrayList<String>();

                // add elements to al, including duplicates
                for (Synset synset : synsets) {
                    String[] wordForms = synset.getWordForms();
                    al.addAll(Arrays.asList(wordForms));
                }
                //removing duplicates
                HashSet<String> hs = new HashSet<>(al);
                al.clear();
                al.addAll(hs);

                //adding all synonym to tempList
                synonym.add(al);
            } else {
                synonym.add(new ArrayList<>());
            }
        }
        System.out.println(synonym);
        return synonym;
    }

    public void newQueryWithSynonym(String query) {
        ArrayList<ArrayList<String>> newQueries = new ArrayList<>();
        ArrayList<String> words_in_query = splitString(query);
        ArrayList<ArrayList<String>> synonym = expendQuery(query, words_in_query);
        newQueries.add(words_in_query);
        ArrayList<ArrayList<String>> tempQuery = new ArrayList<>();
        System.out.println(newQueries);
        int count = 0;
        for (ArrayList<String> list : synonym) {
            if (!list.isEmpty()) {
                for (String word : list) {
                    words_in_query.remove(count);
                    words_in_query.add(count, word);
                    newQueries.add(words_in_query);
                    System.out.println(newQueries);
                }
            }
            count++;

        }


    }

}

