package com.hrycan.prime.view;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.hrycan.prime.entity.Office;
import com.hrycan.prime.service.ClassicModelsService;

@FacesConverter(forClass = Office.class)
public class OfficeConverter implements Converter {

    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        ClassicModelsService service = (ClassicModelsService) facesContext.getApplication().getELResolver().
                getValue(facesContext.getELContext(), null, "classicModelsService");
        return service.findOffice(getKey(value));
    }

    java.lang.String getKey(String value) {
        java.lang.String key;
        key = value;
        return key;
    }

    String getStringKey(java.lang.String value) {
        StringBuffer sb = new StringBuffer();
        sb.append(value);
        return sb.toString();
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Office) {
            com.hrycan.prime.entity.Office o = (Office) object;
            String val = getStringKey(o.getOfficeCode());
            return val;
        } else {
            throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Office.class.getName());
        }
    }
}
