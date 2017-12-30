import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

public class BuildHeap {
    private int[] data;
    private List<Swap> swaps;
    private boolean debug = false;
    private boolean debug2 = false;
    private FastScanner in;
    private PrintWriter out;

    public static void main(String[] args) throws IOException {
        new BuildHeap().solve();
    	//new BuildHeap().UnitTest();
    }

    private static final int findParentIndex(int childIndex){
    	if(childIndex == 0)
    		return -1;
    	return (childIndex+1)/2 - 1;
    }
    
    private static final int[] findChildrenIndex(long heap[], int parentIndex){
    	int childrenIndex[] = new int[2];
    	for(int i=1;i<childrenIndex.length;i++){
    		childrenIndex[i] = -1;
    	}
    	if((parentIndex + 1) * 2 - 1<heap.length)
    		childrenIndex[1] = (parentIndex + 1) * 2 -1;
    	if((parentIndex + 1) * 2<heap.length)
    		childrenIndex[0] = (parentIndex + 1) * 2;
    	return childrenIndex;
    }
    
    private static final void shiftUp(long[] heap, int childIndex){
    	int parentIndex = findParentIndex(childIndex);
    	while(parentIndex!=-1 && heap[childIndex]<heap[parentIndex]){
    		swapArrayValues(heap, parentIndex, childIndex);
    		childIndex = parentIndex;
    		parentIndex = findParentIndex(childIndex);
    	}
    }
    
    private static final void shiftDown(long[] heap, int parentIndex){
    	int childrenIndex[] = findChildrenIndex(heap, parentIndex);
    	boolean moved;
    	do{
    		moved = false;
    		for(int i=0; i<childrenIndex.length;i++){
    			if(childrenIndex[i]!=-1 &&
    					heap[parentIndex]>heap[childrenIndex[i]]){
    				swapArrayValues(heap, parentIndex, childrenIndex[i]);
    				parentIndex = childrenIndex[i];
    				childrenIndex = findChildrenIndex(heap, parentIndex);
    				moved = true;
    				break;
    			}
    		}
    	} while(moved);
    }
    
    private static final void  swapArrayValues(long[] arr, int index1, int index2){
    	long tmp = arr[index1];
    	arr[index1] = arr[index2];
    	arr[index2] = tmp;
    }

    private void UnitTest() {
    	Random rnd = new Random();
    	
    	for(int i=0;i<10;i++){
    		System.out.println("Testing.");
    		int n = rnd.nextInt(10);
    		data = new int[n];
    		for(int j=0;j<n;j++){
    			data[j] = rnd.nextInt(1000);
    		}
    		int dataBackup[] = new int[data.length];
    		System.arraycopy(data, 0, dataBackup, 0, data.length);
    		
    		generateSwapsEvenBetter();

    		boolean correct = true;
    		for(int k=0; k<data.length;k++){
    			if((k+1)*2-1<data.length && data[k]>data[(k+1)*2-1]){
    				correct = false;
    				System.out.printf("Wrong answer at index %d parent %d > child %d%n", 
    						k, data[k], data[(k+1)*2-1]);
    			}
    			if((k+1)*2<data.length && data[k]>data[(k+1)*2]){
    				correct = false;
       				System.out.printf("Wrong answer at index %d parent %d > child %d%n", 
    						k, data[k], data[(k+1)*2]);
    			}
    			if(!correct){
        			System.out.println("Wrong answer.");
        			System.out.println("Original data: ");
        			for(int l=0;l<data.length;l++){
        				System.out.printf("%d ", dataBackup[l]);
        			}
        			System.out.println();
        			System.out.println("answer: ");
        			for(int l=0;l<data.length;l++){
        				System.out.printf("%d ", data[l]);
        			}
        			System.out.println();
    				
    			}
    		}
    		
			/*System.out.println("Original data: ");
			for(int k=0;k<data.length;k++){
				System.out.printf("%d ", data[k]);
			}
			System.out.println();
    		generateSwaps();
    		int dataNaive[] = new int[data.length];
    		System.arraycopy(data, 0, dataNaive, 0, data.length);
			System.out.println("Naive answer: ");
			for(int k=0;k<data.length;k++){
				System.out.printf("%d ", dataNaive[k]);
			}
			System.out.println();
    		System.arraycopy(dataBackup, 0, data, 0, data.length);*/
    		/*
    		System.out.printf("Length of swapss is%d%n", swaps.size());
            System.out.println(swaps.size());
            for (Swap swap : swaps) {
              System.out.println(swap.index1 + " " + swap.index2);
            }
    		int dataFaster[] = new int[data.length];
    		System.arraycopy(data, 0, dataFaster, 0, data.length);
			System.out.println();
			/*System.out.println("Faster answer: ");
			for(int k=0;k<data.length;k++){
				System.out.printf("%d ", dataFaster[k]);
			}
			System.out.println();
			
			
    		if(!Arrays.equals(dataNaive, dataFaster)){
    			System.out.println("Wrong answer.");
    			System.out.println("Original data: ");
    			for(int k=0;k<data.length;k++){
    				System.out.printf("%d ", dataBackup[k]);
    			}
    			System.out.println();
    			System.out.println("Naive answer: ");
    			for(int k=0;k<data.length;k++){
    				System.out.printf("%d ", dataNaive[k]);
    			}
    			System.out.println();
    			System.out.println("Faster answer: ");
    			for(int k=0;k<data.length;k++){
    				System.out.printf("%d ", dataFaster[k]);
    			}
    			System.out.println();
    		}*/
    	}
    	
    }
    
    private void readData() throws IOException {
        int n = in.nextInt();
        data = new int[n];
        for (int i = 0; i < n; ++i) {
          data[i] = in.nextInt();
        }
    }

    private void writeResponse() {
        out.println(swaps.size());
        for (Swap swap : swaps) {
          out.println(swap.index1 + " " + swap.index2);
        }
    }

    private void generateSwaps() {
      swaps = new ArrayList<Swap>();
      // The following naive implementation just sorts 
      // the given sequence using selection sort algorithm
      // and saves the resulting sequence of swaps.
      // This turns the given array into a heap, 
      // but in the worst case gives a quadratic number of swaps.
      //
      // TODO: replace by a more efficient implementation
      for (int i = 0; i < data.length; ++i) {
        for (int j = i + 1; j < data.length; ++j) {
          if (data[i] > data[j]) {
            swaps.add(new Swap(i, j));
            int tmp = data[i];
            data[i] = data[j];
            data[j] = tmp;
          }
        }
      }
    }
    
    private void generateSwapsEvenBetter(){
    	swaps = new ArrayList<Swap>();
    	int child = data.length-1;
    	int parent = 0;
    	
    	if(child>0)
    		parent = (child+1)/2-1;
    	
    	int index = data.length - 1;
    	while(index>0){
    		
    		
    		
	    	child = index;
	    	if(child>0)
	    		parent = (child+1)/2-1;
	    	
    		if(debug){
    			System.out.printf("child is %d%n", child);
    		}
	    	int loops = 0;
	    	while(testEdge(child)){
	    		if(child>0)
	    			child = (child+1)/2-1;
	    		loops++;
	    	}

	    	if(loops==0){
	    		index--;
	    	}
    	}
     }

    
	private boolean testEdge(int child){
		int parent = 0;
		if(child>0)
			parent = (child+1)/2-1;
		
		if(data[parent]>data[child]){
			int tmp = data[child];
			data[child] = data[parent];
			data[parent] = tmp;
			swaps.add(new Swap(parent, child));
			if((child+1)*2-1<data.length)
				testEdge((child+1)*2-1);
			if((child+1)*2<data.length)
				testEdge((child+1)*2);
			
			return true;
		}
		return false;
	}

    private void generateSwapsBetter(){
    	swaps = new ArrayList<Swap>();
    	int dataBackup[] = new int[data.length];
    	System.arraycopy(data, 0, dataBackup, 0, data.length);
    	data = new int[dataBackup.length];
    	for(int i=0;i<dataBackup.length;i++){
    		data[i] = dataBackup[i];
    		int childIndex = i;
    		int parentIndex = 0;
    		if(childIndex>0){
    			parentIndex = (childIndex+1)/2 - 1;
    		}
    		while(data[childIndex]<data[parentIndex]){
    			int tmp = data[childIndex];
    			data[childIndex] = data[parentIndex];
    			data[parentIndex] = tmp;
    			swaps.add(new Swap(parentIndex, childIndex));
    			childIndex = parentIndex;
        		if(childIndex>0){
        			parentIndex = (childIndex+1)/2 - 1;
        		} else {
        			parentIndex = 0;
        		}
   			
    		}
    	}
    }
    
    private void generateSwapsFaster(){
        int child;
    	int parent;
    	int childIndex;
    	int parentIndex;
    	swaps = new ArrayList<Swap>();
    	for(int i=data.length-1; i>0;i--){
    		childIndex = i;
    		parentIndex = (i + 1)/2 - 1;
		

    		
    		while(childIndex!=0 && data[(childIndex + 1)/2 - 1]>data[childIndex]){
        		parentIndex = (childIndex + 1)/2 - 1;
    			child = data[childIndex];

        		
   			
    			
    			parent = data[parentIndex];
 
        		
    			data[parentIndex] = child;
    			data[childIndex] = parent;
    			swaps.add(new Swap(parentIndex, childIndex));
    			childIndex = parentIndex;
        		
        		
        		
        		////////////////////////////////////////////////////
         		if(debug){
        			System.out.printf("swapping child %d with parent %d%n", 
        					child, parent);
    				System.out.println("data is now:");
        			for(int j=0; j < data.length;j++){
        				System.out.printf("%d ", data[j]);
        			}
        			System.out.println();
        		}
        		////////////////////////////////////////////////////
        		
        		
    		}
    	}

    }

    public void solve() throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        readData();
        generateSwapsEvenBetter();
        writeResponse();
        out.close();
    }

    static class Swap {
        int index1;
        int index2;

        public Swap(int index1, int index2) {
            this.index1 = index1;
            this.index2 = index2;
        }
    }

    static class FastScanner {
        private BufferedReader reader;
        private StringTokenizer tokenizer;

        public FastScanner() {
            reader = new BufferedReader(new InputStreamReader(System.in));
            tokenizer = null;
        }

        public String next() throws IOException {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                tokenizer = new StringTokenizer(reader.readLine());
            }
            return tokenizer.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }
}
