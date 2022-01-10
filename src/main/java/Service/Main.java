package Service;

import java.net.InetAddress;
public class Main
{
    public static void main(String args[]) throws Exception
    {
        InetAddress ip = InetAddress.getLocalHost();

        System.out.print("Mon adresse IP est: ");
        System.out.println(ip.getHostAddress());
    }
}