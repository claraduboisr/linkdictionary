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
            char [] alphabet = new char [size];
            LinkedList<String> [] words = new LinkedList[size];

            // mientras haya una linea mas, leer esa linea
            while (scan.hasNextLine()) {
                String s = scan.nextLine();
                String normal = Normalizer.normalize(s, Normalizer.Form.NFD);

                for (int i = 0; i==size; i++){
                    if (normal.charAt(0)==alphabet[i]){
                        // put_method(words[i], s);
                    }
                }
            }

            scan.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File not found :(");
        }







    }


}

