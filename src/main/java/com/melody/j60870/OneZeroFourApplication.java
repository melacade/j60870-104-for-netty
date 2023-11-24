package com.melody.j60870;

import java.io.IOException;

public class OneZeroFourApplication {
	
	
	public static void main(String[] args) {
		//		SpringApplication.run(OneZeroFourApplication.class, args);
		
		try {
			//			Server.builder().build().start();
			Client build = Client.builder().build("127.0.0.1");
			build.start();
			build.sendStartDt();
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}finally {
		
		}
		/*catch (InterruptedException e) {
			throw new RuntimeException(e);
		*/
//		Scanner scanner = new Scanner(System.in);
//		scanner.next();
	}
	
	
}
