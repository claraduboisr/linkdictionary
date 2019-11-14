# Link Dictionary
The following project is a pair programming project done by Clara Dubois and Clara Benzadon.
The project without applying the coding principles can be found in Github at @clarabenzadon
 
## Table of contents
### 1. General information
### 2. Code Description
### 3. Verification Test
### 4. Performance Test
### 5. Coding principles


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
            slowMethod = "ExecuteTestMode() Method";
        }
        if (((endTime2 - startTime2)) < minTime) {
            minTime = (endTime2 - startTime2);
            fastMethod = "ExecuteTestMode()";
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
### 5. Coding principles
#### Meaningful Names
| Before | After |
| :---: | :-----------: |
| ```insert()``` | ```SortAndInsertInLinkdictionary``` |
| ```file_ordered()``` | ```CreateFileWithSortedDictionary``` |
| ```process_user_input()``` | ```AcceptAndProcessUserCommandLineInput``` |
| ```test()``` | ```ExecuteTestMode``` |

On the other hand, we considered that proper naming and proper declaration of the varibales, that is why ´´´double maxTime = 0´´´ (and similar variables) where changed to private.

#### Keep Functions Small
Different changes in the different methods:

##### Main()
**1.	PrintIntroduction()**
```java
System.out.println("\nPLEASE WAIT");
        System.out.println("_________________________________________________________________________________________________\n");
        System.out.println("Hello there! You can do plenty of functionalities with this program :)");
        System.out.println("If you write a number, it will be considered as an indux and it will return the word in that index");
        System.out.println("If you write a word, it will return the index related to that word");
        System.out.println("REMEMBER: If in the command line you write a '-1', no matter the position, all the arguments will be analyzed in the test mode file");
        System.out.println("____________________________________________________________________________________________");
```

**2.	CreateAndSortDictionary(file, alphabet)**
```java 
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
```

   -  ```File file = new file (file_dict_path)``` and ```char [] alphabet``` are moved outside of the new method
   -  We removed the prints for showing the slowest and the fastest method among the methods and created a new method to simplify it more: ```PrintSpeedMethod()```
   -  As it returns a LinkedList (already sorted) named dictionary, outside the method, it is declared as a new variable: ```LinkedList <String>[] dictionary = CreateAndSortDictionary(file, alphabet)```

**3.	CreateFileWithSortedDictionary(Objects.requireNonNull(dictionary), ResourcesPath)**
```java 
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
``` 
   -  _Objects.requireNonNull(dictionary)_: means that, as the dictionary may return a "null", this specifies not to be null
   - _ResourcesPath_: Applying the 5th principle, it is a new variable which holds path + "/Resources" 

##### ExecuteTestMode(String ResourcesPath)
We divided the code in this method into three different methods: one for introducing, one for test 1 and another for test 2 code:

**1. InitializingTestMode()**
We include:
```java
System.out.println("________________________________________________________________________________________\n");
System.out.println("TESTS:");
```
   - Applying the 7th principle, we eliminated lines of code that we weren´t using. So instead of using ```System.out.println()```(so we could include a white space, we changed add to a using line of code ```/n```.

**2. Test1(File correct_file, File to_check_file)**
```java 
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
```
**3. Test2(File correct_file, File to_check_file)**
```java
System.out.println("\nTest #2: Check 5 random positions");
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
System.out.println("___________________________________________________________________________________________\n");
```

#### Avoid Redundant Commenting
In the last commit, we had up to 31 lines of commenting. Right now we have just **6 lines**.
We decided to remove these lines of commenting because, having changed the method's names and declaring a more self-explanatory variables's names, it made it clearer the purpose of the code and the comments were redundant information. 

#### Single Responsibility Principle
| Entity | Description |
| --- | ----------- |
| ```Main``` class | Declares the initial time for the methods|
| ```main()```  | Execute all the Main class |
| ```PrintIntroduction()```  | Print the description of the possible inputs |
| ```CreateAndSortDictionary()```  | Insert the words in the proper dictionary sorted |
| ```SortAndInsertInLinkdictionary()``` | Sort the words and insert them in dictionary |
| ```CreateFileWithSortedDictionary()``` | Writes in the file the sorted dictionary |
| ```AcceptAndProcessUserCommandLineInput()``` | Reads the user inputs in the CLI | 
| ```ExecuteTestMode()``` | Opens the files necessary and runs the test1 and test2 methods |
| ```InitializingTestMode()``` | Prints the introduction text of the Test Mode |
| ```Test1()``` | Execution of test 1 |
| ```Test2()``` | Execution of test 2 |
| ```PrintSpeedMethod()``` | Prints the fastest and slowest method |

#### Don't Repeat Yourself
String ResourcesPath = path + "/Resources"; insted of writing all the time path + "/Resources"
  -  Also, for a few methods, this made us change the variables that the methods needed: from path to ResourcesPath

On the other hand, we were already using constants variables: 
  -  ```int size = 26```which was used for declaring the size for the LinkedList dictionary. We decided to use it since the beginning because for the spanish alphabet we would need 27 and it would be inefficient to change everywhere we declared that size.

#### Keep Your Code Simple
Instead of using a normal while loop, we used a foreach loop for two particular cases: 
 1.
 -  Before:
 ```java 
 ListIterator<String> iterator = list_words.listIterator();
                 while(iterator.hasNext()){
                     String element = iterator.next();
                     dict_ordered.write(element + "\n");
                 }  
 ```
   -  After
 ```java
 for (String element : (Iterable<String>) list_words) {
                     dict_ordered.write(element + "\n");
                 }
 ```
 2. 
 -  Before:
 ```java
 for (int i = 0; i < args.length; i++)
 ```
 -  After:
 ```java
 if (arg.equals("-1")) ExecuteTestMode(ResourcesPath);
 ```

#### YAGNI (You Ain't Gonna Need It)
As mentioned in the "keeping functions small" principle, instead of inserting a whole ```System.out.println``` completely blank for created a white line, we removed those lines of code and insert a ´´´/n´´´ in the ```System.out.println```s.  However, before making the last commit, we made sure not to keep lines of code that we were not using
