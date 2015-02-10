package models;

/**
 * Created by dhoffman on 2/10/2015.
 */
public class UserCompensationGroup {
    
    private String groupName; 
    private int groupCount; 
    private double totalCompaRatio;
    private double totalSalary;

    private double avgCompaRatio = 0.0;
    private double avgSalary = 0.0;

    public void addCompaRatio(double compaRatio){
        setTotalCompaRatio(getTotalCompaRatio()+compaRatio); 
    }
    public void addTotalSalary(double salary){
        setTotalSalary(getTotalSalary()+salary);
    }
    
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(int groupCount) {
        this.groupCount = groupCount;
    }

    public double getTotalCompaRatio() {
        return totalCompaRatio;
    }

    public void setTotalCompaRatio(double totalCompaRatio) {
        this.totalCompaRatio = totalCompaRatio;
    }

    public double getTotalSalary() {
        return totalSalary;
    }

    public void setTotalSalary(double totalSalary) {
        this.totalSalary = totalSalary;
    }

    public double getAvgCompaRatio() {
        if(getGroupCount() > 0){
            setAvgCompaRatio(getTotalCompaRatio() / ((double) getGroupCount()));
        }
        return avgCompaRatio;
    }

    public void setAvgCompaRatio(double avgCompaRatio) {
        this.avgCompaRatio = avgCompaRatio;
    }

    public double getAvgSalary() {
        if(getGroupCount() > 0){
            setAvgSalary(getTotalSalary()/((double)getGroupCount()));
        }
        return avgSalary;
    }

    public void setAvgSalary(double avgSalary) {
        this.avgSalary = avgSalary;
    }
}
