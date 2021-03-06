/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-30 下午5:23:31
 */
package com.absir.appserv.developer.editor;

import com.absir.bean.inject.value.Bean;
import com.absir.core.kernel.KernelClass;
import com.absir.orm.value.JaClasses;
import com.absir.property.PropertyResolverAbstract;

/**
 * @author absir
 * 
 */
@Bean
public class EditorClasses extends PropertyResolverAbstract<EditorObject, JaClasses> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.property.PropertyResolverAbstract#getPropertyObjectAnnotation
	 * (com.absir.property.PropertyObject, java.lang.annotation.Annotation)
	 */
	@Override
	public EditorObject getPropertyObjectAnnotation(EditorObject propertyObject, JaClasses annotation) {
		// TODO Auto-generated method stub
		if (propertyObject == null) {
			propertyObject = new EditorObject();
		}

		propertyObject.setKeyClass(annotation.key());
		propertyObject.setValueClass(annotation.value());
		return propertyObject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.property.PropertyResolverAbstract#getPropertyObjectAnnotationValue
	 * (com.absir.property.PropertyObject, java.lang.String)
	 */
	@Override
	public EditorObject getPropertyObjectAnnotationValue(EditorObject propertyObject, String annotationValue) {
		// TODO Auto-generated method stub
		if (propertyObject == null) {
			propertyObject = new EditorObject();
		}

		String[] names = annotationValue.split(",");
		if (names.length == 1) {
			propertyObject.setValueClass(KernelClass.forName(names[0]));

		} else if (names.length == 2) {
			propertyObject.setKeyClass(KernelClass.forName(names[0]));
			propertyObject.setValueClass(KernelClass.forName(names[1]));
		}

		return propertyObject;
	}

}
