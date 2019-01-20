import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    public static int wordCount;
    public static int positiveCount;
    public static int negativeCount;

    public static ArrayList<Word> dictionary = new ArrayList<>(2000000);
    public static HashMap<Integer, Word> hmapDictionary = new HashMap<Integer, Word>(2000000);

    public static int inputCount = 80000;
    public static int outputCount = 20000;

    public static void main(String[] args){
        readInput();
    }

    public static void readInput() {
        try {
            // Tanito mintak beolvasasa, ezek alapjan megcsinalni az osztalyokat
            InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(isr);
            ArrayList<String> fulltext = new ArrayList<>(80000);
            ArrayList<Integer> results = new ArrayList<>(80000);
            for (int i = 0; i < inputCount; i++) {
                String tmp = br.readLine();

                if (tmp.isEmpty()){
                    fulltext.add("99999999");
                } else {
                    fulltext.add(tmp);
                }


            }
            for (int i = 0; i < inputCount; i++) {
                results.add(Integer.parseInt(br.readLine()));
            }

            String line = "";
            String[] ids;
            int result;
            boolean contained;

            for (int i = 0; i < inputCount; i++) {
                line = fulltext.get(i);
                result = results.get(i);
                ids = line.split("\\t");
                for (int j = 0; j < ids.length; j++) {
                    //add class if not stored
                    Word temp = new Word(ids[j]);
                    temp.context = result;
                    Integer key;
                    key = Integer.parseInt(ids[j]);

                    contained = false;

                    if (hmapDictionary.containsKey(key)) {
                        contained = true;
                        hmapDictionary.get(key).occured(result);
                        if (hmapDictionary.get(key).context != result) {
                            if (result == 1) {
                                positiveCount++;
                            } else {
                                negativeCount++;
                            }
                        }
                    }

                    if (!contained) {
                        if (result == 1) {
                            positiveCount++;
                        } else {
                            negativeCount++;
                        }
                        hmapDictionary.put(key, temp);
                    }




                    /*for (int k = 0; k < dictionary.size(); k++) {
                        if (temp.equals(dictionary.get(k))) {
                            contained = true;
                            dictionary.get(k).occured(result);
                            if (dictionary.get(k).context != result) {
                                if (result == 1) {
                                    positiveCount++;
                                } else {
                                    negativeCount++;
                                }
                            }
                        }
                    }
                    if (!contained) {
                        if (result == 1) {
                            positiveCount++;
                        } else {
                            negativeCount++;
                        }
                        dictionary.add(temp);
                    }*/
                }

            }

            //wordCount = dictionary.size();
            wordCount = hmapDictionary.size();


            //System.out.println(wordCount + " szo, ebbol " + positiveCount + " pozitiv es "+ negativeCount +" negativ szo van");


            //tesztmintak:

            ArrayList<String> fullTestText = new ArrayList<>(20000);

            for (int i = 0; i < outputCount; i++) {
                String tmp = br.readLine();
                if (tmp.isEmpty()){
                    fullTestText.add("99999999");
                } else {
                    fullTestText.add(tmp);
                }
            }

            double positiveProduct = 0.0;
            double negativeProduct = 0.0;

            for (int i = 0; i < outputCount; i++) {
                line = fullTestText.get(i);
                ids = line.split("\\s+");


                for (int j = 0; j < ids.length; j++) {
                    Word temp = new Word(ids[j]);
                    Integer key = Integer.parseInt(ids[j]);

                    if (hmapDictionary.containsKey(key)) {
                        if (positiveProduct == 0.0) {
                            positiveProduct = calcPositiveProbablity(hmapDictionary.get(key));
                        } else {
                            positiveProduct = positiveProduct*calcPositiveProbablity(hmapDictionary.get(key));
                        }

                        if (negativeProduct == 0.0) {
                            negativeProduct = calcNegativeProbablity(hmapDictionary.get(key));
                        } else {
                            negativeProduct = negativeProduct*calcNegativeProbablity(hmapDictionary.get(key));
                        }
                    }


                    /*for (int k = 0; k < dictionary.size(); k++) {
                        if (temp.equals(dictionary.get(k))) {

                            if (positiveProduct == 0.0) {
                                positiveProduct = calcPositiveProbablity(dictionary.get(k));
                            } else {
                                positiveProduct = positiveProduct*calcPositiveProbablity(dictionary.get(k));
                            }

                            if (negativeProduct == 0.0) {
                                negativeProduct = calcNegativeProbablity(dictionary.get(k));
                            } else {
                                negativeProduct = negativeProduct*calcNegativeProbablity(dictionary.get(k));
                            }


                        }
                    }*/
                }

                if ( negativeProduct > (positiveProduct*1.5)) {
                    System.out.println(0);
                } else {
                    System.out.println(1);
                }

                positiveProduct = 0;
                negativeProduct = 0;

            }

            isr.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static class Word {
        public String ID;
        public int context;
        public int positiveOccurance;
        public int negativeOccurance;

        public Word(String id) {
            ID = id;
            negativeOccurance = 1;
            positiveOccurance = 1;
        }

        public void occured(int i){
            if (i == 1) {
                positiveOccurance += 1;
            }else {
                negativeOccurance +=1;
            }
        }

        @Override
        public boolean equals(Object v) {
            boolean retVal = false;

            if (v instanceof Word){
                Word ptr = (Word) v;
                retVal = ptr.ID.equals(this.ID);
            }

            return retVal;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 17 * hash + (this.ID != null ? this.ID.hashCode() : 0);
            return hash;
        }

    }

    public static double calcPositiveProbablity(Word w) {
        double convert = (double)w.positiveOccurance/((double) wordCount+(double) positiveCount);
        return convert;
    }

    public static double calcNegativeProbablity(Word w) {
        double convert = (double)w.negativeOccurance/((double) wordCount+(double) negativeCount);
        return convert;
    }

}
