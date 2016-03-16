import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

class VocabDictionary {
	String vocabWord;
	boolean isSorted;
	boolean partialMatch;
	ArrayList <String> dictionary;
	ArrayList <String> foundWords = new ArrayList<String> (16);
	VocabDictionary() {
		this.dictionary = new ArrayList<String>(50);
		this.isSorted = true;
	}
	void addWord(String word) {
		this.isSorted = false;
		dictionary.add(word);
	}
	void sortDictionary() {
		Collections.sort(dictionary);
		this.isSorted = true;
	}
	void addToFoundWords(String word) {
		foundWords.add(word);
	}
	int binarySearchDictionary(String word) {
		//set partial match flag to false.
		this.partialMatch = false;

		if(!this.isSorted){
			this.sortDictionary();
		}
		int low=0, high=this.dictionary.size()-1; int mid; int result=-1;
		while(low<=high){
			mid=(low+((high-low)/2));
			if( dictionary.get(mid).startsWith(word)) {
				//set a flag to indicate that the word sequence has a partial match at least.
				this.partialMatch = true;
			}
			if(word.compareToIgnoreCase(dictionary.get(mid))< 0){
				high = mid-1;
			}
			else if(word.compareToIgnoreCase(dictionary.get(mid))>0) {
				low =mid+1;
			}
			else if(word.compareToIgnoreCase(dictionary.get(mid))== 0) {
				result=mid;
				low = high +1;
			}
		}
		return result;
	}
}

class Vertex {
	char vertexLetter;
	int letterIndx;
	ArrayList <Vertex> adjCharacters = new ArrayList<Vertex>(8);
	Vertex() {}
	Vertex(char vert) {
		vertexLetter = vert;
	}
	ArrayList<Vertex> getAdjCharacters() {
		return adjCharacters;	
	}
	char getLetter() {
		return vertexLetter;
	}
	void addAdjCharacters(Vertex l) {
		adjCharacters.add(l);
	}
	void addIndx(int num) {
		letterIndx=num;
	}
	int getIndx() {
		return letterIndx;
	}
}

public class Boggle {

	public static void main(String[] args) { 
		Boggle boggleGame = new Boggle();
		VocabDictionary vocab = new VocabDictionary(); 
		String line;
		String[] strArray;
		BufferedReader br=null;
		Vertex[][] grid = null;
		try {
			String filepath = "board.tx";
			String filepath2 = "dict.txt";
			//REMEMBER TO TAKE OUT THE .TXT

			br = new BufferedReader(new FileReader(filepath));
			
			line = br.readLine();
			strArray=line.split(" ");
			grid = new Vertex[Integer.parseInt(strArray[0])][Integer.parseInt(strArray[1])];
			int letterCounter=0;
			for(int i=0;i<grid.length;i++){
				line= br.readLine();
				strArray = line.split(" ");
				for(int j=0;j<grid[0].length;j++){
					grid[i][j] = new Vertex(strArray[j].charAt(0));
					grid[i][j].addIndx(letterCounter);
					letterCounter++;
				}
			}
			br.close();
			br = new BufferedReader(new FileReader(filepath2));
			line=br.readLine();
			while(line!=null) {
				vocab.addWord(line.toUpperCase());
				line=br.readLine();
			}
			if(vocab.dictionary.size()>0) {
				vocab.sortDictionary();
			}
			else {
				System.exit(0);
			}
			
		}
		
		catch (Throwable event) {
			System.err.println("An IOexception was caught "+event.getMessage());
			System.exit(1);
		}
		finally {
			try{
				if(br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		boggleGame.addEdges(grid);

		int i=0;
		for(i=0;i<grid.length;i++) {
			for(int j=0;j<grid[0].length;j++) {
				boggleGame.DepthFirstSearch(grid[i][j], grid.length,  grid[0].length,vocab);
			}
		}
		for( i=0;i<vocab.foundWords.size();i++) {
			System.out.println(vocab.foundWords.get(i));
		}
	}

	private void DepthFirstSearch(Vertex vertex, int row, int col, VocabDictionary vocab) {
		boolean[] seen = new boolean[row*col];
		for(int i=0;i<seen.length;i++) {
			seen[i] = false;
		}
		DFS(vertex,seen,"",vocab);
	}

	private void DFS(Vertex vertex, boolean[] seen, String curLetters, VocabDictionary vocab) {
		String possWord = curLetters+ vertex.getLetter();
		seen[vertex.getIndx()]=true;
		for(int i=0;i<vertex.getAdjCharacters().size();i++) {
			
			if(possWord.equals("Q")) {
				possWord = possWord + "U";
			}
			if(possWord.length() >= 2) {
				/*  
				 * vocab has a boolean flag that returns true if a partial match is found during the binary search
				 * 
				 * if wordMatchIndex returns >= 0 then the entire word is matched
				 * if -1 returns but the partialMatch flag is true; the sequences exists and continue with the sequence
				 * if -1 returns but the partialMatch flag is false; the sequence doesnt exist and you should remove the last letter. 
				*/
				int wordMatchIndex = vocab.binarySearchDictionary(possWord);

				if(wordMatchIndex >= 0) {
					vocab.addToFoundWords(possWord);
					vocab.dictionary.remove(wordMatchIndex);   
					}
				else if (wordMatchIndex == -1 && vocab.partialMatch==false) {
					break;
				}
			}
			if(seen[vertex.getAdjCharacters().get(i).letterIndx] != true ){  
				DFS(vertex.getAdjCharacters().get(i),seen,possWord,vocab);
			}
		}
		seen[vertex.getIndx()] = false;
	}

	private void addEdges(Vertex[][] grid) {
		for(int i=0;i< grid.length;i++) {
			for( int j=0;j<grid[1].length;j++) {
				if(i>0) {
					grid[i][j].addAdjCharacters(grid[i-1][j]);
					//up edge
				}
				if(i<grid.length-1) {
					grid[i][j].addAdjCharacters(grid[i+1][j]);
					//down edge
				}
				if(j>0) {
					grid[i][j].addAdjCharacters(grid[i][j-1]);
					//left edge
				}
				if(j<grid[1].length-1) {
					grid[i][j].addAdjCharacters(grid[i][j+1]);
					//right edge
				}
				if(i!=0 && j!=grid[1].length-1) {
					grid[i][j].addAdjCharacters(grid[i-1][j+1]);
					//upper right diagonal
				}
				if(i<grid.length-1 && j<grid[1].length-1) {
					grid[i][j].addAdjCharacters(grid[i+1][j+1]);
					//lower right diagonal
				}
				if(i!=0 &&j!=0) {
					grid[i][j].addAdjCharacters(grid[i-1][j-1]);
					//upper left diagonal
				}
				if(i<grid.length-1 && j!=0) {
					grid[i][j].addAdjCharacters(grid[i+1][j-1]);
					//lower left diagonal
				}
			}
		}	
	}



}
