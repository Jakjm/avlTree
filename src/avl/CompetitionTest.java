package avl;
import java.util.ArrayList;

/**
 * @author jakjm
 */
public class CompetitionTest {
	public static void main(String[] args)
	{
		AVL<Integer> tree = new AVL<Integer>();
		ArrayList<Integer> list = new ArrayList<Integer>();
		long startTime = System.currentTimeMillis();
		long endTime;
		final int size = 100000;
		boolean check = false;
		
		//For my AVL
		System.out.println("AVL");
		
		//Adding time
		for(int i = 0;i < size;i++)tree.add(i);
		endTime = System.currentTimeMillis();
		System.out.println("Adding " + size +  " took " + ((endTime-startTime)/1000.0) + "second(s)");
		
		//Checking time
		for(int i = 0;i < size;i++)check = tree.contains(i);
		startTime = endTime;
		endTime = System.currentTimeMillis();
		System.out.println("Checking " + size +  " took " + ((endTime-startTime)/1000.0) + "second(s)");
		
		//Removing time
		for(int i = 0;i < size;i++)tree.remove(i);
		startTime = endTime;
		endTime = System.currentTimeMillis();
		System.out.println("Removing " + size +  " took " + ((endTime-startTime)/1000.0) + "second(s)");
		
		//For the ArrayList
		System.out.println("ArrayList");
		
		//Adding time
		for(int i = 0;i < size;i++)list.add(i);
		startTime = endTime;
		endTime = System.currentTimeMillis();
		System.out.println("Adding " + size +  " took " + ((endTime-startTime)/1000.0) + "second(s)");
		
		//Checking time
		for(int i = 0;i < size;i++)check = list.contains(i);
		startTime = endTime;
		endTime = System.currentTimeMillis();
		System.out.println("Checking " + size +  " took " + ((endTime-startTime)/1000.0) + "second(s)");
		
		//Removing time
		for(int i = 0;i < size;i++)list.remove(0);
		startTime = endTime;
		endTime = System.currentTimeMillis();
		System.out.println("Removing " + size +  " took " + ((endTime-startTime)/1000.0) + "second(s)");
	}
}
