
public class Test {
	static {int x = 5;}
	static int x, y;
	public static void main(String[] args){
		System.out.println(x--);
		System.out.println(x);
		myMethod();
		System.out.println(x+";"+y);
		//1+ 0 + 1
		System.out.println(x + y++ + x);
		System.out.println(x+";"+y);
		
		//*************************
//		int i = 0;
//		System.out.println(i+'0');
		
		
		
		//*************************
		int j = 0;
		for(int i = 0; i<100 ; i++){
			j= j++;
		}
		System.out.println(j);
	}
	public static void myMethod(){
		// y = -1 + 1 = 0
		y = x++ + ++x;
	}
	
	
	//******************
	
//	int t = "abc".length();
//	System.out.println(t);
}
