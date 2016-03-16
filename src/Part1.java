import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.Scanner;
/* Amanda McCarty
 * COSC 314
 * Winter 2016 */

public class Part1 {
	
	private static int [][] adjMatrix; 
	private static int [] vertexColors;
	private static int n;

	public static void main(String [] args){

		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
	
		System.out.print("Enter file name for input graph: "); //ask for input file
		String inFile = in.next();
	
		fileToMatrix(inFile);
		
		System.out.println("Looking for chromatic number...");
		
		int color = color();
		
		PrintWriter outStream = null;
		try {
			outStream = new PrintWriter(new FileOutputStream(inFile + "_out.txt"));
			outStream.println("Chomatic number is " + color);
			outStream.println("How the vertices are colored (vertex) (color)");
			for (int i = 0;i < n;i++){
				outStream.print(i+1);
				outStream.print(" --> " +vertexColors[i]);
				outStream.println();
			}
					
		} catch (IOException e) {
			System.out.println("Could not access file");
		} finally {
			outStream.close();
		}
		System.out.println("Data has been saved to the '"+ inFile + "_out' file in project folder");
	}


	//method to return the chromatic number
	public static int color(){
		//fill color array with zeros
		for (int i = 0; i < n; i++){
			vertexColors[i] = 0;
		}
		
		int i;
		for (i = 1; i < n; i++){
			if (color(0,i)){
				return i;
			}
		}
		return -1;
	}

	//recursive method to find the chromatic number
	static boolean color(int vertex, int color)
	{
		if (vertex > n-1)
			return true;

		for (int i = 1; i <= color; i++) {
			vertexColors[vertex] = i;
			
			//check for neighbor conflicts
			if (!isConflict(vertex, i)) {
				
				//recurse to see if the rest of graph can be colored
				if (color(vertex+1, color))
					return true;
			}
		}
		
		//set to zero and fail
		vertexColors[vertex] = 0;
		return false;
	}

	//method to check for conflicting neighbors
	static boolean isConflict(int v, int c){
		for (int i = 0; i < n; i++)
			if (adjMatrix[v][i] == 1 && c == vertexColors[i] || 
				adjMatrix[i][v] == 1 && c == vertexColors[i])
				return true;
		return false; 
	}
	
	//Method to turn file into matrix
	public static int [][] fileToMatrix(String fileName){
		Scanner inStream = null;

		try {

			inStream = new Scanner(new FileInputStream(fileName)); // read  data from file 

			@SuppressWarnings("unused")
			int numberOfVertices, numberOfEdges; //get number of vertices and edges from file
			numberOfVertices = inStream.nextInt(); 
			numberOfEdges = inStream.nextInt();

			//initialize with data from file
			adjMatrix = new int [numberOfVertices][numberOfVertices];
			vertexColors = new int [numberOfVertices];
			n = vertexColors.length;

			//first fill matrix with zeros
			for (int i = 0; i < adjMatrix.length; i++){
				for (int j = 0; j < adjMatrix[i].length; j++){
					adjMatrix[i][j] = 0;
				}
			}

			while (inStream.hasNextLine()) // checks to see if the file as another  line
			{
				int currentiVertex = inStream.nextInt();
				int currentjVertex =inStream.nextInt();
				//file ones where edges are
				for (int i = 0; i < adjMatrix.length; i++){
					for (int j = 0; j < adjMatrix[i].length; j++){
						if (i == currentiVertex-1 && j == currentjVertex-1){
							adjMatrix[i][j] = 1;
							break;
						}
					}
				}
			}
		} catch (FileNotFoundException e) { // catch any exceptions 
			System.out.println("Error accessing file");
			System.out.println("Ending program");
		} catch (NoSuchElementException e) {
		} finally {
			inStream.close(); // close stream  
		}
		return adjMatrix;
	}

}
