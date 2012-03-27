package com.hrycan.prime.view;


import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import com.hrycan.prime.entity.Employee;
import com.hrycan.prime.entity.Office;
import com.hrycan.prime.exception.DataNotFoundException;
import com.hrycan.prime.exception.StaleDataException;
import com.hrycan.prime.service.ClassicModelsService;
import com.hrycan.prime.util.EmployeeQueryData;
import com.hrycan.prime.util.JsfUtil;
import com.hrycan.prime.util.QuerySortOrder;


@ManagedBean(name= "employeesController")
@ViewScoped
public class EmployeeController implements Serializable {

	final Logger log = LoggerFactory.getLogger(EmployeeController.class);
    
    @ManagedProperty(value = "#{classicModelsService}")
    private transient ClassicModelsService classicModelsService; 
	
	private Employee current = new Employee();
	private Employee detailedEmployee = new Employee();
	
    private LazyDataModel<Employee> lazyModel;
    private List<String> jobTitlesList;
    
	private SelectItem[] jobTitleFilter;
	private SelectItem[] officeFilter;
	private List<Employee> allManagers;
	private List<Office> allOffices;
	
	public SelectItem[] getJobTitleFilter() {
		return jobTitleFilter;
	}
    
    public LazyDataModel<Employee> getLazyModel() {
    	if (lazyModel == null) {
    		lazyModel = new LazyDataModel<Employee>() {
				
				@Override
				public List<Employee> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,String> filters) {  
			log.info("first=" + first + ", pagesize=" + pageSize + ", sortfield=" + sortField + " sortorder=" + sortOrder + " filter:" + filters);	
			int start = first;
                	int end = first + pageSize;
                	
                	QuerySortOrder order = QuerySortOrder.ASC;
                	if (sortOrder == SortOrder.DESCENDING) {
            			order = QuerySortOrder.DESC;     
            		}
                	
            		EmployeeQueryData qData = new EmployeeQueryData(start, end, sortField, order, filters);
            		getClassicModelsService().findEmployees(qData);
            		List<Employee> empList = qData.getResult();
            		int count = qData.getTotalResultCount().intValue();
            		this.setRowCount(count);
                		
//                	List<Employee> empList = getClassicModelsService().findEmployees(start, end);
//                		this.setRowCount(getClassicModelsService().getEmployeeCount());
                	
                    return empList;
				}

			};
    	}
    	
    	return lazyModel;
    }


	@PostConstruct
	public void init() {
		//StopWatch sw = new StopWatch("EmployeeController @PostConstruct");
		//sw.start();
		System.out.println("inside EmployeeController @PostConstruct ");
		jobTitlesList = Arrays.asList("Sales Rep",
				"Sales Associate",
				"Sr. Sales Associate",
				"Sales Manager",
				"Sr. Sales Manager",
				"Sales Advisor",
				"Sales Consultant",
				"Sales Champion",
				"Sales Evangelist");
		Collections.sort(jobTitlesList);
	
		jobTitleFilter = new SelectItem[jobTitlesList.size() + 1];
		jobTitleFilter[0] = new SelectItem("", "Select");
		
		for (int i=1; i< jobTitleFilter.length; i++) {
			jobTitleFilter[i] = new SelectItem(jobTitlesList.get(i-1), jobTitlesList.get(i-1));
		}
		
		List<Office> officesList = classicModelsService.getAllOffices();
		officeFilter = new SelectItem[officesList.size() +1];
		officeFilter[0] = new SelectItem("", "Select");
		for (int i=1; i< officeFilter.length; i++) {
			officeFilter[i] = new SelectItem(officesList.get(i-1).getCity(), officesList.get(i-1).getCity());
		}
		allOffices = officesList;
		
		//sw.stop();
		//log.info(sw.shortSummary());
	}
	
	public List<String> getJobTitlesList() {
		return jobTitlesList;
	}
    
    public void createOrUpdateListener(ActionEvent event) {
    	try {
	    	if (current.getEmployeeNumber() == null) {
	    		getClassicModelsService().create(current);
	            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("Bundle").getString("EmployeesCreated"));
	    	} else {
	    		getClassicModelsService().edit(current);
	            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("Bundle").getString("EmployeesUpdated"));
	    	}
	    	current = new Employee();
    	} catch (StaleDataException e) {
    		current = getClassicModelsService().findEmployee(current.getEmployeeNumber());
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("Bundle").getString("OptimisticLockError"));
    	} catch (DataNotFoundException e) {
    		current = new Employee();
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("Bundle").getString("OptimisticLockErrorUserDeleted"));
    	} catch (Exception e) {
			log.warn(e.toString(), e);
			JsfUtil.addErrorMessage(ResourceBundle.getBundle("Bundle").getString("PersistenceErrorOccured"));
        }
    }



    public void clearListener(ActionEvent event) {
    	current = new Employee();
    	UIViewRoot viewRoot =  FacesContext.getCurrentInstance().getViewRoot();
	    UIComponent component1 = viewRoot.findComponent("form2");  
	    JsfUtil.clearComponentHierarchy(component1);
    }
    

    
    public void destroyListener(ActionEvent event) {
    	 try {
             getClassicModelsService().remove(detailedEmployee);
             detailedEmployee = new Employee();
             JsfUtil.addSuccessMessage(ResourceBundle.getBundle("Bundle").getString("EmployeesDeleted"));
    	 } catch (StaleDataException e) {
             JsfUtil.addErrorMessage(ResourceBundle.getBundle("Bundle").getString("OptimisticLockErrorUserUpdated"));
    	 } catch (Exception e) {
        	 log.warn(e.toString(), e);
             JsfUtil.addErrorMessage(ResourceBundle.getBundle("Bundle").getString("PersistenceErrorOccured"));
         }
    }
    
    public List<Employee> getAllManagers() {
    	if (allManagers == null) {
    		allManagers = getClassicModelsService().findManagers();
    	}
        return allManagers;
    }
    
    public List<Office> getAllOffices() {
    	if (allOffices == null) {
    		allOffices = getClassicModelsService().getAllOffices();
    	}
    	
        return allOffices;
    }


	public ClassicModelsService getClassicModelsService() {
		if (classicModelsService == null) {
			//classicModelsService = (ClassicModelsService) JsfUtil.getManagedBean("classicModelsService");
			FacesContext ctx = FacesContext.getCurrentInstance();
			classicModelsService = (ClassicModelsService) ctx.getApplication().getELResolver().
	                getValue(ctx.getELContext(), null, "classicModelsService");
		}
		
		return classicModelsService;
	}

	public void setClassicModelsService(ClassicModelsService classicModelsService) {
		this.classicModelsService = classicModelsService;
	}

	public Employee getCurrent() {
		return current;
	}

	public void setCurrent(Employee current) {
		this.current = current;
	}

	public Employee getDetailedEmployee() {
		return detailedEmployee;
	}

	public void setDetailedEmployee(Employee detailedEmployee) {
		this.detailedEmployee = detailedEmployee;
	}

	public SelectItem[] getOfficeFilter() {
		return officeFilter;
	}

}
