
public  class saleman extends employee {

	
	public int getBasesalesalary() {
		return basesalesalary;
	}

	public void setBasesalesalary(int basesalesalary) {
		this.basesalesalary = basesalesalary;
	}

	public int getGrowsalary() {
		return growsalary;
	}

	public void setGrowsalary(int growsalary) {
		this.growsalary = growsalary;
	}

	int basesalesalary;
	int growsalary;
	
	public int getSalary(int basesalesalary,int growsalary){
		this.basesalesalary=basesalesalary;
		this.growsalary=growsalary;
		return growsalary+basesalesalary;
	}

}
