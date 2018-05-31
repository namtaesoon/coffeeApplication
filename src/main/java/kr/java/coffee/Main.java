package kr.java.coffee;

import kr.java.coffee.dto.Product;

public class Main {

	public static void main(String[] args) {
		System.out.println("마찰시간");
		
		
		Product p1 =new Product("A001");
		Product p2 =new Product("A001");
		
		System.out.println(p1);
		
		System.out.println(p1);
		
		
		if (p1.equals(p2))  {
			System.out.println("같음");	
			
		}else {
			System.out.println("다름");	
			
		}
		
		//이부분은 Eclipse에서 추가....
	}

}
