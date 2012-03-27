package com.hrycan.prime.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Version;



@TableGenerator(name="EMPLOYEE_TABLE_GENERATOR",
	table="sequence",
	pkColumnName="name",
	valueColumnName="nextid",
	pkColumnValue="employeeNumber")

@Entity
@Table(name = "Employees")
@NamedQueries({
@NamedQuery(name = "Employee.findAll", query = "SELECT e FROM Employee e"),
@NamedQuery(name = "Employee.manager", 
	query = "SELECT e FROM Employee e where e.jobTitle LIKE '%Manager' OR e.jobTitle like '%President' OR e.jobTitle like 'VP%'"),
@NamedQuery(name = "Employee.noManager", query = "SELECT e FROM Employee e where e.reportsTo is NULL"),
@NamedQuery(name = "Employee.count", query = "SELECT COUNT(e) FROM Employee e"),
@NamedQuery(name = "Employee.withCustomers", query = "SELECT e FROM Employee e JOIN FETCH e.customers where e.employeeNumber = :id"),
@NamedQuery(name = "Employee.withSubordinates", query = "SELECT e FROM Employee e JOIN FETCH e.subordinates where e.employeeNumber = :id")
})
@Cacheable
public class Employee implements Serializable {
	

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="EMPLOYEE_TABLE_GENERATOR")
	private Integer employeeNumber;
	
	private String lastName;
	private String firstName;
	private String extension;
	private String email;
	
	@ManyToOne
	@JoinColumn(name="officeCode", referencedColumnName="officeCode")
	private Office office;
	
	@ManyToOne
	@JoinColumn(name="reportsTo", referencedColumnName="employeeNumber")
	private Employee reportsTo;
	
    @OneToMany(mappedBy = "reportsTo")
    private List<Employee> subordinates;
	
	@OneToMany(mappedBy="salesRep")
	private List<Customer> customers;
	
	private String jobTitle;
	
	@Version
    private int version;
	
	public Integer getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(Integer employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public Employee getReportsTo() {
		return reportsTo;
	}

	public void setReportsTo(Employee reportsTo) {
		this.reportsTo = reportsTo;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public List<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeeNumber != null ? employeeNumber.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.employeeNumber == null && other.employeeNumber != null) || (this.employeeNumber != null && !this.employeeNumber.equals(other.employeeNumber))) {
            return false;
        }
        return true;
    }

	public int getVersion() {
		return version;
	}

	public List<Employee> getSubordinates() {
		return subordinates;
	}

	public void setSubordinates(List<Employee> subordinates) {
		this.subordinates = subordinates;
	}

	@Override
	public String toString() {
		return "Employee [lastName=" + lastName + ", firstName=" + firstName + ", extension=" + extension + ", email=" + email
				+ ", office=" + office.getCity() + ", reportsTo=" + reportsTo.getEmployeeNumber() + ", jobTitle=" + jobTitle + "]";
	}
	

	

}