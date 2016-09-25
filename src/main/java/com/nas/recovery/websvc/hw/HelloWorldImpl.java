package com.nas.recovery.websvc.hw;

import javax.jws.WebService;

import org.jboss.seam.annotations.Name;

import com.nas.recovery.websvc.hw.HelloWorld;

@WebService(endpointInterface = "com.nas.recovery.websvc.hw.HelloWorld", 
            serviceName = "HelloWorldService")
@Name("helloWorld")
public class HelloWorldImpl implements HelloWorld{

   

	public String sayHello(String name) {
		return "Hello " + name;
	}
}