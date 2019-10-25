
public  class worker extends employee {

	public int getDaysalary() {
		return daysalary;
	}

	public void setDaysalary(int daysalary) {
		this.daysalary = daysalary;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	int daysalary;
	int days;
	
	public int getSalary(int days,int daysalary){
		this.days=days;
		this.daysalary=daysalary;
		return daysalary*days;
	}

}
