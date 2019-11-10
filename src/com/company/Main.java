package com.company;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;
import java.util.Scanner;

// Whenever a user wants to input a word with an apostrophe they have tpo surround it with ""
// IMPORTANT !!!

public class Main {

    double maxTime = 0;
    String slowMethod = "";
    double minTime = 1000;
    String fastMethod = "";

    public static void main(String[] args) {
        Main app = new Main(args);
        // System.out.println("Execution time in seconds  : " + timeElapsed/1000);
    }

    public Main(String[] args){

        System.out.println();
        System.out.println("PLEASE WAIT");
        System.out.println("____________________________________________________________________________________________________________________________________");
        System.out.println("");
        System.out.println("Hello there! You can do plenty of functionalities with this program :)");
        System.out.println("If you write a number, it will be considered as an indux and it will return the word in that index");
        System.out.println("If you write a word, it will return the index related to that word");
        System.out.println("REMEMBER: If in the command line you write a '-1', no matter the position, all the arguments will be analyzed in the test mode file");
        System.out.println("____________________________________________________________________________________________________________________________________");

        Path path = FileSystems.getDefault().getPath("").toAbsolutePath();

        // Si el programa se ejecuta desde src, salir de source para encontrar el file unsorteddict.txt
        if(path.endsWith("src")){
            path = path.getParent();
        }

        String file_to_order = "unsorteddict";

        // Para comprobar si esta en test mode o no, se comprueba una vez el user input
        if(args != null){
            for (String arg : args){
                if(arg.equals("-1")){
                    file_to_order = "unsortedDictTest";
                }
            }
        }

        // Para encontrar (y crear) el "unsorteddict.txt"
        String file_dict_path = path + "/Resources" + "/" + file_to_order + ".txt";
        File file = new File(file_dict_path);

        // try catch porque el file puede dar error por si no esta ahi o tiene otro nombre
        try {
            // Con el Scanner podemos leer el file
            Scanner scan = new Scanner (file);

            int size = 26;

            char[] alphabet = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
            LinkedList<String> []dictionary = new LinkedList[size];

            double startTime = System.currentTimeMillis();

            // mientras haya una linea mas, leer esa linea
            while (scan.hasNextLine()) {
                String s = scan.nextLine();
                String normal = Normalizer.normalize(s, Normalizer.Form.NFD);
                for (int i = 0; i<size; i++){
                    if (Character.toLowerCase(normal.charAt(0))== alphabet[i]){
                        dictionary[i] = insert(dictionary[i], s, normal);
                    }
                }
            }
            System.out.println();
            // Cerrar scanner para habilitar el acceso al file
            scan.close();

            file_ordered(dictionary, path);

            process_user_input(args, dictionary, alphabet);

            double endTime = System.currentTimeMillis();
            System.out.println("Thank you for waiting, all the program has taken: " + ((endTime - startTime)/1000) + " seconds");

            System.out.println("  -  The SLOWEST method is : " + slowMethod + " with " + maxTime + " miliseconds");
            System.out.println("  -  The FASTEST method is : " + fastMethod + " with " + minTime + " miliseconds");
            System.out.println("____________________________________________________________________________________________________________________________________");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File not found :(");
        }
    }

    private void test() throws FileNotFoundException {

        double startTime1 = System.currentTimeMillis();
        System.out.println("____________________________________________________________________________________________________________________________________");
        System.out.println();
        System.out.println("TESTS:");

        System.out.println("Test #1");
        System.out.println("   ...checking the 10,000 words, word by word...");

        // Para abrir los dos files sorteddict.txt & sortedDictTest.txt
        Path path = FileSystems.getDefault().getPath("").toAbsolutePath();
        if(path.endsWith("src")){
            path = path.getParent();
        }
        String correct_file_dict_path = path + "/Resources" + "/sortedDictTest.txt";
        File correct_file = new File(correct_file_dict_path);

        String to_check_file_dict_path = path + "/Resources" + "/sorteddict.txt";
        File to_check_file = new File(to_check_file_dict_path);

        // Cada scan esta conectado con un file
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

        System.out.println("");
        System.out.println("Test #2: Check 5 random positions");

        // Cada scan esta conectado con un file
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

        System.out.println("____________________________________________________________________________________________________________________________________");
        System.out.println();
        double endTime1 = System.currentTimeMillis();
        if ((endTime1 - startTime1) > maxTime) {
            maxTime = (endTime1 - startTime1);
            slowMethod = "test() Method";
        }
        if ((endTime1 - startTime1) < minTime) {
            minTime = (endTime1 - startTime1);
            fastMethod = "test() Method";
        }
    }

    public void process_user_input(String[] args, LinkedList<String>[] dictionary, char[] alphabet) throws FileNotFoundException {
        double startTime2 = System.currentTimeMillis();
        if (args.length > 10) {
            System.out.println("Too many arguments, max number of arguments: 10");
        } else {
            // Regular expressions
            // -? significa que puede tener un - delante del numero
            // /d significa que es un integer
            // + puede tener tanto integers como sean necesarios
            // -?[0-9]+
            String pattern = "-?\\d+";
            for (int i = 0; i < args.length; i++) {

                if (args[i].equals("-1")) test();

                else if (args[i].matches(pattern)) {
                    int position = Integer.parseInt(args[i]) - 1;

                    if (position < 0) {
                        System.out.println(args[i] + ": " + "is a negative number, please introduce a postive number.");
                    } else {
                        for (int j = 0; j < alphabet.length; j++) {
                            if (position - dictionary[j].size() < 0) {
                                System.out.println(args[i] + ": " + dictionary[j].get(position));
                                break;
                            } else {
                                position = position - dictionary[j].size();
                            }
                            if (j == 25) {
                                System.out.println(args[i] + ": " + "Index not in dictionary");
                            }
                        }
                    }
                } else {

                    if (!args[i].matches(".*\\d.*")){
                        for (int j = 0; j < alphabet.length; j++) {
                            if (Character.toLowerCase(args[i].charAt(0))== alphabet[j]) {
                                int position = dictionary[j].indexOf(args[i]);
                                if (position == -1) {
                                    System.out.println(args[i] + ": " + position);
                                    break;
                                } else {
                                    position++;
                                    for (int g = j - 1; g >= 0; g--) {
                                        position += dictionary[g].size();
                                    }
                                    System.out.println(args[i] + ": " + position);
                                    break;
                                }
                            }
                            if (j == 25) {
                                System.out.println(args[i] + ": " + "-1");
                            }
                        }
                    } else {
                        System.out.println(args[i] + ": is not a String");
                    }
                }
            }
        }
        double endTime2 = System.currentTimeMillis();
        if ((endTime2 - startTime2) > maxTime) {
            maxTime = (endTime2 - startTime2);
            slowMethod = "process_user_input() Method";
        }
        if (((endTime2 - startTime2)) < minTime) {
            minTime = (endTime2 - startTime2);
            fastMethod = "process_user_input()";
        }
    }

    public LinkedList insert (LinkedList<String>words, String s, String normal){

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
            /*int position = 1;
            for (String element:words) {
                int comparisson = normal.compareToIgnoreCase(Normalizer.normalize(element, Normalizer.Form.NFD));
                if (comparisson < 0){
                    position = words.indexOf(element);
                    // una vez encontrada la posicion, sales del for loop
                    break;
                }
            }
            words.add(position, s);*/
        }
        int time_end = (int) System.currentTimeMillis();
        double endTime3 = System.currentTimeMillis();
        if (((endTime3 - startTime3)) > maxTime) {
            maxTime = (endTime3 - startTime3);
            slowMethod = "insert() Method";
        }
        if (((endTime3 - startTime3)) < minTime) {
            minTime = (endTime3 - startTime3);
            fastMethod = "insert() Method";
        }
        return words;
    }

    public void file_ordered (LinkedList[] dictionary, Path path){
        double startTime4 = System.currentTimeMillis();
        File file = new File(path + "/Resources" + "/sorteddict.txt");

        //Write Content
        FileWriter dict_ordered = null;
        try {
            dict_ordered = new FileWriter(file);
            for (LinkedList list_words : dictionary){

                ListIterator<String> iterator = list_words.listIterator();
                while(iterator.hasNext()){
                    String element = iterator.next();
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
            slowMethod = "file_ordered() Method";
        }
        if (((endTime4 - startTime4)) < minTime) {
            minTime = (endTime4 - startTime4);
            fastMethod = "file_ordered() Method";
        }
    }
}