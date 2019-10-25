
public  class manager extends employee {

	
   int managersalary;
	
	public int getManagersalary() {
	return managersalary;
}

public void setManagersalary(int managersalary) {
	this.managersalary = managersalary;
}

	public int getSalary(int managersalary){
		this.managersalary=managersalary;
		System.out.println(managersalary);
		return managersalary;
	}

}
