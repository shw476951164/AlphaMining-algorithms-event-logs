import java.util.Scanner; 
public class test {
public static void main(String[] args) {
	   manager a=new manager();
	   saleman b=new saleman();
	   worker c=new worker();
	   Scanner s = new Scanner(System.in);
	   a.salary=s.nextInt();
	   int asar=a.getSalary();
	   System.out.println(asar);
	   b.basesalesalary=s.nextInt();
	   b.growsalary=s.nextInt();
	   int salemansar=b.getSalary(b.basesalesalary, b.growsalary);
	   System.out.println(salemansar);
	   int workersar=c.getSalary(s.nextInt(), s.nextInt());
	   System.out.println(workersar);
} 
}
