# Link Dictionary
The following project is a pair programming project done by Clara Dubois and Clara Benzadon.
 
## Table of contents
### 1. General information
### 2. Code Description


![background](https://s7d5.scene7.com/is/image/horizonhobby/HHEU-airme-info-960x100?$Default$)

### 1. General information
 * **Instructions**: Create a JAVA project to manage a dictionary built using _Linked lists_.
 * **Functionalities required**: 
   * The application will open and read line by line a file called _**"unsorteddict.txt"**_ (a collection of words where each line contains a word).
   * The application will read a new word each time a new line is read.
   * Each time a new line is read, the new word will be inserted in the correct position of a data    structure called “dictionary” that will be of a Linked list type.
   * The created “dictionary” will be a sorted list of all the words read from the file
   * After the “unsorteddict.txt” has been fully read, the application will create another file called “sorteddict.txt” which will contain all the words
(one word per line) alphabetically ordered
 
### 2. Code Description
#### **Structure**:
Due to IntelliJ set up, we needed to change our Main class to non-static, that's why we created an object to execute the code in the ``` public static void main(String[] args)```

```java
Main app = new Main(args);
```
##### File Unsorted:
1. Find the file _"unsorteddict.txt"_:
```java
Path path = FileSystems.getDefault().getPath("").toAbsolutePath();

if(path.endsWith("src")){
    path = path.getParent();
}

String file_dict_path = path + "/Resources" + "/unsorteddict.txt";

File file = new File(file_dict_path);
```

2. Read the file
```java
Scanner scan = new Scanner (file);

while (scan.hasNextLine()) {
    String s = scan.nextLine();
    String normal = Normalizer.normalize(s, Normalizer.Form.NFD);

    for (int i = 0; i<size; i++){
        if (Character.toLowerCase(normal.charAt(0))== alphabet[i]){
            dictionary[i] = put_method(dictionary[i], s, normal);
        }
    }
}

scan.close();
```

##### Algorithm
The two main parts of the algorithm are:
- Array made of the alphabet called "alphabet".
- Array of Linked Lists (of Strings) with the same size as the alphabet named "dictionary".

###### *How does it sort?*
We decided to create an array of linked lists, where in each linked list the words that start with the same letter are grouped. The reason behind this is for being able to sort in parallel and saving a lot of time (indeed, it can sort all the 99171 words in aprox. 8 seconds).

To have a clearer and more organized structure of the code, we created a specific method for inserting each word in the proper alphabetical order:
  ```java
  public LinkedList put_method (LinkedList<String>words, String s, String normal)
  ```
* If its smaller than the *first* word, its inserted before the first word
* If its larger than the *last* word, its inserted in the last position of the linked list
* If not, It compares the word that you want to insert - in which has been normalized (to not take into account accents such as "é" or "Å") - and it is compared to every word of the linked list.
  - If it is smaller than the word already in the linked list, it is inserted before that word
  - If it is larger than the word, it moves to the next word. 
    - If it is smaller than the word already in the linked list, it is inserted before that word
    - If it is larger than the word, the process continues. 

##### File Sorted:
First we create a new file (empty) with the correct path to be inserted:
```java
File file = new File(path + "/Resources" + "/sorteddict.txt");
```

To write all the words sorted, we imported ```FileWriter```

```java
for (LinkedList list_words : dictionary){
      for(int i=0; i<list_words.size(); i++){
          String word = (String) list_words.get(i);
          dict_ordered.write(word + "\n");
      }
}
dict_ordered.close();
```



### Accept Command Line Arguments
We created a new method called ```public void process_user_input(String[] args, LinkedList<String>[] dictionary, char[] alphabet)```. All the code is inside an if/else to first make sure that there are not more than 10 inputs. If there is less or 10 inputs:
  We used a try/catch exception.
  - Try: If we can create the input into an int, then is considered as an index.

   ```java
         
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
   ```
  - Catch: If it cannot be converted into an int, then it can only be a String and it is considered as the word-to-be-found.
  
  ```java
  
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
  
