package com.hrycan.prime.view;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.hrycan.prime.entity.Employee;
import com.hrycan.prime.service.ClassicModelsService;

@FacesConverter(forClass = Employee.class)
public class EmployeeConverter implements Converter {

    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        
        ClassicModelsService service = (ClassicModelsService) facesContext.getApplication().getELResolver().
                getValue(facesContext.getELContext(), null, "classicModelsService");
        return service.findEmployee(getKey(value));
    }

    java.lang.Integer getKey(String value) {
        java.lang.Integer key;
        key = Integer.valueOf(value);
        return key;
    }

    String getStringKey(java.lang.Integer value) {
        StringBuffer sb = new StringBuffer();
        sb.append(value);
        return sb.toString();
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Employee) {
            Employee o = (Employee) object;
            return getStringKey(o.getEmployeeNumber());
        } else {
            throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Employee.class.getName());
        }
    }
}