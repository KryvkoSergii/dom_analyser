# Dom tree search 

Tries find element from diff html file, that likes (has similar path) from original html file with 
id attribute [make-everything-ok-button]

### Prerequisites

- jre 8
- original file
- diff file

### Run

select correct directory and execute command
java -jar parser-1.0-SNAPSHOT.jar <original_file_path> <dif_file_path>  
Example:
```
java -jar parser-1.0-SNAPSHOT.jar sample-0-origin.html sample-1-evil-gemini.html
```
## Result
path example
```
root->html->body->div->div->div->div->div->div->a
```
## Running the tests

Use maven to run pom.xml tests

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Kryvko Sergii** - *develop*

Git https://github.com/KryvkoSergii/dom_analyser
