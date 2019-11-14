package com.company;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.*;
import static java.util.Objects.*;


public class Main {

    private double maxTime = 0;
    private String slowMethod = "";
    private double minTime = 1000;
    private String fastMethod = "";

    public static void main(String[] args) {
        try {
            new Main(args);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Main(String[] args) throws FileNotFoundException {

        PrintIntroduction();

        Path path = FileSystems.getDefault().getPath("").toAbsolutePath();

        // Si el programa se ejecuta desde src, salir de source para encontrar el file unsorteddict.txt
        if(path.endsWith("src")){
            path = path.getParent();
        }

        String file_to_order = "unsorteddict";

        // Para comprobar si esta en test mode o no, se comprueba una vez el user input
        if(args != null){
            for (String arg : args){
                if (arg.equals("-1")) {
                    file_to_order = "unsortedDictTest";
                    break;
                }
            }
        }

        String ResourcesPath = path + "/Resources";
        // Para encontrar (y crear) el "unsorteddict.txt"
        String file_dict_path = ResourcesPath + "/" + file_to_order + ".txt";
        File file = new File(file_dict_path);

        char[] alphabet = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

        double startTime = System.currentTimeMillis();

        LinkedList <String>[] dictionary = CreateAndSortDictionary(file, alphabet);
        CreateFileWithSortedDictionary(Objects.requireNonNull(dictionary), ResourcesPath);
        AcceptAndProcessUserCommandLineInput(requireNonNull(args), dictionary, alphabet, ResourcesPath);

        double endTime = System.currentTimeMillis();
        System.out.println("Thank you for waiting, all the program has taken: " + ((endTime - startTime)/1000) + " seconds");

        PrintSpeedMethod();

    }

    private void PrintIntroduction() {
        System.out.println("\nPLEASE WAIT");
        System.out.println("____________________________________________________________________________________________________________________________________\n");
        System.out.println("Hello there! You can do plenty of functionalities with this program :)");
        System.out.println("If you write a number, it will be considered as an indux and it will return the word in that index");
        System.out.println("If you write a word, it will return the index related to that word");
        System.out.println("REMEMBER: If in the command line you write a '-1', no matter the position, all the arguments will be analyzed in the test mode file");
        System.out.println("REMEMBER: Whenever a user wants to input a word with an apostrophe they have to surround it with ''");
        System.out.println("____________________________________________________________________________________________________________________________________");
    }

    private LinkedList<String>[] CreateAndSortDictionary(File file, char[] alphabet) {
        int size = 26;
        LinkedList<String>[]dictionary = new LinkedList[size];
        try {
            Scanner scan = new Scanner (file);
            while (scan.hasNextLine()) {
                String s = scan.nextLine();
                String normal = Normalizer.normalize(s, Normalizer.Form.NFD);
                for (int i = 0; i<size; i++){
                    if (Character.toLowerCase(normal.charAt(0))== alphabet[i]){
                        dictionary[i] = SortAndInsertInLinkdictionary(dictionary[i], s, normal);
                    }
                }
            }
            System.out.println();
            scan.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File not found :(");
            return null;
        }
        return dictionary;
    }

    private LinkedList SortAndInsertInLinkdictionary(LinkedList<String> words, String s, String normal){

        double startTime3 = System.currentTimeMillis();
        if (words == null){
            words = new LinkedList<>();
            words.add(0, s);
        }
        else if (normal.compareToIgnoreCase(Normalizer.normalize(words.getFirst(), Normalizer.Form.NFD))< 0){
            words.add(0, s);
        }
        else if (normal.compareToIgnoreCase(Normalizer.normalize(words.getLast(), Normalizer.Form.NFD))> 0){
            words.add(s);
        }
        else{
            IteratorAddingWord(words, s, normal);
        }
        double endTime3 = System.currentTimeMillis();
        if (((endTime3 - startTime3)) > maxTime) {
            maxTime = (endTime3 - startTime3);
            slowMethod = "SortAndInsertInLinkdictionary() Method";
        }
        if (((endTime3 - startTime3)) < minTime) {
            minTime = (endTime3 - startTime3);
            fastMethod = "SortAndInsertInLinkdictionary() Method";
        }
        return words;
    }

    private void IteratorAddingWord(LinkedList<String> words, String s, String normal) {
        ListIterator<String> iterator = words.listIterator();
        while(iterator.hasNext()){
            String element = iterator.next();
            int comparisson = normal.compareToIgnoreCase(Normalizer.normalize(element, Normalizer.Form.NFD));
            if (comparisson < 0){
                iterator.previous();
                break;
            }
        }
        iterator.add(s);
    }

    private void CreateFileWithSortedDictionary(LinkedList[] dictionary, String ResourcesPath){
        double startTime4 = System.currentTimeMillis();
        File file = new File( ResourcesPath + "/sorteddict.txt");
        //Write Content
        FileWriter dict_ordered = null;
        try {
            dict_ordered = new FileWriter(file);
            for (LinkedList list_words : dictionary){

                for (String element : (Iterable<String>) list_words) {
                    dict_ordered.write(element + "\n");
                }
            }
            dict_ordered.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("File not found! :(");
        }
        double endTime4 = System.currentTimeMillis();
        if (((endTime4 - startTime4)) > maxTime) {
            maxTime = (endTime4 - startTime4);
            slowMethod = "CreateFileWithSortedDictionary() Method";
        }
        if (((endTime4 - startTime4)) < minTime) {
            minTime = (endTime4 - startTime4);
            fastMethod = "CreateFileWithSortedDictionary() Method";
        }
    }

    private void AcceptAndProcessUserCommandLineInput(String[] args, LinkedList<String>[] dictionary, char[] alphabet, String ResourcesPath) throws FileNotFoundException {
        double startTime2 = System.currentTimeMillis();
        if (args.length > 10) {
            System.out.println("Too many arguments, max number of arguments: 10");
        } else {
            String pattern = "-?\\d+";
            for (String arg : args) {
                if (arg.equals("-1")) ExecuteTestMode(ResourcesPath);

                else if (arg.matches(pattern)) {
                    int position = Integer.parseInt(arg) - 1;
                    if (position < 0) {
                        System.out.println(arg + ": " + "is a negative number, please introduce a positive number.");
                    } else {
                        for (int j = 0; j < alphabet.length; j++) {
                            if (position - dictionary[j].size() < 0) {
                                System.out.println(arg + ": " + dictionary[j].get(position));
                                break;
                            } else {
                                position = position - dictionary[j].size();
                            }
                            if (j == 25) {
                                System.out.println(arg + ": " + "Index not in dictionary");
                            }
                        }
                    }
                } else {
                    if (!arg.matches(".*\\d.*")) {
                        for (int j = 0; j < alphabet.length; j++) {
                            if (Character.toLowerCase(arg.charAt(0)) == alphabet[j]) {
                                int position = dictionary[j].indexOf(arg);
                                if (position == -1) {
                                    System.out.println(arg + ": " + position);
                                    break;
                                } else {
                                    position++;
                                    for (int g = j - 1; g >= 0; g--) {
                                        position += dictionary[g].size();
                                    }
                                    System.out.println(arg + ": " + position);
                                    break;
                                }
                            }
                            if (j == 25) {
                                System.out.println(arg + ": " + "-1");
                            }
                        }
                    } else {
                        System.out.println(arg + ": is not a String");
                    }
                }
            }
        }
        double endTime2 = System.currentTimeMillis();
        if ((endTime2 - startTime2) > maxTime) {
            maxTime = (endTime2 - startTime2);
            slowMethod = "AcceptAndProcessUserCommandLineInput() Method";
        }
        if (((endTime2 - startTime2)) < minTime) {
            minTime = (endTime2 - startTime2);
            fastMethod = "AcceptAndProcessUserCommandLineInput()";
        }
    }

    private void ExecuteTestMode(String ResourcesPath) throws FileNotFoundException {

        double startTime1 = System.currentTimeMillis();
        InitializingTestMode();
        // Para abrir los dos files sorteddict.txt & sortedDictTest.txt
        Path path = FileSystems.getDefault().getPath("").toAbsolutePath();
        if(path.endsWith("src")){
            path = path.getParent();
        }
        String correct_file_dict_path = ResourcesPath + "/sortedDictTest.txt";
        File correct_file = new File(correct_file_dict_path);

        String to_check_file_dict_path = ResourcesPath + "/sorteddict.txt";
        File to_check_file = new File(to_check_file_dict_path);

        Test1(correct_file, to_check_file);
        Test2(correct_file, to_check_file);

        double endTime1 = System.currentTimeMillis();
        if ((endTime1 - startTime1) > maxTime) {
            maxTime = (endTime1 - startTime1);
            slowMethod = "ExecuteTestMode() Method";
        }
        if ((endTime1 - startTime1) < minTime) {
            minTime = (endTime1 - startTime1);
            fastMethod = "ExecuteTestMode() Method";
        }
    }

    private void InitializingTestMode() {
        System.out.println("____________________________________________________________________________________________________________________________________\n");
        System.out.println("TESTS:");
    }

    private void Test1(File correct_file, File to_check_file) throws FileNotFoundException {
        System.out.println("Test #1");
        System.out.println("   ...checking the 10,000 words, word by word...");
        Scanner scan_correct = new Scanner(correct_file);
        Scanner scan_to_check = new Scanner(to_check_file);

        int word_index = 0;
        while (scan_correct.hasNext()){
            word_index ++;
            String correct = scan_correct.next();
            String to_check = scan_to_check.next();
            if (!correct.equals(to_check)){
                System.out.println("   The file has an error, line "+ word_index + " : " + correct + " != " + to_check);
            }
        }

        if(scan_to_check.hasNext()) System.out.println("   The files are of different lengths");
        else System.out.println("   Test 1 done succesfully");
        scan_correct.close();
        scan_to_check.close();
    }

    private void Test2(File correct_file, File to_check_file) throws FileNotFoundException {
        System.out.println("\nTest #2: Check 5 random positions");

        // Each scan is connected with each file
        Scanner scan_correct2 = new Scanner(correct_file);
        Scanner scan_to_check2 = new Scanner(to_check_file);

        LinkedList<String> testing = new LinkedList<>();
        LinkedList<String> correct = new LinkedList<>();

        while(scan_correct2.hasNext()){
            correct.add(scan_correct2.next());
            testing.add(scan_to_check2.next());
        }
        Random random = new Random();
        for(int i=0; i<5; i++){
            int index = random.nextInt(10000);
            System.out.println( "  -  " +index + " --> Correct: " + correct.get(index) + " | Test: " + testing.get(index));
        }
        System.out.println("____________________________________________________________________________________________________________________________________\n");
    }

    private void PrintSpeedMethod() {
        System.out.println("  -  The SLOWEST method is : " + slowMethod + " with " + maxTime + " miliseconds");
        System.out.println("  -  The FASTEST method is : " + fastMethod + " with " + minTime + " miliseconds");
        System.out.println("____________________________________________________________________________________________________________________________________");
    }

}