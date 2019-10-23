package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
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

            // Creo e inicializo el linked list vacio
            LinkedList <String> sorteddict= new LinkedList();

            // mientras haya una linea mas, leer esa linea
            while (scan.hasNextLine()) {
                String s = scan.nextLine();
                System.out.println(s);
            }



            scan.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File not found :(");
        }







    }


}

