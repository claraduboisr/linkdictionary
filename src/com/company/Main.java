package com.company;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {



    public static void main(String[] args) {
        Main app = new Main(args);
    }

    public Main(String[] args){

        // Entrar en Resources y encontrar el "unsorteddict.txt"

        // Encontrar el path hasta la carpeta linkdictionary incluida
        Path path = FileSystems.getDefault().getPath("").toAbsolutePath();

        // Si el programa se ejecuta desde src, salir de source para encontrar el file unsorteddict.txt
        if(path.endsWith("src")){
            path = path.getParent();
        }

        // Para encontrar (y crear) el "unsorteddict.txt"
        String file_dict_path = path + "/Resources" + "/unsorteddict.txt";

        File file = new File(file_dict_path);

        // try catch porque el file puede dar error por si no esta ahi o tiene otro nombre
        try {
            // Con el Scanner podemos leer el file
            Scanner scan = new Scanner (file);

            int size = 26;

            char[] alphabet = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
            LinkedList<String> []dictionary = new LinkedList[size];

            // mientras haya una linea mas, leer esa linea
            while (scan.hasNextLine()) {
                String s = scan.nextLine();
                String normal = Normalizer.normalize(s, Normalizer.Form.NFD);

                for (int i = 0; i<size; i++){
                    if (Character.toLowerCase(normal.charAt(0))== alphabet[i]){
                        dictionary[i] = put_method(dictionary[i], s, normal);
                    }
                }
            }
            /*for (LinkedList list_words : dictionary){
                for(int i=0; i<list_words.size(); i++){
                    // Java no sabe el tipo de variable que hay en el LinkedList dentro del Array asi q se indica con un cast --> (String)
                    String word = (String) list_words.get(i);
                    System.out.println(word);
                }
            }*/
            // Cerrar scanner para habilitar el acceso al file
            scan.close();


            file_ordered(dictionary, path);

            process_user_input(args, dictionary, alphabet);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File not found :(");
        }


    }

    public void process_user_input(String[] args, LinkedList<String>[] dictionary, char[] alphabet) {

        if (args.length > 10){
            System.out.println("Too many arguments, max number of arguments: 10");

        }
        else{
            for(int i=0; i<args.length; i++){
                try {
                    int position = Integer.parseInt(args[i]) - 1;

                    if(position < 0){
                        System.out.println("Please introduce a postive number.");
                    }else{
                        for (int j = 0; j<alphabet.length; j++){
                            if (position - dictionary[j].size() < 0){
                                System.out.println(dictionary[j].get(position));
                                break;
                            }
                            else{
                                position = position-dictionary[j].size();
                            }
                            if(j==25){
                                System.out.println("Index not in dictionary");
                            }
                        }
                    }



                    // if it gets to "catch" it means that it is a String
                } catch(NumberFormatException e) {

                    for (int j = 0; j<alphabet.length; j++){
                        if (Character.toLowerCase(args[i].charAt(0))== alphabet[j]){
                            int position = dictionary[j].indexOf(args[i]);
                            if (position == -1){
                                System.out.println(position);
                                break;
                            }else{
                                position++;
                                for (int g=j-1; g>=0; g--){
                                    position += dictionary[g].size();
                                }
                                System.out.println(position);
                                break;
                            }
                        }
                        if (j == 25){
                            System.out.println("-1");
                        }
                    }
                } catch(NullPointerException e) {
                    return;
                }
            }
        }



    }

    public LinkedList put_method (LinkedList<String>words, String s, String normal){

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
            int position = 0;
            for (String element:words) {
                int comparisson = normal.compareToIgnoreCase(Normalizer.normalize(element, Normalizer.Form.NFD));
                if (comparisson < 0){
                    position = words.indexOf(element);
                    // una vez encontrada la posicion, sales del for loop
                    break;
                }
            }
            words.add(position, s);
        }

        return words;

    }

    public void file_ordered (LinkedList[] dictionary, Path path){
        File file = new File(path + "/Resources" + "/sorteddict.txt");

        //Write Content
        FileWriter dict_ordered = null;
        try {
            dict_ordered = new FileWriter(file);
            for (LinkedList list_words : dictionary){
                for(int i=0; i<list_words.size(); i++){
                    // Java no sabe el tipo de variable que hay en el LinkedList dentro del Array asi q se indica con un cast --> (String)
                    String word = (String) list_words.get(i);
                    dict_ordered.write(word + "\n");
                }
            }

            dict_ordered.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("File not found! :(");
        }


    }

}

