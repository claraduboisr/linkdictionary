# Link Dictionary
The following project is a pair programming project done by Clara Dubois and Clara Benzadon.
 
## Table of contents
### 1. General information
### 2. Code Description
### 3. Verification Test
### 4. Performance Test


![background](https://s7d5.scene7.com/is/image/horizonhobby/HHEU-airme-info-960x100?$Default$)

### 1. General information
 * **Instructions**: Create a JAVA project to manage a dictionary built using _Linked lists_.
 * **Functionalities required**: 
   * The application will open and read line by line a file called _"unsorteddict.txt"_ (a collection of words where each line contains a word).
   * The application will read a new word each time a new line is read.
   * Each time a new line is read, the new word will be inserted in the correct position of a data    structure called “dictionary” that will be of a Linked list type.
   * The created “dictionary” will be a sorted list of all the words read from the file
   * After the “unsorteddict.txt” has been fully read, the application will create another file called “sorteddict.txt” which will contain all the words
(one word per line) alphabetically ordered
   * Performance test by evaluating timing 
   * Verification test
 
### 2. Code Description
##### Structure:
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

##### Algorithm:
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

### Accept Command Line Arguments:
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
   ```
### Inputs
   -  Word (_apple_)
      -  If it is in the list, it will return the index
      -  If it does not exist, it will return -1
      
   -  Integer (_4_, _765_)
      -  Using regex: ```"-?\\d+"``` we know that it is an integer if it follows that structure
        -  -? --> you can have a "-" in front of the number
        -  /d --> means that it is a integer
        -  '+'  --> can have many integers as you wish
        -  It is the same as ```-?[0-9]+```
      -  If it is more than 1 and less than the dictionary's size, it will return the word 
      -  If it does not exist, "Index not in dictionary"
      -  If it is a negative number, it will return "x is a negative number, please introduce a positive number"
   - Others
      -  Mix between strings and integers (_appl4e_)
      -  Using regex: ```".*\\d.*"```, it will know that, if a string has any integer in between, it will return "It is not a String"

### 3. Verification Test
Once the user inputs the value "-1" in the command line arguments, no matter what is the position, the following command will be run in the test file. 

> Why do not we choose the word "test" as an argument to activate the test? Otherwise, it will be considered as a String input and, as is not in the file, it will return a "-1".

#### Test 10,000
Having a unsorted file, first we sort it with the same code as with the big file. Once is sorted, it is checked with the file already given ("sortedDictTest.txt")

1. Find both files: the sorted file created with this code and the given one.
```java
        Path path = FileSystems.getDefault().getPath("").toAbsolutePath();
        if(path.endsWith("src")){
            path = path.getParent();
        }
        String correct_file_dict_path = path + "/Resources" + "/sortedDictTest.txt";
        File correct_file = new File(correct_file_dict_path);

        String to_check_file_dict_path = path + "/Resources" + "/sorteddict.txt";
        File to_check_file = new File(to_check_file_dict_path);
```
2. Create a "scan", one for each file, to read both files.
```java
        Scanner scan_correct = new Scanner(correct_file);
        Scanner scan_to_check = new Scanner(to_check_file);
```
3. If the given sorted-file has a next line, it will continue with the while, and in that way, it will compare both lines.
```java
        int word_index = 0;
        while (scan_correct.hasNext()){
            word_index ++;
            String correct = scan_correct.next();
            String to_check = scan_to_check.next();
            if (!correct.equals(to_check)){
                System.out.println("   The file has an error, line "+ word_index + " : " + correct + " != " + to_check);
            }
        }
```
4. If the given sorted-file has a next line and the other one hasn't, it will return "Files has different lengths".
```java
        if(scan_to_check.hasNext()) System.out.println("   The files are of different lengths");
```
5. Close scanners
```java
        else System.out.println("   Test 1 done succesfully");
        scan_correct.close();
        scan_to_check.close();
```
* **_Return:_**
   ![test_1return](https://user-images.githubusercontent.com/42964691/68551669-92139980-040f-11ea-8e09-0512e63f5d17.png)

#### Check several words that the position matches (5 random words)
Using the method Random, the system selects 5 indexes and compare if it matches with the file already corrected and the file created (with this code).

1. We need two scanners, one for each file.
```java
        Scanner scan_correct2 = new Scanner(correct_file);
        Scanner scan_to_check2 = new Scanner(to_check_file);
```
2. As we cannot directly read the file, we created two lists.
```java
        LinkedList<String> testing = new LinkedList<>();
        LinkedList<String> correct = new LinkedList<>();
```
3. I add all the file in the "testing" and "correct" lists so I can compare it
```java
        while(scan_correct2.hasNext()){
            correct.add(scan_correct2.next());
            testing.add(scan_to_check2.next());
        }
```
4. The system picks randomly 5 indexes and it is printed the word with that index in the sorted-given file and the other sorted file (created with this code).
```java
        Random random = new Random();
        for(int i=0; i<5; i++){
            int index = random.nextInt(10000);
            System.out.println( "  -  " +index + " --> Correct: " + correct.get(index) + " | Test: " + testing.get(index));
        }
```
* **_Return:_**
  ![test2_return](https://user-images.githubusercontent.com/42964691/68551597-da7e8780-040e-11ea-9b3f-95b0466e623b.png)

### 4. Performance Test
Using ```double startTime = System.currentTimeMillis(); ``` and ```double startTime = System.currentTimeMillis();``` and having placed them in different sections of the code, we have calculated:
  -  Total time it takes to run all the code
  -  Fastest and slowest method between all.
  
  _Example_: For each method, it compares the endTime with the startTime and if it's bigger than the maximum time, it will be replaced with this number. On the other hand, if it is smaller, it will be considered as the new minimul time.
```java
        double startTime1 = System.currentTimeMillis();
        // Method's code
        double endTime1 = System.currentTimeMillis();
        if ((endTime1 - startTime2) > maxTime) {
            maxTime = (endTime2 - startTime2);
            slowMethod = "process_user_input() Method";
        }
        if (((endTime2 - startTime2)) < minTime) {
            minTime = (endTime2 - startTime2);
            fastMethod = "process_user_input()";
        }
```
* **_Return:_**
   ![return](https://user-images.githubusercontent.com/42964691/68551742-61802f80-0410-11ea-9c07-d5306e59ced2.png)


In addition, having placed this timers, we noticed that in several iterators, the process was very slow. For that reason, we used list iterators:
```java
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
```                          
