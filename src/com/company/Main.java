package com.company;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Entrar en Resources y encontrar el "unsorteddict.txt"

        // Encontrar el path hasta la carpeta linkdictionary incluida
        Path path = FileSystems.getDefault().getPath(".").toAbsolutePath();

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
            for (LinkedList list_words : dictionary){
                for(int i=0; i<list_words.size(); i++){
                    // Java no sabe el tipo de variable que hay en el LinkedList dentro del Array asi q se indica con un cast --> (String)
                    String word = (String) list_words.get(i);
                    System.out.println(word);
                }
            }
            // Cerrar scanner para habilitar el acceso al file
            scan.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File not found :(");
        }
    }

    public static LinkedList put_method (LinkedList<String>words, String s, String normal){

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
}

