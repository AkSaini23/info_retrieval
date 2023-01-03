import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

public class QueryExpansion {
    ArrayList<ArrayList<String>> newQueries;
    public ArrayList<ArrayList<String>> expendQuery(ArrayList<String> words_in_query) {

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
                //removing the word that appear in or query originally
                al.remove(wordForm);
                //adding all synonym to tempList
                synonym.add(al);
            } else {
                synonym.add(new ArrayList<>());
            }
        }


        return synonym;
    }

    public ArrayList<String> newQueryWithSynonym(ArrayList<String> words_in_query) {
        newQueries = new ArrayList<>(); //New Query made by synonym
        ArrayList<ArrayList<String>> synonym = expendQuery(words_in_query); //contains synonym of words in our query
        newQueries.add(words_in_query);
        ArrayList<String> temp = new ArrayList<>(); // temp list to store words of query

        int count = 0;


        for (ArrayList<String> list : synonym) {
            if (!list.isEmpty()) {
                for (String word : list) {
                    String tempStr=words_in_query.get(count);
                    words_in_query.set(count, word); // replace it with synonym
                    temp.addAll(words_in_query);
                    newQueries.add(temp); // add the temp query list to our newQueries list
                    temp = new ArrayList<>();
                    words_in_query.set(count,tempStr);
                }
            }
            count++;
        }
        //converting list values into string Example [What,is,your,name]->[What is your name]
        ArrayList<String> queries = new ArrayList<>();
        StringBuilder tempStr = new StringBuilder();
        for (ArrayList<String> list : newQueries) {
            for (String str : list) {
                tempStr.append(str);
                tempStr.append(" ");
            }
            if (!queries.contains(tempStr)) {
                queries.add(String.valueOf(tempStr));
            }
            tempStr = new StringBuilder();
        }
        return queries;
    }

}

