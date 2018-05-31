package kr.java.coffee;

import kr.java.coffee.dto.Product;

public class Main {

	public static void main(String[] args) {
		System.out.println("마칠시간");
		
		
		Product p1 =new Product("A001");
		Product p2 =new Product("A001");
		
		System.out.println(p1);
		
		System.out.println(p1);
		
		
		if (p1.equals(p2))  {
			System.out.println("°°Ŕ˝");	
			
		}else {
			System.out.println("´Ů¸§");	
			
		}
		//이문장은 GitHub에서 
		
	}

}
