package hello.client;

import java.lang.reflect.Proxy;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import hello.common.Hello;

/**
 * Le CLIENT. Il ne connait que l'interface Hello, jamais HelloImpl.
 *
 * Usage : java hello.client.Client [hote]
 *         (l'argument est un NOM D'HOTE, pas un numero de port)
 */
public class Client {

    public static void main(String[] args) {
        String host = (args.length < 1) ? null : args[0];
        try {
            Registry registry = LocateRegistry.getRegistry(host, 1099);
            Hello stub = (Hello) registry.lookup("Hello");

            System.out.println("classe du stub : " + stub.getClass().getName());
            System.out.println("proxy dynamique ? " + Proxy.isProxyClass(stub.getClass()));

            System.out.println("response: " + stub.sayHello());
            stub.printHello();
        } catch (Exception e) {
            System.err.println("Client exception: " + e);
            e.printStackTrace();
        }
    }
}
