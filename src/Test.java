import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Test{
    public static void main(String[] args) {
        String wordForm = "cancer";
        //  Get the synsets containing the word form=capicity

        File f = new File("WordNet\\2.1\\dict");
        System.setProperty("wordnet.database.dir", f.toString());
        //setting path for the WordNet Directory

        WordNetDatabase database = WordNetDatabase.getFileInstance();
        Synset[] synsets = (Synset[]) database.getSynsets(wordForm);
        //  Display the word forms and definitions for synsets retrieved

        if (synsets.length > 0) {
            ArrayList<String> al = new ArrayList<String>();
            ArrayList<String> def = new ArrayList<String>();

            // add elements to al, including duplicates
            for (Synset synset : synsets) {
                def.add(synset.getDefinition());
                String[] wordForms = synset.getWordForms();
                al.addAll(Arrays.asList(wordForms));
            }
            //removing duplicates
            HashSet hs = new HashSet(al);
            al.clear();
            al.addAll(hs);

            //showing all synsets
            for (String s : al) {
                System.out.println(s);
            }
            for (String s: def){
                System.out.println(s);
            }
        } else {
            System.err.println("No synsets exist that contain the word form '" + wordForm + "'");
        }
    }
}