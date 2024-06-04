## Autocomplete
This implementation was a part of my degree project, where I compared different algorithms for autocomplete: trie, hash table and ngram.

### Populate database
There are three files with saved common words of different sizes: <br />
3000.txt<br/>
5006.txt<br/>
10000.txt<br/>
and another three files for each algorithm:<br/>
trie-words.txt<br/>
hash-words.txt<br/>
ngram-words.txt<br/>
The latter three are the ones being loaded into the database when pressing "Load database ...". These files are empty by default, so either type in your own words separated by enter, or copy information from the three files above.
### Run
- Make sure postgresql is setup properly according to the "application.properties" file, a schema called "autocomplete" has to be created.
- Compile and run the code.
- Open "home.html" located in client folder.
- Copy the information in whichever amount you want to run, to whichever algorithm file you want to run.
- Press each "Load database ..." button to populate the databases, make sure the database is populated. Ngram takes some time, don't worry, population time is also measured, when the time is shown, it's done.
- Start typing in one field, each letter added will return results.
- Delete and insert will also work, and the time will also show.
- To upload a new list of words, delete the content of the file first, then add from one of the presaved files, or just add own words, separate the words with enter.
### Stuff todo
Was in a rush to finish this project so everything is not as intuitive as it should be. :-P