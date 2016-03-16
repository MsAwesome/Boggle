Within my code I used the built in ArrayList sort. This sort is a modified mergeSort.
The java documentation indicates that this modified mergeSort will offer a guaranteed 
n log(n) performance. 

Regarding my method for adding edges to my adjacency lists, below is a list of which 
type of edge is added when:
1.) Up then down
2.) Left then right
3.) Upper right diagonal then lower right diagonal
4.) Upper left diagonal then lower left diagonal

I created my own binary search. My verson of BS acts like a normal binary 
search except that it returns the index of where the extact word is in the list, if 
found and if the entire word isnt found it checks to see if the last word it came to 
starts with the sequence given. If it does then a flag in vocab (an instance of the 
vocabDictionary class) is set to true.  

